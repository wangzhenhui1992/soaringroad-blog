package com.soaringroad.blog.restapi;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaringroad.blog.dao.SrBlogDao;
import com.soaringroad.blog.entity.AbstractSrBlogEntity;
import com.soaringroad.blog.entity.SrBlogEntity;
import com.soaringroad.blog.entity.SrBlogEsEntity;
import com.soaringroad.blog.entity.SrBlogH2Entity;
import com.soaringroad.blog.vo.SrBlogEntityTypeEnum;
import com.soaringroad.blog.vo.SrBlogQueryEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSrBlogApiService<T extends AbstractSrBlogEntity, E extends Serializable> {

	@Value("${app.entitytype}")
	private SrBlogEntityTypeEnum entityType;

	/**
	 * ObjectMapper
	 */
	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = { "/{id:(?!^(?:search|count)$).*}",
			"/{id:(?!^(?:search|count)$).*}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SrBlogEntity> get(@PathVariable E id) {

		if (!checkGet(id)) {
			return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		log.info("GET:" + id);
		SrBlogEntity entity = callGet(id);
		log.info("GET返回:" + entity);
		return entity == null ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>(entity, HttpStatus.OK);
	}

	@RequestMapping(value = { "/search",
			"/search/" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SrBlogEntity>> search(@RequestParam String qStr) {
		SrBlogQueryEntity queryEntity = parseQueryEntity(qStr);
		if (!checkSearch(queryEntity)) {
			return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		log.info("SEARCH:" + queryEntity);
		Iterable<? extends SrBlogEntity> result = callSearch(queryEntity);
		Iterator<? extends SrBlogEntity> iterator = result.iterator();
		if (!iterator.hasNext()) {
			log.info("SEARCH返回:未找到数据");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		List<SrBlogEntity> resultList = new ArrayList<SrBlogEntity>();
		resultList.add(iterator.next());
		while (iterator.hasNext()) {
			resultList.add(iterator.next());
		}
		log.info("SEARCH返回:" + resultList);
		return new ResponseEntity<>(resultList, HttpStatus.OK);
	}

	@RequestMapping(value = { "/count",
			"/count/" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Long> count() {

		if (!checkCount()) {
			return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Long count = callCount();
		log.info("GET返回:" + count);
		return new ResponseEntity<>(count, HttpStatus.OK);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SrBlogEntity> post(@RequestBody T entity) {
		if (!checkPost(entity)) {
			return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		log.info("POST:" + entity);
		SrBlogEntity result = callPost(entity);
		log.info("POST返回:" + result);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@RequestMapping(value = { "/{id:(?!^(?:search|count)$).*}",
			"/{id:(?!^(?:search|count)$).*}/" }, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SrBlogEntity> put(@RequestBody T entity) {
		if (!checkPut(entity)) {
			return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		log.info("PUT:" + entity);
		SrBlogEntity result = callPut(entity);
		log.info("PUT返回:" + result);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@RequestMapping(value = { "/{id:(?!^(?:search|count)$).*}",
			"/{id:(?!^(?:search|count)$).*}" }, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<T> delete(@RequestBody T entity) {
		if (!checkDelete(entity)) {
			return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		log.info("DELETE:" + entity);
		// TODO 通过ID去获取Entity，然后再删除
		callDelete(entity);
		log.info("DELETE返回:" + entity);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private SrBlogEntity callGet(E id) {
		Optional<? extends SrBlogEntity> opt = getDao().findById(id);
		return opt.isPresent() ? opt.get() : null;
	}

	private Long callCount() {
		return getDao().count();
	}

	private Iterable<? extends SrBlogEntity> callSearch(SrBlogQueryEntity q) {
		return getDao().search(q);
	}

	private SrBlogEntity callPost(T entity) {
		return getDao(entity).create(entity);
	}

	private SrBlogEntity callPut(T entity) {
		return getDao(entity).save(entity);
	}

	private void callDelete(T entity) {
		getDao(entity).delete(entity);
	}

	private SrBlogDao<T,? extends SrBlogEsEntity, ? extends SrBlogH2Entity, E> getDao(T srBlogEntity) {
		return srBlogEntity.getDao();
	}

	private SrBlogDao<T, ? extends SrBlogEsEntity, ? extends SrBlogH2Entity, E> getDao() {
		T newInstance = getInstance();
		if (newInstance == null) {
			return null;
		}
		return newInstance.getDao();
	}

	@SuppressWarnings("unchecked")
	private T getInstance() {
		Class<T> entityClass = (Class<T>) ParameterizedType.class.cast(this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		T newInstance = null;
		try {
			newInstance = entityClass.newInstance();
		} catch (InstantiationException e) {
			log.error("无法生成实例. class=" + entityClass, e);
		} catch (IllegalAccessException e) {
			log.error("无法生成实例. class=" + entityClass, e);
		}
		return newInstance;
	}

	protected boolean checkGet(E id) {
		return false;
	}

	protected boolean checkSearch(SrBlogQueryEntity q) {
		return false;
	}

	protected boolean checkPost(T entity) {
		return false;
	}

	protected boolean checkPut(T entity) {
		return false;
	}

	protected boolean checkDelete(T entity) {
		return false;
	}

	protected boolean checkCount() {
		return false;
	}

	private SrBlogQueryEntity parseQueryEntity(String qStr) {
		if (qStr == null || qStr.isEmpty()) {
			return null;
		}
		SrBlogQueryEntity queryEntity = null;
		try {
			queryEntity = objectMapper.readValue(qStr, SrBlogQueryEntity.class);
		} catch (JsonParseException e) {
			log.error("JSON转换错误", e);
		} catch (JsonMappingException e) {
			log.error("JSON转换错误", e);
		} catch (IOException e) {
			log.error("JSON转换错误", e);
		}
		return queryEntity;
	}

}
