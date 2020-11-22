package com.beiwangshan.blog.service.Impl;

import com.beiwangshan.blog.utils.EmailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @className: com.beiwangshan.blog.service-> TaskService
 * @description: 异步服务类
 * @author: 曾豪
 * @createDate: 2020-11-19 19:18
 * @version: 1.0
 * @todo:
 */
@Service
public class TaskService {

    @Async
    public void SendEmailVerifyCode(String verifyCode,String emailAddress) throws Exception{
        EmailSender.sendRegisterVerifyCode(verifyCode, emailAddress);
    }
}
