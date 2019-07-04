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
package com.soaringroad.blog.manager;

import com.soaringroad.blog.common.AbstractDataManager;
import com.soaringroad.blog.entity.Setting;
import com.soaringroad.blog.repository.rdb.SettingRdbRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class SettingManager extends AbstractDataManager<Setting, Long> {
  
  public Setting getSettingByName(String name) {
    SettingRdbRepository settingRepository = (SettingRdbRepository) this.rdbRepository;
    List<Setting> settings = settingRepository.findByName(name);
    return CollectionUtils.isEmpty(settings) ? null : settings.get(0);
  }
}
