package com.kilikili.web;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.kilikili")
@MapperScan("com.kilikili.mappers")
@EnableTransactionManagement
@EnableScheduling
public class kilikiliWebRunApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(kilikiliWebRunApplication.class, args);
    }
}
