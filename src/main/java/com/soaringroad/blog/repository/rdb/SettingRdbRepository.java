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
package com.soaringroad.blog.repository.rdb;

import com.soaringroad.blog.common.RdbRepository;
import com.soaringroad.blog.entity.Setting;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <pre>
 * 
 * </pre>
 */
@Component
public interface SettingRdbRepository extends RdbRepository<Setting,Long> {
  List<Setting> findByName(String name);
  default Setting findFirstByName(String name) {
    List<Setting> settings = findByName(name);
    return CollectionUtils.isEmpty(settings) ? null : settings.get(0);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  default Setting newEntity() {
    return new Setting();
  }
}
