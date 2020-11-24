package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.dao.ImageDao;
import com.beiwangshan.blog.pojo.BwsUser;
import com.beiwangshan.blog.pojo.Image;
import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.BaseService;
import com.beiwangshan.blog.service.IImageService;
import com.beiwangshan.blog.service.IUserService;
import com.beiwangshan.blog.utils.Constants;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.beiwangshan.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @className: com.beiwangshan.blog.service.Impl-> ImageServiceImpl
 * @description: 图片相关事务的实现类
 * @author: 曾豪
 * @createDate: 2020-11-23 20:25
 * @version: 1.0
 * @todo:
 */
@Slf4j
@Service
@Transactional
public class ImageServiceImpl extends BaseService implements IImageService {

    @Value("${bws.blog.image.save-Path}")
    private String imagePath;

    @Value("${bws.blog.image-max-size}")
    private long imageMaxSize;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private IUserService userService;

    @Autowired
    private ImageDao imageDao;

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");


    /**
     * 上传图片
     * 上传的路径：可以配置，在配置文件里面配置
     * 上传内容：命名--> 可以用ID，每天一个文件夹保存
     * 保存记录到数据库里面
     * ID | 存储路径 | url | 原名称 | 创建日期 | 更新日期
     *
     * @param file
     * @return
     */
    @Override
    public ResponseResult uploadImage(MultipartFile file) {
//        判断文件类型，只支持图片的类型：JPG png
        if (file == null) {
            return ResponseResult.FAILED("图片不能为空");
        }
        String contentType = file.getContentType();
        if (TextUtils.isEmpty(contentType)) {
            return ResponseResult.FAILED("图片格式错误或不支持");
        }
        //        获取相关数据，比如文件类型，文件名字
        String originalFilename = file.getOriginalFilename();
        log.info("file.getOriginalFilename() ==> " + originalFilename);
//        图片类型
        String imageType = getType(contentType, originalFilename);
        if (imageType == null) {
            return ResponseResult.FAILED("不支持此图片类型");
        }

//        限制文件的大小 5M
        long size = file.getSize();
        log.info("imageMaxSize ==> " + imageMaxSize + "  size ==> " + size);
        if (size > imageMaxSize) {
            return ResponseResult.FAILED("图片最大仅支持 " + (imageMaxSize / 1024 / 1024) + "MB");
        }
//        创建图片的保存目录：
//            规则： 配置目录 + 日期 + 类型 + ID.类型
        long currentMillions = System.currentTimeMillis();
        String currentDay = simpleDateFormat.format(currentMillions);
        log.info("currentDay ==> " + currentDay);
        String dayPath = imagePath + File.separator + currentDay;
        log.info("dayPath ==> " + dayPath);
        File dayPathFile = new File(dayPath);
//        判断日期是否存在
        if (!dayPathFile.exists()) {
//            如果路径不存在，则创建路径
            dayPathFile.mkdirs();
        }
        String targetName = String.valueOf(snowflakeIdWorker.nextId());
        String targetPath = dayPath + File.separator + imageType + File.separator +
                targetName + "." + imageType;
        File targetFile = new File(targetPath);
        log.info("targetPath ==> " + targetPath);
//        判断类型是否存在
        if (!targetFile.getParentFile().exists()) {
            targetFile.mkdirs();
        }
//        保存文件
        try {
            if (dayPathFile.exists()) {
                dayPathFile.createNewFile();
            }
//        TODO:根据自定义命名规则进行命名
            log.info("targetFile === > " + targetFile);
            file.transferTo(targetFile);

//            返回结果：包含图片的名称和访问路径
//              第一个是访问的路径
            Map<String, String> result = new HashMap<>();
            String resultPath = currentMillions + "_" + targetName + "." + imageType;
//              第二个是名称 --> alt=“图片描述”，如果不写，前端可以使用名称作为描述
            result.put("id", resultPath);
            result.put("name", originalFilename);

            //TODO: 保存数据到数据库
            Image image = new Image();
            image.setContentType(contentType);
            image.setId(targetName);
            image.setCreateTime(new Date());
            image.setUpdateTime(new Date());
            image.setPath(targetFile.getPath());
            image.setName(originalFilename);
            image.setUrl(resultPath);
            image.setState("1");
            BwsUser bwsUser = userService.checkBwsUser();
            image.setUserId(bwsUser.getId());

            imageDao.save(image);
            return ResponseResult.SUCCESS("图片上传成功").setData(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        返回结果
        return ResponseResult.FAILED("服务器异常，请稍后重试");

    }

    private String getType(String contentType, String name) {
        String imageType = null;
        if (Constants.Image.IMAGE_PNG_WITH_PREFIX.equals(contentType) && name.endsWith(Constants.Image.IMAGE_PNG)) {
            imageType = Constants.Image.IMAGE_PNG;
        } else if (Constants.Image.IMAGE_GIF_WITH_PREFIX.equals(contentType) && name.endsWith(Constants.Image.IMAGE_GIF)) {
            imageType = Constants.Image.IMAGE_GIF;
        } else if (Constants.Image.IMAGE_JPG_WITH_PREFIX.equals(contentType) && name.endsWith(Constants.Image.IMAGE_JPG)) {
            imageType = Constants.Image.IMAGE_JPG;
        }
        return imageType;
    }

    /**
     * 获取图片，预览图片
     *
     * @param response
     * @param imageId
     * @return
     */
    @Override
    public void viewImage(HttpServletResponse response, String imageId) throws IOException {
//        TODO:根据尺寸来动态返回图片给前端
//        好处：减少带宽占用，传输速度更快
//        缺点：消耗后台cup资源
//        推荐做法：上传上来的时候，把图片复制成三个尺寸，大，中，小，根据尺寸范围，返回结果

//        配置的目录已知
//        需要日期
//        使用日期的时间戳_ID.类型
        String[] paths = imageId.split("_");
        String dayValue = paths[0];
        //        ID
        String name = paths[1];
        String dayValueFormat = simpleDateFormat.format(Long.parseLong(dayValue));
        log.info("dayValueFormat ==> " + dayValueFormat);
        //        需要类型
        String type = name.substring(name.length() - 3);
        String targetPath = imagePath + File.separator + dayValueFormat +
                File.separator + type + File.separator + name;
        log.info("targetPath === > " + targetPath);

        File file = new File(targetPath);
        OutputStream writer = null;
        FileInputStream fos = null;
        try {
            response.setContentType("image/png");
            writer = response.getOutputStream();
//            读取
            fos = new FileInputStream(file);
            byte[] buff = new byte[1024];
            int len;
            while ((len = fos.read(buff)) != -1) {
                writer.write(buff, 0, len);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (writer != null) {
                writer.close();
            }
        }

    }

    /**
     * 获取图片列表
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public ResponseResult listImages(int page, int size) {
        //参数检查
        page = checkPage(page);
        size = checkSize(size);
//        创建条件
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
//        查询数据
        Page<Image> allImages = imageDao.findAll(new Specification<Image>() {
            @Override
            public Predicate toPredicate(Root<Image> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("state").as(String.class),"1");
            }
        },pageable);
//        返回结果
        return ResponseResult.SUCCESS("图片列表查询成功").setData(allImages);
    }
}
