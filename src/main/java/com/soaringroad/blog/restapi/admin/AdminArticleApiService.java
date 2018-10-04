package com.soaringroad.blog.restapi.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.restapi.AbstractSrBlogApiService;
import com.soaringroad.blog.vo.SrBlogQueryEntity;
import static com.soaringroad.blog.util.SrBlogConsts.ENTITY_KEY_ARTICLE;

@RestController
@RequestMapping("/api/admin/article")
public class AdminArticleApiService extends AbstractSrBlogApiService<Article, Integer> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkGet(Integer id) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkPost(Article entity) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkPut(Article entity) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkDelete(Article entity) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkSearch(SrBlogQueryEntity q) {
        return true;
    }

    @Override
    protected String entityKey() {
        return ENTITY_KEY_ARTICLE;
    }

}
