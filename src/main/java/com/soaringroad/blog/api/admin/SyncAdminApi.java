package com.soaringroad.blog.api.admin;

import com.soaringroad.blog.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/admin/sync")
public class SyncAdminApi {
  
  @Autowired
  private SyncService syncService;

  @RequestMapping(value = {
      "/all/rdb2es",
      "/all/rdb2es/" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> rdb2Es() {
    syncService.dbToEs();
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
  
  @RequestMapping(value = {
      "/all/es2rdb",
      "/all/es2rdb/" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void>  es2Rdb() {
    syncService.esToDb();
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
  
  @RequestMapping(value = {
      "/{id:(?!^(?:all)$).*}/rdb2es",
      "/{id:(?!^(?:all)$).*}/rdb2es/" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> rdb2Es(@PathVariable String id) {
    syncService.dbToEs(id);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
  
  @RequestMapping(value = {
      "/{id:(?!^(?:all)$).*}/es2rdb",
      "/{id:(?!^(?:all)$).*}/es2rdb/" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> es2Rdb(@PathVariable String id) {
    syncService.esToDb(id);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
