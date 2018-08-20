package com.soaringroad.blog.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.soaringroad.blog.entity.SrBlogEntity;

/**
 * 
 * @author wangzhenhui1992
 *
 * @param <T>
 * @param <E>
 */
@NoRepositoryBean
public interface SrBlogH2Repository<T extends SrBlogEntity, E extends Serializable> extends JpaSpecificationExecutor<T>,
SrBlogRepository<T,E>{
}
