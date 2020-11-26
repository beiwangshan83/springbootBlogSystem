package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.ArticleDao;
import com.beiwangshan.blog.dao.ArticleNoContentDao;
import com.beiwangshan.blog.pojo.Article;
import com.beiwangshan.blog.pojo.ArticleNoContent;
import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.BaseService;
import com.beiwangshan.blog.service.IArticleService;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.utils.Constants;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.beiwangshan.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @className: com.beiwangshan.blog.service.Impl-> ArticleServiceImpl
 * @description: 文章的实现类
 * @author: 曾豪
 * @createDate: 2020-11-24 19:55
 * @version: 1.0
 * @todo:
 */
@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class ArticleServiceImpl extends BaseService implements IArticleService {

    @Autowired
    private IUserService userService;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private ArticleDao articleDao;

    /**
     * 没有文章的内容
     */
    @Autowired
    private ArticleNoContentDao articleNoContentDao;

    /**
     * 添加文章
     * 后期可以做定时提交的功能，多人博客的审核功能
     * <p>
     * 保存草稿的问题
     * 1、用户手动提交 --> 提交完会发生页面跳转 --> 提交完即可
     * 2、机器自动提交，每隔一段时间 --> 不会发生页面跳转 --> 会出现多次提交 --> 如果没唯一的标识，就会重复添加数据到数据库
     * <p>
     * 不管是哪种草稿，都必须有标题
     * <p>
     * 方案一：
     * 每次发文之前 --> 向后台请求一个唯一的文章ID
     * 如果有更新文件，则不需要请求这个唯一的ID
     * 这样子，提交的文章就携带了ID ，避免重复的数据
     * <p>
     * 方案二：
     * 可以直接提交，后台判断有没有ID ，
     * 如果没有ID，就创建，并且ID 作为此次返回的结果
     * 如果有ID，就修改已经存在的内容
     * <p>
     * 推荐做法：
     * 自动保存草稿，在前端本地完成，也就是保存在本地
     * 如果是用户手动提交的，就提交到后台
     * <p>
     * 防止重复提交的问题（网络或者系统卡顿的时候，用户点击多次）
     * 可以通过ID 的方式：
     * 通过token_key的提交频率，30秒之内如果有多次的提交，已最前一次的提交为准
     * 其他的提交直接return，提示用户不能操作过于频繁
     * 前端的处理：
     * 点击了提交之后，禁止按钮的使用状态，等待有结果返回的时候回才改变按钮的状态。
     *
     * @param article
     * @return
     */
    @Override
    public ResponseResult addArticle(Article article) {
//        检查用户，获取到用户对象
        BwsUser bwsUser = userService.checkBwsUser();
        if (bwsUser == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }

//        检查数据 title 分类ID 内容 类型 摘要 标签
        String title = article.getTitle();
        if (TextUtils.isEmpty(title)) {
            return ResponseResult.FAILED("文章标题不能为空");
        }

        //        文章状态： 1表示发布 2表示草稿
        String state = article.getState();
        if (!Constants.Article.STATE_PUBLIC.equals(state)
                && !Constants.Article.STATE_DRAFT.equals(state)) {
            return ResponseResult.FAILED("不支持此操作");
        }

        String type = article.getType();
        if (TextUtils.isEmpty(type)) {
            return ResponseResult.FAILED("文章类型不能为空");
        }

        if (!Constants.Article.ARTICLE_TYPE_FU.equals(type)
                && !Constants.Article.ARTICLE_TYPE_MD.equals(type)) {
            return ResponseResult.FAILED("文章类型不正确");
        }

//        以下是发布的检查，草稿不需要检查
        if (Constants.Article.STATE_PUBLIC.equals(state)) {
            if (title.length() > Constants.Article.TITLE_MAX_LENGTH) {
                return ResponseResult.FAILED("标题长度不能大于" + Constants.Article.TITLE_MAX_LENGTH + "个字符");
            }
            String categoryId = article.getCategoryId();
            if (TextUtils.isEmpty(categoryId)) {
                return ResponseResult.FAILED("文章分类不能为空");
            }
            String content = article.getContent();
            if (TextUtils.isEmpty(content)) {
                return ResponseResult.FAILED("文章内容不能为空");
            }


            String summary = article.getSummary();
            if (TextUtils.isEmpty(summary)) {
                return ResponseResult.FAILED("文章摘要不能为空");
            }
            if (summary.length() > Constants.Article.SUMMARY_MAX_LENGTH) {
                return ResponseResult.FAILED("文章摘要不能超过" + Constants.Article.SUMMARY_MAX_LENGTH + "字");
            }
            String labels = article.getLabels();
//        存储格式 标签1-标签2-
            if (TextUtils.isEmpty(labels)) {
                return ResponseResult.FAILED("文章标签不能为空");
            }
        }

        String articleId = article.getId();
        if (TextUtils.isEmpty(articleId)) {
//            新内容，数据库里没有的
            //        补充数据
            article.setId(snowflakeIdWorker.nextId() + "");
            article.setCreateTime(new Date());
        } else {
//            否则就是更新内容，对状态进行处理，如果已经给发布了，就不能再发布和保存为草稿
            Article articleFromDb = articleDao.findOneById(articleId);
            if (Constants.Article.STATE_PUBLIC.equals(articleFromDb.getState())
            && Constants.Article.STATE_DRAFT.equals(articleFromDb.getState())) {
//                如果已经发布，只能更新，不能保存为草稿
                return ResponseResult.FAILED("已经发布的文章不能保存为草稿");
            }
        }
        article.setUpdateTime(new Date());
        article.setUserId(bwsUser.getId());
//        保存数据
        articleDao.save(article);
//        TODO:保存到搜索的数据库里

//        返回结果，只有一种情况使用到这个ID
//        如果要做程序自动保存为草稿（每30秒保存一次，就需要加上个这个ID ，否则就会创建多个Item）
        return Constants.Article.STATE_DRAFT.equals(state)?
                ResponseResult.SUCCESS("草稿保存成功").setData(article.getId())
                :ResponseResult.SUCCESS("文章发布成功").setData(article.getId());
    }

    /**
     * 获取文章列表
     *
     * @param page 页码
     * @param size 没一页的数量
     * @param keyword 标题关键词
     * @param categoryId 分类ID
     * @param state 文章状态
     * @return
     */
    @Override
    public ResponseResult listArticle(int page, int size, String keyword, String categoryId,String state) {
        BwsUser bwsUser = userService.checkBwsUser();
        if (bwsUser == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        page = checkPage(page);
        size=checkSize(size);
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Page<ArticleNoContent> all = articleNoContentDao.findAll(new Specification<ArticleNoContent>() {
            @Override
            public Predicate toPredicate(Root<ArticleNoContent> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!TextUtils.isEmpty(state)) {
                    Predicate statePre = cb.equal(root.get("state").as(String.class),state);
                    predicates.add(statePre);
                }

                if (!TextUtils.isEmpty(categoryId)) {
                    Predicate categoryIdPre = cb.equal(root.get("categoryId").as(String.class),categoryId);
                    predicates.add(categoryIdPre);
                }

                if (!TextUtils.isEmpty(keyword)) {
                    Predicate titlePre = cb.like(root.get("title").as(String.class),"%"+keyword+"%");
                    predicates.add(titlePre);
                }
                Predicate[] preArray = new Predicate[predicates.size()];
                predicates.toArray(preArray);
                return cb.and(preArray);
            }
        }, pageable);


        return ResponseResult.SUCCESS("文章列表查询成功").setData(all);
    }
}
