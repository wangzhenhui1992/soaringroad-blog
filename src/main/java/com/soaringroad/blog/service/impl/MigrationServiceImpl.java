package com.soaringroad.blog.service.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
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

    @Value("${app.migration.api-gateway:localhost}")
    private String apiGateway;

    @Autowired
    private SrBlogRepository<Article, Long> repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    private static final HttpHeaders httpHeaders;
    static {
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void migrationToDynamoDB() {
        Iterable<Article> itr = repository.findAll();

        for (Article article : itr) {
            if (CollectionUtils.isEmpty(article.getKeywords())) {
                article.setKeywords(Arrays.asList(article.getCategory()));
            }
            String jsonStr = null;
            try {
                jsonStr = objectMapper.writeValueAsString(article);
            } catch (JsonProcessingException e) {
                log.error("JSON序列化异常", e);
                continue;
            }
            log.info(jsonStr);
            restTemplate.exchange(apiGateway, HttpMethod.PUT, new HttpEntity<>(jsonStr, httpHeaders), Void.class,
                    article.getId());
        }
    }

}
