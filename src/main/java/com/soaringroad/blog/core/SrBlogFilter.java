package com.soaringroad.blog.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

	private static List<String> matchList = Arrays.asList("/favicon.ico", "/index.html", "/inline.bundle.js",
			"/inline.bundle.js.map", "/main.bundle.js", "/main.bundle.js.map", "/polyfills.bundle.js",
			"/polyfills.bundle.js.map", "/styles.bundle.js", "/styles.bundle.js.map", "/vendor.bundle.js",
			"/vendor.bundle.js.map");

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		return;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String uri = httpRequest.getRequestURI();
		log.info(String.format("收到请求 : %s ", uri));

		// 允许跨域
		allowCros(response);

		// 管理功能API认证
		if (!auth(httpRequest)) {
			request.getRequestDispatcher("/login");
			return;
		}

		// 处理请求，API对象和静态资源以外的请求转发到Angular前端
		if (isNotMatchPatten(uri)) {
			request.getRequestDispatcher("/index.html").forward(request, response);
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		return;
	}

	private static boolean isNotMatchPatten(String uri) {
		return !matchList.contains(uri) && !uri.startsWith("/api/");
	}

	private void allowCros(ServletResponse response) {
		if (!allowCros) {
			return;
		}
		HttpServletResponse httpResponse = (HttpServletResponse) response;
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
