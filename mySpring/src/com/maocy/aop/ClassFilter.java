package com.maocy.aop;

public interface ClassFilter {

    Boolean matchers(Class beanClass) throws Exception;
}
