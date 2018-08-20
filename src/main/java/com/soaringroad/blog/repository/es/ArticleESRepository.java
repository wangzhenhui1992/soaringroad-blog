package com.soaringroad.blog.repository.es;

import org.springframework.stereotype.Component;

import com.soaringroad.blog.entity.es.ArticleEs;
import com.soaringroad.blog.repository.SrBlogEsRepository;

@Component
public interface ArticleESRepository extends SrBlogEsRepository<ArticleEs, Integer> {

}
