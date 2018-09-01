package com.soaringroad.blog.entity;

import java.io.Serializable;

import com.soaringroad.blog.core.SrBlogContextContainer;
import com.soaringroad.blog.dao.AbstractSrBlogDao;

/**
 * Entity接口
 * 
 * @author wangzhenhui1992
 */
public abstract class AbstractSrBlogEntity implements SrBlogEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public <A extends AbstractSrBlogEntity, E extends SrBlogEsEntity, H extends SrBlogH2Entity, I extends Serializable, T extends AbstractSrBlogDao<A, E, H, I>> T getDao() {
		return SrBlogContextContainer.getBean(this.daoClass());
	}

	public abstract <A extends AbstractSrBlogEntity, E extends SrBlogEsEntity, H extends SrBlogH2Entity, I extends Serializable, T extends AbstractSrBlogDao<A, E, H, I>> Class<T> daoClass();

	public abstract <E extends SrBlogEsEntity, I extends Serializable> E toEsEntity(I id);

	public abstract <H extends SrBlogH2Entity> H toH2Entity();
}
