package com.jk.yftdemo.model;

import lombok.Data;

@Data
public class Customer {
    private String customerId;
    private String customerName;
    private Integer certificatesType;
    private String certificatesNumber;
    private String customerPhone;
    private String customerMailbox;
    private Integer certificatesTypeId;
    private String certificatesTypeName;
}
