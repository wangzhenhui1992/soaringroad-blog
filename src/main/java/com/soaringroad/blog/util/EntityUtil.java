package com.soaringroad.blog.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaringroad.blog.common.AbstractEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public final class EntityUtil {
  
  private static ObjectMapper om;
  
  @Autowired
  public EntityUtil(ObjectMapper objectMapper) {
    om = objectMapper;
  }

  public static <T> Object parseMapToEntity(Object obj, Class<T> clazz) {
    if (obj.getClass().equals(clazz)) {
      return obj;
    }
    T result = null;
    try {
      result = om.readValue(om.writeValueAsString(obj), clazz);
    } catch (IOException e) {
      log.error("Fail to pasrse redis result[LinkedHashMap] to " + clazz.getName(), e);
      return result;
    }
    return result;
  }

  public static String getCacheKey(AbstractEntity entity) {
    return Optional.ofNullable(entity)
        .map(e -> String.format(SrBlogConsts.ENTITY_KEY, e.getEntityName(), e.getEntityId())).orElse(null);
  }
}
