package com.atguigu.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.ribbon.FeignRetryPolicy;

@SpringBootApplication
@EnableFeignClients
public class FeignOrderMain80 {

    public static void main(String[] args) {
        SpringApplication.run(FeignOrderMain80.class,args);
    }
}
