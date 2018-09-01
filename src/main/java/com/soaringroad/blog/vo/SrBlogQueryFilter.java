package com.soaringroad.blog.vo;

import lombok.Data;

@Data
public class SrBlogQueryFilter {

	private String fieldName;
	private SrBlogQueryOptionEnum option;
	private String value;
}
