package com.atguigu.springcloud.controller;



import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.lb.LoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class OrderController {


    // 通过在eureka上注册过的微服务名称调用
    public static final String PAYMENT_URL="http://CLOUD-PAYMENT-SERVICE";

    @Resource //默认按照类型自动注入（找配置文件或配置类）
    private RestTemplate restTemplate;//HTTP服务通讯

    @Resource
    private LoadBalance loadBalance;//自定义负载平衡

    @Resource
    private DiscoveryClient discoveryClient;//发现服务


    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment)
    {

        return restTemplate.postForObject(PAYMENT_URL+"/payment/create",payment,CommonResult.class);//调用Eureka上的服务
    }


    @GetMapping("/consumer/payment/get/{id}")
    public  CommonResult<Payment> getPayment(@PathVariable("id") Long id)
    {
        //getForObject将响应体中的数据转化为json
        return restTemplate.getForObject(PAYMENT_URL+"/payment/get/"+id,CommonResult.class);//调用Eureka上的服务
    }


    @GetMapping("/consumer/payment/getForEntity/{id}")
    public CommonResult<Payment> getPayment2(@PathVariable("id") Long id)
    {

        //getForEntity返回的是ResponseEntity的对象(包含状态码，响应头，响应体等数据)
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);

        //获取状态码并且判断是否成功
        if(entity.getStatusCode().is2xxSuccessful())
        {
            return entity.getBody();
        }
        else
        {
            return new CommonResult<>(444,"操作失败");
        }
    }

    @GetMapping(value ="/consumer/payment/lb")
    public String getPaymentLB()
    {

        //获得CLOUD-PAYMENT-SERVICE服务的所有服务
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");

        if(instances==null || instances.size()<=0)
        {
            return null;
        }

        ServiceInstance serviceInstance=loadBalance.instance(instances);

        URI uri=serviceInstance.getUri();

        return restTemplate.getForObject(uri+"/payment/lb",String.class);
    }


}
