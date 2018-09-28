package com.soaringroad.blog.util;

import javax.servlet.http.HttpServletRequest;

public final class HttpUtil {
    
    private HttpUtil() {};

    /**
     * 获取真实IP
     * @param request HTTPServletRequest
     * @return 真实IP
     */
    public static final String getRealIp(HttpServletRequest request) {
        String ip = null;
        // X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader(SrBlogConsts.HTTP_HEADER_X_FORWARDED_FOR);
        // Proxy-Client-IP：apache 服务代理
        if (isIpUnkown(ipAddresses)) {
            ipAddresses = request.getHeader(SrBlogConsts.HTTP_HEADER_PROXY_CLIENT_IP);
        }
        // WL-Proxy-Client-IP：weblogic 服务代理
        if (isIpUnkown(ipAddresses)) {
            ipAddresses = request.getHeader(SrBlogConsts.HTTP_HEADER_WL_PROXY_CLIENT_IP);
        }
        // X-Real-IP：nginx服务代理
        if (isIpUnkown(ipAddresses)) {
            ipAddresses = request.getHeader(SrBlogConsts.HTTP_HEADER_X_Real_IP);
        }
        // HTTP_CLIENT_IP：未知
        if (isIpUnkown(ipAddresses)) {
            ipAddresses = request.getHeader(SrBlogConsts.HTTP_HEADER_HTTP_CLIENT_IP);
        }
        if (!isIpUnkown(ipAddresses)) {
            ip = ipAddresses.split(",")[0];
        }
        if (isIpUnkown(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    public static final boolean isNotApi(String uri) {
        return !uri.startsWith(SrBlogConsts.API_PATTERN);
    }
    
    /**
     * 判断未知IP
     * @param ipAddresses IP地址
     * @return true:未知 false: 已知
     */
    private static final boolean isIpUnkown(String ipAddresses) {
        return ipAddresses == null || ipAddresses.isEmpty()|| SrBlogConsts.IP_ADDRESS_UNKNOWN.equalsIgnoreCase(ipAddresses);
    }
}
