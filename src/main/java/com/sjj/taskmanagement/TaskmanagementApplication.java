package com.sjj.taskmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan("com.sjj.taskmanagement")
@MapperScan("com.sjj.mapper")
@EnableScheduling
public class TaskmanagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskmanagementApplication.class, args);
    }

}
