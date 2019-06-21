package com.soaringroad.blog.repository.rdb;

import com.soaringroad.blog.common.RdbRepository;
import com.soaringroad.blog.entity.Advertisement;
import org.springframework.stereotype.Component;

@Component
public interface AdvertisementRdbRepository extends RdbRepository<Advertisement, Long> {

  /**
   * {@inheritDoc}
   */
  @Override
  default Advertisement newEntity() {
    return new Advertisement();
  }
  
}
