package com.soaringroad.blog.restapi.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.soaringroad.blog.service.NotifyBaiduService;
import com.soaringroad.blog.vo.BaiduCommitResponse;
@RestController
@RequestMapping(value = "/api/admin/notify")
public class NotifyApiService {

    @Autowired
    private NotifyBaiduService notifyBaiduService;
    
    @RequestMapping(value = {"/baidu/","/baidu"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaiduCommitResponse> notifyBaidu() {
        BaiduCommitResponse responseBody = this.notifyBaiduService.notifyBaidu();
        return ResponseEntity.ok(responseBody);
    }
}
