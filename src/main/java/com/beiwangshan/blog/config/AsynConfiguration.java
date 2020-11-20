package com.beiwangshan.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @className: com.beiwangshan.blog.config-> AsynConfiguration
 * @description: 异步发送邮箱配置类
 * @author: 曾豪
 * @createDate: 2020-11-19 19:12
 * @version: 1.0
 * @todo:
 */
@Configuration
@EnableAsync
public class AsynConfiguration {

    @Bean
    public Executor asynExcutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);//配置核心数量
        executor.setMaxPoolSize(10);
        executor.setThreadNamePrefix("bws_blog_task_word-");
        executor.setQueueCapacity(30);

        executor.initialize();
        return executor;
    }
}
