package com.soaringroad.blog.entity.common;

import static com.soaringroad.blog.util.SrBlogConsts.ENTITY_KEY_ARTICLE;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soaringroad.blog.dao.impl.ArticleDao;
import com.soaringroad.blog.entity.AbstractSrBlogEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity(name = "article")
public class Article extends AbstractSrBlogEntity{

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
    private Long view;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签
     */
    @ElementCollection(targetClass=String.class, fetch=FetchType.EAGER)
    private List<String> labels;
    
    /**
     * 标签
     */
    @ElementCollection(targetClass=String.class, fetch=FetchType.EAGER)
    private List<String> keywords;

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
    @Column(length=10000)
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
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<ArticleDao> daoClass() {
        return ArticleDao.class;
    }

    @Override
    public String redisKey() {
        return String.format(ENTITY_KEY_ARTICLE, id);
    }

}
