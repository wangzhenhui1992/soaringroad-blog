package com.soaringroad.blog.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface RdbRepository<T extends AbstractEntity, E extends Serializable>
                              extends JpaRepository<T, E>, JpaSpecificationExecutor<T>, SrBlogRepository<T, E> {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  default T saveObject(Object entity) {
    return this.saveAndFlush((T)entity);
  }
}
