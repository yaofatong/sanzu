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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
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

    @RequestMapping("updatecusstatus")
    @ResponseBody
    public String updatecusstatus(String zt,String cusId){
        service.updatecusstatus(zt,cusId);
        return null;
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
    @RequestMapping("tosuccess")
    public String tosuccess(){
        return "success";
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
    @RequestMapping("verificationCode2")
    public void phoneTest2(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

        session.setAttribute("imgcode2", code);
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
                        map.put("status","账号被封禁");
                    }else{
                        map.put("status",1);
                        request.getSession().setAttribute("customer",customer);
                    }
                }else{
                    map.put("status","账号或者密码错误");
                }
            }else {
                map.put("status","验证码错误");
            }
        }else{
            map.put("status","不可为空");
        }

        return map;
    }
    @RequestMapping("sendMessage")
    @ResponseBody
    public HashMap<String, Object> phoneTest(String phoneNumber) {

        Jedis redis = jedisPool.getResource();
        HashMap<String, Object> hash = new HashMap<>();
        Date date = new Date();
        //先去mongodb中查询看是否在黑名单  避免多次添加该用户 TODO

        //判断是否今天三次上限
        SimpleDateFormat si = new SimpleDateFormat("yyyy-MM-dd");
        String format = si.format(date);

        //判断是否1 分钟以内

        Integer randomNumber= (int)(Math.random()*899999+100000);
        System.out.println(randomNumber);
        HashMap<String, Object> params = new HashMap<>();
        params.put("accountSid",ConstantConf.ACCOUNTSID);
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
            redis.expire(phoneNumber,300);

            redis.lpush(phoneNumber+format,s);
            redis.expire(phoneNumber+format,86400);

            hash.put("yzm",randomNumber);
            hash.put("code",0);
            hash.put("msg","短信发送成功，五分钟内有效");

            return hash;
        }else {
            hash.put("code", 1);
            hash.put("msg", "发送失败");
            return hash;
        }

    }
    @RequestMapping("sjlogin")
    @ResponseBody
    public HashMap<String ,Object> sjlogin(HttpServletRequest request,String phoneNumber,String yzm){
        Jedis redis = jedisPool.getResource();
        HashMap<String, Object> map = new HashMap<>();
        //1代表登录成功 2代表超时5分钟了 3 代表验证码错误 4 代表该手机号未注册 5代表不可为空 6 状态封禁中 7 代表 图片验证码错误


        if(phoneNumber==null||yzm==null){
            map.put("status",5);
        }else{
        Customer cus=service.getCusByPhone(phoneNumber);
        if(cus!=null){
            if(redis.get(cus.getCustomerPhone())==null){
                map.put("status",2);
            }else{
                if(redis.get(cus.getCustomerPhone()).equals(yzm)){
                    if(cus.getTitleStatus()==2){
                        map.put("status",6);
                    }else{
                        map.put("status",1);
                        request.getSession().setAttribute("customer",cus);
                    }
                }else{
                    map.put("status",3);
                }
            }
        }else{
            map.put("status",4);
        }
        }
        return map;
    }
    @RequestMapping("sendMessage2")
    @ResponseBody
    public HashMap<String, Object> phoneTest2(HttpServletRequest request,String phoneNumber,String imgcode2) {

        Jedis redis = jedisPool.getResource();
        HashMap<String, Object> hash = new HashMap<>();
        Date date = new Date();

        //判断是否今天三次上限
        SimpleDateFormat si = new SimpleDateFormat("yyyy-MM-dd");
        String format = si.format(date);

        //判断是否1 分钟以内



        Integer randomNumber = (int) (Math.random() * 899999 + 100000);
        System.out.println(randomNumber);
        HashMap<String, Object> params = new HashMap<>();
        params.put("accountSid", ConstantConf.ACCOUNTSID);
        params.put("to", phoneNumber);
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        params.put("timestamp", timestamp);
        String sig = Md5Util.getMd532(ConstantConf.ACCOUNTSID + ConstantConf.AUTH_TOKEN + timestamp);
        params.put("sig", sig);
        params.put("templateid", ConstantConf.TEMPLATEID);
        params.put("param", randomNumber);
        String string = HttpClientUtil.post(ConstantConf.SMS_URL, params);

        System.out.println(string);

        JSONObject parseObject = JSON.parseObject(string);
        String string2 = parseObject.getString("respCode");
        if (ConstantConf.SMS_SUCCESS.equals(string2)) {

            String s = randomNumber.toString();

            redis.set(phoneNumber, s);
            redis.expire(phoneNumber, 300);

            redis.lpush(phoneNumber + format, s);
            redis.expire(phoneNumber + format, 86400);
            if (StringUtils.isNotEmpty(imgcode2)) {
                HttpSession session = request.getSession(true);
                String imgcode1 = (String) session.getAttribute("imgcode2");

                if (imgcode1.equals(imgcode2)) {
                    hash.put("yzm", randomNumber);
                    hash.put("code", 0);
                    hash.put("msg", "短信发送成功，五分钟内有效");

                    return hash;
                } else {
                    hash.put("code", 1);
                    hash.put("msg", "验证码错误，请重试");
                    return hash;
                }
            } else {
                hash.put("code", 1);
                hash.put("msg", "图片验证码不可为空，请重试");
                return hash;
            }

        } else {
            hash.put("code", 1);
            hash.put("msg", "发送失败");
            return hash;
        }
    }
    @RequestMapping("toregister")
    public String toregister(){
        return "register";
    }
    @RequestMapping("registerzc")
    @ResponseBody
    public  HashMap<String ,Object> register(Customer cus){
        System.out.println(cus.toString());
        HashMap<String, Object> map = new HashMap<>();
        //状态 1 注册成功 2 用户名账号已被占用 3 单手机号只能注册1位 4 全部为空

        if(cus!=null){
            Customer cus1=service.getCusByPhone(cus.getCustomerPhone());
            Customer cus2=service.getCustomer(cus.getCustomerName());
            if(cus1!=null){
                map.put("msg","单手机号只能注册1位");
                return map;
            }else if(cus2!=null){
                map.put("msg","用户名账号已被占用");
                return map;
            }else{
                map.put("msg","1");
                service.addCus(cus);
                return map;
            }
        }else{
            map.put("msg","全部为空");
            return map;
        }

    }
    @RequestMapping("torelieve")
    public String torelieve(){
        return "relieve";
    }
    @RequestMapping("relievejc")
    @ResponseBody
    public  HashMap<String ,Object> relievejc(String username,String userpass,String phone,String sjyzm){
        // 1为解除封号 2 用户名和密码不匹配 3 用户名和手机号不匹配 4 验证码错误 5不可为空 6 没有该用户
        //7 该账号未被封禁
        HashMap<String, Object> map = new HashMap<>();
        Jedis redis = jedisPool.getResource();
        String s = redis.get(phone);
        if(username==null||userpass==null||phone==null||sjyzm==null){
            map.put("status","不可为空");
        }else{
            if(s!=null){
                if(s.equals(sjyzm)){
                    Customer cus=service.getCustomer(username);
                    if(cus!=null){
                        if(cus.getCustomerPass().equals(userpass)){
                            if(cus.getCustomerPhone().equals(phone)){
                                if(cus.getTitleStatus()==1){
                                    map.put("status","该账号未被封禁");
                                }else{
                                    service.updateStatus(cus.getCustomerId());
                                    map.put("status","解除封号成功");
                                }


                            }else{
                                map.put("status","用户名和手机号不匹配");
                            }
                        }else{
                            map.put("status","用户名和密码不匹配");
                        }
                    }else{
                        map.put("status","没有该用户");
                    }
                }else{
                    map.put("status","验证码错误");
                }
            }else{
                map.put("status","验证码超十五分钟，请重新获取");
            }
        }
        return map;
    }
    @RequestMapping("toqueryOneCustomer")
    public String toqueryOneCustomer(HttpServletRequest request, Model model){
        Customer cus = (Customer) request.getSession().getAttribute("customer");
        System.out.println(cus);
        if(cus==null){
            return "login";
        }else{
            if(cus.getCertificatesType()==1){
                cus.setCertificatesTypeName("身份证");
            }else{
                cus.setCertificatesTypeName("驾驶证");
            }
            model.addAttribute("cus",cus);
            return "queryOneCustomer";
        }
    }
    @RequestMapping("toupdateCustomer")
    public String toupdateCustomer(){
        return "updateCutomer";
    }
    @RequestMapping("selectCutomer")
    @ResponseBody
    public Customer selectCutomer(HttpServletRequest request,Model model){
        Customer cus = (Customer) request.getSession().getAttribute("customer");

        return cus;
    }
    @RequestMapping("updatexg")
    @ResponseBody
    public HashMap<String ,Object> updatexg(HttpServletRequest request,Customer cus){
        HashMap<String, Object> map = new HashMap<>();
        //1 用户名重复了 2 手机号已绑定 3 用户名不可为空 4 手机号不可为空
        if(cus!=null){
            if(StringUtils.isEmpty(cus.getCustomerName())){
                map.put("msg","用户名不可为空");
            }else if(StringUtils.isEmpty(cus.getCustomerPhone())){
                map.put("msg","手机号不可为空");
            }else{
                    service.updatecus(cus);
                    map.put("msg","修改成功");
                    Customer customer3=service.getCustomer(cus.getCustomerName());
                    request.getSession().setAttribute("customer",customer3);
                }
            }

        return map;
    }
}
