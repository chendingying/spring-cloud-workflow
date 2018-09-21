package com.spring.cloud.identity.response;

import com.spring.cloud.common.model.ObjectMap;
import com.spring.cloud.identity.domain.User;

/**
 * 人员数据转换表
 * @author cdy
 * @create 2018/9/5
 */
public class UserConverter {
    public static ObjectMap convertAuth(User user, String token) {
        ObjectMap result = new ObjectMap();
        result.put("id", user.getId());
        result.put("name", user.getName());
        result.put("avatar", user.getAvatar());
        result.put("token", token);
        return result;
    }
}
