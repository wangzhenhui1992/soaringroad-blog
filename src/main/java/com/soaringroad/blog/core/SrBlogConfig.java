package com.soaringroad.blog.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.handler.codec.http.HttpHeaderNames;

@Configuration
public class SrBlogConfig {
	
	@Value("${app.allowcros}")
	private boolean allowCors;
	
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
			corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
			corsConfiguration.addExposedHeader(HttpHeaderNames.AUTHORIZATION.toString());
		}
		return corsConfiguration;
	}
}
