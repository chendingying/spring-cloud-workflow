package com.spring.cloud.identity.resource;

import com.spring.cloud.common.jpa.Criteria;
import com.spring.cloud.common.jpa.Restrictions;
import com.spring.cloud.common.model.ObjectMap;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.common.resource.PageResponse;
import com.spring.cloud.common.utils.ObjectUtils;
import com.spring.cloud.identity.domain.*;
import com.spring.cloud.identity.repository.*;
import com.spring.cloud.identity.response.ConvertFactory;
import com.spring.cloud.identity.constant.ErrorConstant;
import com.spring.cloud.identity.constant.TableConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 人员资源控制类
 * @author cdy
 * @create 2018/9/5
 */
@RestController
@Api(description = "用户接口")
public class UserResource extends BaseResource {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    private User getUserFromRequest(Integer id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.USER_NOT_FOUND);
        }
        return user;
    }

    @ApiOperation(value = "用户查询" , httpMethod = "GET")
    @GetMapping(value = "/users")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getUsers(@RequestParam Map<String, String> requestParams) {
        Criteria<User> criteria = new Criteria<User>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.like("phone", requestParams.get("phone")));
        criteria.add(Restrictions.eq("status", requestParams.get("status")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(userRepository.findAll(criteria, getPageable(requestParams)));
    }

    @GetMapping(value = "/users/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public User getUser(@PathVariable Integer id) {
        return getUserFromRequest(id);
    }

    @GetMapping(value = "/users/roles")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ObjectMap> getUserRoles(@RequestParam(required = false) Integer id) {
        List<Role> roleRoles = null;
        List<Role> allRoles = roleRepository.findByStatus(TableConstant.ROLE_STATUS_NORMAL);
        if (ObjectUtils.isNotEmpty(id)) {
            roleRoles = roleRepository.findByUserId(id);
        }
        return ConvertFactory.convertUseRoles(allRoles, roleRoles);
    }

    @GetMapping(value = "/users/groups")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ObjectMap> getUserGroups(@RequestParam(required = false) Integer id) {
        List<Group> roleGroups = null;
        List<Group> allGroups = groupRepository.findByStatusOrderByOrderAsc(TableConstant.GROUP_STATUS_NORMAL);
        if (ObjectUtils.isNotEmpty(id)) {
            roleGroups = groupRepository.findByUserId(id);
        }
        return ConvertFactory.convertUserGroups(allGroups, roleGroups);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public User createUser(@RequestBody ObjectMap userRequest) {
        String account = userRequest.getAsString("account");
        User user = userRepository.findByAccount(account);
        if (user != null) {
            exceptionFactory.throwConflict(ErrorConstant.USER_ACCOUNT_REPEAT);
        }
        return saveUserAndGroupAndRole(null, userRequest);
    }

    @PutMapping(value = "/users/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public User updateUser(@PathVariable Integer id, @RequestBody ObjectMap userRequest) {
        User user = getUserFromRequest(id);
        return saveUserAndGroupAndRole(user, userRequest);
    }

    private User saveUserAndGroupAndRole(User user, ObjectMap userRequest) {
        String phone = userRequest.getAsString("phone");
        if (user == null) {
            user = new User();
            user.setPwd(phone.substring(phone.length() - 6, phone.length()));
            user.setAccount(userRequest.getAsString("account"));
        }
        user.setName(userRequest.getAsString("name"));
        user.setSex(userRequest.getAsByte("sex"));
        user.setAvatar("http://wx.qlogo.cn/mmopen/fsFT5ibPNuBiaZGWzb7yT0yFy0ibaTENudO3LTia7fn4ibSc3mlma5alTpUDw39tx8EuCMrVqjCF9rMicak7H5MQ2tQ7LQTNt6cicv1/0");
        user.setEmail(userRequest.getAsString("email"));
        user.setPhone(phone);
        user.setStatus(userRequest.getAsByte("status"));
        user.setRemark(userRequest.getAsString("remark"));
        user.setTenantId(userRequest.getAsString("tenantId"));
        userRepository.save(user);

        userRoleRepository.deleteByUserId(user.getId());
        List<ObjectMap> roles = userRequest.getAsList("userRoles");
        for (ObjectMap role : roles) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(role.getAsInteger("id"));
            userRole.setUserId(user.getId());
            userRoleRepository.save(userRole);
        }

        userGroupRepository.deleteByUserId(user.getId());
        List<ObjectMap> groups = userRequest.getAsList("userGroups");
        for (ObjectMap group : groups) {
            UserGroup userGroup = new UserGroup();
            userGroup.setGroupId(group.getAsInteger("id"));
            userGroup.setUserId(user.getId());
            userGroupRepository.save(userGroup);
        }
        return user;
    }

    @DeleteMapping(value = "/users/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteUser(@PathVariable Integer id) {
        User user = getUserFromRequest(id);
        userRepository.delete(user);
        userRoleRepository.deleteByUserId(user.getId());
        userGroupRepository.deleteByUserId(user.getId());
    }
}
