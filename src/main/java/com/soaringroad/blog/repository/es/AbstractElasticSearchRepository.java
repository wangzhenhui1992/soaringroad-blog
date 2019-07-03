package com.soaringroad.blog.repository.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaringroad.blog.common.AbstractEntity;
import com.soaringroad.blog.common.ElasticSearchRepository;
import com.soaringroad.blog.vo.SrBlogQueryEntity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

/**
 * <pre>
 * // 抽象ESRepositoy
 * </pre>
 * 
 * @author wangzhenhui1992
 * @since 2018/11/19
 */
@Slf4j
public abstract class AbstractElasticSearchRepository<T extends AbstractEntity, E extends Serializable>
                                                     implements ElasticSearchRepository<T, E>, InitializingBean {

  @Autowired
  private RestHighLevelClient restClient;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public boolean index(T entity) {
    String source = null;
    try {
      source = objectMapper.writeValueAsString(entity);
    } catch (JsonProcessingException e) {
      log.warn("JSON化失败: " + entity, e);
      return false;
    }
    IndexRequest request = new IndexRequest().index(entity.getEntityName()).type("all")
        .id(String.valueOf(entity.getEntityId())).source(source, XContentType.JSON);
    try {
      restClient.index(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      log.error("无法完成Index请求", e);
      return false;
    }
    return true;
  }

  @Override
  public boolean delete(T entity) {
    DeleteRequest request = new DeleteRequest().index(entity.getEntityName()).type("all")
        .id(String.valueOf(entity.getEntityId()));
    return this.delete(request);
  }

  private boolean delete(DeleteRequest request) {
    try {
      restClient.delete(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      log.error("无法完成Index请求", e);
      return false;
    }
    // TODO response解析
    return true;
  }

  @Override
  public List<T> search(SrBlogQueryEntity entity) {
    SearchSourceBuilder sourceBuilder = new SrBlogEsRestQueryBuilder(entity).build();
    SearchRequest searchRequest = new SearchRequest().indices(newEntity().getEntityName()).types("all")
        .source(sourceBuilder);
    return search(searchRequest);
  }

  private List<T> search(SearchRequest searchRequest) {
    SearchResponse response = null;
    try {
      response = this.restClient.search(searchRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      log.error("无法完成Search请求", e);
      return null;
    }
    // TODO response解析
    List<T> entities = buildArticles(response);
    return entities;
  }

  @Override
  public boolean indexEntities(Iterable<T> entities) {
    final BulkRequest bulkRequest = new BulkRequest();
    entities.forEach(entity -> {
      String source = null;
      try {
        source = objectMapper.writeValueAsString(entity);
      } catch (JsonProcessingException e) {
        log.warn("JSON化失败: " + entity, e);
        return;
      }
      IndexRequest request = new IndexRequest().index(entity.getEntityName()).type("all")
          .id(String.valueOf(entity.getEntityId())).source(source, XContentType.JSON);
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

  @SuppressWarnings("unchecked")
  private List<T> buildArticles(SearchResponse response) {
    SearchHits searchHits = response.getHits();
    SearchHit[] hits = searchHits.getHits();
    List<T> entities = Arrays.asList(hits).stream().map(hit -> {
      T entity = null;
      try {
        entity = (T) objectMapper.readValue(hit.getSourceAsString(), newEntity().getClass());
      } catch (Exception e) {
        log.warn("无法反JSON化", e);
        return null;
      }
      return entity;
    }).collect(Collectors.toList());
    return entities;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T get(E id) {
    T entity = this.newEntity();
    GetRequest request = new GetRequest().index(entity.getEntityName()).type("all").id(String.valueOf(id));
    try {
      GetResponse response = this.restClient.get(request, RequestOptions.DEFAULT);
      String source = response.getSourceAsString();
      if (StringUtils.isEmpty(source)) {
        return null;
      }
      entity = (T) objectMapper.readValue(source, entity.getClass());
    } catch (IOException e) {
      log.error("无法完成Get请求", e);
      return null;
    }
    return entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<T> findAll() {
    SrBlogQueryEntity queryEntity = new SrBlogQueryEntity();
    queryEntity.setEntityName(this.newEntity().getEntityName());
    queryEntity.setQueryNumber(10000);
    return search(queryEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    String indexName = this.newEntity().getEntityName();
    GetIndexRequest getIndexRequest = new GetIndexRequest();
    getIndexRequest.indices(indexName);
    boolean isExist = this.restClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    if (isExist) {
      return;
    }
    CreateIndexRequest createIndexRequest = new CreateIndexRequest();
    createIndexRequest.index(indexName);
    createIndexRequest.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 1));
    this.restClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    ALL_REPOSITORIES.add(this);
  }

}
