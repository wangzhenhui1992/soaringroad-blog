package com.soaringroad.blog.entity.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import com.soaringroad.blog.entity.AbstractSrBlogEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "aws_question")
public class AwsQuestion extends AbstractSrBlogEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2399262431827670838L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator="awsQuestionGenerator")
    @TableGenerator(name = "awsQuestionGenerator",pkColumnValue="AWS_QUESTION", allocationSize=1)
    private Long id;
    @Column(length = 10000,columnDefinition="TEXT")
    private String question;
    private String answer;
    @Column(length = 10000,columnDefinition="TEXT")
    private String explanation;
    @Column(length = 10000,columnDefinition="TEXT")
    private String reference;

    @Override
    public String redisKey() {
        return null;
    }

}
