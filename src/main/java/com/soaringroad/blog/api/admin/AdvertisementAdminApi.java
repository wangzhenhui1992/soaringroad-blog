package com.soaringroad.blog.api.admin;

import com.soaringroad.blog.common.AbstractRestApiService;
import com.soaringroad.blog.entity.Advertisement;
import com.soaringroad.blog.vo.SrBlogQueryEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/advertisement")
public class AdvertisementAdminApi extends AbstractRestApiService<Advertisement, Long>{

    @Override
    protected boolean checkGet(Long id) {
        return true;
    }

    @Override
    protected boolean checkSearch(SrBlogQueryEntity q) {
        return true;
    }

    @Override
    protected boolean checkPost(Advertisement entity) {
        return true;
    }

    @Override
    protected boolean checkPut(Advertisement entity) {
        return true;
    }

    @Override
    protected boolean checkDelete(Advertisement entity) {
        return true;
    }

    @Override
    protected boolean checkCount() {
        return true;
    }

}
