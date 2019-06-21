/******************************************************************
*   _____                  _             _____                 _  *
*  / ____|                (_)           |  __ \               | | *
* | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
*  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
*  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
* |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
*                                   __/ |                         *
*                                  |___/                          *
* Copyright ©2017-2018 www.soaringroad.com | All rights reserved. *
******************************************************************/
package com.soaringroad.blog.api.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.soaringroad.blog.api.visit.ArticleApi;
import com.soaringroad.blog.entity.Article;

@RestController
@RequestMapping("/api/admin/article")
public class ArticleAdminApi extends ArticleApi {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkPost(Article entity) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkPut(Article entity) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkDelete(Article entity) {
        return true;
    }

}
