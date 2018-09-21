package com.spring.cloud.common.exception;

import com.spring.cloud.common.constant.CoreConstant;
import com.spring.cloud.common.model.ErrorInfo;
import org.springframework.context.MessageSource;

import java.lang.*;
import java.util.Locale;

/**
 * 异常工厂类
 * @author cdy
 * @create 2018/9/4
 */
public class ExceptionFactory {

    private MessageSource messageSource;

    public ExceptionFactory(MessageSource messageSource) {this.messageSource = messageSource;}

    /** 默认local 输出中文 **/
    public static final Locale DEFALUT_LOCALE = Locale.CHINA;

    public String getResource(String code,Object... arg) {return messageSource.getMessage(code,arg,DEFALUT_LOCALE);}

    /** 创建并返回错误信息 **/
    public ErrorInfo createInternalError() {
        return new ErrorInfo(CoreConstant.ERROR_CODE,getResource(CoreConstant.ERROR_CODE));
    }

    /** 自定义异常 **/
    public void throwInternalError() {
        throw new BaseException(CoreConstant.ERROR_CODE,getResource(CoreConstant.ERROR_CODE));
    }

    /** 自定义异常 **/
    public void throwDefinedException(String code, Object... args) {
        throw new BaseException(code,getResource(code,args));
    }

    /** 授权验证失败异常 **/
    public void throwAuthError(String code,Object... args) {
        throw new AuthErrorException(code,getResource(code,args));
    }

    /** 对象没有找到异常 **/
    public void throwObjectNotFound(String code,Object... args) {
        throw new ObjectNotFoundException(code,getResource(code,args));
    }
    /** 非法参数异常 **/
    public void throwIllegalArgument(String code,Object... args) {
        throw new IllegalArgumentException(code,getResource(code,args));
    }

    /** 禁止异常 **/
    public void throwForbidden(String code, Object... args) {
        throw new ForbiddenException(code, getResource(code, args));
    }

    /** 冲突异常 **/
    public void throwConflict(String code,Object... args){
        throw new ConflictException(code,getResource(code,args));
    }

    /** token失效 **/
    public void throwToken(String code,Object... args){throw new TokenErrorException(code,getResource(code,args));}
}
