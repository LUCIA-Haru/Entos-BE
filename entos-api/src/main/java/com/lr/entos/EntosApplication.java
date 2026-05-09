package com.lr.entos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.lr.entos")
public class EntosApplication {
    public static void main(String[] args){
        SpringApplication.run(EntosApplication.class,args);
    }
}
