package com.jk.yftdemo.mapper;

import com.jk.yftdemo.model.Customer;
import com.jk.yftdemo.util.MenuTree;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


import java.util.HashMap;
import java.util.List;
@Repository
public interface CustomerMapper {
    Long getCount(@Param("customerId") String customerId, @Param("certificatesNumber") String certificatesNumber, @Param("customerPhone") String customerPhone, @Param("customerName") String customerName);

    List<Customer> getCustomer(@Param("page") Integer page, @Param("limit") Integer limit, @Param("customerId") String customerId, @Param("certificatesNumber") String certificatesNumber, @Param("customerPhone") String customerPhone, @Param("customerName") String customerName);

    @Select("select * from menutree")
    List<MenuTree> getTree();
    @Select("select * from customer where customerName=#{customerName}")
    Customer getCusByName(@Param("customerName") String customerName);
    @Select("select * from customer where customerPhone=#{phoneNumber}")
    Customer getCusByPhone(@Param("phoneNumber")String phoneNumber);

    void addCus(Customer cus);

    void addBalance(@Param("customerid") String customerId);
    @Update("update customer set titleStatus=1 where customerid=#{customerId}")
    void updateStatus(@Param("customerId") String customerId);

    void updatecus(Customer cus);
    @Update("update customer set titleStatus=#{zt} where customerId=#{cusId}")
    void updatecusstatus(@Param("zt")String zt, @Param("cusId") String cusId);
}
