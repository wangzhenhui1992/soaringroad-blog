/******************************************************************
 *   _____                  _             _____                 _  *
 *  / ____|                (_)           |  __ \               | | *
 * | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
 *  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
 *  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
 * |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
 *                                   __/ |                         *
 *                                  |___/                          *
 * Copyright ©2017-2020 www.soaringroad.com | All rights reserved. *
 ******************************************************************/
package com.soaringroad.blog.core;

import com.soaringroad.blog.common.CacheRepository;
import com.soaringroad.blog.repository.rdb.ArticleRdbRepository;
import com.soaringroad.blog.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 
 * </pre>
 */
@Component
public class CacheApplicationRunner  implements ApplicationRunner  {
  @Autowired
  private ArticleRdbRepository articleRepository;
  
  @Autowired
  private CacheRepository cacheRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public void run(ApplicationArguments args) throws Exception {
    articleRepository.findAll().forEach(article->{
      cacheRepository.setValue(EntityUtil.getCacheKey(article), article.getView());
    });
  }

}
