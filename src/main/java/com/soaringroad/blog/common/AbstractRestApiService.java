/******************************************************************
*   _____                  _             _____                 _  *
*  / ____|                (_)           |  __ \               | | *
* | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
*  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
*  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
* |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
*                                   __/ |                         *
*                                  |___/                          *
* Copyright ©2017-2018 www.soaringroad.com | All rights reserved. *
******************************************************************/
package com.soaringroad.blog.common;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaringroad.blog.vo.SrBlogQueryEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <pre>
 * API服务抽象类
 * </pre>
 * 
 * @author wangzhenhui1992
 * @since 2019/01/24
 */
@Slf4j
public abstract class AbstractRestApiService<T extends AbstractEntity, E extends Serializable> {

    /**
     * ObjectMapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataManager<T, E> dataManager;

    @RequestMapping(value = { "/{id:(?!^(?:search|count)$).*}",
            "/{id:(?!^(?:search|count)$).*}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityObject> get(@PathVariable E id) {

        if (!checkGet(id)) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // log.info("GET:" + id);
        EntityObject entity = callGet(id);
        // log.info("GET返回:" + entity);
        return entity == null ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @RequestMapping(value = { "/search",
            "/search/" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EntityObject>> search(@RequestParam String qStr) {
        SrBlogQueryEntity queryEntity = parseQueryEntity(qStr);
        if (!checkSearch(queryEntity)) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // log.info("SEARCH:" + queryEntity);
        Iterable<? extends EntityObject> result = callSearch(queryEntity);
        Iterator<? extends EntityObject> iterator = result.iterator();
        if (!iterator.hasNext()) {
            log.info("SEARCH返回:未找到数据");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityObject> resultList = new ArrayList<EntityObject>();
        resultList.add(iterator.next());
        while (iterator.hasNext()) {
            resultList.add(iterator.next());
        }
        // log.info("SEARCH返回:" + resultList);
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @RequestMapping(value = { "/count",
            "/count/" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> count() {

        if (!checkCount()) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Long count = callCount();
        log.info("GET返回:" + count);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityObject> post(@RequestBody T entity) {
        if (!checkPost(entity)) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // log.info("POST:" + entity);
        EntityObject result = callPost(entity);
        // log.info("POST返回:" + result);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/{id:(?!^(?:search|count)$).*}",
            "/{id:(?!^(?:search|count)$).*}/" }, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityObject> put(@RequestBody T entity) {
        if (!checkPut(entity)) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // log.info("PUT:" + entity);
        EntityObject result = callPut(entity);
        // log.info("PUT返回:" + result);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/{id:(?!^(?:search|count)$).*}",
            "/{id:(?!^(?:search|count)$).*}" }, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<T> delete(@PathVariable E id) {
        T entity = this.dataManager.findById(id);
        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if (!checkDelete(entity)) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // log.info("DELETE:" + entity);
        // TODO 通过ID去获取Entity，然后再删除
        callDelete(entity);
        // log.info("DELETE返回:" + entity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    protected EntityObject callGet(E id) {
        return dataManager.findById(id);
    }

    protected Long callCount() {
        return dataManager.count();
    }

    protected Iterable<? extends EntityObject> callSearch(SrBlogQueryEntity q) {
        return dataManager.search(q);
    }

    protected EntityObject callPost(T entity) {
        return dataManager.create(entity);
    }

    protected EntityObject callPut(T entity) {
        return dataManager.save(entity);
    }

    protected void callDelete(T entity) {
        dataManager.delete(entity);
    }

//    private T getInstance() {
//        Class<T> entityClass = (Class<T>) ParameterizedType.class.cast(this.getClass().getGenericSuperclass())
//                .getActualTypeArguments()[0];
//        T newInstance = null;
//        try {
//            newInstance = entityClass.newInstance();
//        } catch (InstantiationException e) {
//            log.error("无法生成实例. class=" + entityClass, e);
//        } catch (IllegalAccessException e) {
//            log.error("无法生成实例. class=" + entityClass, e);
//        }
//        return newInstance;
//    }

    protected boolean checkGet(E id) {
        return false;
    }

    protected boolean checkSearch(SrBlogQueryEntity q) {
        return false;
    }

    protected boolean checkPost(T entity) {
        return false;
    }

    protected boolean checkPut(T entity) {
        return false;
    }

    protected boolean checkDelete(T entity) {
        return false;
    }

    protected boolean checkCount() {
        return false;
    }

    private SrBlogQueryEntity parseQueryEntity(String qStr) {
        if (qStr == null || qStr.isEmpty()) {
            return null;
        }
        SrBlogQueryEntity queryEntity = null;
        try {
            queryEntity = objectMapper.readValue(qStr, SrBlogQueryEntity.class);
        } catch (JsonParseException e) {
            log.error("JSON转换错误", e);
        } catch (JsonMappingException e) {
            log.error("JSON转换错误", e);
        } catch (IOException e) {
            log.error("JSON转换错误", e);
        }
        return queryEntity;
    }
}
