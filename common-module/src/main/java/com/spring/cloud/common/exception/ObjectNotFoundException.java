package com.spring.cloud.common.exception;

/**
 * 对象没有找到异常
 *
 * @author cdy
 * @create 2018/9/4
 */
public class ObjectNotFoundException extends BaseException {
    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(String ret,String msg) {super(ret,msg);}

    public ObjectNotFoundException(String ret,String msg,Throwable cause) {super(ret,msg,cause);}
}
