package com.soaringroad.blog.common;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

@NoRepositoryBean
public interface RdbRepository<T extends AbstractEntity, E extends Serializable>
                              extends PagingAndSortingRepository<T, E>, JpaSpecificationExecutor<T>, SrBlogRepository<T, E> {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  default T saveObject(Object entity) {
    return this.save((T)entity);
  }
}
