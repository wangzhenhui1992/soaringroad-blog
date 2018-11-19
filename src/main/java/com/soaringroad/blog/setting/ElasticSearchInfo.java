package com.soaringroad.blog.setting;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.soaringroad.blog.vo.SrBlogAuthInfo;
import com.soaringroad.blog.vo.SrBlogEsNode;
import com.soaringroad.blog.vo.SrBlogKeyStoreInfo;

import lombok.Data;

@Component
@ConfigurationProperties("app.data.elasticsearch")
@Data
public class ElasticSearchInfo {
    private List<SrBlogEsNode> nodes;
    private String cluster;
    private SrBlogAuthInfo authInfo;
    private SrBlogKeyStoreInfo keyStoreInfo;
}
