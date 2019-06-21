package com.soaringroad.blog.vo;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.soaringroad.blog.common.EntityObject;
import lombok.Data;

@Data
public class SrBlogRdbQuery<T extends EntityObject> {
	@NotNull
	private Pageable pageRequest;
	
	@NotNull
	private Specification<T> specification;
	
	public SrBlogRdbQuery(@NotNull Pageable pageRequest, @NotNull Specification<T> specification) {
		this.pageRequest = pageRequest;
		this.specification = specification;
	}
	
}
