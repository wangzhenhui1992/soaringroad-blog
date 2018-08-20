package com.soaringroad.blog.restapi.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.soaringroad.blog.entity.AuthParam;
import com.soaringroad.blog.service.SrAuthService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AdminLoginApiService {

	
	@Autowired
	private SrAuthService authService;
	
	@RequestMapping(value="/api/admin/login" , method=RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> auth(@RequestBody AuthParam authParam) {
		log.debug("接收到登陆请求.参数:"+authParam);
		String username = authParam.getUsername();
		String password = authParam.getPassword();
		if (username == null || password == null || username.isEmpty() || password.isEmpty() ) {
			return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
		} 
		String jwtToken = authService.login(username, password);
		return new ResponseEntity<String>("\""+jwtToken+"\"",HttpStatus.OK);
	}
}
