package com.example.demo.service;

import com.example.demo.entity.Notice;
import com.ylsoft.framework.core.http.orm.PaginationResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface NoticeService{

//  根据id删除整个公告
    boolean deleteByPrimaryKey(Integer id);

//  待发公告，最新公告，历史公告查询
    List<Notice> selectByState(String state);

//  根据id获取单个公告的完整信息
    Notice selectByPrimaryKey(Integer id);

//  更新
    int updateByPrimaryKeySelective(Notice data);

//  条件查询
    List<Notice> findByCondition(Notice data);

//  关键字查询
    List<Notice> findByFuzzy(String keyword);

//  保存
    void saveNotice(Notice notice);

//  上传
    String storeFile(MultipartFile file) throws IOException;

//  下载
    Path loadFile(String filename);

//  检查并更新已过有效期的公告状态
    void updateExpiredNotices();

}
