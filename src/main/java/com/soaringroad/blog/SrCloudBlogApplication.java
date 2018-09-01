package com.soaringroad.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 云博客应用主程序
 * 
 * @author wangzhenhui1992
 */
@SpringBootApplication(scanBasePackages = { "com.soaringroad.blog" })
public class SrCloudBlogApplication {

	/**
	 * Main方法
	 * @param args 参数
	 */
	public static void main(String[] args) {
		SpringApplication.run(SrCloudBlogApplication.class, args);
	}
}
