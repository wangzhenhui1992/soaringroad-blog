package com.soaringroad.blog.util;

public final class SrBlogConsts {

	private SrBlogConsts() {}
	
	/**
	 * Redis : 网站流量
	 */
	public static final String REDIS_KEY_VIEW_COUNT = "viewcount";
	
	/**
	 * Redis : 文章流量
	 */
	public static final String REDIS_KEY_ARTICLE_VIEW_COUNT = "articleviewcount";
}
