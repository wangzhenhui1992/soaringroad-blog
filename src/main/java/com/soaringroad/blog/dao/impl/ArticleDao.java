package com.soaringroad.blog.dao.impl;

import org.springframework.stereotype.Component;

import com.soaringroad.blog.core.SrBlogContextContainer;
import com.soaringroad.blog.dao.AbstractSrBlogDao;
import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.entity.es.ArticleEs;
import com.soaringroad.blog.entity.h2.ArticleH2;
import com.soaringroad.blog.repository.SrBlogEsRepository;
import com.soaringroad.blog.repository.SrBlogH2Repository;
import com.soaringroad.blog.repository.es.ArticleESRepository;
import com.soaringroad.blog.repository.h2.ArticleH2Repository;

@Component
public class ArticleDao extends AbstractSrBlogDao<Article,ArticleEs,ArticleH2,Long>{
	

	@Override
	protected SrBlogEsRepository<ArticleEs, Long> getEsRepository() {
		return SrBlogContextContainer.getBean(ArticleESRepository.class);
	}

	@Override
	protected SrBlogH2Repository<ArticleH2, Long> getH2Repository() {
		return SrBlogContextContainer.getBean(ArticleH2Repository.class);
	}
	
}
