/** 
 * <pre>项目名称:ssm-users-chh 
 * 文件名称:ConstantConf.java 
 * 包名:com.jk.chh 
 * 创建日期:2019年3月8日下午3:27:17 
 * Copyright (c) 2019, yuxy123@gmail.com All Rights Reserved.</pre> 
 */  
package com.jk.yftdemo.util;


/**
 * @Author chh
 * @Description //TODO 
 * @Date 14:51 2019/5/16
 * @Param 
 * @return 
 **/
public class ConstantConf {
//发送短信验证码
public static final String SMS_URL="https://api.miaodiyun.com/20150822/industrySMS/sendSMS";

public static final String ACCOUNTSID = "0374867b2c1844dbbe0bf019bf0def28";

public static final String AUTH_TOKEN = "d05d06f418974fc6aceb9233e38b7539";

public static final String TEMPLATEID = "164547838";

public static final String SMS_SUCCESS = "00000";
//登录验证码
public static final String SMS_LOGIN_CODE = "dlyzm";


//记住密码的分隔
public static final  String cookieNamePaw = "1810b";//勾选记住密码的标记
public static final  String splitC = "sdfghj";//密码和用户之间的分割

    //cookie 保存的常量
    public static final  String COOKIEUUID = "COOKIEJIAUUID";


}
