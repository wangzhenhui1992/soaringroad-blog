package com.soaringroad.blog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soaringroad.blog.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity(name = Article.ENTITY_NAME)
public class Article extends AbstractEntity{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    public static final String ENTITY_NAME = "article";

    /**
     * 文章ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator="idGenerator")
    @TableGenerator(name = "idGenerator",pkColumnValue="ARTICLE", allocationSize=1)
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
    @ElementCollection(targetClass=String.class, fetch=FetchType.LAZY)
    @LazyCollection(value=LazyCollectionOption.FALSE)
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

    @Override
    public long getEntityId() {
      return id;
    }


    @Override
    public String getEntityName() {
      return ENTITY_NAME;
    }

}
