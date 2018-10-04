package com.soaringroad.blog.repository.h2;

import org.springframework.stereotype.Component;

import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.repository.SrBlogH2Repository;

@Component
public interface ArticleH2Repository extends SrBlogH2Repository<Article, Long> {

}
