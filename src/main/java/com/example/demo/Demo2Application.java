package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
@SpringBootApplication(scanBasePackages = "com.example.demo")
//@MapperScan 注解来扫描MyBatis的Mapper接口，并将它们注册为Spring管理的Bean。这样，你就可以在其他组件中直接注入Mapper接口，而无需在Mapper接口上添加额外的注解
@MapperScan("com.example.demo.mapper")
@EnableCaching
@ServletComponentScan
public class Demo2Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo2Application.class, args);
    }

}
