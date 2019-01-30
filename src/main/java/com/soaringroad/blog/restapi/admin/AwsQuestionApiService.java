package com.soaringroad.blog.restapi.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soaringroad.blog.entity.common.AwsQuestion;
import com.soaringroad.blog.restapi.AbstractSrBlogApiService;
import com.soaringroad.blog.vo.SrBlogQueryEntity;

@RestController
@RequestMapping("/api/admin/awsquestion")
public class AwsQuestionApiService extends AbstractSrBlogApiService<AwsQuestion, Long>{

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
