package com.soaringroad.blog.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.soaringroad.blog.entity.SrBlogEntity;
import com.soaringroad.blog.vo.SrBlogQueryEntity;

public interface SrBlogDao<A, I> {
  Optional<? extends SrBlogEntity> findById(I id);

  Page<? extends SrBlogEntity> search(SrBlogQueryEntity queryEntity);

  SrBlogEntity create(A entity);

  SrBlogEntity save(A entity);

  void delete(A entity);

  Long count();

  Iterable<A> findAll();
}
