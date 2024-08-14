package com.example.demo.controller;


import com.example.demo.entity.Notice;
import com.example.demo.service.NoticeService;
import org.apache.ibatis.jdbc.Null;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest

class NoticeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NoticeService noticeService;

    @InjectMocks
    private NoticeController noticeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(noticeController).build();
    }

    @Test
    void update() {

    }

    @Test
    public void delete_true() throws Exception {
        int test_id = 1;
        Mockito.when(noticeService.deleteByPrimaryKey(test_id)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/notice/delete/{id}",test_id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("删除了id为：" + test_id + "的公告"));
    }

    @Test
    public void delete_false() throws Exception{
        int text_id = 2;
        Mockito.when(noticeService.deleteByPrimaryKey(text_id)).thenReturn(false);
        mockMvc.perform(delete("/notice/delete/{id}",text_id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("删除失败"));
    }
    @Test
    public void delete_id_null() throws Exception{
        Null text_id = null;
        Mockito.when(noticeService.deleteByPrimaryKey(null)).thenReturn(false);
        mockMvc.perform(delete("/notice/delete/{id}",text_id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("id为空"));
    }


    @Test
    void differentnotice() {
    }

    @Test
    public void testSelectone() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date releaseTime = dateFormat.parse("2023-01-01 10:00:00");
        Date effectiveTime = dateFormat.parse("2023-12-31 23:59:59");

        Notice notice = new Notice();
        notice.setId(1);
        notice.setTheme("Test Notice");
        notice.setCategory("General");
        notice.setUnit("IT Department");
        notice.setApprover("John Doe");
        notice.setPublisher("Jane Smith");
        notice.setReleasetime(releaseTime);
        notice.setEffectivetime(effectiveTime);
        notice.setText("This is a test notice.");
        notice.setImage("test.jpg");
        notice.setAnnex("test.pdf");
        notice.setState("Active");

        when(noticeService.selectByPrimaryKey(1)).thenReturn(notice);

        mockMvc.perform(get("/notice/selectone")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"theme\":\"Test Notice\",\"category\":\"General\",\"unit\":\"IT Department\",\"approver\":\"John Doe\",\"publisher\":\"Jane Smith\",\"releasetime\":\"2023-01-01 10:00:00\",\"effectivetime\":\"2023-12-31 23:59:59\",\"text\":\"This is a test notice.\",\"image\":\"test.jpg\",\"annex\":\"test.pdf\",\"state\":\"Active\"}"));
    }

    @Test
    void condition() {

    }

    @Test
    void keyword() {
    }

    @Test
    void uploadNotice() {
    }

    @Test
    void downloadFile() {
    }

    @Test
    void printNotice() {
    }

    @Test
    void approveNotice() {
    }

    @Test
    void updateExpiredNotices() {
    }
}