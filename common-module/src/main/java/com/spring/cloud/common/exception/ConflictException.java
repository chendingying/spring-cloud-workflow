package com.spring.cloud.common.exception;

/**
 * 冲突异常
 *
 * @author cdy
 * @create 2018/9/4
 */
public class ConflictException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ConflictException(String ret,String msg) {super(ret,msg);}

    public ConflictException(String ret,String msg,Throwable cause) {super(ret,msg,cause);}
}
