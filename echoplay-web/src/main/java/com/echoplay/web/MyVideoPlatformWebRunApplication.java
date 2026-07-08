package com.echoplay.web;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.echoplay")
@MapperScan("com.echoplay.mappers")
@EnableTransactionManagement
@EnableScheduling
public class MyVideoPlatformWebRunApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(MyVideoPlatformWebRunApplication.class, args);
    }
}