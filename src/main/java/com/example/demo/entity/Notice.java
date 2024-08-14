package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice implements Serializable {
    /**
    * 公告id
    */
    @NotBlank(message = "用户名不能为空")
    private Integer id;

    /**
    * 主题
    */
    private String theme;

    /**
    * 类别
    */
    private String category;

    /**
    * 发布单位
    */
    private String unit;

    /**
    * 批准人
    */
    private String approver;

    /**
    * 发布人
    */
    private String publisher;

    /**
    * 发布时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date releasetime;

    /**
    * 有效时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date effectivetime;

    /**
    * 文本
    */
    private String text;

    /**
    * 图片
    */
    private String image;

    /**
    * 附件
    */
    private String annex;

    /**
    * 状态
    */
    private String state;

}