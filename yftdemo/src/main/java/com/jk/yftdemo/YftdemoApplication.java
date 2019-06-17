package com.jk.yftdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@MapperScan("com.jk.yftdemo.mapper")
@EnableEurekaClient
@SpringBootApplication
public class YftdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(YftdemoApplication.class, args);
    }

}
