/******************************************************************
 *   _____                  _             _____                 _  *
 *  / ____|                (_)           |  __ \               | | *
 * | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
 *  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
 *  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
 * |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
 *                                   __/ |                         *
 *                                  |___/                          *
 * Copyright ©2017-2020 www.soaringroad.com | All rights reserved. *
 ******************************************************************/
package com.soaringroad.blog.repository.es;

import com.soaringroad.blog.vo.SrBlogAuthInfo;
import com.soaringroad.blog.vo.SrBlogEsNode;
import com.soaringroad.blog.vo.SrBlogKeyStoreInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClient.FailureListener;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;

/**
 * <pre>
 * 
 * </pre>
 */
@Configuration
@Slf4j
@ConditionalOnProperty(name = "app.data.elasticsearch.enable", havingValue = "true")
public class EsConfiguration {

  @Autowired
  private ElasticSearchInfo elasticSearchInfo;

  /***** ElasticSearch *****/
  @Bean
  public RestHighLevelClient restHighLevelClient() {
    RestClientBuilder restClientBuilder = restClientBuilder();
    return restClientBuilder == null ? null : new RestHighLevelClient(restClientBuilder);
  }

  // @Bean(initMethod="init",destroyMethod = "destroy")
  // public ElasticSearchRepository elasticSearchRepository(RestHighLevelClient
  // restHighLevelClient) {
  // return ElasticSearchRepositoryImpl.of(restHighLevelClient);
  // }

  private RestClientBuilder restClientBuilder() {
    HttpHost[] httpHosts = buildHttpHosts();
    if (httpHosts == null || httpHosts.length == 0) {
      return null;
    }
    RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);
    restClientBuilder.setDefaultHeaders(buildHeaders());
    restClientBuilder.setFailureListener(buildFailureListener());
    restClientBuilder.setMaxRetryTimeoutMillis(30000);
    restClientBuilder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);
    // 暂时不需要
    // restClientBuilder.setPathPrefix("");
    // restClientBuilder.setHttpClientConfigCallback(buildHttpClientConfigCallback());
    // restClientBuilder.setRequestConfigCallback(buildRequestConfigCallback());
    return restClientBuilder;
  }

  private HttpHost[] buildHttpHosts() {
    List<SrBlogEsNode> esNodes = null;
    if (elasticSearchInfo == null || (esNodes = elasticSearchInfo.getNodes()) == null || esNodes.isEmpty()) {
      log.warn("没有可用的ElasticSearch节点。");
      return null;
    }
    log.info("ElasticSearch节点: " + esNodes);
    HttpHost[] httpHosts = new HttpHost[esNodes.size()];
    httpHosts = esNodes.stream().map(node -> new HttpHost(node.getHostname(), node.getPort(), node.getScheme()))
        .collect(Collectors.toList()).toArray(httpHosts);
    return httpHosts;
  }

  private Header[] buildHeaders() {
    Header[] defaultHeaders = new Header[] { new BasicHeader("Content-Type", "application/json") };
    return defaultHeaders;
  }

  private FailureListener buildFailureListener() {
    FailureListener failueListener = new FailureListener() {

      @Override
      public void onFailure(Node node) {
        log.info(String.format("ElasticSearch通信失败: nodename=%s, host=%s, port=%d, version=%s", node.getName(),
            node.getHost().getHostName(), node.getHost().getPort(), node.getVersion()));
        super.onFailure(node);
      }

    };
    return failueListener;
  }

  private NodeSelector buildNodeSelector() {
    return new NodeSelector() {

      private final Logger LOG = LoggerFactory.getLogger(NodeSelector.class);

      @Override
      public void select(Iterable<Node> nodes) {
        Iterator<Node> iterator = null;
        if (nodes == null || !(iterator = nodes.iterator()).hasNext()) {
          LOG.warn("ElasticSearch: 没有任何节点.");
          return;
        }
        Node node = null;
        while (iterator.hasNext() && (node = iterator.next()) != null) {
          if (node.getName().startsWith("Rp")) {
            iterator.remove();
          }
        }
      }
    };
  }

  private HttpClientConfigCallback buildHttpClientConfigCallback() {
    // 生成SSLContext
    SSLContext sslContext = buildSSLContext();
    // 生成CredentialsProvider
    CredentialsProvider credentialsProvider = buildCredentialsProvider();
    return new HttpClientConfigCallback() {
      @Override
      public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
        // HTTPS SSL通信
        httpClientBuilder.setSSLContext(sslContext);

        // 用户名密码认证通信
        httpClientBuilder.disableAuthCaching();
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

        // TOKEN认证通信
        List<Header> defaultHeaders = new ArrayList<>();
        defaultHeaders.add(new BasicHeader("Authenication", elasticSearchInfo.getAuthInfo().getToken()));
        httpClientBuilder.setDefaultHeaders(defaultHeaders);

        // 线程设置
        httpClientBuilder.setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build());
        return httpClientBuilder;
      }
    };
  }

  private CredentialsProvider buildCredentialsProvider() {
    SrBlogAuthInfo srBlogAuthInfo = elasticSearchInfo.getAuthInfo();
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(AuthScope.ANY,
        new UsernamePasswordCredentials(srBlogAuthInfo.getUsername(), srBlogAuthInfo.getPassword()));
    return credentialsProvider;
  }

  private SSLContext buildSSLContext() {
    SrBlogKeyStoreInfo srBlogKeyStoreInfo = elasticSearchInfo.getKeyStoreInfo();
    SSLContext sslContext = null;
    try {
      KeyStore truststore = KeyStore.getInstance(srBlogKeyStoreInfo.getKeyStoreName());
      try (InputStream is = Files.newInputStream(Paths.get(srBlogKeyStoreInfo.getKeyStoreFilePath()))) {
        truststore.load(is, srBlogKeyStoreInfo.getKeyStorePassword().toCharArray());
      }
      SSLContextBuilder sslBuilder = SSLContexts.custom().loadTrustMaterial(truststore, null);
      sslContext = sslBuilder.build();
    } catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException
        | IOException e) {
      log.error("ElasticSearch: SSL设定失败", e);
    }
    return sslContext;
  }
  //
  // private RequestConfigCallback buildRequestConfigCallback() {
  // return new RequestConfigCallback() {
  //
  // @Override
  // public Builder customizeRequestConfig(Builder requestConfigBuilder) {
  // return requestConfigBuilder;
  // }
  // };
  // }
}
