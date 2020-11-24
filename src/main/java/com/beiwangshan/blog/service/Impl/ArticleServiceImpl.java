package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.pojo.Article;
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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

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

    /**
     * 添加文章
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
        if (title.length() > Constants.Article.TITLE_MAX_LENGTH){
            return ResponseResult.FAILED("标题长度不能大于"+Constants.Article.TITLE_MAX_LENGTH+"个字符");
        }
        String categoryId = article.getCategoryId();
        if (TextUtils.isEmpty(categoryId)) {
            return ResponseResult.FAILED("文章分类不能为空");
        }
        String content = article.getContent();
        if (TextUtils.isEmpty(content)) {
            return ResponseResult.FAILED("文章内容不能为空");
        }
        String type = article.getType();
        if (TextUtils.isEmpty(type)) {
            return ResponseResult.FAILED("文章类型不能为空");
        }
        if (Constants.Article.ARTICLE_TYPE_FU.equals(type)
                && Constants.Article.ARTICLE_TYPE_MD.equals(type)) {
            return ResponseResult.FAILED("文章类型不正确");
        }
        String summary = article.getSummary();
        if (TextUtils.isEmpty(summary)) {
            return ResponseResult.FAILED("文章摘要不能为空");
        }
        if (summary.length() > Constants.Article.SUMMARY_MAX_LENGTH) {
            return ResponseResult.FAILED("文章摘要不能超过"+Constants.Article.SUMMARY_MAX_LENGTH+"字");
        }
        String labels = article.getLabels();
//        存储格式 标签1-标签2-
        if (TextUtils.isEmpty(labels)) {
            return ResponseResult.FAILED("文章标签不能为空");
        }


//        补充数据
        article.setId(snowflakeIdWorker.nextId()+"");
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
//        保存数据

//        TODO:保存到搜索的数据库里

//        返回结果
        return null;
    }
}
