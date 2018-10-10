package com.spring.cloud.flow.test;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.el.FixedValue;

/**
 * @author cdy
 * @create 2018/9/6
 */
public class a implements JavaDelegate {

    private FixedValue fieldName;

    public FixedValue getFieldName() {
        return fieldName;
    }

    public void setFieldName(FixedValue fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println(getFieldName().getExpressionText());
        System.out.println("自动注册到外部系统");
    }
}
