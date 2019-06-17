package com.jk.mapper;

import com.jk.model.DepModel;
import com.jk.model.SerModel;
import com.jk.model.SetModel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FupMapper {
    @Select("select count(1) from service")
    Long getCount();

    List<SerModel> serFind(@Param("page") Integer page, @Param("limit") Integer limit);

    void saveMyLine(SerModel serModel);

    @Delete("delete from service where serId=#{serId}")
    void deleteAll(@Param("serId") Integer serId);

    SerModel viewData(@Param("serId") Integer serId);

    SetModel licheng();

    DepModel tian();

    void cheng(SetModel setModel);

    void tianshu(DepModel depModel);
}
