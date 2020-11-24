package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.SettingsDao;
import com.beiwangshan.blog.pojo.Setting;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.BaseService;
import com.beiwangshan.blog.service.IWebSiteInfoService;
import com.beiwangshan.blog.utils.Constants;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.beiwangshan.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @className: com.beiwangshan.blog.service.Impl-> WebSiteInfoServiceImpl
 * @description: 网站信息的实现类
 * @author: 曾豪
 * @createDate: 2020-11-24 18:52
 * @version: 1.0
 * @todo:
 */
@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class WebSiteInfoServiceImpl extends BaseService implements IWebSiteInfoService {

    @Autowired
    private SettingsDao settingsDao;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 获取网站的标题
     *
     * @return
     */
    @Override
    public ResponseResult getWebSiteTitle() {
        Setting oneByKey = settingsDao.findOneByKey(Constants.Settings.WEB_SITE_TITLE);
        if (oneByKey == null) {
            return ResponseResult.FAILED("网站标题获取失败");
        }
        return ResponseResult.SUCCESS("网站标题获取成功").setData(oneByKey);
    }

    /**
     * 更新网站的标题
     *
     * @return
     */
    @Override
    public ResponseResult updateWebSiteTitle(String title) {
//        找出来
        if (TextUtils.isEmpty(title)) {
            return ResponseResult.FAILED("网站标题不能为空");
        }
        Setting titleFromDb = settingsDao.findOneByKey(Constants.Settings.WEB_SITE_TITLE);
        if (titleFromDb == null) {
            titleFromDb = new Setting();
            titleFromDb.setId(snowflakeIdWorker.nextId() + "");
            titleFromDb.setKey(Constants.Settings.WEB_SITE_TITLE);
            titleFromDb.setCreateTime(new Date());
            titleFromDb.setUpdateTime(new Date());
        }
        titleFromDb.setValue(title);
        settingsDao.save(titleFromDb);
        return ResponseResult.SUCCESS("网站标题更新成功").setData(titleFromDb);
    }

    /**
     * 获取网站seo信息
     *
     * @return
     */
    @Override
    public ResponseResult getSeoInfo() {
        Setting descFromDb = settingsDao.findOneByKey(Constants.Settings.WEB_SITE_DESCRIPTION);
        Setting keywordFromDb = settingsDao.findOneByKey(Constants.Settings.WEB_SITE_KEYWORD);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put(descFromDb.getKey(), descFromDb.getValue());
        resultMap.put(keywordFromDb.getKey(), keywordFromDb.getValue());
        return ResponseResult.SUCCESS("seo信息获取成功").setData(resultMap);
    }

    /**
     * 修改seo 信息
     *
     * @param keywords
     * @param description
     * @return
     */
    @Override
    public ResponseResult putSeoInfo(String keywords, String description) {
        if (TextUtils.isEmpty(keywords)) {
            return ResponseResult.FAILED("网站关键字不能为空");
        }
        if (TextUtils.isEmpty(description)) {
            return ResponseResult.FAILED("seo描述不能为空");
        }
        Setting descFromDb = settingsDao.findOneByKey(Constants.Settings.WEB_SITE_DESCRIPTION);
        if (descFromDb == null) {
            descFromDb = new Setting();
            descFromDb.setId(snowflakeIdWorker.nextId() + "");
            descFromDb.setCreateTime(new Date());
            descFromDb.setUpdateTime(new Date());
            descFromDb.setKey(Constants.Settings.WEB_SITE_DESCRIPTION);
        }
        descFromDb.setValue(description);
        settingsDao.save(descFromDb);

        Setting keywordFromDb = settingsDao.findOneByKey(Constants.Settings.WEB_SITE_KEYWORD);
        if (keywordFromDb == null) {
            keywordFromDb = new Setting();
            keywordFromDb.setId(snowflakeIdWorker.nextId() + "");
            keywordFromDb.setCreateTime(new Date());
            keywordFromDb.setUpdateTime(new Date());
            keywordFromDb.setKey(Constants.Settings.WEB_SITE_KEYWORD);

        }
        keywordFromDb.setValue(keywords);
        settingsDao.save(keywordFromDb);

        return ResponseResult.SUCCESS("seo信息修改成功");
    }

    /**
     * 获取统计信息 view_count
     * TODO:拦截器===分类别统计，详细的
     * 这里只统计浏览量，只统计文章的浏览量，提供一个浏览量的统计接口（页面级别的）
     *
     * @return
     */
    @Override
    public ResponseResult getWebSiteViewCount() {
        Setting viewCountFromDb = settingsDao.findOneByKey(Constants.Settings.WEB_SITE_VIEW_COUNT);
        if (viewCountFromDb == null) {
            viewCountFromDb = new Setting();
            viewCountFromDb.setId(snowflakeIdWorker.nextId() + "");
            viewCountFromDb.setKey(Constants.Settings.WEB_SITE_VIEW_COUNT);
            viewCountFromDb.setUpdateTime(new Date());
            viewCountFromDb.setCreateTime(new Date());
            viewCountFromDb.setValue("1");
            settingsDao.save(viewCountFromDb);
        }
        Map<String, Integer> result = new HashMap<>();
        result.put(viewCountFromDb.getKey(),Integer.valueOf(viewCountFromDb.getValue()));
        return ResponseResult.SUCCESS("网站浏览量获取成功").setData(result);
    }
}
