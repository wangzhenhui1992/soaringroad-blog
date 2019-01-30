package com.soaringroad.blog.repository.h2;

import org.springframework.stereotype.Component;

import com.soaringroad.blog.entity.common.AwsQuestion;
import com.soaringroad.blog.repository.SrBlogH2Repository;

@Component
public interface AwsQuestionRepository extends SrBlogH2Repository<AwsQuestion, Long> {

}
