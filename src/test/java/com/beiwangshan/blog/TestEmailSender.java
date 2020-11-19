package com.beiwangshan.blog;

/**
 * @className: com.beiwangshan.blog-> TestEmailSender
 * @description: 测试邮箱发送
 * @author: 曾豪
 * @createDate: 2020-11-19 10:35
 * @version: 1.0
 * @todo:
 */

import com.beiwangshan.blog.utils.EmailSender;

import javax.mail.MessagingException;

/**
 * 发送邮件
 */
public class TestEmailSender {

    public static void main(String[] args) throws MessagingException {
        EmailSender.subject("测试邮件发送")
                .from("北忘山博客系统")
                .text("这是发送的内容：ab12rf")
                .to("beiwangshan@yeah.net")
                .send();
    }
}