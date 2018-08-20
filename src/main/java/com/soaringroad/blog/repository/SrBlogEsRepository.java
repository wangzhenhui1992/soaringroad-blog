package com.soaringroad.blog.repository;

import java.io.Serializable;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
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
public interface SrBlogEsRepository<T extends SrBlogEntity, E extends Serializable>
		extends ElasticsearchRepository<T, E>,SrBlogRepository<T,E> {
}
