package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.ArticleNoContentDao;
import com.beiwangshan.blog.dao.CommentDao;
import com.beiwangshan.blog.pojo.ArticleNoContent;
import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.pojo.Comment;
import com.beiwangshan.blog.pojo.PageList;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.BaseService;
import com.beiwangshan.blog.service.ICommentService;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.utils.Constants;
import com.beiwangshan.blog.utils.RedisUtils;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.beiwangshan.blog.utils.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * @className: com.beiwangshan.blog.service.Impl-> CommentServiceImpl
 * @description: 评论相关的实现类
 * @author: 曾豪
 * @createDate: 2020-11-28 19:15
 * @version: 1.0
 * @todo:
 */
@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class CommentServiceImpl extends BaseService implements ICommentService {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private IUserService userService;

    @Autowired
    private ArticleNoContentDao articleNoContentDao;

    @Autowired
    private CommentDao commentDao;

    /**
     * 上传评论 发表评论
     *
     * @param comment
     * @return
     */
    @Override
    public ResponseResult postComment(Comment comment) {
//        检查用户是否有登录
        BwsUser bwsUser = userService.checkBwsUser();
        if (bwsUser == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
//        检查内容
        String articleId = comment.getArticleId();
        if (TextUtils.isEmpty(articleId)) {
            return ResponseResult.FAILED("文章ID不能为空");
        }
        ArticleNoContent articleFromDb = articleNoContentDao.findOneById(articleId);
        if (articleFromDb == null) {
            return ResponseResult.FAILED("文章不存在");
        }

        String content = comment.getContent();
        if (TextUtils.isEmpty(content)) {
            return ResponseResult.FAILED("评论内容不能为空");
        }
//        补全内容
        comment.setId(snowflakeIdWorker.nextId() + "");
        comment.setUpdateTime(new Date());
        comment.setCreateTime(new Date());
        comment.setUserAvatar(bwsUser.getAvatar());
        comment.setUserId(bwsUser.getId());
        comment.setUserName(bwsUser.getUserName());
//保存入库
        commentDao.save(comment);
        //清除对应文章的评论缓存
        redisUtils.del(Constants.Comment.KEY_COMMENT_FIRST_PAGE_CACHE + comment.getArticleId());
        //TODO：发送通知 --> 通过邮件
        //EmailSender.sendCommentMotify("你的文章被评论啦");
        //返回结果
        return ResponseResult.SUCCESS("评论成功");
    }

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private Gson gson;

    /**
     * 通过文章ID 来获取评论列表
     * 评论的排序策略：
     * 1.最基本的就是按照时间排序，新发布的放在最前面
     * <p>
     * 2.置顶的，一定在最前面
     * <p>
     * 3.后发表的，前单位时间会在前面排序，过了此时间，会按点赞量和发表的时间来排序。
     *
     * @param articleId
     * @param page
     * @param size
     * @return
     */
    @Override
    public ResponseResult listCommentByArticleId(String articleId, int page, int size) {
        ArticleNoContent oneById = articleNoContentDao.findOneById(articleId);
        if (oneById == null) {
            return ResponseResult.FAILED("评论列表获取失败，文章不存在");
        }

        page = checkPage(page);
        size = checkSize(size);
        //如果是第一页，那我们先从缓存中拿
        //如果有就直接返回
        //如果没有就直接向下继续
        String cacheJson = (String) redisUtils.get(Constants.Comment.KEY_COMMENT_FIRST_PAGE_CACHE + articleId);
        if (!TextUtils.isEmpty(cacheJson) && page == 1) {
            PageList<Comment> result = gson.fromJson(cacheJson, new TypeToken<PageList<Comment>>() {
            }.getType());
            log.info("这是从cache里面获取的。。。");
            return ResponseResult.SUCCESS("文章评论列表获取成功").setData(result);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "state", "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Comment> all = commentDao.findAllByArticleId(articleId, pageable);
        //把结果转成pageList
        PageList<Comment> result = new PageList<>();
        result.parsePage(all);
        //保存一份到缓存里面
        if (page == 1) {
            redisUtils.set(Constants.Comment.KEY_COMMENT_FIRST_PAGE_CACHE + articleId, gson.toJson(result), Constants.TimeValueInMillions.MIN_10);
        }
        return ResponseResult.SUCCESS("文章评论列表获取成功").setData(result);
    }

    /**
     * 通过评论的ID来删除评论
     *
     * @param commentId
     * @return
     */
    @Override
    public ResponseResult deleteCommentById(String commentId) {
//        检查用户的角色
        BwsUser bwsUser = userService.checkBwsUser();
        if (bwsUser == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
//        把评论找出来，比对用户权限
        Comment commentFromDb = commentDao.findOneById(commentId);
        if (commentFromDb == null) {
            return ResponseResult.FAILED("评论不存在");
        }
        //        登录了要判断角色
        if (bwsUser.getId().equals(commentFromDb.getUserId()) || bwsUser.getRoles().equals(Constants.User.ROLE_ADMIN)) {
            //            如果和当前操作用户的ID 一致，说明评论是当前用户的，可以删除
            //            用户 ID 不一致，只有管理员可以删除
            commentDao.deleteById(commentId);
            return ResponseResult.SUCCESS("评论删除成功");
        } else {
            return ResponseResult.PERMISSION_DENIAL();
        }

    }

    /**
     * 获取评论列表
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public ResponseResult listComments(int page, int size) {
        page = checkPage(page);
        size = checkSize(size);
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Comment> all = commentDao.findAll(pageable);
        return ResponseResult.SUCCESS("获取评论列表成功").setData(all);
    }

    /**
     * 置顶评论
     *
     * @param commentId
     * @return
     */
    @Override
    public ResponseResult TopComment(String commentId) {
        Comment comment = commentDao.findOneById(commentId);
        if (comment == null) {
            return ResponseResult.FAILED("评论不存在");
        }
        String state = comment.getState();
        if (Constants.Comment.STATE_PUBLISH.equals(state)) {
            comment.setState(Constants.Comment.STATE_TOP);
            return ResponseResult.SUCCESS("评论置顶成功");
        } else if (Constants.Comment.STATE_TOP.equals(state)) {
            comment.setState(Constants.Comment.STATE_PUBLISH);
            return ResponseResult.SUCCESS("取消置顶成功");
        } else {
            return ResponseResult.FAILED("评论状态非法");
        }
    }
}
