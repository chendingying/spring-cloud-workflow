package com.spring.cloud.flow.test;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @author cdy
 * @create 2018/9/6
 */
public class a implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("自动注册到外部系统");
    }
}
