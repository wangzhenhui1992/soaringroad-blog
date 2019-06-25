package com.soaringroad.blog.entity;

import com.soaringroad.blog.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = AwsQuestion.ENTITY_NAME)
public class AwsQuestion extends AbstractEntity {
  
  public static final String ENTITY_NAME = "aws_question";

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2399262431827670838L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator="idGenerator")
    @TableGenerator(name = "idGenerator",pkColumnValue="AWS_QUESTION", allocationSize=1)
    private Long id;
    @Column(length = 10000,columnDefinition="TEXT")
    private String question;
    private String answer;
    @Column(length = 10000,columnDefinition="TEXT")
    private String explanation;
    @Column(length = 10000,columnDefinition="TEXT")
    private String reference;

    @Override
    public long getEntityId() {
      return this.id;
    }

    @Override
    public String getEntityName() {
      return ENTITY_NAME;
    }

}
