package com.spring.cloud.common.annotation;

import java.lang.annotation.*;

/**
 * 无须认证
 * @author cdy
 * @create 2018/9/4
 */
@Retention(RetentionPolicy.RUNTIME) // 表示需要在什么级别保存该注释信息，用于描述注释的生命周期（即：被描述的注释在什么范围内有效）
//RYBTUNE：在运行时有效（即运行时保留）
@Target({ElementType.TYPE,ElementType.METHOD}) // 作用：用于描述注解的使用范围（即：被描述的注解可以用在什么地方）
//TYPE：用于描述类，接口（包括注解类型）或enum声明  METHOD：用来描述方法
@Documented //用于描述其他类型的annotation应该被作为被标注的程序成员的公共API，因此可以被例如javadoc此类的工具文档化。Documented是一个标记注解，没有成员。
public @interface NotAuth {
}
