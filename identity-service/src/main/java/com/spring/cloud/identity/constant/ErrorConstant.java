package com.spring.cloud.identity.constant;

/**
 * 错误码常量
 * @author cdy
 * @create 2018/9/5
 */
public class ErrorConstant {
    public static final String USER_NOT_FOUND = "20001";
    public static final String USER_ACCOUNT_REPEAT = "20002";
    public static final String USER_ALREADY_STOP = "20003";
    public static final String USER_PWD_NOT_MATCH = "20004";
    public static final String USER_PASSWORD_CONFIRM_ERROR = "20006";
    public static final String USER_OLD_PASSWORD_WRONG = "20007";


    public static final String MENU_NOT_FOUND = "21001";
    public static final String MENU_HAVE_CHILDREN = "21002";
    public static final String MENU_ALREADY_ROLE_USE = "21003";

    public static final String ROLE_NOT_FOUND = "22001";
    public static final String ROLE_ALREADY_USER_USE = "22002";


    public static final String GROUP_NOT_FOUND = "23001";
    public static final String GROUP_HAVE_CHILDREN = "23002";
    public static final String Group_ALREADY_USER_USE = "23003";

    public static final String MODULETYPE_NOT_FOUND = "24001";
    public static final String MODULETYPE_HAVE_CHILDREN = "24002";
    public static final String MODULETYPE_ALREADY_USER_USE = "24003";
}
