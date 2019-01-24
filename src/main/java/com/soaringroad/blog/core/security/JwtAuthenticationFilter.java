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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import lombok.extern.slf4j.Slf4j;
/**
 * <pre>
 * JWT认证过滤器
 * </pre>
 * @author wangzhenhui1992
 * @since 2019/01/24
 */
@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter{

    protected JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new RegexRequestMatcher("^/api/admin/((?!login).)*$",null));
        this.setAuthenticationManager(authenticationManager);
    }

    private JwtAuthenticationToken getAuthentication(String token) {
        SrBlogUserDetail userDetail = new SrBlogUserDetail("", "");
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userDetail);
        jwtAuthenticationToken.setCredentials(token);
        return jwtAuthenticationToken;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("token="+token);
        
        Authentication authenticationToken = getAuthentication(token==null?"":token);
        authenticationToken = this.getAuthenticationManager().authenticate(authenticationToken);
        return authenticationToken;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        this.getRememberMeServices().loginSuccess(request, response, authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
    
}

