/******************************************************************
*   _____                  _             _____                 _  *
*  / ____|                (_)           |  __ \               | | *
* | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
*  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
*  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
* |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
*                                   __/ |                         *
*                                  |___/                          *
* Copyright ©2017-2018 www.soaringroad.com | All rights reserved. *
******************************************************************/
package com.soaringroad.blog.manager;

import com.soaringroad.blog.common.AbstractDataManager;
import com.soaringroad.blog.entity.Article;
import com.soaringroad.blog.util.SrBlogConsts;
import com.soaringroad.blog.vo.SrBlogQueryEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ArticleManager extends AbstractDataManager<Article, Long> {
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Page<Article> search(SrBlogQueryEntity queryEntity) {
    return super.search(queryEntity).map(article->{
      Object obj = this.cacheRepository.getValue(String.format(SrBlogConsts.REDIS_KEY_ARTICLE_VIEW_COUNT,article.getId()));
      if (obj != null) {
        article.setView(Long.valueOf(String.valueOf(obj)));
      }
      return article;
    });
  }
  
}
