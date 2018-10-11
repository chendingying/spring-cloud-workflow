package com.spring.cloud.common.filter;

import com.spring.cloud.common.utils.IDUtil;
import com.spring.cloud.common.utils.JsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Hom
 * @version 1.0
 * @Time 2018/1/3 20:02.
 */
@Aspect
@Component
@Order(3)
public class WebLogAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    private String headString;
    private int len = 600;

    @Pointcut("execution(public * com.spring..*.resource.*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        headString = "[" + IDUtil.getShortUUID().toUpperCase() + "]";
        startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info(headString + " URL :  " + request.getRequestURL().toString());
        logger.info(headString + " HTTP_METHOD : " + request.getMethod());
        logger.info(headString + " IP : " + request.getRemoteAddr());
        logger.info(headString + " CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info(headString + " REQUEST ARGS : " + JsonUtil.objectToString(joinPoint.getArgs()));
        logger.info(headString + " REQUEST TIME : " + (System.currentTimeMillis() - startTime.get()) + "ms");
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        StringBuffer stringBuffer = new StringBuffer();
        String args = JsonUtil.objectToString(ret);
        if (args.length()> len) {
            stringBuffer.append(args.substring(0, len));
            stringBuffer.append(" ** Only show the top " + len + " **");
        }else{
            stringBuffer.append(args);
        }
        logger.info(headString + " RESPONSE ARGS: " + stringBuffer);
        logger.info(headString + " RESPONSE TIME : " + (System.currentTimeMillis() - startTime.get()) + "ms");
        startTime.remove();
    }
}
