package com.soaringroad.blog.dao.impl;

import com.soaringroad.blog.core.LRUCache;
import com.soaringroad.blog.core.LRUTimedValue;
import com.soaringroad.blog.dao.AbstractSrBlogDao;
import com.soaringroad.blog.entity.common.Advertisement;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdvertisementDao extends AbstractSrBlogDao<Advertisement, Long> {

  private LRUCache<Long, Advertisement> cache = new LRUCache<>(12);

  @Override
  public Advertisement save(Advertisement entity) {
    cache.set(entity.getAdsNo(), entity);
    return super.save(entity);
  }

  @Override
  public Optional<Advertisement> findById(Long id) {
    LRUTimedValue<Advertisement> adsValue = cache.get(id);
    if (adsValue != null) {
      return Optional.ofNullable(adsValue.getValue());
    }
    Optional<Advertisement> opt = super.findById(id);
    cache.set(id, opt.orElse(null));
    return opt;
  }

}
