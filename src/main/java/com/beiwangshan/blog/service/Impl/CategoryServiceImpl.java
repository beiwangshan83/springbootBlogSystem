package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.CategoryDao;
import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.pojo.Category;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.BaseService;
import com.beiwangshan.blog.service.ICategoryService;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.utils.Constants;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.beiwangshan.blog.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @className: com.beiwangshan.blog.service.Impl-> CategoryServiceImpl
 * @description: 文章分类接口实现类
 * @author: 曾豪
 * @createDate: 2020-11-23 14:35
 * @version: 1.0
 * @todo:
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class CategoryServiceImpl extends BaseService implements ICategoryService {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private IUserService userService;

    /**
     * 添加文章分类
     * * 添加分类的接口
     * * 需要管理员权限
     * * 需要添加的数据：
     * *      1.分类名称
     * *      2.分类的pinyin
     * *      3.顺序 默认是 1
     * *      4.描述
     *
     * @param category
     * @return
     */
    @Override
    public ResponseResult addCategory(Category category) {
//        检查数据
        if (TextUtils.isEmpty(category.getName())) {
            return ResponseResult.FAILED("分类名称不可以为空");
        }
        if (TextUtils.isEmpty(category.getPinyin())) {
            return ResponseResult.FAILED("分类拼音不可以为空");
        }
        if (TextUtils.isEmpty(category.getDescription())) {
            return ResponseResult.FAILED("分类的描述不可以为空");
        }
//        补全数据
        category.setId(snowflakeIdWorker.nextId() + "");
        category.setStatus("1");
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
//        保存数据
        categoryDao.save(category);
//        返回结果
        return ResponseResult.SUCCESS("分类保存成功");
    }

    /**
     * 获取分类信息
     *
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult getCategory(String categoryId) {
        Category oneById = categoryDao.findOneById(categoryId);
        if (oneById == null) {
            return ResponseResult.FAILED("分类不存在");
        }
        return ResponseResult.SUCCESS("分类获取成功").setData(oneById);
    }

    /**
     * 删除分类信息
     * 简单的改变分类的状态
     *
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult deleteCategory(String categoryId) {
        if (TextUtils.isEmpty(categoryId)) {
            return ResponseResult.FAILED("文章Id不能为空");
        }
        int result = categoryDao.deleteCategoryByUpdateStatus(categoryId);
        if (result == 0) {
            return ResponseResult.FAILED("分类删除失败");
        }
        return ResponseResult.SUCCESS("分类删除成功");
    }

    /**
     * 获取分类的列表
     *
     * @return
     */
    @Override
    public ResponseResult listCategories() {
        //参数检查
//        创建条件
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime", "order");
//        判断用户角色，普通用户/未登录用户，只能获取已经发布的文章（正常的文章），管理员账号可以拿到所有的
        BwsUser bwsUser = userService.checkBwsUser();
        String roles = bwsUser.getRoles();
        List<Category> categories;
        if (bwsUser == null || !Constants.User.ROLE_ADMIN.equals(roles)) {
            // 用户未登录，或者是非管理员的正常用户，只能获取到正常状态的文章
            categories = categoryDao.listCategoriesByState("1");

        }else{
            categories = categoryDao.findAll(sort);

        }

        return ResponseResult.SUCCESS("获取分类列表成功").setData(categories);
    }

    /**
     * 更新分类信息
     *
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult updateCategory(String categoryId, Category category) {
//        第一步是找出来
        Category categoryFromDb = categoryDao.findOneById(categoryId);
        if (categoryFromDb == null) {
            return ResponseResult.FAILED("分类不存在");
        }
//        第二步对内容进行判断
        String name = category.getName();
        if (!TextUtils.isEmpty(name)) {
            categoryFromDb.setName(name);
        }

        String pinyin = category.getPinyin();
        if (!TextUtils.isEmpty(pinyin)) {
            categoryFromDb.setPinyin(pinyin);
        }

        String description = category.getDescription();
        if (!TextUtils.isEmpty(description)) {
            categoryFromDb.setDescription(description);
        }
        categoryFromDb.setOrder(category.getOrder());
        categoryFromDb.setUpdateTime(new Date());
//        第三步是保存数据
        categoryDao.save(categoryFromDb);
//        第四步返回结果
        return ResponseResult.SUCCESS("更新成功");
    }
}
