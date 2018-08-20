package com.soaringroad.blog.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface SrBlogRepository<T,E> extends PagingAndSortingRepository<T,E>{

}
