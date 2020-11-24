package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.LopperDao;
import com.beiwangshan.blog.pojo.Looper;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.BaseService;
import com.beiwangshan.blog.service.ILooperService;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.beiwangshan.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * @className: com.beiwangshan.blog.service.Impl-> LooperServiceImpl
 * @description: 轮播图的实现类
 * @author: 曾豪
 * @createDate: 2020-11-24 10:20
 * @version: 1.0
 * @todo:
 */
@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class LooperServiceImpl extends BaseService implements ILooperService {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private LopperDao lopperDao;

    /**
     * 添加轮播图
     *
     * @param looper
     * @return
     */
    @Override
    public ResponseResult addLoop(Looper looper) {
//        获取数据
        String title = looper.getTitle();
        String imageUrl = looper.getImageUrl();
        String targetUrl = looper.getTargetUrl();
//        检查数据
        if (TextUtils.isEmpty(title)) {
                return ResponseResult.FAILED("轮播图标题不能为空");
        }
        if (TextUtils.isEmpty(imageUrl)) {
            return ResponseResult.FAILED("轮播图图片链接地址不能为空");
        }
        if (TextUtils.isEmpty(targetUrl)) {
            return ResponseResult.FAILED("轮播图跳转链接地址不能为空");
        }
//        补充数据
        looper.setId(snowflakeIdWorker.nextId()+"");
        looper.setCreateTime(new Date());
        looper.setUpdateTime(new Date());
        looper.setOrder(looper.getOrder());
        looper.setState(looper.getState());
//        保存数据
        Looper saveLooper = lopperDao.save(looper);
//        返回结果
        return ResponseResult.SUCCESS("轮播图保存成功").setData(saveLooper);
    }
}
