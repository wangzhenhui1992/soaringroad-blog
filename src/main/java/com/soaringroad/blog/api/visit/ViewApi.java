package com.soaringroad.blog.api.visit;

import com.soaringroad.blog.common.CacheRepository;
import com.soaringroad.blog.common.DataManager;
import com.soaringroad.blog.entity.Article;
import com.soaringroad.blog.service.CountCacheService;
import com.soaringroad.blog.util.SrBlogConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * 流量统计API
 * @author wangzhenhui1992
 */
@RestController
@RequestMapping("/api/view")
public class ViewApi {

    @Autowired(required=false)
    private CacheRepository redisRepository;
    
    @Autowired
    private DataManager<Article,Long> articleManager;
    
    @Autowired
    private CountCacheService countService;
    
    @RequestMapping(value = { "/viewcount",
            "/viewcount/" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> viewCount() {
        Long view = countService.getSiteView();
        return view == null ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(view, HttpStatus.OK);
    }

    @RequestMapping(value = { "/articleviewcount/{id}",
            "/articleviewcount/{id}/" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> articleViewCount(@PathVariable Long id) {
        Long view = getArticleViewCount(id);
        return view == null ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(view, HttpStatus.OK);
    }
    
    private Long getArticleViewCount(Long id) {
      Object object = redisRepository.getValue(String.format(SrBlogConsts.REDIS_KEY_ARTICLE_VIEW_COUNT, id));
      return object == null ?  getArticleViewCountRdb(id) : Long.valueOf(object.toString());
    }
    
    private Long getArticleViewCountRdb(Long id) {
      return Optional.ofNullable(articleManager.findById(id)).map(Article::getView).orElse(null);
    }

}
