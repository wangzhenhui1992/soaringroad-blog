/******************************************************************
 *   _____                  _             _____                 _  *
 *  / ____|                (_)           |  __ \               | | *
 * | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
 *  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
 *  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
 * |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
 *                                   __/ |                         *
 *                                  |___/                          *
 * Copyright ©2017-2018 www.soaringroad.com | All rights reserved. *
 ******************************************************************/
package com.soaringroad.blog.core;

import com.soaringroad.blog.service.CountCacheService;
import com.soaringroad.blog.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * <pre>
 * 过滤器
 * </pre>
 * @author wangzhenhui1992
 * @since 
 */
@Component
@Slf4j
public class SrBlogFilter implements Filter {

    @Value("${app.allowcros}")
    private boolean allowCros;

    @Value("${app.auth}")
    private boolean auth;
    
    @Autowired
    private CountCacheService countService;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        // 请求URI
        String uri = httpRequest.getRequestURI();
        // 请求方法
        String method = httpRequest.getMethod();
        // IP
        String ip = httpRequest.getRemoteAddr();
        // 真实IP
        String realIp = HttpUtil.getRealIp(httpRequest);
		log.info(String.format("收到请求 : %s(%s) %s %s", ip, realIp, uri, method));
        // 处理请求，API对象和静态资源以外的请求转发到Angular前端
        if (HttpUtil.isNotApi(uri)) {
            httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // OPTIONS
        if (HttpMethod.OPTIONS.matches(method)) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
            return;
        }
        countService.increaseViewByIp(realIp);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

//    private boolean auth(HttpServletRequest request) {
//
//        if (!auth) {
//            return true;
//        }
//
//        String uri = request.getRequestURI();
//        // 管理功能API以外的请求可以通过
//        if (!uri.startsWith("/api/admin") || uri.equals("/api/admin/login") || uri.startsWith("/api/admin/druid") || uri.startsWith("/druid") ) {
//            return true;
//        }
//        String srToken = request.getHeader("Authorization");
//        log.info(srToken);
//        if (srToken == null || srToken.isEmpty()) {
//            return false;
//        }
//        return authService.auth(srToken);
//    }

}
