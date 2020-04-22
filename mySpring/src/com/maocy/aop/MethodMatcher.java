package com.maocy.aop;

import java.lang.reflect.Method;

public interface MethodMatcher {

    Boolean matchers(Method method, Class beanClass);
}
