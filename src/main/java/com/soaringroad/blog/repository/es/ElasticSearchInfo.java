package com.soaringroad.blog.repository.es;

import com.soaringroad.blog.vo.SrBlogAuthInfo;
import com.soaringroad.blog.vo.SrBlogEsNode;
import com.soaringroad.blog.vo.SrBlogKeyStoreInfo;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("app.data.elasticsearch")
@Data
@ConditionalOnProperty(name = "app.data.elasticsearch.enable", havingValue = "true")
public class ElasticSearchInfo {
  private List<SrBlogEsNode> nodes;
  private String cluster;
  private SrBlogAuthInfo authInfo;
  private SrBlogKeyStoreInfo keyStoreInfo;
  private boolean enable;
}
