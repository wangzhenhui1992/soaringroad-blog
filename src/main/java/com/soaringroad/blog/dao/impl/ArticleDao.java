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
package com.soaringroad.blog.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.soaringroad.blog.dao.AbstractSrBlogDao;
import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.repository.ElasticSearchRepository;
import com.soaringroad.blog.repository.RedisRepository;
import com.soaringroad.blog.util.SrBlogConsts;
import com.soaringroad.blog.util.TransformUtil;
import com.soaringroad.blog.vo.SrBlogQueryEntity;

@Component
public class ArticleDao extends AbstractSrBlogDao<Article, Long> {

    @Autowired
    private RedisRepository redisRepository;
    
    @Autowired
    private ElasticSearchRepository elasticSearchRepository;

    @Override
    public Optional<Article> findById(Long id) {
        Object object = redisRepository.getValue(String.format(SrBlogConsts.ENTITY_KEY_ARTICLE, id));
        Article result = null;
        // 击中缓存
        if (object != null) {
            result = TransformUtil.parseMapToEntity(object, Article.class);
            // 未击中缓存
        } else {
            Optional<Article> resultOpt = super.findById(id);
            result = resultOpt.isPresent() ? resultOpt.get() : null;
            // 添加缓存
            redisRepository.setValue(String.format(SrBlogConsts.ENTITY_KEY_ARTICLE, id), result);
        }
        // VIEW统计
        if (result != null) {
            Long view = redisRepository.increaseValue(String.format(SrBlogConsts.REDIS_KEY_ARTICLE_VIEW_COUNT, id), 1);
            result.setView(view);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Article save(Article entity) {
        Article newArticle = super.save(entity);
        redisRepository.setValue(String.format(SrBlogConsts.ENTITY_KEY_ARTICLE, newArticle.getId()), newArticle);
        elasticSearchRepository.indexArticle(newArticle);
        return newArticle;
    }

    @Override
    public void delete(Article entity) {
        super.delete(entity);
        redisRepository.delete(String.format(SrBlogConsts.ENTITY_KEY_ARTICLE, entity.getId()));
        elasticSearchRepository.deleteArticle(entity);
    }

    @Override
    public Article create(Article entity) {
        Article newArticle = super.create(entity);
        redisRepository.setValue(String.format(SrBlogConsts.ENTITY_KEY_ARTICLE, newArticle.getId()), newArticle);
        elasticSearchRepository.indexArticle(newArticle);
        return newArticle;
    }

    @Override
    public Page<Article> search(SrBlogQueryEntity queryEntity) {
        Page<Article> page = searchEs(queryEntity);
        if (page == null) {
            return null;
        }
        for (Article article : page) {
            Object o = redisRepository.getValue(String.format(SrBlogConsts.REDIS_KEY_ARTICLE_VIEW_COUNT, article.getId()));
            Long view = (o == null ? Long.valueOf(0) : TransformUtil.parseMapToEntity(o,  Long.class));
            article.setView(view);
        }
        return page;
    }
    
    private Page<Article> searchDb(SrBlogQueryEntity queryEntity) {
        return super.search(queryEntity);
    }
    
    private Page<Article> searchEs(SrBlogQueryEntity queryEntity) {
        List<Article> articleList = elasticSearchRepository.searchArticle(queryEntity);
        Page<Article> articlePages = new PageImpl<Article>(articleList, Pageable.unpaged(), articleList.size()); 
        return articlePages;
    }

}
