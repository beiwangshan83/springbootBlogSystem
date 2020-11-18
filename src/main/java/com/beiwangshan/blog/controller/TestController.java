package com.beiwangshan.blog.controller;

import com.beiwangshan.blog.dao.LabelDao;
import com.beiwangshan.blog.pojo.Label;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.utils.Contants;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Date;

@Slf4j
@RestController
@Transactional
@RequestMapping("/test")
public class TestController {

    @Autowired
    SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private LabelDao labelDao;

    @ResponseBody
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String helloWorld(){
        log.info("hello world...");
        return "hello world";
    }

    @ResponseBody
    @GetMapping("/test-json")
    public String testJson(){
        return "hello world";
    }

    /**
     * 增加的联系
     * @param label
     * @return
     */
    @ResponseBody
    @PostMapping("/label")
    public ResponseResult addLabel(@RequestBody Label label ){

//        判断数据是否正确，有效

//        补全数据
        label.setId(String.valueOf(snowflakeIdWorker.nextId()));
        label.setCreateTime(new Date());
        label.setUpdateTime(new Date());
//        保存数据
        labelDao.save(label);
//        return null;
        return ResponseResult.SUCCESS("保存成功");
    }

    /**
     * 删除
     * @param labelId
     * @return
     */

    @DeleteMapping("/label/{labelId}")
    public ResponseResult delLabel(@PathVariable("labelId")String labelId){
        int deleteResult =  labelDao.deleteOneById(labelId);
        log.info("deleteResult ==>  "+ deleteResult);
        if (deleteResult > 0) {
            return ResponseResult.SUCCESS("删除成功");
        }else{
            return ResponseResult.FAILD("删除失败");
        }
    }

    /**
     * 修改
     * @param labelId
     * @param label
     * @return
     */
    @PutMapping("/label/{labelId}")
    public ResponseResult updateLabel(@PathVariable("labelId")String labelId,@RequestBody Label label ){
        Label dblabel = labelDao.findOneById(labelId);
        if (dblabel == null) {
            return ResponseResult.FAILD("标签不存在");

        }
        dblabel.setCount(label.getCount());
        dblabel.setName(label.getName());
        dblabel.setUpdateTime(new Date());
        labelDao.save(dblabel);
        return ResponseResult.SUCCESS("标签更新成功");
    }

    /**
     * 按照ID查询label
     * @param labelId
     * @return
     */
    @GetMapping("/label/{labelId}")
    public ResponseResult getLabelById(@PathVariable("labelId")String labelId){
        Label dblabel = labelDao.findOneById(labelId);
        if (dblabel == null) {
            return ResponseResult.FAILD("标签不存在");
        }
        return ResponseResult.SUCCESS("查询成功").setData(dblabel);
    }

//    分页

    /**
     * label 的分页 加上排序
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/label/list/{page}/{size}")
    public ResponseResult listLabels(@PathVariable("page")int page,@PathVariable("size")int size){
//        判断page是否大于0
        if (page<1){
            page = 1;
        }
//        判断size的大小是否符合规范
        if (size<=0){
            size = Contants.DEFAULT_SIZE;
        }
//        排序
//        Sort sort= new Sort(Sort.Direction.DESC,"createTime");
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");

//        page-1 因为后台是从0 开始计算的，前端是 1 的时候，后台 是 0
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Page<Label> result =  labelDao.findAll(pageable);
        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

}
