package com.soaringroad.blog.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.repository.RedisRepository;
import com.soaringroad.blog.repository.SrBlogRepository;
import com.soaringroad.blog.util.SrBlogConsts;

@Component
public class TaskScheduler {

    @Autowired
    private RedisRepository redisRepository;
    
    @Autowired
    private SrBlogRepository<Article,Long> repository;
    
    @Scheduled(fixedDelay=3600000)
    public void execute() {
        Iterable<Article> itr = repository.findAll();
        for (Article article : itr) {
            Object obj = redisRepository.getValue(String.format(SrBlogConsts.REDIS_KEY_ARTICLE_VIEW_COUNT, article.getId()));
            if (obj == null) {
                continue;
            }
            article.setView(Long.valueOf(obj.toString()));
            repository.save(article);
        }
    }
}
