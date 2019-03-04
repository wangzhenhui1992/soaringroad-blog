package com.soaringroad.blog.util;

public final class SrBlogConsts {

    private SrBlogConsts() {
    }

    //------------------------------------------------------------------//
    // Entity
    //-------------------------------------------------------------------//
    
    /**
     * Entity : 文章
     */
    public static final String ENTITY_KEY_ARTICLE = "article:%s";

    //------------------------------------------------------------------//
    // Redis 
    //-------------------------------------------------------------------//
    
    /**
     * Redis : 网站流量
     */
    public static final String REDIS_KEY_VIEW_COUNT = "viewcount";

    /**
     * Redis : 文章流量
     */
    public static final String REDIS_KEY_ARTICLE_VIEW_COUNT = "articleviewcount:%s";
    
    /**
     * Redis : 流量IP
     */
    public static final String REDIS_KEY_VIEW_IP = "ip:%s";

    //------------------------------------------------------------------//
    // HTTP
    //-------------------------------------------------------------------//
    
    /**
     * IPAddress : unknown
     */
    public static final String IP_ADDRESS_UNKNOWN = "unknown";

    /**
     * HttpHeader : X-Forwarded-For[Squid 服务代理]
     */
    public static final String HTTP_HEADER_X_FORWARDED_FOR = "X-Forwarded-For";
    
    /**
     * HttpHeader : Proxy-Client-IP[Apache 服务代理]
     */
    public static final String HTTP_HEADER_PROXY_CLIENT_IP = "Proxy-Client-IP";
    
    /**
     * HttpHeader : WL-Proxy-Client-IP[Weblogic 服务代理]
     */
    public static final String HTTP_HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    
    /**
     * HttpHeader : X-Real-IP[Nginx 服务代理]
     */
    public static final String HTTP_HEADER_X_Real_IP = "X-Real-IP";

    /**
     * HttpHeader : HTTP_CLIENT_IP[未知代理]
     */
    public static final String HTTP_HEADER_HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    
    //------------------------------------------------------------------//
    // API
    //-------------------------------------------------------------------//
    /**
     * API : /api/
     */
    public static final String API_PATTERN = "/api/";
    
    //------------------------------------------------------------------//
    // URL
    //-------------------------------------------------------------------//
    public static final String ARTICTLE_URL_TEMPLATE = "https://www.soaringroad.com/article/%d";
    public static final String LABEL_URL_TEMPLATE = "https://www.soaringroad.com/label/%s";
    public static final String CATEGORY_URL_TEMPLATE = "https://www.soaringroad.com/category/%s";
    public static final String HOME_URL = "https://www.soaringroad.com";
    public static final String INTRODUCTION_URL = "https://www.soaringroad.com/myself";
    public static final String BAIDU_COMMIT_URL_TEMPLATE = "http://data.zz.baidu.com/urls?site=https://www.soaringroad.com&token=%s";
    
}
