package com.soaringroad.blog.vo;

import lombok.Data;

@Data
public class SrBlogQueryEntity {
	SrBlogQueryCondition[] queryConditions;
	SrBlogQuerySort[] querySort;
	Integer queryNumber;
	Integer pageNumber;
}
