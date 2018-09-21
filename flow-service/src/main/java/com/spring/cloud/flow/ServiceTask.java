package com.spring.cloud.flow;

import org.flowable.engine.common.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @author cdy
 * @create 2018/9/6
 */
public class ServiceTask implements JavaDelegate {

    private Expression test1;

    @Override
    public void execute(DelegateExecution delegateExecution) {

    }
}
