package com.soaringroad.blog.restapi.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.repository.ElasticSearchRepository;
import com.soaringroad.blog.repository.h2.ArticleH2Repository;

import lombok.extern.slf4j.Slf4j;
@RestController
@RequestMapping(value = "/api/admin/index")
@Slf4j
public class AdminIndexApiService {

    @Autowired
    ArticleH2Repository articleRepository;
    @Autowired
    ElasticSearchRepository esRepository;
    
    @RequestMapping(value = {"/",""}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> index() {
        Iterable<Article> articleIterable = articleRepository.findAll();
        articleIterable.forEach(article->{
           log.info(article.toString()); 
        });
        boolean result = esRepository.indexArticles(articleIterable);
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }
}
