package com.soaringroad.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.repository.SrBlogRepository;
import com.soaringroad.blog.service.MigrationService;

@Component
public class MigrationServiceImpl implements MigrationService {

    @Autowired
    private SrBlogRepository<Article, Long> repository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void migrationToDynamoDB() {
        Iterable<Article> itr = repository.findAll();
        for (Article article : itr) {
            restTemplate.exchange(
                    "https://e5ccbz4gbi.execute-api.ap-northeast-1.amazonaws.com/test/admin/article/" + article.getId(),
                    HttpMethod.PUT, new HttpEntity<>(article), Article.class);
        }
    }

}
