package com.spring.cloud.common.exception;

/**
 * 授权验证失败异常
 *
 * @author cdy
 * @create 2018/9/4
 */
public class AuthErrorException extends BaseException {

    private static final long serialVersionUID = 1L;

    public AuthErrorException(String ret,String msg) {
        super(ret,msg);
    }

    public AuthErrorException(String ret,String msg,Throwable cause) {super(ret,msg,cause);}
}
