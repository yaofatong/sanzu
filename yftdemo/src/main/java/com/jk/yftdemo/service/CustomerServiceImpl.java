package com.jk.yftdemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jk.yftdemo.mapper.CustomerMapper;
import com.jk.yftdemo.model.Customer;
import com.jk.yftdemo.util.MenuTree;
import com.jk.yftdemo.util.TreeNoteUtil;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerMapper mapper;
    @Autowired
    private JedisPool jedisPool;

    @Override
    public HashMap<String, Object> findcustomer(Integer page, Integer limit, Customer cus) {
        HashMap<String , Object> map=new HashMap<>();

        String customerId = cus.getCustomerId();
        String certificatesNumber = cus.getCertificatesNumber();
        String customerPhone = cus.getCustomerPhone();
        String customerName = cus.getCustomerName();
        Jedis jedis = jedisPool.getResource();

        if(jedis.get("customer"+page+limit+cus.toString())==null||jedis.get("customerCount")==null){
            Long count=mapper.getCount(customerId,certificatesNumber,customerPhone,customerName);
            List<Customer> list=mapper.getCustomer(page,limit,customerId,certificatesNumber,customerPhone,customerName);
            jedis.set("customerCount",count+"");
            jedis.set("customer"+page+limit+cus.toString(),JSON.toJSONString(list));
            List<Customer> list1=JSON.parseArray(jedis.get("customer"+page+limit+cus.toString()),Customer.class);
            map.put("data",list1);
            map.put("count",jedis.get("customerCount"));
        }else{
            List<Customer> list1=JSON.parseArray(jedis.get("customer"+page+limit+cus.toString()),Customer.class);
            map.put("data", list1);
            map.put("count",jedis.get("customerCount"));

        }

        //List<Customer> list=mapper.getCustomer(page,limit,customerId,certificatesNumber,customerPhone,customerName);

        map.put("code","0");
        return map;
    }

    @Override
    public List<MenuTree> getTree() {
        Jedis resource = jedisPool.getResource();
        if(StringUtils.isNotEmpty(resource.get("tree"))){
            List<MenuTree> list1=JSON.parseArray(resource.get("tree"),MenuTree.class);
            return list1;
        }else{
            List<MenuTree> list=mapper.getTree();
            list= TreeNoteUtil.getFatherNode(list);
            String s = JSON.toJSONString(list);

            resource.set("tree",s);
            List<MenuTree> list1=JSON.parseArray(resource.get("tree"),MenuTree.class);
            return list1;
        }



    }

    @Override
    public Customer getCustomer(String customerName) {

        return mapper.getCusByName(customerName);
    }

    @Override
    public Customer getCusByPhone(String phoneNumber) {

        return mapper.getCusByPhone(phoneNumber);
    }

    @Override
    public void addCus(Customer cus) {
        mapper.addCus(cus);
        Jedis resource = jedisPool.getResource();
        resource.del("customerCount");
        //Customer cusByPhone = mapper.getCusByPhone(cus.getCustomerPhone());
        //mapper.addBalance(cusByPhone.getCustomerId());
    }

    @Override
    public void updateStatus(String customerId) {
        mapper.updateStatus(customerId);
    }

    @Override
    public void updatecus(Customer cus) {
        mapper.updatecus(cus);
        Jedis resource = jedisPool.getResource();
        resource.del("customerCount");
    }

    @Override
    public void updatecusstatus(String zt, String cusId) {
        mapper.updatecusstatus(zt,cusId);
        Jedis resource = jedisPool.getResource();
        resource.del("customerCount");
    }
}
