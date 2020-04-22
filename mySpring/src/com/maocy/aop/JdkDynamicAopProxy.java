package com.maocy.aop;

import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

final public class JdkDynamicAopProxy extends AbstractAopProxy implements InvocationHandler {

    public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
        super(advisedSupport);
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(getClass().getClassLoader(), advisedSupport.getTargetSource().getInterfaces(), this);
    }

    /**
     * InvocationHandler 接口中的 invoke 方法具体实现，封装了具体的代理逻辑
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodMatcher methodMatcher = advisedSupport.getMethodMatcher();
        // 使用方法匹配器 methodMatcher 测试 bean 中原始方法 method 是否符合匹配规则
        if (methodMatcher != null && methodMatcher.matchers(method, advisedSupport.getTargetSource().getTargetClass())) {
            // 获取 Advice。MethodInterceptor 的父接口继承了 Advice
            MethodInterceptor methodInterceptor = advisedSupport.getMethodInterceptor();

            /*
             * 将 bean 的原始 method 封装成 MethodInvocation 实现类对象，
             * 将生成的对象传给 Adivce 实现类对象，执行通知逻辑
             */
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(advisedSupport.getTargetSource().getTarget(), method, args));
        } else {
            // 当前 method 不符合匹配规则，直接调用 bean 中的原始 method
            return method.invoke(advisedSupport.getTargetSource().getTarget(), args);
        }
    }
}
