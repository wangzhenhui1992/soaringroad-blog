/******************************************************************
 *   _____                  _             _____                 _  *
 *  / ____|                (_)           |  __ \               | | *
 * | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
 *  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
 *  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
 * |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
 *                                   __/ |                         *
 *                                  |___/                          *
 * Copyright ©2017-2020 www.soaringroad.com | All rights reserved. *
 ******************************************************************/
package com.soaringroad.blog.service.impl;

import com.soaringroad.blog.common.AbstractEntity;
import com.soaringroad.blog.common.ElasticSearchRepository;
import com.soaringroad.blog.common.RdbRepository;
import com.soaringroad.blog.common.RepositoryFactory;
import com.soaringroad.blog.entity.Advertisement;
import com.soaringroad.blog.entity.Article;
import com.soaringroad.blog.entity.AwsQuestion;
import com.soaringroad.blog.entity.Setting;
import com.soaringroad.blog.service.SyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * <pre>
 * 
 * </pre>
 */
@Slf4j
@Component
public class SyncServiceImpl implements SyncService {
  
  
  private static final List<String> ENTITY_NAMES;
  
  static {
    ENTITY_NAMES = new LinkedList<>();
    ENTITY_NAMES.add(Article.ENTITY_NAME);
    ENTITY_NAMES.add(AwsQuestion.ENTITY_NAME);
    ENTITY_NAMES.add(Setting.ENTITY_NAME);
    ENTITY_NAMES.add(Advertisement.ENTITY_NAME);
  }
  
  private boolean isEntityName(String entityName) {
    return ENTITY_NAMES.contains(entityName);
  }
  
  
  @Override
  public boolean dbToEs(String entityName) {
    log.info("同步处理开始。(DB->ES {})", entityName);
    boolean result = isEntityName(entityName) ? toEs(entityName) : false;
    log.info("同步处理完成。(DB->ES {} 结果 {})", entityName, result);
    return result;
  }
  
  private boolean toEs(String entityName) {
    RdbRepository<? extends AbstractEntity,? extends Serializable> rdbRepository = RepositoryFactory.getRdbRepository(entityName);
    ElasticSearchRepository<? extends AbstractEntity,? extends Serializable> esRepository =  RepositoryFactory.getEsRepository(entityName);
    rdbRepository.findAll().forEach(esRepository::saveObject);
    return true;
  }
  
  @Override
  public boolean esToDb(String entityName) {
    log.info("同步处理开始。(ES->DB {})", entityName);
    boolean result = isEntityName(entityName) ? toDb(entityName) : false;
    log.info("同步处理完成。(ES->DB {} 结果 {})", entityName, result);
    return result;
  }
  
  private boolean toDb(String entityName) {
    RdbRepository<? extends AbstractEntity,? extends Serializable> rdbRepository = RepositoryFactory.getRdbRepository(entityName);
    ElasticSearchRepository<? extends AbstractEntity,? extends Serializable> esRepository =  RepositoryFactory.getEsRepository(entityName);
    esRepository.findAll().stream().sorted((o1,o2)-> {
      return o1.getEntityId()-o2.getEntityId() >= 0?1:-1 ;
    }).forEach(rdbRepository::saveObject);
    return true;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean dbToEs() {
    log.info("同步处理开始。(DB->ES)");
    ENTITY_NAMES.forEach(this::dbToEs);
    log.info("同步处理完成。(DB->ES)");
    return true;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean esToDb() {
    log.info("同步处理开始。(ES->DB)");
    ENTITY_NAMES.forEach(this::esToDb);
    log.info("同步处理完成。(ES->DB)");
    return true;
  }
}
