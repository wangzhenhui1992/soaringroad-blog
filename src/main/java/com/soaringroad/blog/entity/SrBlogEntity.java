package com.soaringroad.blog.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soaringroad.blog.dao.AbstractSrBlogDao;

public interface SrBlogEntity extends Serializable {
    String redisKey();

    @JsonIgnore
    <A extends SrBlogEntity, I extends Serializable, T extends AbstractSrBlogDao<A, I>> T getDao();

    <A extends SrBlogEntity, I extends Serializable, T extends AbstractSrBlogDao<A, I>> Class<T> daoClass();
}
