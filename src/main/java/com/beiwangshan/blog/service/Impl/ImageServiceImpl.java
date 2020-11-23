package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.response.ResponseResult;
import com.beiwangshan.blog.service.IImageService;
import com.beiwangshan.blog.utils.Constants;
import com.beiwangshan.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;

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
public class ImageServiceImpl implements IImageService {

private static final String imagePath = "E:\\javaProjects\\blog\\code\\images";

    /**
     * 上传图片
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
        //TODO:正确的判断上传的格式，允许其放行
        if (!Constants.Image.IMAGE_PNG.equals(contentType) &&
                !Constants.Image.IMAGE_GIF.equals(contentType) &&
                !Constants.Image.IMAGE_JPG.equals(contentType)) {
            return ResponseResult.FAILED("不支持此图片类型");
        }
//        获取相关数据，比如文件类型，文件名字
        String originalFilename = file.getOriginalFilename();
        log.info("file.getOriginalFilename() ==> " +originalFilename);
//        TODO:根据自定义命名规则进行命名
        File targetFile = new File(imagePath+File.separator+originalFilename);
        log.info("targetFile === > " +targetFile);
//        保存文件
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.FAILED("服务器异常，请稍后重试");
        }
//        记录文件
//        返回结果
        return ResponseResult.SUCCESS("图片上传成功");
    }
}
