package com.soaringroad.blog.common;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <pre>
 * 
 * </pre>
 */
public class RepositoryFactory {
  public static final HashMap<String, ElasticSearchRepository<? extends AbstractEntity, ? extends Serializable>> ES_REPOSITORIES = new HashMap<>();
  public static final HashMap<String, RdbRepository<? extends AbstractEntity, ? extends Serializable>> RDB_REPOSITORIES = new HashMap<>();

  public static void registerEsRepository(ElasticSearchRepository<? extends AbstractEntity, ? extends Serializable> repository) {
    ES_REPOSITORIES.put(repository.newEntity().getEntityName(), repository);
  }

  public static void registerRdbRepository(RdbRepository<? extends AbstractEntity, ? extends Serializable> repository) {
    RDB_REPOSITORIES.put(repository.newEntity().getEntityName(), repository);
  }

  public static <T extends AbstractEntity, E extends Serializable> void registerRepository(SrBlogRepository<T, E> repository) {
    if (repository instanceof ElasticSearchRepository) {
      registerEsRepository((ElasticSearchRepository<T, E>) repository);
      return;
    }
    if (repository instanceof RdbRepository) {
      registerRdbRepository((RdbRepository<T, E>) repository);
      return;
    }
  }

  public static ElasticSearchRepository<? extends AbstractEntity, ? extends Serializable> getEsRepository(String entityName) {
    return ES_REPOSITORIES.get(entityName);
  }

  public static RdbRepository<? extends AbstractEntity, ? extends Serializable> getRdbRepository(String entityName) {
    return RDB_REPOSITORIES.get(entityName);
  }
}
