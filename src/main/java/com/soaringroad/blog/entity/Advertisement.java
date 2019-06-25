package com.soaringroad.blog.entity;

import com.soaringroad.blog.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = Advertisement.ENTITY_NAME)
public class Advertisement extends AbstractEntity {
  
  /**
   * 
   */
  private static final long serialVersionUID = 9217307680304380027L;
  
  public static final String ENTITY_NAME = "advertisement";
  
  /**
   * 文章ID
   */
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE,generator="idGenerator")
  @TableGenerator(name = "idGenerator",pkColumnValue="ADS", allocationSize=1)
  private long adsNo;
  private String image;
  private String url;
  private String text;
  private int showFlg;

  @Override
  public long getEntityId() {
    return this.adsNo;
  }

  @Override
  public String getEntityName() {
    return ENTITY_NAME;
  }


}
