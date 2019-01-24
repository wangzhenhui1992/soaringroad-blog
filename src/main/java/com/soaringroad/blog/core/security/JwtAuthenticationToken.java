/******************************************************************
 *   _____                  _             _____                 _  *
 *  / ____|                (_)           |  __ \               | | *
 * | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
 *  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
 *  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
 * |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
 *                                   __/ |                         *
 *                                  |___/                          *
 * Copyright ©2017-2019 www.soaringroad.com | All rights reserved. *
 ******************************************************************/
package com.soaringroad.blog.core.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * <pre>
 * JWT认证Token
 * </pre>
 * @author wangzhenhui1992
 * @since 2019/01/24
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    /**
     * 
     */
    private static final long serialVersionUID = 5774193347034635923L;
    private String credentials;
    private SrBlogUserDetail userDetail;
    
    public JwtAuthenticationToken(SrBlogUserDetail  userDetail) {
        super(null);
        this.credentials = null;
        this.userDetail = userDetail;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return userDetail;
    }
    
}
