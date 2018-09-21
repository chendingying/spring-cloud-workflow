package com.spring.cloud.common.exception;

/**
 * 禁止异常
 *
 * @author cdy
 * @create 2018/9/4
 */
public class ForbiddenException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException(String ret,String msg) {super(ret,msg);}

    public ForbiddenException(String ret,String msg,Throwable cause) {super(ret,msg,cause);}
}
