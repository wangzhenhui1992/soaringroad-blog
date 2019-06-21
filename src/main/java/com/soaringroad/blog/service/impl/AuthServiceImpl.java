package com.soaringroad.blog.service.impl;
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
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.soaringroad.blog.service.AuthService;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 认证服务实现类
 * </pre>
 * @author wangzhenhui1992
 * @since 2019/01/24
 */
@Slf4j
@Component
public class AuthServiceImpl implements AuthService {

    @Value("${app.secretkey}")
    private String secretKey;
    @Value("${app.logintimeout:86400000}")
    private long loginTimeout;
    @Value("${app.admin.username:tester-username}")
    private String userName;
    @Value("${app.admin.password:tester-password}")
    private String passowrd;

    @Override
    public String authonrize(@NotNull String username, @NotNull String password) {
        log.debug(String.format("登陆验证: %s %s", username, password));
        // TODO 从ES或者数据库获取用户名密码并验证
        if (!userName.equals(username) || !passowrd.equals(password)) {
            return null;
        }
        Algorithm hmac256 = Algorithm.HMAC256(secretKey);
        String token = JWT.create().withIssuer("login").withClaim("username", username)
                .withClaim("date", System.currentTimeMillis()).sign(hmac256);
        log.info(String.format("给用户<%s>签发Token<%s>", username, token));
        return token;
    }

    @Override
    public boolean authenticate(@NotNull String username, @NotNull String password) {
        return userName.equals(username) && passowrd.equals(password);
    }

    @Override
    public String authenticate(@NotNull String jwtToken) {
        Algorithm hmac256 = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(hmac256).withIssuer("login").build();
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(jwtToken);
        } catch (JWTVerificationException e) {
            log.error("JWT认证失败: token=" + jwtToken);
            return null;
        }
        String username = jwt.getClaim("username").asString();
        Long date = jwt.getClaim("date").asLong();
        if (username == null || !isUserExist(username) || isTokenTimeout(date)) {
            return null;
        }
        return username;

    }

    private boolean isUserExist(String username) {
        return userName.equals(username);
    }

    private boolean isTokenTimeout(Long date) {
        return System.currentTimeMillis() - date > loginTimeout;
    }

}
