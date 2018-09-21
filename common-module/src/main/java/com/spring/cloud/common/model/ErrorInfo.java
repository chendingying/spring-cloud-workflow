package com.spring.cloud.common.model;

/**
 * 错误返回类
 * @author cdy
 * @create 2018/9/4
 */
public class ErrorInfo {
    private String ret;
    private String msg;

    public ErrorInfo(String ret, String msg){
        this.ret = ret;
        this.msg = msg;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
