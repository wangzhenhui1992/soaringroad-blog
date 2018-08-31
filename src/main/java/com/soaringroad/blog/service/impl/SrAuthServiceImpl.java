package com.soaringroad.blog.service.impl;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.soaringroad.blog.service.SrAuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SrAuthServiceImpl implements SrAuthService {

	@Value("${app.secretkey}")
	private String secretKey;
	@Value("${app.logintimeout:86400000}")
	private long loginTimeout;
	@Value("${app.admin.username:tester-username}")
	private String userName;
	@Value("${app.admin.password:tester-password}")
	private String passowrd;

	@Override
	public String login(@NotNull String username,@NotNull  String password) {
		log.debug(String.format("登陆验证: %s %s", username, password));
		// TODO 从ES或者数据库获取用户名密码并验证
		if (!userName.equals(username) || !passowrd.equals(password)) {
			return null;
		}
		Algorithm hmac256 = Algorithm.HMAC256(secretKey);
		String token = JWT.create().withIssuer("login").withClaim("username", username)
				.withClaim("date", System.currentTimeMillis()).sign(hmac256);
		log.info(String.format("给用户<%s>签发Token<%s>",username, token));
		return token;
	}

	@Override
	public boolean auth(@NotNull String jwtToken) {
		Algorithm hmac256 = Algorithm.HMAC256(secretKey);
		JWTVerifier verifier = JWT.require(hmac256).withIssuer("login").build();
		DecodedJWT jwt = null;
		try {
			jwt = verifier.verify(jwtToken);
		} catch (JWTVerificationException e) {
			log.error("JWT认证失败: token=" + jwtToken);
			return false;
		}
		String username = jwt.getClaim("username").asString();
		Long date = jwt.getClaim("date").asLong();
		if (username == null || !isUserExist(username) || isTokenTimeout(date)) {
			return false;
		}
		return true;

	}

	private boolean isUserExist(String username) {
		return userName.equals(username);
	}

	private boolean isTokenTimeout(Long date) {
		return System.currentTimeMillis() - date > loginTimeout;
	}

}
