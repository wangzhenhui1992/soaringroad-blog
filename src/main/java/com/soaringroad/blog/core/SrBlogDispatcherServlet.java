package com.soaringroad.blog.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

public class SrBlogDispatcherServlet extends DispatcherServlet{
	
//	private static final Logger LOGGER = LoggerFactory.getLogger(SrBlogDispatcherServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.doDispatch(request, response);
	}

}
