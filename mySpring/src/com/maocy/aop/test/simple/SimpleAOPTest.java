package com.maocy.aop.test.simple;

import com.maocy.service.HelloService;
import com.maocy.service.HelloServiceImpl;
import com.maocy.simple.Advice;
import com.maocy.simple.BeforeAdvice;
import com.maocy.simple.MethodInvocation;
import com.maocy.simple.SimpleAOP;
import org.junit.Test;

public class SimpleAOPTest {

    @Test
    public void getProxy() throws Exception {

        // 1. 创建一个 MethodInvocation 实现类
        MethodInvocation logTask = () -> System.out.println("log task start");
        HelloServiceImpl helloServiceImpl = new HelloServiceImpl();

        // 2. 创建一个 Advice
        Advice beforeAdvice = new BeforeAdvice(helloServiceImpl, logTask);

        // 3. 为目标对象生成代理
        HelloService helloServiceImplProxy = (HelloService) SimpleAOP.getProxy(helloServiceImpl,beforeAdvice);

        helloServiceImplProxy.sayHelloWorld();
    }
}
