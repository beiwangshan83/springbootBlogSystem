package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.LopperDao;
import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.pojo.Looper;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.BaseService;
import com.beiwangshan.blog.service.ILooperService;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.beiwangshan.blog.utils.TextUtils;
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

    @Autowired
    private IUserService userService;

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
        looper.setId(snowflakeIdWorker.nextId() + "");
        looper.setCreateTime(new Date());
        looper.setUpdateTime(new Date());
        looper.setOrder(looper.getOrder());
        looper.setState(looper.getState());
//        保存数据
        Looper saveLooper = lopperDao.save(looper);
//        返回结果
        return ResponseResult.SUCCESS("轮播图保存成功").setData(saveLooper);
    }

    /**
     * 获取轮播图 单个
     *
     * @param loopId
     * @return
     */
    @Override
    public ResponseResult getLooper(String loopId) {
        Looper oneById = lopperDao.findOneById(loopId);
        if (oneById == null) {
            return ResponseResult.FAILED("轮播图不存在");
        }
        return ResponseResult.SUCCESS("轮播图查询成功").setData(oneById);
    }

    /**
     * 获取轮播图列表 多个
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public ResponseResult listLooper(int page, int size) {
        page = checkPage(page);
        size = checkSize(size);
        BwsUser bwsUser = userService.checkBwsUser();
        if (bwsUser == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
//        创建条件
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Looper> allLooper = lopperDao.findAll(pageable);
        return ResponseResult.SUCCESS("轮播图列表查询成功").setData(allLooper);
    }

    /**
     * 更新轮播图 单个
     *
     * @param loopId
     * @return
     */
    @Override
    public ResponseResult updateLooper(String loopId, Looper looper) {
//        找出数据
        Looper oneById = lopperDao.findOneById(loopId);
        if (oneById == null) {
            return ResponseResult.FAILED("轮播图不存在");
        }
//        不可以为空的要判空，可以为空的直接设置
        String title = looper.getTitle();
        if (!TextUtils.isEmpty(title)) {
            oneById.setTitle(title);
        }
        String imageUrl = looper.getImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            oneById.setImageUrl(imageUrl);
        }
        String targetUrl = looper.getTargetUrl();
        if (!TextUtils.isEmpty(targetUrl)) {
            oneById.setTargetUrl(targetUrl);
        }
        if (!TextUtils.isEmpty(looper.getState())) {
            oneById.setState(looper.getState());
        }
        oneById.setOrder(looper.getOrder());
        oneById.setUpdateTime(new Date());

//        保存数据
        Looper save = lopperDao.save(oneById);

//        返回结果
        return ResponseResult.SUCCESS("轮播图更新成功").setData(save);
    }

    /**
     * 删除轮播图
     *
     * @param loopId
     * @return
     */
    @Override
    public ResponseResult deleteLooper(String loopId) {
        int result = lopperDao.deleteOneById(loopId);
        if (result == 0){
            return ResponseResult.FAILED("轮播图删除失败");
        }
        return ResponseResult.SUCCESS("轮播图删除成功");
    }
}
