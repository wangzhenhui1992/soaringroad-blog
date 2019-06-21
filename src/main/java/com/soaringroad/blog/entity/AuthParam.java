package com.soaringroad.blog.entity;

import com.soaringroad.blog.common.AbstractCommonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AuthParam extends AbstractCommonEntity {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 密码
	 */
	private String password;

}
