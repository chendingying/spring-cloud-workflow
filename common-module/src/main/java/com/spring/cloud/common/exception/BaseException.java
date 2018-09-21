package com.spring.cloud.common.exception;

/**
 * 自定义异常
 *
 * @author cdy
 * @create 2018/9/4
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String ret;

    public BaseException(String ret, String msg) {
        super(msg);
        this.ret = ret;
    }

    public BaseException(String ret, String msg, Throwable cause) {
        super(msg,cause);
        this.ret = ret;
    }

    public String getRet() {
        return ret;
    }
}
