package com.soaringroad.blog.entity.es;

import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soaringroad.blog.entity.SrBlogEsEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * 文章
 * </pre>
 */
@Document(indexName = "article", shards = 2, type = "main")
@Data
@EqualsAndHashCode(callSuper=false)
public class ArticleEs implements SrBlogEsEntity{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 文章ID
	 */
	@Id
	private Long id;

	/**
	 * 文章版本
	 */
	private Long version;

	/**
	 * 分类
	 */
	private String category;

	/**
	 * 标签
	 */
	@ElementCollection(targetClass=String.class)
	private List<String> labels;

	/**
	 * 文章标题
	 */
	private String title;

	/**
	 * 概要
	 */
	private String summary;

	/**
	 * 主题图片
	 */
	private String image;

	/**
	 * 文章内容
	 */
	private String content;

	/**
	 * 作者
	 */
	@NotNull
	private String author;

	/**
	 * 发表日期
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date date;

}
