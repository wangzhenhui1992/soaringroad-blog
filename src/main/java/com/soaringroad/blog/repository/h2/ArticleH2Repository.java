package com.soaringroad.blog.repository.h2;

import org.springframework.stereotype.Component;

import com.soaringroad.blog.entity.h2.ArticleH2;
import com.soaringroad.blog.repository.SrBlogH2Repository;

@Component
public interface ArticleH2Repository extends SrBlogH2Repository<ArticleH2, Long> {

}
