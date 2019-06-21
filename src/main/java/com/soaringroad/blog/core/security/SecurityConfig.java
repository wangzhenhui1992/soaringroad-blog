/******************************************************************
 *   _____                  _             _____                 _  *
 *  / ____|                (_)           |  __ \               | | *
 * | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
 *  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
 *  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
 * |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
 *                                   __/ |                         *
 *                                  |___/                          *
 * Copyright ©2017-2019 www.soaringroad.com | All rights reserved. *
 ******************************************************************/
package com.soaringroad.blog.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.filter.CorsFilter;

import com.soaringroad.blog.core.CoreConfig;
import com.soaringroad.blog.service.AuthService;

/**
 * <pre>
 * 安全相关的构建类
 * </pre>
 * 
 * @author wangzhenhui1992
 * @since 2019/01/24
 */
@EnableWebSecurity
@AutoConfigureAfter(CoreConfig.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthService srAuthService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new SrBlogAuthenticationProvider(srAuthService)).eraseCredentials(false);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().authorizeRequests().antMatchers("/api/admin/login").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll().antMatchers(HttpMethod.GET).permitAll().anyRequest()
                .authenticated().and().sessionManagement().disable().csrf().disable().logout().and()
//                .addFilter(new JwtLoginFilter(srAuthService,objectMapper))
                .addFilterAfter(new JwtAuthenticationFilter(this.authenticationManager()), CorsFilter.class);
    }

}
