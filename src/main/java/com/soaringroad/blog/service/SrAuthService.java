package com.soaringroad.blog.service;

public interface SrAuthService {

	public String login(String username, String password);
	
	public boolean auth(String jwtToken);
}
