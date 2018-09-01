package com.soaringroad.blog.dao;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.soaringroad.blog.entity.AbstractSrBlogEntity;
import com.soaringroad.blog.entity.SrBlogEntity;
import com.soaringroad.blog.entity.SrBlogEsEntity;
import com.soaringroad.blog.entity.SrBlogH2Entity;
import com.soaringroad.blog.query.SrBlogEsQueryBuilder;
import com.soaringroad.blog.query.SrBlogH2QueryBuilder;
import com.soaringroad.blog.repository.SrBlogEsRepository;
import com.soaringroad.blog.repository.SrBlogH2Repository;
import com.soaringroad.blog.vo.SrBlogEntityTypeEnum;
import com.soaringroad.blog.vo.SrBlogH2Query;
import com.soaringroad.blog.vo.SrBlogQueryEntity;

public abstract class AbstractSrBlogDao<A extends AbstractSrBlogEntity, E extends SrBlogEsEntity, H extends SrBlogH2Entity, I extends Serializable>
		implements SrBlogDao<A, E, H, I> {

	@Value("${app.entitytype}")
	private SrBlogEntityTypeEnum entityType;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<? extends SrBlogEntity> findById(I id) {
		switch (entityType) {
		case ES:
			return getByIdEs(id);
		case H2:
			return getByIdH2(id);
		default:
			return Optional.empty();
		}
	}

	private Optional<E> getByIdEs(I id) {
		return getEsRepository().findById(id);
	}

	private Optional<H> getByIdH2(I id) {
		return getH2Repository().findById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<? extends SrBlogEntity> search(SrBlogQueryEntity queryEntity) {
		switch (entityType) {
		case ES:
			return searchEs(queryEntity);
		case H2:
			return searchH2(queryEntity);
		default:
			return null;
		}
	}

	public Page<E> searchEs(SrBlogQueryEntity queryEntity) {
		SearchQuery searachQuery = SrBlogEsQueryBuilder.of(queryEntity).build();
		return getEsRepository().search(searachQuery);
	}

	public Page<H> searchH2(SrBlogQueryEntity queryEntity) {
		SrBlogH2Query<H> searchQuery = new SrBlogH2QueryBuilder<H>(queryEntity).build();
		return getH2Repository().findAll(searchQuery.getSpecification(), searchQuery.getPageRequest());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SrBlogEntity save(A entity) {
		switch (entityType) {
		case ES:
			return saveEs(entity);
		case H2:
			return saveH2(entity);
		default:
			return null;
		}
	}

	public E saveEs(A entity) {
		return getEsRepository().save(entity.toEsEntity(null));
	}

	public H saveH2(A entity) {
		return getH2Repository().save(entity.toH2Entity());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(A entity) {
		switch (entityType) {
		case ES:
			deleteEs(entity);
			break;
		case H2:
			deleteH2(entity);
			break;
		default:
			break;
		}
	}

	public void deleteEs(A entity) {
		getEsRepository().delete(entity.toEsEntity(null));
	}

	public void deleteH2(A entity) {
		getH2Repository().delete(entity.toH2Entity());
	}

	protected abstract SrBlogEsRepository<E, I> getEsRepository();

	protected abstract SrBlogH2Repository<H, I> getH2Repository();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SrBlogEntity create(A entity) {
		switch (entityType) {
		case ES:
			return createEs(entity);
		case H2:
			return saveH2(entity);
		default:
			return null;
		}
	}

	public E createEs(A entity) {
		SrBlogEsRepository<E, I> repository = getEsRepository();
		return repository.index(entity.toEsEntity(repository.count() + 1));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long count() {
		switch (entityType) {
		case ES:
			return countEs();
		case H2:
			return countH2();
		default:
			return null;
		}
	}

	private Long countEs() {
		return getEsRepository().count();
	}

	private Long countH2() {
		return getH2Repository().count();
	}

}
