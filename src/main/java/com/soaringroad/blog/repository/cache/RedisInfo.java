package com.soaringroad.blog.repository.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties("app.data.redis")
@Data
@ConditionalOnProperty(name = "app.data.redis.enable", havingValue = "true")
public class RedisInfo {

	private String hostName;
	private int port;
	private int database;
	private String password;
	private long timeout;
	private boolean enable;
}
