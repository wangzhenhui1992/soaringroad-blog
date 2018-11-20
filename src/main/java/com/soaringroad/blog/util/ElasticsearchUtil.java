package com.soaringroad.blog.util;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.soaringroad.blog.vo.SrBlogQueryCondition;
import com.soaringroad.blog.vo.SrBlogQueryOptionEnum;

public class ElasticsearchUtil {
    private ElasticsearchUtil() {
    }

    public static void buildEsQueryConditions(SrBlogQueryCondition[] queryConditions, BoolQueryBuilder queryBuilder) {
        if (queryConditions == null || queryConditions.length == 0) {
            queryBuilder.must(QueryBuilders.matchAllQuery());
            return;
        }
        for (SrBlogQueryCondition queryCondition : queryConditions) {
            String name = queryCondition.getName();
            Object value = queryCondition.getValue();
            SrBlogQueryOptionEnum queryOption = queryCondition.getOption();
            switch (queryOption) {
            case EQ:
                queryBuilder.must(QueryBuilders.matchPhraseQuery(name, value));
                break;
            case GEQ:
                queryBuilder.must(QueryBuilders.rangeQuery(name).from(value));
                break;
            case LEQ:
                queryBuilder.must(QueryBuilders.rangeQuery(name).to(value));
                break;
            default:
                break;
            }
        }
    }
}
