package com.jk.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SerModel {
    private Integer serId;//Integer
    private String title;    // 标题  String

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date addDate;  //添加时间
    private String content; //服务内容
}
