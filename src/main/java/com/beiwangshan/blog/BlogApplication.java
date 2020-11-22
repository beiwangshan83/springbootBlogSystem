package com.beiwangshan.blog;

import com.beiwangshan.blog.utils.RedisUtils;
import com.beiwangshan.blog.utils.SnowflakeIdWorker;
import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;


/**
 * @author 曾豪
 */
@SpringBootApplication
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    /**
     * 注入雪花算法
     * @return
     */
    @Bean
    public SnowflakeIdWorker createIdWoker() {
        return new SnowflakeIdWorker(0, 0);
    }

    /**
     * 注入 BCryptPasswordEncoder密码验证
     * @return
     */
    @Bean
    public BCryptPasswordEncoder createPasswordEncode() {
        return new BCryptPasswordEncoder();
    }

    /**
     * redis
     * @return
     */
    @Bean
    public RedisUtils createRedisUtil() {
        return new RedisUtils();
    }

    /**
     * 随机数
     * @return
     */
    @Bean
    public Random createRandom() {
        return new Random();
    }

    /**
     * Gson
     * @return
     */
    @Bean
    public Gson createGson(){
        return new Gson();
    }

}

