package com.soaringroad.blog.query;

import javax.validation.constraints.NotNull;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.soaringroad.blog.vo.SrBlogQueryCondition;
import com.soaringroad.blog.vo.SrBlogQueryEntity;
import com.soaringroad.blog.vo.SrBlogQueryOptionEnum;
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

	public SearchQuery build() {
		SrBlogQueryCondition[] queryConditions = queryEntity.getQueryConditions();
		SrBlogQuerySort[] querySorts = queryEntity.getQuerySort();
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		buildEsQueryConditions(queryConditions, queryBuilder);
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

	private static void buildEsQueryConditions(SrBlogQueryCondition[] queryConditions, BoolQueryBuilder queryBuilder) {
		if (queryConditions == null || queryConditions.length == 0) {
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
