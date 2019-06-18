package com.jk.service;

import com.alibaba.fastjson.JSON;
import com.jk.mapper.FupMapper;
import com.jk.model.DepModel;
import com.jk.model.SerModel;
import com.jk.model.SetModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;

@Service
public class FupServiceImpl implements FupService {
    @Autowired
    private FupMapper fupMapper;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public HashMap<String, Object> serFind(Integer page, Integer limit) {
        HashMap<String, Object> map=new HashMap<>();
        Jedis jedis = jedisPool.getResource();
        if(jedis.get("customer"+page)==null||jedis.get("customerCount")==null){
            Long count=fupMapper.getCount();
            List<SerModel> list=fupMapper.serFind(page,limit);
            jedis.set("customerCount",count+"");
            jedis.set("customer"+page, JSON.toJSONString(list));
            List<SerModel> list1=JSON.parseArray(jedis.get("customer"+page),SerModel.class);
            map.put("data",list1);
            map.put("count",jedis.get("customerCount"));
        }else{
            List<SerModel> list1=JSON.parseArray(jedis.get("customer"+page),SerModel.class);
                map.put("data",list1);
                map.put("count",jedis.get("customerCount"));

        }

        map.put("code","0");
        return map;


      /* Long count=fupMapper.getCount();
        List<SerModel> list=fupMapper.serFind(page,limit);
        HashMap<String, Object> map=new HashMap<>();
        map.put("data",list);
        map.put("count",count);
        map.put("code",0);
        map.put("msg","");
        return map;*/
    }

    @Override
    public void saveMyLine(SerModel serModel) {
        fupMapper.saveMyLine(serModel);
    }

    @Override
    public String deleteAll(Integer serId) {
        fupMapper.deleteAll(serId);
        return null;
    }

    @Override
    public SerModel viewData(Integer serId) {
        SerModel serModel=fupMapper.viewData(serId);

        return serModel;
    }

    @Override
    public SetModel licheng() {
        SetModel setModel=fupMapper.licheng();
        return setModel;
    }

    @Override
    public DepModel tian() {
        DepModel depModel=fupMapper.tian();
        return depModel;
    }

    @Override
    public void cheng(SetModel setModel) {
        fupMapper.cheng(setModel);
    }

    @Override
    public void tianshu(DepModel depModel) {
        fupMapper.tianshu(depModel);
    }

    @Override
    public void updateBasis(SerModel serModel) {
        fupMapper.updateBasis(serModel);
    }




    @Override
    public HashMap<String, Object> selectbd(Integer couponId) {

        return fupMapper.selectbd(couponId);
    }


}
