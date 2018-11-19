package com.soaringroad.blog.setting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties("app.data.redis")
@Data
public class RedisInfo {

	private String hostName;
	private int port;
	private int database;
	private String password;
	private long timeout;
}
