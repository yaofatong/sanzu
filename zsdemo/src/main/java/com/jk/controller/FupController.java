package com.jk.controller;

import com.jk.model.DepModel;
import com.jk.model.SerModel;
import com.jk.model.SetModel;
import com.jk.service.FupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;


@Controller
public class FupController {

    @Autowired
    private FupService fupService;

    @RequestMapping("tobasis")
    public String tobasis(){
        return "basis";
    }


    @RequestMapping("serFind")
    @ResponseBody
    public HashMap<String,Object> serFind(Integer page , Integer limit){

        return  fupService.serFind((page-1)*limit,limit);
    }

    @RequestMapping("toaddSer")
    public String toaddSer(){
        return "addSer";
    }




    @RequestMapping("saveMyLine")
    @ResponseBody
    public String saveMyLine(SerModel serModel){
            fupService.saveMyLine(serModel);
        return null;
    }

    @RequestMapping("tdbasis")
    public String tdbasis(){
        return "basis";
    }


    @RequestMapping("deleteAll")
    @ResponseBody
    public String deleteAll(Integer serId){
        return fupService.deleteAll(serId);
    }



    @RequestMapping("viewData")
    @ResponseBody
    public ModelAndView viewData(Integer serId){
        ModelAndView mv = new ModelAndView();

        SerModel serModel=fupService.viewData(serId);
        mv.addObject("serModel",serModel);
        mv.setViewName("view");
        return mv;
    }


 /*   @RequestMapping("updateXg")
    public String updateXg(){
        return "addSer";
    }*/

    @RequestMapping("fh")
    public String fh(){
        return "basis";
    }




    @RequestMapping("todays")
    public ModelAndView todays(){
        ModelAndView mv = new ModelAndView();
        SetModel setModel= fupService.licheng();
        mv.addObject("setModel",setModel);

        DepModel depModel=fupService.tian();
        mv.addObject("depModel",depModel);

        mv.setViewName("days");
        return mv;
    }


    @RequestMapping("updateHx")
    public String updateHx(){

        return  "upcheng";
    }



    @RequestMapping("addQd")
    @ResponseBody
    public String addQd(SetModel setModel, DepModel depModel){
        fupService.cheng(setModel);
        fupService.tianshu(depModel);
        return null;
    }



    @RequestMapping("updateBasis")
    @ResponseBody
    public String updateBasis(SerModel serModel){

        fupService.updateBasis(serModel);


        return null;
    }






    @RequestMapping("tocoupon")
    public String tocoupon(){
        return "coupon";
    }

    @RequestMapping("selectbd")
    @ResponseBody
    public HashMap<String,Object> selectbd(Integer couponId){

        return fupService.selectbd(couponId);
    }








}
