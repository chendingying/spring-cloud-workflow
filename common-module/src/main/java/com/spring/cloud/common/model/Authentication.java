package com.spring.cloud.common.model;

/**
 * 验证信息线程类
 * @author cdy
 * @create 2018/9/4
 */
public class Authentication {
    static ThreadLocal<String> userIdThreadLocal = new ThreadLocal<String>();
    static ThreadLocal<String> tokenThreadLocal = new ThreadLocal<String>();

    public static void setUserId(String userId) {
        userIdThreadLocal.set(userId);
    }

    public static String getUserId() {
        return userIdThreadLocal.get();
    }

    public static void setToken(String token) {
        tokenThreadLocal.set(token);
    }

    public static String getToken() {
        return tokenThreadLocal.get();
    }

    public static void clear() {
        userIdThreadLocal.remove();
        tokenThreadLocal.remove();
    }
}
