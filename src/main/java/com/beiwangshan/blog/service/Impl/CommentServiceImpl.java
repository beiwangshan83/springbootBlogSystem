package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.ArticleNoContentDao;
import com.beiwangshan.blog.dao.CommentDao;
import com.beiwangshan.blog.pojo.ArticleNoContent;
import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.pojo.Comment;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.BaseService;
import com.beiwangshan.blog.service.ICommentService;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.beiwangshan.blog.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        comment.setId(snowflakeIdWorker.nextId()+"");
        comment.setUpdateTime(new Date());
        comment.setCreateTime(new Date());
        comment.setUserAvatar(bwsUser.getAvatar());
        comment.setUserId(bwsUser.getId());
        comment.setUserName(bwsUser.getUserName());
//        保存入库
        commentDao.save(comment);
////        TODO：发送通知 --> 通过邮件
//        EmailSender.sendCommentMotify("你的文章被评论啦");
//        返回结果
        return ResponseResult.SUCCESS("评论成功");
    }

    /**
     * 通过文章ID 来获取评论列表
     * 评论的排序策略：
     * 1.最基本的就是按照时间排序，新发布的放在最前面
     *
     * 2.置顶的，一定在最前面
     *
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
        Pageable pageable = PageRequest.of(page-1,size);
        Page<Comment> all = commentDao.findAll(pageable);
        return ResponseResult.SUCCESS("文章评论列表获取成功").setData(all);
    }
}
