package com.soaringroad.blog.repository.h2;

import com.soaringroad.blog.entity.common.Advertisement;
import com.soaringroad.blog.repository.SrBlogH2Repository;
import org.springframework.stereotype.Component;

@Component
public interface AdvertisementRepository extends SrBlogH2Repository<Advertisement, Long> {

}
