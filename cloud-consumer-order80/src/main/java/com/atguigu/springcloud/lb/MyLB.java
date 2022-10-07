package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Component   //扫描
public class MyLB implements LoadBalance {

    private AtomicInteger atomicInteger=new AtomicInteger(0);


    //轮询
    public final  int getAndOncrement()
    {
        int current;
        int next;

        do{
            current=this.atomicInteger.get();
            next=current>=2147483647?0:current+1;

        }while(!this.atomicInteger.compareAndSet(current,next));
        System.out.println("******next:"+next);
        return next;
    }


    //负载平衡   返回一个服务
    @Override
    public ServiceInstance instance(List<ServiceInstance> serviceInstances)
    {

        int index=getAndOncrement()%serviceInstances.size();
        return serviceInstances.get(index);
    }
}
