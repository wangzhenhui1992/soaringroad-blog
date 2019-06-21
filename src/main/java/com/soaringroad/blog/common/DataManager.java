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
package com.soaringroad.blog.common;

import com.soaringroad.blog.vo.SrBlogQueryEntity;
import org.springframework.data.domain.Page;

import java.io.Serializable;

public interface DataManager<A extends AbstractEntity, I extends Serializable> {
  A findById(I id);

  Page<A> search(SrBlogQueryEntity queryEntity);

  A create(A entity);

  A save(A entity);

  void delete(A entity);

  Long count();

  Iterable<A> findAll();
}
