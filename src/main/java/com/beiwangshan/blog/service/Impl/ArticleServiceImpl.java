package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.service.BaseService;
import com.beiwangshan.blog.service.IArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
