package com.spring.cloud.identity.resource;

import com.spring.cloud.common.annotation.NotAuth;
import com.spring.cloud.common.constant.CoreConstant;
import com.spring.cloud.common.model.ObjectMap;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.common.utils.DateUtils;
import com.spring.cloud.identity.domain.Menu;
import com.spring.cloud.identity.constant.ErrorConstant;
import com.spring.cloud.identity.constant.TableConstant;
import com.spring.cloud.identity.domain.User;
import com.spring.cloud.identity.repository.MenuRepository;
import com.spring.cloud.identity.repository.UserRepository;
import com.spring.cloud.identity.response.ConvertFactory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 授权控制类
 * @author cdy
 * @create 2018/9/5
 */
@RestController
public  class AuthResource extends BaseResource {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MenuRepository menuRepository;

    @PostMapping("/auths/login")
    @ResponseStatus(HttpStatus.OK)
    @NotAuth
    public ObjectMap login(@RequestBody ObjectMap loginRequest) {
        String account = loginRequest.getAsString("account");
        String password = loginRequest.getAsString("password");
        User user = userRepository.findByAccount(account);
        if (user == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.USER_NOT_FOUND);
        }
        if (!user.getPwd().equals(password)) {
            exceptionFactory.throwConflict(ErrorConstant.USER_PWD_NOT_MATCH);
        }
        if (user.getStatus() == TableConstant.USER_STATUS_STOP) {
            exceptionFactory.throwForbidden(ErrorConstant.USER_ALREADY_STOP);
        }
        String token = Jwts.builder().setSubject(account).setId(user.getId().toString()).setIssuedAt(DateUtils.currentTimestamp())
                .setExpiration(new Date(DateUtils.currentTimeMillis() + CoreConstant.LOGIN_USER_EXPIRE_IN)).signWith(SignatureAlgorithm.HS256, CoreConstant.JWT_SECRET).compact();

        return ConvertFactory.convertUseAuth(user, token);
    }

    @GetMapping("/auths/menus")
    @ResponseStatus(HttpStatus.OK)
    public List<ObjectMap> getUserMenus(@RequestParam Integer userId) {
        List<Menu> childMenus = menuRepository.findByUserId(userId);
        List<Menu> parentMenus = menuRepository.findByTypeAndStatusOrderByOrderAsc(TableConstant.MENU_TYPE_PARENT, TableConstant.MENU_STATUS_NORMAL);
        return ConvertFactory.convertUserMenus(parentMenus, childMenus);
    }

    @PutMapping("/auths/password/change")
    @ResponseStatus(HttpStatus.OK)
    public User changePwd(@RequestBody ObjectMap changeRequest) {
        String newPassword = changeRequest.getAsString("newPassword");
        String confirmPassword = changeRequest.getAsString("confirmPassword");
        String oldPassword = changeRequest.getAsString("oldPassword");
        Integer userId = changeRequest.getAsInteger("userId");
        if (!newPassword.equals(confirmPassword)) {
            exceptionFactory.throwConflict(ErrorConstant.USER_PASSWORD_CONFIRM_ERROR);
        }

        User user = userRepository.findOne(userId);
        if (user == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.USER_NOT_FOUND);
        }

        if (!user.getPwd().equals(oldPassword)) {
            exceptionFactory.throwConflict(ErrorConstant.USER_OLD_PASSWORD_WRONG);
        }

        user.setPwd(newPassword);

        return userRepository.save(user);
    }
}
