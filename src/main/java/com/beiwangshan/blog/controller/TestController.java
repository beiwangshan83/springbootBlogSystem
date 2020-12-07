package com.beiwangshan.blog.controller;

import com.beiwangshan.blog.dao.CommentDao;
import com.beiwangshan.blog.dao.LabelDao;
import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.pojo.Comment;
import com.beiwangshan.blog.pojo.Label;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.service.Impl.TestSolrService;
import com.beiwangshan.blog.utils.Constants;
import com.beiwangshan.blog.utils.CookieUtils;
import com.beiwangshan.blog.utils.RedisUtils;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.awt.*;
import java.util.Date;
import java.util.List;

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
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String helloWorld() {
        String redisCode = (String) redisUtils.get(Constants.User.KEY_CAPTCHA_CONTENT + "123456");
        log.info("hello world...redisCode ===> " + redisCode);
        return "hello world";
    }

    @ResponseBody
    @GetMapping("/test-json")
    public String testJson() {
        return "hello world";
    }

    /**
     * 增加的联系
     *
     * @param label
     * @return
     */
    @ResponseBody
    @PostMapping("/label")
    public ResponseResult addLabel(@RequestBody Label label) {

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
     *
     * @param labelId
     * @return
     */

    @DeleteMapping("/label/{labelId}")
    public ResponseResult delLabel(@PathVariable("labelId") String labelId) {
        int deleteResult = labelDao.deleteOneById(labelId);
        log.info("deleteResult ==>  " + deleteResult);
        if (deleteResult > 0) {
            return ResponseResult.SUCCESS("删除成功");
        } else {
            return ResponseResult.FAILED("删除失败");
        }
    }

    /**
     * 修改
     *
     * @param labelId
     * @param label
     * @return
     */
    @PutMapping("/label/{labelId}")
    public ResponseResult updateLabel(@PathVariable("labelId") String labelId, @RequestBody Label label) {
        Label dblabel = labelDao.findOneById(labelId);
        if (dblabel == null) {
            return ResponseResult.FAILED("标签不存在");

        }
        dblabel.setCount(label.getCount());
        dblabel.setName(label.getName());
        dblabel.setUpdateTime(new Date());
        labelDao.save(dblabel);
        return ResponseResult.SUCCESS("标签更新成功");
    }

    /**
     * 按照ID查询label
     *
     * @param labelId
     * @return
     */
    @GetMapping("/label/{labelId}")
    public ResponseResult getLabelById(@PathVariable("labelId") String labelId) {
        Label dblabel = labelDao.findOneById(labelId);
        if (dblabel == null) {
            return ResponseResult.FAILED("标签不存在");
        }
        return ResponseResult.SUCCESS("查询成功").setData(dblabel);
    }

//    分页

    /**
     * label 的分页 加上排序
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/label/list/{page}/{size}")
    public ResponseResult listLabels(@PathVariable("page") int page, @PathVariable("size") int size) {
//        判断page是否大于0
        if (page < 1) {
            page = 1;
        }
//        判断size的大小是否符合规范
        if (size <= 0) {
            size = Constants.Page.DEFAULT_SIZE;
        }
//        排序
//        Sort sort= new Sort(Sort.Direction.DESC,"createTime");
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

//        page-1 因为后台是从0 开始计算的，前端是 1 的时候，后台 是 0
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Label> result = labelDao.findAll(pageable);
        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

    /**
     * 模糊查询
     * @param keyword
     * @param count
     * @return
     */
    @GetMapping("/label/search")
    public ResponseResult doLabelSearch(@RequestParam("keyword") String keyword, @RequestParam("count") String count) {
        List<Label> all = labelDao.findAll(new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate namePre = cb.like(root.get("name").as(String.class), "%" + keyword + "%");
                Predicate countPre = cb.equal(root.get("count").as(Integer.class), count);
                Predicate and = cb.and(namePre, countPre);
                return and;
            }
        });
        if (all.size() == 0 || all == null) {
            return ResponseResult.FAILED("结果为空").setData(all);
        }
        return ResponseResult.SUCCESS("查找成功").setData(all);
    }

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 图灵验证码
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置字体 // 有默认字体，可以不用设置
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);

        String content = specCaptcha.text().toLowerCase();
        log.info("图灵验证码 ==> " + content);

//        保存在redis里
        redisUtils.set(Constants.User.KEY_CAPTCHA_CONTENT + "123456", content, 60 * 10);

        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private IUserService userService;

    /**
     * 评论的测试
     *
     * @param comment
     * @param request
     * @return
     */
    @PostMapping("/comment")
    public ResponseResult testComment(@RequestBody Comment comment, HttpServletRequest request, HttpServletResponse response) {
        String content = comment.getContent();
        log.info("comment content ==> " + content);
//        需要知道是谁的评论，对评论进行身份验证
        String tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKEN_KEY);
        log.info("tokenKey ==>" + tokenKey);
        if (tokenKey == null) {
            return ResponseResult.FAILED("账号未登录");
        }

        String redisToken = (String) redisUtils.get(Constants.User.KEY_TOKEN+tokenKey);
        log.info("comment == >redisToken ==> "+redisToken);

        BwsUser bwsUser =  userService.checkBwsUser();
        log.info("检查登录之后的user ===> "+ bwsUser);

        if (bwsUser == null) {
            return ResponseResult.FAILED("账号未登录");
        }
        comment.setUserId(bwsUser.getId());
        comment.setUserAvatar(bwsUser.getAvatar());
        comment.setUserName(bwsUser.getUserName());
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        comment.setId(String.valueOf(snowflakeIdWorker.nextId()));
        commentDao.save(comment);
        return ResponseResult.SUCCESS("评论成功");
    }

    @Autowired
    private TestSolrService solrService;

    @PostMapping("/solr")
    public ResponseResult solrAdd(){
        solrService.add();
        return ResponseResult.SUCCESS("solr添加成功");

    }
}
