package com.glxy.pro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.glxy.pro.mapper")
@EnableSwagger2WebMvc
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
