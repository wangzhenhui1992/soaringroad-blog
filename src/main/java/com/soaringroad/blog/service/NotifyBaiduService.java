package com.soaringroad.blog.service;

import java.util.List;
import java.util.Set;

import com.soaringroad.blog.vo.BaiduCommitResponse;

public interface NotifyBaiduService {
    
    BaiduCommitResponse notifyBaidu(List<String> urls, Set<String> categories, Set<String> labels);
    
    BaiduCommitResponse notifyBaidu();
}
