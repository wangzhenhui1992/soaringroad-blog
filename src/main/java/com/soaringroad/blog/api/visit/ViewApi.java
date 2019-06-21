package com.soaringroad.blog.api.visit;

import com.soaringroad.blog.common.CacheRepository;
import com.soaringroad.blog.common.DataManager;
import com.soaringroad.blog.entity.Article;
import com.soaringroad.blog.util.SrBlogConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        Long view = getArticleViewCount(id);
        return view == null ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(view, HttpStatus.OK);
    }
    
    public Long getArticleViewCount(String id) {
      if (redisRepository == null) {
        Article article = articleManager.findById(Long.valueOf(id));
        return article == null ? null : article.getView();
      }
      Object object = redisRepository.getValue(String.format(SrBlogConsts.REDIS_KEY_ARTICLE_VIEW_COUNT, id));
      return object == null ? null : Long.valueOf(object.toString());
    }

}
