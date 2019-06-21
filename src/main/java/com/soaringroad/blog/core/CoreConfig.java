/******************************************************************
 *   _____                  _             _____                 _  *
 *  / ____|                (_)           |  __ \               | | *
 * | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
 *  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
 *  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
 * |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
 *                                   __/ |                         *
 *                                  |___/                          *
 * Copyright ©2017-2020 www.soaringroad.com | All rights reserved. *
 ******************************************************************/
package com.soaringroad.blog.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpHeaderNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@EnableScheduling
public class CoreConfig {

    @Value("${app.allowcros}")
    private boolean allowCors;

    /***** DispatcherServlet *****/
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new SrBlogDispatcherServlet();
    }

    /***** ObjectMapper *****/
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    
    /***** RestTemplate *****/
    @Bean
    public RestTemplate restTemplate(HttpMessageConverters httpConverters) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(httpConverters.getConverters());
        return restTemplate;
    }

    /***** CorsFilter *****/
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
    
}
