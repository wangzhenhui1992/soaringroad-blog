package com.soaringroad.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.repository.SrBlogRepository;
import com.soaringroad.blog.service.MigrationService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MigrationServiceImpl implements MigrationService {

    @Autowired
    private SrBlogRepository<Article, Long> repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void migrationToDynamoDB() {
        Iterable<Article> itr = repository.findAll();
        for (Article article : itr) {
            String jsonStr = null;
            try {
                jsonStr = objectMapper.writeValueAsString(article);
            } catch (JsonProcessingException e) {
                log.error("JSON序列化异常", e);
            }
            restTemplate.exchange("https://e5ccbz4gbi.execute-api.ap-northeast-1.amazonaws.com/test/admin/article/{1}",
                    HttpMethod.PUT, new HttpEntity<>(jsonStr), Void.class, article.getId());
        }
    }

}
