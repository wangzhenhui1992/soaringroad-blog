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

import org.springframework.security.core.AuthenticationException;

/**
 * <pre>
 * 认证异常类
 * </pre>
 * @author wangzhenhui1992
 * @since 2019/01/24
 */
public class SrBlogAuthenticationException extends AuthenticationException {

  /**  */
  private static final long serialVersionUID = 1L;

  public SrBlogAuthenticationException(String msg) {
    super(msg);
  }
}
