package com.soaringroad.blog.restapi.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.soaringroad.blog.service.MigrationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class MigrationApiService {

    @Autowired
    private MigrationService migrationService;
    
    @RequestMapping(value = {"/migration/","/migration"}, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> migration() {
        log.info("移行开始");
        migrationService.migrationToDynamoDB();
        log.info("移行结束");
        return ResponseEntity.ok().body(null);
    }
}
