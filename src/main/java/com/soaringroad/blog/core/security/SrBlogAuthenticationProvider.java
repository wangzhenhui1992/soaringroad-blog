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

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.soaringroad.blog.service.AuthService;

/**
 * <pre>
 * 认证逻辑
 * </pre>
 * @author wangzhenhui1992
 * @since 2019/01/24
 */
public class SrBlogAuthenticationProvider implements AuthenticationProvider {

    private AuthService srAuthService;

    public SrBlogAuthenticationProvider(AuthService srAuthService) {
        this.srAuthService = srAuthService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getCredentials().toString();
        Authentication result = getAuthentication(token);
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.equals(authentication);
    }

    private JwtAuthenticationToken getAuthentication(String token) {
        String username = srAuthService.authenticate(token);
        if (username == null) {
            throw new SrBlogAuthenticationException("JWT认证失败");
        }
        SrBlogUserDetail userDetail = new SrBlogUserDetail(username, "");
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userDetail);
        jwtAuthenticationToken.setCredentials(token);
        jwtAuthenticationToken.setAuthenticated(true);
        return jwtAuthenticationToken;
    }
}
