package com.soaringroad.blog.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class SrBlogSpecifier<T> implements Specification<T> {

	private static final long serialVersionUID = 1L;

	private SrBlogQueryCondition[] queryConditions;

	public SrBlogSpecifier(SrBlogQueryCondition[] queryConditions) {
		this.queryConditions = queryConditions;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (queryConditions == null || queryConditions.length == 0) {
			return null;
		}
		List<Predicate> predicateList = new ArrayList<>();
		for (SrBlogQueryCondition queryCondition : queryConditions) {
			Object value = queryCondition.getValue();
			switch (queryCondition.getOption()) {
			case EQ:
				Path<Object> pathEq = root.get(queryCondition.getName());
				predicateList.add(criteriaBuilder.equal(pathEq, value));
				break;
			case GEQ:
				Path<Long> pathGe = root.get(queryCondition.getName());
				predicateList.add(criteriaBuilder.ge(pathGe, Long.valueOf(value.toString())));
				break;
			case LEQ:
				Path<Long> pathLe = root.get(queryCondition.getName());
				predicateList.add(criteriaBuilder.ge(pathLe, Long.valueOf(value.toString())));
				break;
			case MEMBER:
				Path<Collection<String>> pathIn = root.<Collection<String>>get(queryCondition.getName());
				predicateList.add(criteriaBuilder.isMember(String.valueOf(value), pathIn));
				break;
			default:
				break;
			}
		}
		Predicate[] predicates = new Predicate[predicateList.size()];
		return query.where(predicateList.toArray(predicates)).getRestriction();

	}

}
