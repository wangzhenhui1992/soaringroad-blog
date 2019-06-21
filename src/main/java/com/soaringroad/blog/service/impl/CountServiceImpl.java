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
package com.soaringroad.blog.service.impl;

import com.soaringroad.blog.common.CacheRepository;
import com.soaringroad.blog.service.CountService;
import com.soaringroad.blog.util.SrBlogConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 
 * </pre>
 */
@Component
public class CountServiceImpl implements CountService {
  
  @Autowired
  private CacheRepository cacheRepository;


  /**
   * {@inheritDoc}
   */
  @Override
  public void countArticle(Long id) {
    cacheRepository.increaseValue(String.format(SrBlogConsts.REDIS_KEY_ARTICLE_VIEW_COUNT, id), 1);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Long getArticleCount(Long id) {
    return null;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void countView(String ip) {
    String redisKey = String.format(SrBlogConsts.REDIS_KEY_VIEW_IP, ip);
    Object obj = cacheRepository.getValue(redisKey);
    if (obj != null) {
        return;
    }
    cacheRepository.increaseValue(SrBlogConsts.REDIS_KEY_VIEW_COUNT, 1);
    cacheRepository.setValue(redisKey, 1);
    cacheRepository.expire(redisKey, 300);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Long getCount() {
    return null;
  }

  
}
