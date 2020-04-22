package com.maocy.aop;

public interface AopProxy {

    /**
     * 为目标 bean 生成代理对象
     * @return
     */
    Object getProxy();
}
