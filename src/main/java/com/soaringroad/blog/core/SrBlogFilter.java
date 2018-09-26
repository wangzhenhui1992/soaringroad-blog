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

import com.soaringroad.blog.service.SrAuthService;

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

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		return;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		String uri = httpRequest.getRequestURI();
		log.info(String.format("收到请求 : %s ", uri));
		// OPTIONS
		if ("OPTIONS".equals(httpRequest.getMethod())) {
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			chain.doFilter(request, response);
			return;
		}
		
		// 允许跨域
		allowCros(httpResponse);

		// API认证
		if (!auth(httpRequest)) {
			
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			
//			request.getRequestDispatcher("/login").forward(request, httpResponse);
			return;
		}

		// 处理请求，API对象和静态资源以外的请求转发到Angular前端
		if (isNotMatchPatten(uri)) {
			httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		return;
	}

	private static boolean isNotMatchPatten(String uri) {
		return !uri.startsWith("/api/");
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
		if (!uri.startsWith("/api/admin") || uri.equals("/api/admin/login")) {
			return true;
		}
		String srToken = request.getHeader("Authorization");
		log.info(srToken);
		if (srToken == null || srToken.isEmpty()) {
			return false;
		}
		return authService.auth(srToken);
	}

}
