package com.soaringroad.blog.restapi.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.soaringroad.blog.service.MigrationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/api/admin")
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
    
    @RequestMapping(value = {"/migration/{id:(?!^(?:search|count)$).*}/","/migration/{id:(?!^(?:search|count)$).*}"}, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> migration(@PathVariable Long id) {
        log.info("移行单个文章开始 id={}",id);
        migrationService.migrationArticleToDynamoDB(id);
        log.info("移行单个文章结束id={}",id);
        return ResponseEntity.ok().body(null);
    }
}
