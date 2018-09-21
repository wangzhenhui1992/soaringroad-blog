package com.soaringroad.blog.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaringroad.blog.data.info.RedisInfo;

import io.netty.handler.codec.http.HttpHeaderNames;

@Configuration
public class SrBlogConfig {

	@Value("${app.allowcros}")
	private boolean allowCors;

	@Autowired
	private RedisInfo redisInfo;

	@Bean
	public DispatcherServlet dispatcherServlet() {
		return new SrBlogDispatcherServlet();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean()
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig());
		return new CorsFilter(source);
	}

	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		if (allowCors) {
			corsConfiguration.addAllowedOrigin(CorsConfiguration.ALL);
			corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
			corsConfiguration.addAllowedHeader(HttpHeaderNames.AUTHORIZATION.toString());
			corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
			corsConfiguration.addExposedHeader(HttpHeaderNames.AUTHORIZATION.toString());
		}
		return corsConfiguration;
	}

	@Bean()
	public  RedisConnectionFactory redisConnectionFactory() {
		// TODO RedisPoolConfigure
		// JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		// RedisStandaloneConfiguration
		RedisStandaloneConfiguration redisStandaloneConfiguration = buildRedisStandaloneConfiguration();
		return new JedisConnectionFactory(redisStandaloneConfiguration);
	}
	
	@Bean()
	public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisFactory) {

		// RedisJsonSerializer
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
				Object.class);

		// RedisTemplate
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisFactory);
		redisTemplate.setEnableDefaultSerializer(true);
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		return redisTemplate;
	}

	private RedisStandaloneConfiguration buildRedisStandaloneConfiguration() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(redisInfo.getHostName());
		redisStandaloneConfiguration.setPort(redisInfo.getPort());
		redisStandaloneConfiguration.setDatabase(redisInfo.getDatabase());
		redisStandaloneConfiguration.setPassword(RedisPassword.of(redisInfo.getPassword()));
		return redisStandaloneConfiguration;
	}

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheManager cacheManager = RedisCacheManager.create(connectionFactory);
		return cacheManager;
	}
}
