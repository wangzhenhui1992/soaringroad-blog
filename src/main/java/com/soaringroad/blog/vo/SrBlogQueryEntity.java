package com.soaringroad.blog.vo;

import lombok.Data;

@Data
public class SrBlogQueryEntity {
	SrBlogQueryCondition[] queryConditions;
	SrBlogQuerySort[] querySort;
	String[] fetchSource;
	Integer queryNumber;
	Integer pageNumber;
	String entityName;
}
