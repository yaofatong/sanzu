package com.jk.model;

import lombok.Data;

@Data
public class SetModel {
    private Integer inId; //里程ID
    private Double milage; //最高里程数:
    private Double beyond;//超出里程每公里费用
}
