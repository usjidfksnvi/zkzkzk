package com.example.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.ylsoft.framework.core.http.orm.PaginationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.example.demo.mapper.NoticeMapper;
import com.example.demo.entity.Notice;
import com.example.demo.service.NoticeService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class NoticeServiceImpl implements NoticeService{

    @Resource
    private NoticeMapper noticeMapper;
//  上传地址
    @Value("${file.upload-dir}")
    private String uploadDir;

//  根据id删除整个公告
    @Override
    public boolean deleteByPrimaryKey(Integer id) {
            return noticeMapper.deleteByPrimaryKey(id);
    }

//  待发公告，最新公告，历史公告查询
    @Override
    public List<Notice> selectByState(String state){
        List<Notice> notice;
        if (state != null){
            PageHelper.startPage(1,3);
            notice = noticeMapper.selectByState(state);
        }else {
            notice = new ArrayList<>();
        }
        return notice;
//        return noticeMapper.selectByState(state);
    }

//  根据id获取单个公告的完整信息
    @Override
    public Notice selectByPrimaryKey(Integer id) {
        return noticeMapper.selectByPrimaryKey(id);
    }

//  更新
    @Override
    public int updateByPrimaryKeySelective(Notice data) {
        return noticeMapper.updateByPrimaryKeySelective(data);
    }

//  条件查询
    @Override
    public List<Notice> findByCondition(Notice date){
        return noticeMapper.findByCondition(date);
    }

//  关键字查询
    @Override
    public List<Notice> findByFuzzy(String keyword){
        String Keyword = "%" + keyword + "%";
        return noticeMapper.findByFuzzy(Keyword);
    }

//  保存
    @Override
    public void saveNotice(Notice notice) {
        noticeMapper.insertNotice(notice);
    }

//  上传
    @Override
    public String storeFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + filename);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        System.out.println("File stored at: " + filePath);
        return filename;
    }

//  下载
    @Override
    public Path loadFile(String filename) {
        Path filePath = Paths.get(uploadDir, filename).toAbsolutePath().normalize();
        System.out.println("Loading file from: " + filePath);
        return filePath;
    }

//  检查并更新已过有效期的公告状态
    @Override
    @Scheduled(fixedRate = 60000)
    public void updateExpiredNotices() {
        List<Notice> notices = noticeMapper.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Notice notice : notices) {
            if ("新发".equals(notice.getState())) {
                LocalDateTime effectiveTime = notice.getEffectivetime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                if (now.isEqual(effectiveTime) || now.isAfter(effectiveTime)) {
                    notice.setState("历史");
                    noticeMapper.updateByPrimaryKeySelective(notice);
                }
            }
        }
    }
//  分页查询

}
