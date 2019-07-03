package com.soaringroad.blog.api.admin;

import com.soaringroad.blog.api.visit.AdvertisementApi;
import com.soaringroad.blog.entity.Advertisement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/advertisement")
public class AdvertisementAdminApi extends AdvertisementApi{

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
}
