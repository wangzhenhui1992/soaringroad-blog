/******************************************************************
 *   _____                  _             _____                 _  *
 *  / ____|                (_)           |  __ \               | | *
 * | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
 *  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
 *  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
 * |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
 *                                   __/ |                         *
 *                                  |___/                          *
 * Copyright ©2017-2020 www.soaringroad.com | All rights reserved. *
 ******************************************************************/
package com.soaringroad.blog.repository.cache;

import com.soaringroad.blog.common.CacheRepository;
import com.soaringroad.blog.core.TimedValue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * 
 * </pre>
 */
@Component
@ConditionalOnProperty(name = "app.data.redis.enable", havingValue = "false", matchIfMissing = true)
public class MemoryCacheRepository implements CacheRepository {

  private static final ConcurrentHashMap<String, TimedValue<Object>> CACHE_CONTAINER = new ConcurrentHashMap<String, TimedValue<Object>>();

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getValue(String key) {
    TimedValue<Object> timedValue = CACHE_CONTAINER.getOrDefault(key, null);
    if (timedValue == null) {
      return null;
    }
    if (timedValue.getExpireTime() == -1
        || System.currentTimeMillis() < timedValue.getExpireTime() + timedValue.getExpireDuration()) {
      return timedValue.getValue();
    }
    return removeKey(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(String key, Object value) {
    setValue(key, value, true);
  }

  private void setValue(String key, Object value, boolean refresh) {
    TimedValue<Object> timedValue = CACHE_CONTAINER.get(key);
    if (timedValue == null) {
      timedValue = new TimedValue<Object>();
      CACHE_CONTAINER.put(key, timedValue);
    }
    timedValue.setValue(value);
    if (timedValue.getExpireDuration() == -1) {
      return;
    }
    long currentTimeMillis = System.currentTimeMillis();
    if (currentTimeMillis < timedValue.getExpireTime() + timedValue.getExpireDuration()) {
      if (refresh) {
        timedValue.setExpireTime(currentTimeMillis);
      }
      return;
    }
    timedValue.setExpireDuration(-1L);
    timedValue.setExpireTime(-1L);
  }

  // /**
  // * {@inheritDoc}
  // */
  // @Override
  // public List<Object> getMultiValue(List<String> keys) {
  // // TODO Auto-generated method stub
  // return null;
  // }
  //
  // /**
  // * {@inheritDoc}
  // */
  // @Override
  // public void setMultiValue(Map<String, Object> map) {
  // // TODO Auto-generated method stub
  //
  // }
  //
  // /**
  // * {@inheritDoc}
  // */
  // @Override
  // public void increaseValue(String key, double delta) {
  // // TODO Auto-generated method stub
  //
  // }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long increaseValue(String key, long delta) {
    Object obj = getValue(key);
    if (obj == null) {
      setValue(key, 1L, false);
      return 1L;
    }
    if (!(obj instanceof Long)) {
      return null;
    }
    long result = (Long) obj + 1L;
    setValue(key, result, false);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  // @Override
  // public Object getHashValue(String key, String hashKey) {
  // // TODO Auto-generated method stub
  // return null;
  // }

  // /**
  // * {@inheritDoc}
  // */
  // @Override
  // public void setHashValue(String key, String hashKey, Object value) {
  // // TODO Auto-generated method stub
  //
  // }
  //
  // /**
  // * {@inheritDoc}
  // */
  // @Override
  // public void setHashAll(String key, Map<String, Object> values) {
  // // TODO Auto-generated method stub
  //
  // }
  //
  // /**
  // * {@inheritDoc}
  // */
  // @Override
  // public void deleteHashValue(String key, String... hashKeys) {
  // // TODO Auto-generated method stub
  //
  // }
  //
  // /**
  // * {@inheritDoc}
  // */
  // @Override
  // public void increaseHashValue(String key, String hashKey, long delta) {
  // // TODO Auto-generated method stub
  //
  // }
  //
  // /**
  // * {@inheritDoc}
  // */
  // @Override
  // public void increaseHashValue(String key, String hashKey, double delta) {
  // // TODO Auto-generated method stub
  //
  // }
  //
   /**
   * {@inheritDoc}
   */
   @Override
   public void delete(String key) {
     removeKey(key);
   }

  /**
   * {@inheritDoc}
   */
  @Override
  public void expire(String key, long seconds) {
    TimedValue<Object> obj = CACHE_CONTAINER.get(key);
    if (obj == null) {
      return;
    }
    obj.setExpireTime(System.currentTimeMillis());
    obj.setExpireDuration(seconds * 1000L);

  }

  private Object removeKey(String key) {
    CACHE_CONTAINER.remove(key);
    return null;
  }

}
