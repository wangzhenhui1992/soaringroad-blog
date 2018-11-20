package com.soaringroad.blog.repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.query.SrBlogEsRestQueryBuilder;
import com.soaringroad.blog.vo.SrBlogQueryEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * // TODO 暂时作为ArticleEsRepository使用，之后需要抽象化
 * </pre>
 * 
 * @author wangzhenhui1992
 * @since 2018/11/19
 */
@Slf4j
@Component
public class ElasticSearchRepositoryImpl implements ElasticSearchRepository {
    @Autowired
    private RestHighLevelClient restClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean indexArticle(Article article) {
        String source = null;
        try {
            source = objectMapper.writeValueAsString(article);
        } catch (JsonProcessingException e) {
            log.warn("JSON化失败: " + article, e);
            return false;
        }
        IndexRequest request = new IndexRequest().index("article").type("all").id(article.getId().toString())
                .source(source, XContentType.JSON);
        IndexResponse response = null;
        try {
            response = restClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("无法完成Index请求", e);
            return false;
        }
        // TODO response解析
        return true;
    }

    @Override
    public List<Article> searchArticle(SrBlogQueryEntity entity) {
        SearchSourceBuilder sourceBuilder = new SrBlogEsRestQueryBuilder(entity).build();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("article").types("all").source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = this.restClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("无法完成Search请求", e);
            return null;
        }
        // TODO response解析
        List<Article> articles = buildArticles(response);
        return articles;
    }

    @Override
    public boolean indexArticles(Iterable<Article> articles) {
        final BulkRequest bulkRequest = new BulkRequest();
        articles.forEach(article -> {
            String source = null;
            try {
                source = objectMapper.writeValueAsString(article);
            } catch (JsonProcessingException e) {
                log.warn("JSON化失败: " + article, e);
                return;
            }
            IndexRequest request = new IndexRequest().index("article").type("all").id(article.getId().toString())
                    .source(source, XContentType.JSON);
            bulkRequest.add(request);
        });
        BulkResponse response = null;
        try {
            response = restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info(response.buildFailureMessage());
        } catch (IOException e) {
            log.error("无法完成BULK请求", e);
            return false;
        }
        // TODO response解析
        return true;
    }

    @PostConstruct
    public void init() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest();
        getIndexRequest.indices("article");
        boolean isExist = restClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        if (isExist) {
            return;
        }
        CreateIndexRequest createIndexRequest = new CreateIndexRequest();
        createIndexRequest.index("article");
        createIndexRequest
                .settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 1));
        restClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    @PreDestroy
    public void destroy() {
        log.info("正在关闭restClient");
        if (restClient == null) {
            return;
        }
        try {
            restClient.close();
        } catch (IOException e) {
            log.warn("无法关闭restClient");
        }
    }

    private List<Article> buildArticles(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        List<Article> articles = Arrays.asList(hits).stream().map(hit -> {
            Article article = null;
            try {
                article = objectMapper.readValue(hit.getSourceAsString(), Article.class);
            } catch (Exception e) {
                log.warn("无法反JSON化", e);
                return null;
            }
            return article;
        }).collect(Collectors.toList());
        return articles;
    }
}
