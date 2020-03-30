package com.ls.l7mall_2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ServletComponentScan
public class L7mall2Application {

    public static void main(String[] args) {
        SpringApplication.run(L7mall2Application.class, args);
    }

}
