package com.soaringroad.blog.restapi.visit;

import com.soaringroad.blog.entity.common.Advertisement;
import com.soaringroad.blog.restapi.AbstractSrBlogApiService;
import com.soaringroad.blog.vo.SrBlogQueryEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/advertisment")
public class AdvertisementApiService extends AbstractSrBlogApiService<Advertisement, Long> {

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
