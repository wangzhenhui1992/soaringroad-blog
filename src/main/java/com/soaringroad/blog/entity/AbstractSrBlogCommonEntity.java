package com.soaringroad.blog.entity;

import java.io.Serializable;

import com.soaringroad.blog.dao.AbstractSrBlogDao;

public abstract class AbstractSrBlogCommonEntity implements SrBlogEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Override
    public final <A extends SrBlogEntity, I extends Serializable, T extends AbstractSrBlogDao<A, I>> T getDao() {
        return null;
    }

    @Override
    public final <A extends SrBlogEntity, I extends Serializable, T extends AbstractSrBlogDao<A, I>> Class<T> daoClass() {
        return null;
    }
	
}
