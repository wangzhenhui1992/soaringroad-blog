package com.soaringroad.blog.repository.es;

import javax.validation.constraints.NotNull;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import com.soaringroad.blog.common.SrBlogQueryBuilder;
import com.soaringroad.blog.util.ElasticsearchUtil;
import com.soaringroad.blog.vo.SrBlogQueryCondition;
import com.soaringroad.blog.vo.SrBlogQueryEntity;
import com.soaringroad.blog.vo.SrBlogQuerySort;
import com.soaringroad.blog.vo.SrBlogQuerySortOrderEnum;
/**
 * <pre>
 * ES(Rest)用QueryBuilder
 * </pre>
 * @author wangzhenhui1992
 * @since 2018/11/20
 */
public class SrBlogEsRestQueryBuilder implements SrBlogQueryBuilder {
    private SrBlogQueryEntity queryEntity;

    public SrBlogEsRestQueryBuilder(@NotNull SrBlogQueryEntity queryEntity) {
        this.queryEntity = queryEntity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public SearchSourceBuilder build() {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        buildConditions(boolQueryBuilder);
        buildSort(sourceBuilder);
        buildPage(sourceBuilder);
        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.fetchSource(queryEntity.getFetchSource(), null);
        return sourceBuilder;
    }

    private void buildConditions(BoolQueryBuilder boolQueryBuilder) {
        SrBlogQueryCondition[] queryConditions = queryEntity.getQueryConditions();
        ElasticsearchUtil.buildEsQueryConditions(queryConditions, boolQueryBuilder);
    }

    private void buildSort(SearchSourceBuilder sourceBuilder) {
        SrBlogQuerySort[] querySorts = queryEntity.getQuerySort();
        if (querySorts != null && querySorts.length > 0) {
            for (SrBlogQuerySort sort : querySorts) {
                sourceBuilder.sort(sort.getName(),
                        SrBlogQuerySortOrderEnum.ASC.equals(sort.getSortOrder()) ? SortOrder.ASC : SortOrder.DESC);
            }
        }
    }

    private void buildPage(SearchSourceBuilder sourceBuilder) {
        int pageNumber = queryEntity.getPageNumber() == null ? 0 : queryEntity.getPageNumber();
        int queryNumber = queryEntity.getQueryNumber() == null ? 10 : queryEntity.getQueryNumber();
        sourceBuilder.from(pageNumber * queryNumber);
        sourceBuilder.size(queryNumber);
    }

}
