package com.soaringroad.blog.dao;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.soaringroad.blog.entity.SrBlogEntity;
import com.soaringroad.blog.query.SrBlogH2QueryBuilder;
import com.soaringroad.blog.repository.SrBlogH2Repository;
import com.soaringroad.blog.vo.SrBlogH2Query;
import com.soaringroad.blog.vo.SrBlogQueryEntity;

public abstract class AbstractSrBlogDao<A extends SrBlogEntity, I extends Serializable> implements SrBlogDao<A, I> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<A> findById(I id) {
        return getByIdH2(id);
    }

    private Optional<A> getByIdH2(I id) {
        return getH2Repository().findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<A> search(SrBlogQueryEntity queryEntity) {
        return searchH2(queryEntity);
    }

    public Page<A> searchH2(SrBlogQueryEntity queryEntity) {
        SrBlogH2Query<A> searchQuery = new SrBlogH2QueryBuilder<A>(queryEntity).build();
        return getH2Repository().findAll(searchQuery.getSpecification(), searchQuery.getPageRequest());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A save(A entity) {
        return saveH2(entity);
    }

    public A saveH2(A entity) {
        return getH2Repository().save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(A entity) {
        deleteH2(entity);
    }

    public void deleteH2(A entity) {
        getH2Repository().delete(entity);
    }

    protected abstract SrBlogH2Repository<A, I> getH2Repository();

    /**
     * {@inheritDoc}
     */
    @Override
    public SrBlogEntity create(A entity) {
        return saveH2(entity);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Long count() {
        return countH2();
    }

    private Long countH2() {
        return getH2Repository().count();
    }
}
