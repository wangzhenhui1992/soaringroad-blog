package com.soaringroad.blog.dao.impl;

import org.springframework.stereotype.Component;

import com.soaringroad.blog.core.SrBlogContextContainer;
import com.soaringroad.blog.dao.AbstractSrBlogDao;
import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.repository.SrBlogH2Repository;
import com.soaringroad.blog.repository.h2.ArticleH2Repository;

@Component
public class ArticleDao extends AbstractSrBlogDao<Article, Long> {

    @Override
    protected SrBlogH2Repository<Article, Long> getH2Repository() {
        return SrBlogContextContainer.getBean(ArticleH2Repository.class);
    }
}
