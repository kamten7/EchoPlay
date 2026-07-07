package com.myvideoplatform.admin;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.myvideoplatform")
@MapperScan("com.myvideoplatform.mappers")
@EnableTransactionManagement
@EnableScheduling
public class MyVideoPlatformAdminRunApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(MyVideoPlatformAdminRunApplication.class, args);
    }
}