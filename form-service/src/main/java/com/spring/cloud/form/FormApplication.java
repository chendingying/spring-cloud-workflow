package com.spring.cloud.form;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 表单接口
 * @author cdy
 * @create 2018/9/5
 */
@SpringBootApplication(scanBasePackages = "com.spring")
public class FormApplication {
    public static void main(String[] args) {
        SpringApplication.run(FormApplication.class,args);
    }
}
