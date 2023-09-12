package com.lucky.ssyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author lucky
 * @date 2023/9/3
 */
@SpringBootApplication
@EnableSwagger2WebMvc
@EnableDiscoveryClient
@EnableTransactionManagement
public class ServiceProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceProductApplication.class, args);
    }
}
