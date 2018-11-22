package com.soaringroad.blog.repository;

import java.util.List;

import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.vo.SrBlogQueryEntity;

public interface ElasticSearchRepository {

    boolean indexArticles(Iterable<Article> articles);

    boolean indexArticle(Article article);

    List<Article> searchArticle(SrBlogQueryEntity entity);

    boolean deleteArticle(Article article);

}
