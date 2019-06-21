package com.soaringroad.blog.repository.es;

import javax.validation.constraints.NotNull;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import com.soaringroad.blog.common.SrBlogQueryBuilder;
import com.soaringroad.blog.util.ElasticsearchUtil;
import com.soaringroad.blog.vo.SrBlogQueryCondition;
import com.soaringroad.blog.vo.SrBlogQueryEntity;
import com.soaringroad.blog.vo.SrBlogQuerySort;
import com.soaringroad.blog.vo.SrBlogQuerySortOrderEnum;

public class SrBlogEsQueryBuilder implements SrBlogQueryBuilder {

    private SrBlogQueryEntity queryEntity;

    public static SrBlogEsQueryBuilder of(@NotNull SrBlogQueryEntity queryEntity) {
        return new SrBlogEsQueryBuilder(queryEntity);
    }

    private SrBlogEsQueryBuilder(SrBlogQueryEntity queryEntity) {
        this.queryEntity = queryEntity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public SearchQuery build() {
        SrBlogQueryCondition[] queryConditions = queryEntity.getQueryConditions();
        SrBlogQuerySort[] querySorts = queryEntity.getQuerySort();
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        ElasticsearchUtil.buildEsQueryConditions(queryConditions, queryBuilder);
        nativeSearchQueryBuilder.withQuery(queryBuilder);
        buildEsSorts(querySorts, nativeSearchQueryBuilder);
        return nativeSearchQueryBuilder.build();
    }

    private static void buildEsSorts(SrBlogQuerySort[] querySorts, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        if (querySorts == null || querySorts.length == 0) {
            return;
        }
        for (SrBlogQuerySort querySort : querySorts) {
            String name = querySort.getName();
            SrBlogQuerySortOrderEnum sortOrder = querySort.getSortOrder();
            nativeSearchQueryBuilder.withSort(new FieldSortBuilder(name)
                    .order(SrBlogQuerySortOrderEnum.ASC.equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC));
        }
    }

}
