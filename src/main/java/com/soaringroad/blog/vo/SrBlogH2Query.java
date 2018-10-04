package com.soaringroad.blog.vo;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.soaringroad.blog.entity.SrBlogEntity;

import lombok.Data;

@Data
public class SrBlogH2Query<T extends SrBlogEntity> {
	@NotNull
	private Pageable pageRequest;
	
	@NotNull
	private Specification<T> specification;
	
	public SrBlogH2Query(@NotNull Pageable pageRequest, @NotNull Specification<T> specification) {
		this.pageRequest = pageRequest;
		this.specification = specification;
	}
	
}
