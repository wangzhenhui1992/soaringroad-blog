package com.soaringroad.blog.common;

import com.soaringroad.blog.repository.es.AbstractElasticSearchRepository;
import com.soaringroad.blog.vo.SrBlogQueryEntity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public interface ElasticSearchRepository<T extends AbstractEntity, E extends Serializable> extends SrBlogRepository<T,E> {
  
    static final LinkedList<AbstractElasticSearchRepository<? extends AbstractEntity, ? extends Serializable>> ALL_REPOSITORIES = new LinkedList<>();

    boolean indexEntities(Iterable<T> entities);

    boolean index(T entity);
    
    @Override
    @SuppressWarnings("unchecked")
    default T saveObject(Object entity) {
      T target = (T)entity;
      return this.index(target) ? target : null;
    }

    List<T> search(SrBlogQueryEntity entity);

    boolean delete(T entity);
    
    T get(E id);
    
    List<T> findAll();

}
