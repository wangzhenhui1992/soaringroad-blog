package com.soaringroad.blog.vo;

import java.util.List;
import lombok.Data;

@Data
public class BaiduCommitResponse {
    private int success;
    private int remain;
    private List<String> not_same_site;
    private List<String> not_valid;
    private String error;
    private String message;
}
