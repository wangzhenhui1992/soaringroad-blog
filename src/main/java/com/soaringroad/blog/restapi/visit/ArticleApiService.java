package com.soaringroad.blog.restapi.visit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.restapi.AbstractSrBlogApiService;
import com.soaringroad.blog.vo.SrBlogQueryEntity;

import static com.soaringroad.blog.util.SrBlogConsts.ENTITY_KEY_ARTICLE;

@RestController
@RequestMapping("/api/article")
public class ArticleApiService extends AbstractSrBlogApiService<Article, Long> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkGet(Long id) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkSearch(SrBlogQueryEntity q) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkCount() {
        return true;
    }

    @Override
    protected String entityKey() {
        return ENTITY_KEY_ARTICLE;
    }

}
