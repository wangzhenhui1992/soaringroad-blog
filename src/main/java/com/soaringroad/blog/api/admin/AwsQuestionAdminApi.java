package com.soaringroad.blog.api.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.soaringroad.blog.common.AbstractRestApiService;
import com.soaringroad.blog.entity.AwsQuestion;
import com.soaringroad.blog.vo.SrBlogQueryEntity;

@RestController
@RequestMapping("/api/admin/awsquestion")
public class AwsQuestionAdminApi extends AbstractRestApiService<AwsQuestion, Long>{

    @Override
    protected boolean checkGet(Long id) {
        return true;
    }

    @Override
    protected boolean checkSearch(SrBlogQueryEntity q) {
        return true;
    }

    @Override
    protected boolean checkPost(AwsQuestion entity) {
        return true;
    }

    @Override
    protected boolean checkPut(AwsQuestion entity) {
        return true;
    }

    @Override
    protected boolean checkDelete(AwsQuestion entity) {
        return true;
    }

    @Override
    protected boolean checkCount() {
        return true;
    }

}
