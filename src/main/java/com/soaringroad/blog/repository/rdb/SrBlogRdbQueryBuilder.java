package com.soaringroad.blog.repository.rdb;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import com.soaringroad.blog.common.EntityObject;
import com.soaringroad.blog.common.SrBlogQueryBuilder;
import com.soaringroad.blog.vo.SrBlogRdbQuery;
import com.soaringroad.blog.vo.SrBlogQueryCondition;
import com.soaringroad.blog.vo.SrBlogQueryEntity;
import com.soaringroad.blog.vo.SrBlogQuerySort;
import com.soaringroad.blog.vo.SrBlogQuerySortOrderEnum;
import com.soaringroad.blog.vo.SrBlogSpecifier;

public class SrBlogRdbQueryBuilder<T extends EntityObject> implements SrBlogQueryBuilder {

	private SrBlogQueryEntity queryEntity;

	public SrBlogRdbQueryBuilder(@NotNull SrBlogQueryEntity queryEntity) {
		this.queryEntity = queryEntity;
	}

	@SuppressWarnings("unchecked")
    @Override
	public SrBlogRdbQuery<T> build() {
		Pageable pageable = buildH2Page(queryEntity);
		Specification<T> spec = buildH2Spec();
		return new SrBlogRdbQuery<T>(pageable, spec);
	}

	private static Pageable buildH2Page(SrBlogQueryEntity queryEntity) {
		SrBlogQuerySort[] querySorts = queryEntity.getQuerySort();
		Integer queryNum = queryEntity.getQueryNumber();
		Integer pageNum = queryEntity.getPageNumber();
		PageRequest pageRequest = PageRequest.of(pageNum == null || pageNum < 0 ? 0 : pageNum,
				queryNum == null || queryNum <= 0 ? 20 : queryNum, buildH2Sort(querySorts));
		return pageRequest;
	}

	private Specification<T> buildH2Spec() {
		final SrBlogQueryCondition[] queryConditions = queryEntity.getQueryConditions();
		Specification<T> spec = new SrBlogSpecifier<T>(queryConditions);
		return spec;
	}

	private static Sort buildH2Sort(SrBlogQuerySort[] querySorts) {
		if (querySorts == null || querySorts.length == 0) {
			return Sort.unsorted();
		}
		Sort sort = null;
		for (SrBlogQuerySort querySort : querySorts) {
			String name = querySort.getName();
			SrBlogQuerySortOrderEnum sortOrder = querySort.getSortOrder();
			Sort tempSort = new Sort(
					SrBlogQuerySortOrderEnum.ASC.equals(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC, name);
			if (sort == null) {
				sort = tempSort;
				continue;
			}
			sort.and(tempSort);
		}
		return sort == null ? Sort.unsorted() : sort;
	}
}
