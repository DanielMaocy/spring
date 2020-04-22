package com.maocy.aop;

import com.maocy.ioc.BeanPostProcessor;
import com.maocy.ioc.factory.BeanFactory;
import com.maocy.ioc.factory.BeanFactoryAware;
import com.maocy.ioc.xml.XmlBeanFactory;

public class AspectJAwareAdvisorAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {

    private XmlBeanFactory xmlBeanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws Exception {

    }
}
