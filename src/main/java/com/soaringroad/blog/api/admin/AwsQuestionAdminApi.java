package com.soaringroad.blog.api.admin;

import com.soaringroad.blog.api.visit.AwsQuestionApi;
import com.soaringroad.blog.entity.AwsQuestion;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/awsquestion")
public class AwsQuestionAdminApi extends AwsQuestionApi {


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

}
