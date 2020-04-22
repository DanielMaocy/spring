package com.maocy.ioc.xml;

import com.maocy.ioc.BeanDefinition;
import com.maocy.ioc.BeanPostProcessor;
import com.maocy.ioc.BeanReference;
import com.maocy.ioc.PropertyValue;
import com.maocy.ioc.factory.BeanFactory;
import com.maocy.ioc.factory.BeanFactoryAware;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlBeanFactory implements BeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private List<String> beanDefinitionNames = new ArrayList<>();

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    private XmlBeanDefinitionReader beanDefinitionReader;

    public XmlBeanFactory(String location) throws Exception {
        beanDefinitionReader = new XmlBeanDefinitionReader();
        loadBeanDefinitions(location);
    }

    @Override
    public Object getBean(String beanId) throws Exception {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanId);
        if (beanDefinition == null) {
            throw new IllegalArgumentException("没有这个Bean：beanId=" + beanId);
        }

        Object bean = beanDefinition.getBean();
        if (bean == null) {
            bean = createBean(beanDefinition);
            bean = initializeBean(bean, beanId);
            beanDefinition.setBean(bean);
        }

        return bean;
    }

    private Object createBean(BeanDefinition beanDefinition) throws Exception {
        Object bean = beanDefinition.getBeanClass().newInstance();
        applyPropertyValues(bean, beanDefinition);

        return bean;
    }

    private void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }

        for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValueList()) {
            Object value = propertyValue.getValue();
            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = getBean(beanReference.getName());
            }

            try {
                Method method = bean.getClass().getDeclaredMethod("set" + propertyValue.getName().substring(0, 1).toUpperCase()
                        + propertyValue.getName().substring(1), value.getClass());
                method.setAccessible(true);
                method.invoke(bean, value);
            } catch (NoSuchMethodException e) {
                Field field = bean.getClass().getDeclaredField(propertyValue.getName());
                field.setAccessible(true);
                field.set(bean, value);
            }

        }
    }

    private Object initializeBean(Object bean, String name) throws Exception {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
        }

        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
            registerBeanDefinition();
            registerBeanPostProcessor();
        }

        return bean;
    }

    private void loadBeanDefinitions(String location) throws Exception {
        beanDefinitionReader.loadBeanDefinitions(location);
        registerBeanDefinition();
        registerBeanPostProcessor();
    }

    /**
     * 注册Bean的定义到容器中
     */
    private void registerBeanDefinition() {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionReader.getRegistry().entrySet()) {
            String name = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            beanDefinitionMap.put(name, beanDefinition);
            beanDefinitionNames.add(name);
        }
    }

    public void registerBeanPostProcessor() throws Exception {
        List beans = getBeansForType(BeanPostProcessor.class);
        for (Object bean : beans) {
            addBeanPostProcessor((BeanPostProcessor) bean);
        }
    }

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.add(beanPostProcessor);
    }

    public List getBeansForType(Class type) throws Exception {
        List beans = new ArrayList();
        for (String beanDefinitionName : beanDefinitionNames) {
            if (type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())) {
                beans.add(getBean(beanDefinitionName));
            }
        }

        return beans;
    }
}
