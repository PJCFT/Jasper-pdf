package com.jasper;/*
 *@author: PJC
 *@time: 2020/2/10
 *@description: null
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.jasper")
public class Application {
    //启动入口
    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }
}
