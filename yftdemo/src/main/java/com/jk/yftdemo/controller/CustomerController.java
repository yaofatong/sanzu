package com.jk.yftdemo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.yftdemo.model.Customer;
import com.jk.yftdemo.service.CustomerService;
import com.jk.yftdemo.util.ConstantConf;
import com.jk.yftdemo.util.HttpClientUtil;
import com.jk.yftdemo.util.Md5Util;
import com.jk.yftdemo.util.MenuTree;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService service;
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("tocustomer")
    public String tocustomer(){
        return "customerQuery";
    }

    @RequestMapping("tologin")
    public String tologin(){
        return "login";
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

    @RequestMapping("verificationCode")
    public void phoneTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
        int width = 85;
        int height = 32;
        int lines = 5;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = img.getGraphics();

        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 设置字体
        g.setFont(new Font("宋体", Font.BOLD, 18));

        // 随机数字
        Random r = new Random(new Date().getTime());
        String code = "";
        for (int i = 0; i < 4; i++) {
            int a = r.nextInt(10);
            code += a;
            int y = 10 + r.nextInt(20);// 10~30范围内的一个整数，作为y坐标

            Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            g.setColor(c);

            g.drawString("" + a, 5 + i * width / 4, y);
        }
        HttpSession session = request.getSession(true);

        session.setAttribute("imgcode", code);
        // 干扰线
        for (int i = 0; i < lines; i++) {
            Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            g.setColor(c);
            g.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height));
        }

        g.dispose();// 类似于流中的close()带动flush()---把数据刷到img对象当中
        // 告诉客户端，输出的格式
        response.reset();
        // 告诉客户端，输出的格式
        response.setContentType("image/jpeg");
        ImageIO.write(img, "jpg", response.getOutputStream());
    }
    @RequestMapping("ordinary")
    @ResponseBody
    public HashMap<String ,Object> ordinaryLogin(HttpServletRequest request,Customer cus,String imgcode){
        HashMap<String, Object> map = new HashMap<>();
        //返回1 登陆成功 2验证码错误 3 密码或者账号错误 4 账号被封禁 5 不可为空
        String customerName = cus.getCustomerName();
        Customer customer=service.getCustomer(customerName);
        HttpSession session = request.getSession(true);
        String imgcode1 = (String)session.getAttribute("imgcode");
        if(cus!=null&& StringUtils.isNotEmpty(imgcode)){
            if(imgcode1.equals(imgcode)){
                if(cus.getCustomerName().equals(customer.getCustomerName())&&
                        cus.getCustomerPass().equals(customer.getCustomerPass())){
                    if(customer.getTitleStatus()==2){
                        map.put("status",4);
                    }else{
                        map.put("status",1);
                        request.setAttribute("customer",customer);
                    }
                }else{
                    map.put("status",3);
                }
            }else {
                map.put("status",2);
            }
        }else{
            map.put("status","5");
        }

        return map;
    }
    @RequestMapping("sss")
    @ResponseBody
    public HashMap<String, Object> phoneTest(String phoneNumber) {

        Jedis redis = jedisPool.getResource();
        HashMap<String, Object> hash = new HashMap<>();
        Date date = new Date();
        //判断是否今天三次上限
        SimpleDateFormat si = new SimpleDateFormat("yyyy-MM-dd");
        String format = si.format(date);
        Long llen1 = redis.llen(phoneNumber + format);

        //判断是否1 分钟以内
        String s2 = redis.get(phoneNumber);

        Integer randomNumber= (int)(Math.random()*899999+100000);
        System.out.println(randomNumber);
        HashMap<String, Object> params = new HashMap<>();

        params.put("accountSid", ConstantConf.ACCOUNTSID);
        params.put("to",phoneNumber);
        String timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        params.put("timestamp",timestamp);
        String sig= Md5Util.getMd532(ConstantConf.ACCOUNTSID+ConstantConf.AUTH_TOKEN+timestamp);
        params.put("sig",sig);
        params.put("templateid",ConstantConf.TEMPLATEID);
        params.put("param",randomNumber);
        String string = HttpClientUtil.post(ConstantConf.SMS_URL,params);

        System.out.println(string);
        JSONObject parseObject = JSON.parseObject(string);
        String string2 = parseObject.getString("respCode");
        if(ConstantConf.SMS_SUCCESS.equals(string2)) {
            String s = randomNumber.toString();

            redis.set(phoneNumber,s);
            redis.expire(phoneNumber,60);

            redis.lpush(phoneNumber+format,s);
            redis.expire(phoneNumber+format,86400);

            hash.put("yzm",randomNumber);
            hash.put("code",0);
            hash.put("msg","短信发送成功，一分钟内有效");

            return hash;
        }else {
            hash.put("code", 1);
            hash.put("msg", "发送失败");
            return hash;
        }

    }
}
