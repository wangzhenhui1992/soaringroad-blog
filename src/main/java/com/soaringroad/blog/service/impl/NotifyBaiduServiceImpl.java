package com.soaringroad.blog.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.repository.SrBlogRepository;
import com.soaringroad.blog.service.NotifyBaiduService;
import com.soaringroad.blog.util.SrBlogConsts;
import com.soaringroad.blog.vo.BaiduCommitResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NotifyBaiduServiceImpl implements NotifyBaiduService {
    @Autowired
    private RestTemplate restTemplate;

    @Value(value = "${app.baidu-commit-token}")
    private String baiduCommitToken;
    
    @Autowired
    private SrBlogRepository<Article, Long> repository;

    @Override
    public BaiduCommitResponse notifyBaidu(List<String> urls, Set<String> categories, Set<String> labels) {
        urls.addAll(categories.stream().map(category -> String.format(SrBlogConsts.CATEGORY_URL_TEMPLATE, category))
                .collect(Collectors.toList()));
        urls.addAll(labels.stream().map(label -> String.format(SrBlogConsts.LABEL_URL_TEMPLATE, label))
                .collect(Collectors.toList()));
        String target = String.format(SrBlogConsts.BAIDU_COMMIT_URL_TEMPLATE, baiduCommitToken);
        ResponseEntity<BaiduCommitResponse> responseEntity = restTemplate.postForEntity(target,
                String.join("\r\n", urls), BaiduCommitResponse.class);
        BaiduCommitResponse responseBody = responseEntity.getBody();
        if (responseEntity.getStatusCode().isError()) {
            log.error(responseBody.toString());
        } else {
            log.info(responseBody.toString());
        }
        return responseBody;
    }

    @Override
    public BaiduCommitResponse notifyBaidu() {
        log.info("定时Task启动");
        Iterable<Article> itr = repository.findAll();
        List<String> urls = new ArrayList<String>();
        urls.add(SrBlogConsts.HOME_URL);
        urls.add(SrBlogConsts.INTRODUCTION_URL);
        Set<String> categories = new HashSet<String>();
        Set<String> labels = new HashSet<String>();
        for (Article article : itr) {
            urls.add(String.format(SrBlogConsts.ARTICTLE_URL_TEMPLATE, article.getId()));
            categories.add(article.getCategory());
            labels.addAll(article.getLabels());
        }
        return this.notifyBaidu(urls,categories,labels);
    }

}
