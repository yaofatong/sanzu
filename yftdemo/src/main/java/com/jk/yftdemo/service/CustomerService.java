package com.jk.yftdemo.service;

import com.jk.yftdemo.model.Customer;
import com.jk.yftdemo.util.MenuTree;

import java.util.HashMap;
import java.util.List;

public interface CustomerService {
    HashMap<String, Object> findcustomer(Integer page, Integer limit, Customer cus);

    List<MenuTree> getTree();
}
