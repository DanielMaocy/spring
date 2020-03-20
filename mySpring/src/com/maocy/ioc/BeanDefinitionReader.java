package com.maocy.ioc;

import java.io.FileNotFoundException;

public interface BeanDefinitionReader {

    void loadBeanDefinitions(String location) throws FileNotFoundException, Exception;
}
