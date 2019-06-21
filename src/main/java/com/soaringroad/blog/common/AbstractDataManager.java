package com.soaringroad.blog.common;

import com.soaringroad.blog.repository.rdb.SrBlogRdbQueryBuilder;
import com.soaringroad.blog.util.EntityUtil;
import com.soaringroad.blog.util.SrBlogConsts;
import com.soaringroad.blog.vo.SrBlogQueryEntity;
import com.soaringroad.blog.vo.SrBlogRdbQuery;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDataManager<A extends AbstractEntity, I extends Serializable> implements DataManager<A, I> , InitializingBean {

  @Autowired
  private RdbRepository<A, I> rdbRepository;

  @Autowired(required = false)
  private ElasticSearchRepository<A, I> esRepository;

  @Autowired
  private CacheRepository cacheRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public A findById(I id) {
    return Optional.ofNullable( getByIdCache(id)).orElse(Optional.ofNullable(getByIdEs(id)).orElse( getByIdRdb(id)));
  }

  private A getByIdRdb(I id) {
    return rdbRepository.findById(id).orElse(null);
  }

  private A getByIdEs(I id) {
    return esRepository.get(id);
  }

  @SuppressWarnings("unchecked")
  private A getByIdCache(I id) {
    A entity =  rdbRepository.newEntity();
    Object object = cacheRepository.getValue(String.format(SrBlogConsts.ENTITY_KEY, entity.getEntityName(), id));
    return object == null ? null : (A)EntityUtil.parseMapToEntity(object, entity.getClass());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Page<A> search(SrBlogQueryEntity queryEntity) {
    return  Optional.of(searchEs(queryEntity)).orElse(searchRdb(queryEntity));
  }

  private Page<A> searchEs(SrBlogQueryEntity queryEntity) {
    if (esRepository == null) {
      return null;
    }
    List<A> entities = esRepository.search(queryEntity);
    return CollectionUtils.isEmpty(entities) ? null : new PageImpl<A>(entities, Pageable.unpaged(), entities.size());
  }

  private Page<A> searchRdb(SrBlogQueryEntity queryEntity) {
    SrBlogRdbQuery<A> searchQuery = new SrBlogRdbQueryBuilder<A>(queryEntity).build();
    return rdbRepository.findAll(searchQuery.getSpecification(), searchQuery.getPageRequest());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public A save(A entity) {
    entity = saveRdb(entity);
    saveCache(entity);
    saveEs(entity);
    return entity;
  }

  private A saveRdb(A entity) {
    return rdbRepository.save(entity);
  }
  
  private A saveEs(A entity) {
    return Optional.ofNullable(esRepository).map(repository->repository.index(entity) ? entity : null).orElse(null) ;
  }
  
  private A saveCache(A entity) {
    if (entity == null) {
      return null;
    }
    cacheRepository.setValue(EntityUtil.getCacheKey(entity), entity);
    return entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(A entity) {
    deleteRdb(entity);
    deleteCache(entity);
    deleteEs(entity);
  }

  private void deleteRdb(A entity) {
    rdbRepository.delete(entity);
  }
  
  private void deleteCache(A entity) {
    cacheRepository.delete(EntityUtil.getCacheKey(entity));
  }
  
  private void deleteEs(A entity) {
      esRepository.delete(entity);
}

  /**
   * {@inheritDoc}
   */
  @Override
  public A create(A entity) {
    return saveRdb(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long count() {
    return countRdb();
  }

  private Long countRdb() {
    return rdbRepository.count();
  }

  @Override
  public Iterable<A> findAll() {
    return Optional.ofNullable(findAllByEs()).orElse(findAllByRdb());
  }
  
  private Iterable<A> findAllByRdb() {
    return rdbRepository.findAll();
  }
  
  private Iterable<A> findAllByEs() {
    return esRepository.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    RepositoryFactory.registerRdbRepository(rdbRepository);
    if (esRepository != null) {
      RepositoryFactory.registerEsRepository(esRepository);
    }
  }
  
}