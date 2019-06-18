package com.jk.service;

import com.jk.model.DepModel;
import com.jk.model.SerModel;
import com.jk.model.SetModel;

import java.util.HashMap;

public interface FupService {

    HashMap<String, Object> serFind(Integer page, Integer limit);

    void saveMyLine(SerModel serModel);

    String deleteAll(Integer serId);

    SerModel viewData(Integer serId);


    SetModel licheng();

    DepModel tian();

    void cheng(SetModel setModel);

    void tianshu(DepModel depModel);


    void updateBasis(SerModel serModel);

    HashMap<String, Object> selectbd(Integer couponId);
}
