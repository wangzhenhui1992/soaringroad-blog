package com.soaringroad.blog.restapi.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.soaringroad.blog.repository.RedisRepository;
import com.soaringroad.blog.util.SrBlogConsts;

/**
 * 流量统计API
 * @author wangzhenhui1992
 */
@RestController
@RequestMapping("/api/view")
public class ViewApiService {

    @Autowired
    private RedisRepository redisRepository;

    @RequestMapping(value = { "/viewcount",
            "/viewcount/" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> viewCount() {
        Object object = redisRepository.getValue(SrBlogConsts.REDIS_KEY_VIEW_COUNT);
        return object == null ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(Long.valueOf(object.toString()), HttpStatus.OK);
    }

    @RequestMapping(value = { "/articleviewcount/{id}",
            "/articleviewcount/{id}/" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> articleViewCount(@PathVariable String id) {
        Object object = redisRepository.getValue(String.format(SrBlogConsts.REDIS_KEY_ARTICLE_VIEW_COUNT, id));
        return object == null ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(Long.valueOf(object.toString()), HttpStatus.OK);
    }

}
