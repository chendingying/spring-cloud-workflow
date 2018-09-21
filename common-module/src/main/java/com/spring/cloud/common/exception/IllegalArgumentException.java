package com.spring.cloud.common.exception;

/**
 * 非法参数异常
 *
 * @author cdy
 * @create 2018/9/4
 */
public class IllegalArgumentException extends BaseException {

    private static final long serialVersionUID = 1L;

    public IllegalArgumentException(String ret,String msg) {super(ret,msg);}

    public IllegalArgumentException(String ret,String msg,Throwable cause){super(ret,msg,cause);}
}
