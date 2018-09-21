package com.spring.cloud.flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * 流程引擎服务类
 * @author cdy
 * @create 2018/9/4
 */
@SpringBootApplication(scanBasePackages = "com.spring")
public class FlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlowApplication.class, args);
    }
}
