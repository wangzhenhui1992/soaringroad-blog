package com.soaringroad.blog.entity.common;

import com.soaringroad.blog.entity.AbstractSrBlogEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "advertisement")
public class Advertisement extends AbstractSrBlogEntity {
  
  /**
   * 
   */
  private static final long serialVersionUID = 9217307680304380027L;
  
  /**
   * 文章ID
   */
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE,generator="adsGenerator")
  @TableGenerator(name = "adsGenerator",pkColumnValue="ADS", allocationSize=1)
  private long adsNo;
  private String image;
  private String url;
  private String text;
  private int showFlg;

  @Override
  public String redisKey() {
    return null;
  }

}
