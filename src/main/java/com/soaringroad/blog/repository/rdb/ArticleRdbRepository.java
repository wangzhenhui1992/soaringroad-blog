package com.soaringroad.blog.repository.rdb;

import com.soaringroad.blog.common.RdbRepository;
import com.soaringroad.blog.entity.Article;
import org.springframework.stereotype.Component;

@Component
public interface ArticleRdbRepository extends RdbRepository<Article, Long> {

  /**
   * {@inheritDoc}
   */
  @Override
  default Article newEntity() {
    return new Article();
  }
}
