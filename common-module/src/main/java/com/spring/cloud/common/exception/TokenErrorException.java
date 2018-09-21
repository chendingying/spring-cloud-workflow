package com.spring.cloud.common.exception;

/**
 * Created by CDZ on 2018/9/15.
 */
public class TokenErrorException extends BaseException {
    private static final long serialVersionUID = 1L;
    public TokenErrorException(String ret,String msg) {
        super(ret,msg);
    }

    public TokenErrorException(String ret,String msg,Throwable cause) {super(ret,msg,cause);}
}
