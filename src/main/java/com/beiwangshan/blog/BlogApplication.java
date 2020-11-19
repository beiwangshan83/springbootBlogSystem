package com.beiwangshan.blog;

import com.beiwangshan.blog.utils.RedisUtil;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    //    注入雪花算法
    @Bean
    public SnowflakeIdWorker createIdWoker() {
        return new SnowflakeIdWorker(0, 0);
    }

    //    注入 BCryptPasswordEncoder密码验证
    @Bean
    public BCryptPasswordEncoder createPasswordEncode() {
        return new BCryptPasswordEncoder();
    }

    //    redis
    @Bean
    public RedisUtil createRedisUtil() {
        return new RedisUtil();
    }

}

