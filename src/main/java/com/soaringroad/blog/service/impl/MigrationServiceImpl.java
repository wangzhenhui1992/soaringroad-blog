package com.soaringroad.blog.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaringroad.blog.common.DataManager;
import com.soaringroad.blog.entity.Article;
import com.soaringroad.blog.service.MigrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
@Slf4j
public class MigrationServiceImpl implements MigrationService {

    @Value("${app.migration.api-gateway:localhost}")
    private String apiGateway;

    @Autowired
    private DataManager<Article, Long> articleManage;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    private static final HttpHeaders HTTP_HEADERS;
    static {
        HTTP_HEADERS = new HttpHeaders();
        HTTP_HEADERS.setContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void migrationToDynamoDB() {
        Iterable<Article> itr = articleManage.findAll();

        for (Article article : itr) {
            insertIntoDynamoDB(article);
        }
    }

    @Override
    public void migrationArticleToDynamoDB(Long id) {
        Article article = articleManage.findById(id);
        if (article == null) {
            return;
        }
        insertIntoDynamoDB(article);
    }

    private void insertIntoDynamoDB(Article article) {
        if (CollectionUtils.isEmpty(article.getKeywords())) {
            article.setKeywords(Arrays.asList(article.getCategory()));
        }
        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(article);
        } catch (JsonProcessingException e) {
            log.error("JSON序列化异常", e);
            return;
        }
        log.info(jsonStr);
        restTemplate.exchange(apiGateway, HttpMethod.PUT, new HttpEntity<>(jsonStr, HTTP_HEADERS), Void.class,
                article.getId());
    }

}
