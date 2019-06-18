package com.jk.yftdemo.controller;

import com.jk.yftdemo.model.Customer;
import com.jk.yftdemo.service.CustomerService;
import com.jk.yftdemo.util.MenuTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService service;
    @RequestMapping("tocustomer")
    public String tocustomer(){
        return "customerQuery";
    }
    @RequestMapping("findcustomer")
    @ResponseBody
    public HashMap<String ,Object> findcustomer(Integer page, Integer limit, Customer cus){
        return service.findcustomer((page-1)*limit,limit,cus);
    }
    @RequestMapping("getTree")
    @ResponseBody
    public List<MenuTree> getTree(){
        return service.getTree();
    }
    @RequestMapping("toindex")
    public String toindex(){
        return "index";
    }
}
