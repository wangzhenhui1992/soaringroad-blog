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
package com.soaringroad.blog.entity;

import com.soaringroad.blog.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

/**
 * <pre>
 * 
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = Setting.ENTITY_NAME)
public class Setting extends AbstractEntity {
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 1L;
  
  public static final String ENTITY_NAME = "setting";
  
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE,generator="settingGenerator")
  @TableGenerator(name = "settingGenerator",pkColumnValue="SETTING", allocationSize=1)
  private Long id;
  
  private String name;
  
  private String value;

  /**
   * {@inheritDoc}
   */
  @Override
  public long getEntityId() {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getEntityName() {
    return ENTITY_NAME;
  }

}
