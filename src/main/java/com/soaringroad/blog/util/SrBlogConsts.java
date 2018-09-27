package com.soaringroad.blog.util;

public final class SrBlogConsts {

	private SrBlogConsts() {}
	
	/**
	 * Entity : 文章
	 */
	public static final String ENTITY_KEY_ARTICLE = "article:%s";
	
	/**
	 * Redis : 网站流量
	 */
	public static final String REDIS_KEY_VIEW_COUNT = "viewcount";
	
	/**
	 * Redis : 文章流量
	 */
	public static final String REDIS_KEY_ARTICLE_VIEW_COUNT = "articleviewcount";
}
