package com.soaringroad.blog.api.visit;

import com.soaringroad.blog.common.AbstractRestApiService;
import com.soaringroad.blog.entity.Advertisement;
import com.soaringroad.blog.vo.SrBlogQueryEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementApi extends AbstractRestApiService<Advertisement, Long> {

  @Override
  protected boolean checkGet(Long id) {
    return true;
  }

  @Override
  protected boolean checkSearch(SrBlogQueryEntity q) {
    return true;
  }

  @Override
  protected boolean checkCount() {
    return true;
  }

}
