package com.soaringroad.blog.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity接口
 * 
 * @author wangzhenhui1992
 */
public abstract class AbstractEntity implements EntityObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonIgnore
    public abstract long getEntityId();
	@JsonIgnore
    public abstract String getEntityName();
}
