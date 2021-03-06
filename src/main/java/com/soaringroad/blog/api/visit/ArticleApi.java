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
package com.soaringroad.blog.api.visit;

import com.soaringroad.blog.common.AbstractRestApiService;
import com.soaringroad.blog.common.EntityObject;
import com.soaringroad.blog.entity.Article;
import com.soaringroad.blog.service.CountCacheService;
import com.soaringroad.blog.vo.SrBlogQueryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/article")
public class ArticleApi extends AbstractRestApiService<Article, Long> {
  
  @Autowired
  private CountCacheService countCacheService;

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkGet(Long id) {
        return true;
    }

    /**
     * {@inheritDoc}O
     */
    @Override
    protected boolean checkSearch(SrBlogQueryEntity q) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkCount() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected EntityObject callGet(Long id) {
      countCacheService.increaseArticleView(id);
      return super.callGet(id);
    }

}
