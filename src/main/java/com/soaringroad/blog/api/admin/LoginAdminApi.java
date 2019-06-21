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
package com.soaringroad.blog.api.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.soaringroad.blog.entity.AuthParam;
import com.soaringroad.blog.service.AuthService;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 管理登陆类
 * </pre>
 * @author wangzhenhui1992
 * @since 2019/01/24
 */
@RestController
@Slf4j
public class LoginAdminApi {

	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(value="/api/admin/login" , method=RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> auth(@RequestBody AuthParam authParam) {
		log.debug("接收到登陆请求.参数:"+authParam);
		String username = authParam.getUsername();
		String password = authParam.getPassword();
		if (username == null || password == null || username.isEmpty() || password.isEmpty() ) {
			return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
		} 
		String jwtToken = authService.authonrize(username, password);
		return new ResponseEntity<String>("\""+jwtToken+"\"",HttpStatus.OK);
	}
}
