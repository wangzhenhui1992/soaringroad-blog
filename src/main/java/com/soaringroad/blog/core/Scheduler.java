/******************************************************************
 *   _____                  _             _____                 _  *
 *  / ____|                (_)           |  __ \               | | *
 * | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
 *  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
 *  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
 * |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
 *                                   __/ |                         *
 *                                  |___/                          *
 * Copyright ©2017-2019 www.soaringroad.com | All rights reserved. *
 ******************************************************************/
package com.soaringroad.blog.core;

import com.soaringroad.blog.common.CacheRepository;
import com.soaringroad.blog.entity.Article;
import com.soaringroad.blog.entity.Setting;
import com.soaringroad.blog.manager.ArticleManager;
import com.soaringroad.blog.manager.SettingManager;
import com.soaringroad.blog.service.NotifyBaiduService;
import com.soaringroad.blog.util.SrBlogConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class Scheduler {

    @Autowired(required=false)
    private CacheRepository cacheRepository;

    @Autowired
    private ArticleManager articleDataManager;
    
    @Autowired
    private SettingManager settingManager;
    
    @Autowired
    private NotifyBaiduService notifyBaiduService;

    private static AtomicInteger counter = new AtomicInteger(0);

    @Scheduled(fixedDelay = 3600000, initialDelay=60000)
    public void execute() {
        log.info("定时Task启动");
        flushViewCount();
        
        List<String> urls = new ArrayList<String>();
        urls.add(SrBlogConsts.HOME_URL); 
        urls.add(SrBlogConsts.INTRODUCTION_URL); 
        Set<String> categories = new HashSet<String>();
        Set<String> labels = new HashSet<String>();
        Iterable<Article> itr = articleDataManager.findAll();
        itr.forEach(article->{
          flushViewCount(article);
          urls.add(String.format(SrBlogConsts.ARTICTLE_URL_TEMPLATE, article.getId()));
          categories.add(article.getCategory());
          if (!CollectionUtils.isEmpty(article.getLabels())) {
            labels.addAll(article.getLabels());
          }
        });
        if (counter.incrementAndGet() == 24) {
            notifyBaiduService.notifyBaidu(urls,categories,labels);
            counter.set(0);
        }
        log.info("定时Task结束");
    }
    
    private void flushViewCount() {
      Setting setting = settingManager.getSettingByName(SrBlogConsts.REDIS_KEY_VIEW_COUNT);
      if (setting == null) {
        setting = new Setting();
        setting.setName(SrBlogConsts.REDIS_KEY_VIEW_COUNT);
      }
      Object obj = cacheRepository.getValue(SrBlogConsts.REDIS_KEY_VIEW_COUNT);
      if (obj == null) {
        return;
      }
      setting.setValue(obj.toString());
      settingManager.save(setting);
    }

    private void flushViewCount(Article article) {
        Object obj = cacheRepository
                .getValue(String.format(SrBlogConsts.REDIS_KEY_ARTICLE_VIEW_COUNT, article.getId()));
        if (obj == null) {
            return;
        }
        article.setView(Long.valueOf(obj.toString()));
        articleDataManager.save(article);
    }
}
