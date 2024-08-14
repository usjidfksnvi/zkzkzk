package com.example.demo.mapper;

import com.example.demo.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NoticeMapper {
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

//  保存和上传
    void insertNotice(Notice notice);
//  查全部
    List<Notice> findAll();

}