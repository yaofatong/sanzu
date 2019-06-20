package com.jk.yftdemo.service;

import com.jk.yftdemo.model.Customer;
import com.jk.yftdemo.util.MenuTree;

import java.util.HashMap;
import java.util.List;

public interface CustomerService {
    HashMap<String, Object> findcustomer(Integer page, Integer limit, Customer cus);

    List<MenuTree> getTree();

    Customer getCustomer(String customerName);

    Customer getCusByPhone(String phoneNumber);

    void addCus(Customer cus);

    void updateStatus(String customerId);

    void updatecus(Customer cus);

    void updatecusstatus(String zt, String cusId);
}
