package com.soaringroad.blog.entity.h2;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soaringroad.blog.entity.SrBlogH2Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * 文章
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "article")
public class ArticleH2 implements SrBlogH2Entity{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 文章ID
	 */
	@Id
	@GeneratedValue()
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
	private String[] labels;

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
