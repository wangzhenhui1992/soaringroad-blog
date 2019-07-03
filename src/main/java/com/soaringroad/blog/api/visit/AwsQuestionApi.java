package com.soaringroad.blog.api.visit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.soaringroad.blog.common.AbstractRestApiService;
import com.soaringroad.blog.entity.AwsQuestion;
import com.soaringroad.blog.vo.SrBlogQueryEntity;

@RestController
@RequestMapping("/api/awsquestion")
public class AwsQuestionApi extends AbstractRestApiService<AwsQuestion, Long>{

    @Override
    protected boolean checkGet(Long id) {
        return true;
    }

    @Override
    protected boolean checkSearch(SrBlogQueryEntity q) {
        return true;
    }

    @Override
    protected boolean checkCount() {
        return true;
    }

}
