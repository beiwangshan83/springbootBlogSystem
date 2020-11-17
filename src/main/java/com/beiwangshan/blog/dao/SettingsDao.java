package com.beiwangshan.blog.dao;

import com.beiwangshan.blog.pojo.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @className: com.beiwangshan.blog.dao-> SettingsDao
 * @description: 关于设置的 dao
 * @author: 曾豪
 * @createDate: 2020-11-18 1:12
 * @version: 1.0
 * @todo:
 */
public interface SettingsDao extends JpaRepository<Setting,String>, JpaSpecificationExecutor<Setting> {

    Setting findOneByKey(String key);

}
