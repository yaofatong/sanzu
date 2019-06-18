package com.jk.yftdemo.mapper;

import com.jk.yftdemo.model.Customer;
import com.jk.yftdemo.util.MenuTree;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;


import java.util.HashMap;
import java.util.List;

public interface CustomerMapper {
    Long getCount(@Param("customerId") String customerId, @Param("certificatesNumber") String certificatesNumber, @Param("customerPhone") String customerPhone, @Param("customerName") String customerName);

    List<Customer> getCustomer(@Param("page") Integer page, @Param("limit") Integer limit, @Param("customerId") String customerId, @Param("certificatesNumber") String certificatesNumber, @Param("customerPhone") String customerPhone, @Param("customerName") String customerName);

    @Select("select * from menutree")
    List<MenuTree> getTree();
}
