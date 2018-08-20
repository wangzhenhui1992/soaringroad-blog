package com.soaringroad.blog.vo;

import lombok.Data;

@Data
public class SrBlogQuerySort {
	private String name;
	private SrBlogQuerySortOrderEnum sortOrder;
}
