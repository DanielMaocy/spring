package com.maocy.aop;

public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();
}
