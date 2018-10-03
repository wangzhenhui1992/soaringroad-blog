package com.soaringroad.blog.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.soaringroad.blog.repository.RedisRepository;
import com.soaringroad.blog.service.SrAuthService;
import com.soaringroad.blog.util.HttpUtil;
import com.soaringroad.blog.util.SrBlogConsts;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SrBlogFilter implements Filter {

    @Value("${app.allowcros}")
    private boolean allowCros;

    @Value("${app.auth}")
    private boolean auth;

    @Autowired
    private SrAuthService authService;
    
    @Autowired
    private RedisRepository redisRepository;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        return;
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
        // OPTIONS
        if ("OPTIONS".equals(method)) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
            return;
        }
        
        // 流量统计
		countView(realIp);
        
        // 允许跨域
        allowCros(httpResponse);

        // API认证
        if (!auth(httpRequest)) {
            
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            
//            request.getRequestDispatcher("/login").forward(request, httpResponse);
            return;
        }

        // 处理请求，API对象和静态资源以外的请求转发到Angular前端
        if (HttpUtil.isNotApi(uri)) {
            httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        return;
    }

    private void allowCros(HttpServletResponse httpResponse) {
        if (!allowCros) {
            return;
        }
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "*");
    }

    private boolean auth(HttpServletRequest request) {

        if (!auth) {
            return true;
        }

        String uri = request.getRequestURI();
        // 管理功能API以外的请求可以通过
        if (!uri.startsWith("/api/admin") || uri.equals("/api/admin/login") || uri.startsWith("/api/admin/druid") || uri.startsWith("/druid") ) {
            return true;
        }
        String srToken = request.getHeader("Authorization");
        log.info(srToken);
        if (srToken == null || srToken.isEmpty()) {
            return false;
        }
        return authService.auth(srToken);
    }
    
    private void countView(String realIp) {
    	String redisKey = String.format(SrBlogConsts.REDIS_KEY_VIEW_IP, realIp);
    	Object obj = redisRepository.getValue(redisKey);
    	if (obj != null) {
    		return;
    	}
    	redisRepository.increaseValue(SrBlogConsts.REDIS_KEY_VIEW_COUNT, 1);
    	redisRepository.setValue(redisKey, 1);
    	redisRepository.expire(redisKey, 300);
    }

}
