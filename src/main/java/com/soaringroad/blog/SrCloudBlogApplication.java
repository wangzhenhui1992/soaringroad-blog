package com.soaringroad.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

import com.amazonaws.ClientConfiguration;

/**
 * 云博客应用主程序
 * 
 * @author wangzhenhui1992
 */
@SpringBootApplication(scanBasePackages = { "com.soaringroad.blog" })
@EnableCaching
@EnableAutoConfiguration(exclude = { RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class })
public class SrCloudBlogApplication {

    /**
     * Main方法
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SrCloudBlogApplication.class, args);
    }
}
