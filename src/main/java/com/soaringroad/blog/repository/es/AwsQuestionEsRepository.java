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
package com.soaringroad.blog.repository.es;

import com.soaringroad.blog.entity.AwsQuestion;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 
 * </pre>
 */
@Component
public class AwsQuestionEsRepository extends AbstractElasticSearchRepository<AwsQuestion,Long> {

  /**
   * {@inheritDoc}
   */
  @Override
  public AwsQuestion newEntity() {
    return new AwsQuestion();
  }

}
