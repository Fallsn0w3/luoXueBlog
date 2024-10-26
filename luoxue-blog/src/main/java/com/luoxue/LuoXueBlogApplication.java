package com.luoxue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.luoxue.mapper")
public class LuoXueBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LuoXueBlogApplication.class, args);
    }
}
