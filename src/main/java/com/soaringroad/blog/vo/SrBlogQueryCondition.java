package com.soaringroad.blog.vo;

import lombok.Data;

@Data
public class SrBlogQueryCondition {
	private String name;
	private SrBlogQueryOptionEnum option;
	private String value;
}
