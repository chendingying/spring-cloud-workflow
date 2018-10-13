package com.spring.cloud.common.aop;

import com.spring.cloud.common.constant.CoreConstant;
import com.spring.cloud.common.exception.ExceptionFactory;
import com.spring.cloud.common.model.Authentication;
import com.spring.cloud.common.utils.ObjectUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * jwt检验类，通过注解屏蔽部分接口
 * @author cdy
 * @create 2018/9/4
 */
@Aspect //作用时把当前类标识为一个切面供容器读取
@Component //把类交给spring容器管理
@Order(1) //使用order属性，设置改类在spring容器中的加载顺序
public class AuthCheckAspect {
    private Logger logger = LoggerFactory.getLogger(AuthCheckAspect.class);

    //异常工厂
    @Autowired
    private ExceptionFactory exceptionFactory;

    @Pointcut("execution(public * com.spring..*.resource.*.*(..))")
    public void webRequestAuth() {}

    @Pointcut("!@within(com.spring.cloud.common.annotation.NotAuth) && !@annotation(com.spring.cloud.common.annotation.NotAuth)")
    public void webRequestNotAuth() {}

//    @Around("webRequestAuth()&& webRequestNotAuth()")
    public Object doAuth(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String userId = null;

        //获取token
        String token = request.getHeader("Token");
        if(ObjectUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }

        logger.info("token:" + token);

        //判断是否有token
        if(ObjectUtils.isEmpty(token)) {
            exceptionFactory.throwAuthError(CoreConstant.HEADER_TOKEN_NOT_FOUND);
        }

        // 判断是否是非法的token
        if(!token.startsWith("Bearer")) {
            exceptionFactory.throwAuthError(CoreConstant.HEADER_TOKEN_ILLEGAL);
        }

        try {
            Claims claims = Jwts.parser().setSigningKey(CoreConstant.JWT_SECRET).parseClaimsJws(token.substring(7)).getBody();
            userId = claims.getId();
            // 判断userId是否有值
            if(ObjectUtils.isEmpty(userId)) {
                exceptionFactory.throwAuthError(CoreConstant.TOKEN_UERID_NOT_FOUND);
            }

            Date expiration = claims.getExpiration();
            //判断token是否最新
            if (expiration != null && expiration.before(new Date())) {
                exceptionFactory.throwAuthError(CoreConstant.TOKEN_EXPIRE_OUT);
            }
        }catch (Exception e) {
            exceptionFactory.throwAuthError(CoreConstant.TOKEN_AUTH_CHECK_ERROR);
        }
        try{
            Authentication.setToken(token);
            Authentication.setUserId(userId);
            return pjp.proceed(pjp.getArgs());
        }finally {
            Authentication.clear();
        }
    }



































}
