package com.spring.cloud.common.utils;

import com.spring.cloud.common.constant.CoreConstant;
import com.spring.cloud.common.exception.ExceptionFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by CDZ on 2018/9/14.
 */
public class TokenUserIdUtils {
    //异常工厂
    @Autowired
    private ExceptionFactory exceptionFactory;

    //获取token下的userId
    public  String tokenUserId(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String userId = null;
        try {
            //获取token
            String token = request.getHeader("Token");
            if(ObjectUtils.isEmpty(token)) {
                token = request.getParameter("token");
            }
            //判断是否有token
            if(ObjectUtils.isEmpty(token)) {
                exceptionFactory.throwAuthError(CoreConstant.HEADER_TOKEN_NOT_FOUND);
            }
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
        return userId;
    }
}
