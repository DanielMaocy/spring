package com.maocy.ioc.test;

import com.maocy.ioc.xml.XmlBeanFactory;
import org.junit.Test;

public class XmlBeanFactoryTest {

    @Test
    public void getBean() throws Exception {
        System.out.println("--------- IOC test ----------");
        String location = getClass().getClassLoader().getResource("com/maocy/ioc/test/resource/toy-spring.xml").getFile();
        XmlBeanFactory bf = new XmlBeanFactory(location);
        Wheel wheel = (Wheel) bf.getBean("wheel");
        System.out.println(wheel);
        Car car = (Car) bf.getBean("car");
        System.out.println(car);

        System.out.println("\n--------- AOP test ----------");
        /*HelloService helloService = (HelloService) bf.getBean("helloService");
        helloService.sayHelloWorld();*/
    }
}
