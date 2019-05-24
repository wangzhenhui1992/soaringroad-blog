package com.soaringroad.blog.repository.h2;

import com.soaringroad.blog.entity.common.AwsQuestion;
import com.soaringroad.blog.repository.SrBlogH2Repository;
import org.springframework.stereotype.Component;

@Component
public interface AwsQuestionRepository extends SrBlogH2Repository<AwsQuestion, Long> {

}
