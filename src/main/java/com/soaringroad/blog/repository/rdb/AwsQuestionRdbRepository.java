package com.soaringroad.blog.repository.rdb;

import com.soaringroad.blog.common.RdbRepository;
import com.soaringroad.blog.entity.AwsQuestion;
import org.springframework.stereotype.Component;

@Component
public interface AwsQuestionRdbRepository extends RdbRepository<AwsQuestion, Long> {
  /**
   * {@inheritDoc}
   */
  @Override
  default AwsQuestion newEntity() {
    return new AwsQuestion();
  }
}
