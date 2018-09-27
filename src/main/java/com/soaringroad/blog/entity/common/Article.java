package com.soaringroad.blog.entity.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soaringroad.blog.dao.impl.ArticleDao;
import com.soaringroad.blog.entity.AbstractSrBlogEntity;
import com.soaringroad.blog.entity.es.ArticleEs;
import com.soaringroad.blog.entity.h2.ArticleH2;
import static com.soaringroad.blog.util.SrBlogConsts.ENTITY_KEY_ARTICLE;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Article extends AbstractSrBlogEntity {
	
	/**
	 * 文章ID
	 */
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<ArticleDao> daoClass() {
		return ArticleDao.class;
	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public  ArticleH2 toH2Entity() {
		ArticleH2 articleH2 = new ArticleH2();
		articleH2.setAuthor(author);
		articleH2.setCategory(category);
		articleH2.setContent(content);
		articleH2.setDate(date);
		articleH2.setId(id);
		articleH2.setImage(image);
		articleH2.setLabels(labels);
		articleH2.setSummary(summary);
		articleH2.setTitle(title);
		articleH2.setVersion(version);
		return articleH2;
	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArticleEs toEsEntity(Serializable ids) {
		ArticleEs articleEs = new ArticleEs();
		articleEs.setAuthor(author);
		articleEs.setCategory(category);
		articleEs.setContent(content);
		articleEs.setDate(date);
			articleEs.setId(ids == null ? id :(Long)ids);
		articleEs.setImage(image);
		articleEs.setLabels(labels);
		articleEs.setSummary(summary);
		articleEs.setTitle(title);
		articleEs.setVersion(version);
		return articleEs;
	}


	@Override
	public String redisKey() {
		return String.format(ENTITY_KEY_ARTICLE, id);
	}
}
