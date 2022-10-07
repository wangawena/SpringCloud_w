package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration   //配置类=配置文件
public class ApplicationContextConfig {


    @Bean
    //@LoadBalanced
    public RestTemplate getRestTemplate()//便捷访问远程Http服务的方法
    {
        return  new RestTemplate();
    }
}
