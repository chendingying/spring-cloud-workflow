package com.spring.cloud.identity.resource;

import com.spring.cloud.common.jpa.Criteria;
import com.spring.cloud.common.jpa.Restrictions;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.common.resource.PageResponse;
import com.spring.cloud.common.utils.ObjectUtils;
import com.spring.cloud.identity.domain.Group;
import com.spring.cloud.identity.repository.GroupRepository;
import com.spring.cloud.identity.constant.ErrorConstant;
import com.spring.cloud.identity.constant.TableConstant;
import com.spring.cloud.identity.domain.User;
import com.spring.cloud.identity.repository.UserGroupRepository;
import com.spring.cloud.identity.repository.UserRepository;
import com.spring.cloud.identity.response.LoggerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 群组资源控制类
 * @author cdy
 * @create 2018/9/5
 */
@RestController
public class GroupResource extends BaseResource {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private LoggerConverter loggerConverter;

    private Group getGroupFromRequest(Integer id) {
        Group group = groupRepository.findOne(id);
        if (group == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.GROUP_NOT_FOUND);
        }
        return group;
    }

    @GetMapping(value = "/groups")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getGroups(@RequestParam Map<String, String> requestParams) {
        Criteria<Group> criteria = new Criteria<Group>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("type", requestParams.get("type")));
        criteria.add(Restrictions.eq("status", requestParams.get("status")));
        criteria.add(Restrictions.eq("parentId", requestParams.get("parentId")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(groupRepository.findAll(criteria, getPageable(requestParams)));
    }

    @GetMapping(value = "/groups/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Group getGroup(@PathVariable Integer id) {
        return getGroupFromRequest(id);
    }

    @PostMapping("/groups")
    @ResponseStatus(HttpStatus.CREATED)
    public Group createGroup(@RequestBody Group groupRequest) {
        loggerConverter.save("添加了'"+groupRequest.getName()+"'群组信息");
        return groupRepository.save(groupRequest);
    }

    @PutMapping(value = "/groups/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Group updateGroup(@PathVariable Integer id, @RequestBody Group groupRequest) {
        Group group = getGroupFromRequest(id);
        group.setName(groupRequest.getName());
        group.setStatus(groupRequest.getStatus());
        group.setType(groupRequest.getType());
        group.setOrder(groupRequest.getOrder());
        group.setParentId(groupRequest.getParentId());
        group.setRemark(groupRequest.getRemark());
        group.setTenantId(groupRequest.getTenantId());
        loggerConverter.save("修改了'"+group.getName()+"'群组信息");
        return groupRepository.save(group);
    }

    @PutMapping(value = "/groups/{id}/switch")
    @ResponseStatus(value = HttpStatus.OK)
    public Group switchStatus(@PathVariable Integer id) {
        Group group = getGroupFromRequest(id);
        if (group.getStatus() == TableConstant.GROUP_STATUS_NORMAL) {
            group.setStatus(TableConstant.GROUP_STATUS_STOP);
        } else {
            group.setStatus(TableConstant.GROUP_STATUS_NORMAL);
        }
        loggerConverter.save("修改了'"+group.getName()+"'群组的状态");
        return groupRepository.save(group);
    }

    @GetMapping(value = "/groups/{id}/users")
    @ResponseStatus(value = HttpStatus.OK)
    public List<User> getGroupUsers(@PathVariable Integer id) {
        return userRepository.findByGroupId(id);
    }

    @DeleteMapping(value = "/groups/{id}/users/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteGroupUser(@PathVariable Integer id, @PathVariable(value = "userId") Integer userId) {
        Group group = getGroupFromRequest(id);
        loggerConverter.save("删除了'"+group.getName()+"'群组");
        userGroupRepository.deleteByGroupIdAndUserId(id, userId);
    }

    @DeleteMapping(value = "/groups/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteGroup(@PathVariable Integer id) {
        Group group = getGroupFromRequest(id);
        if (group.getType() == TableConstant.GROUP_TYPE_PARENT) {
            List<Group> children = groupRepository.findByParentId(group.getId());
            if (ObjectUtils.isNotEmpty(children)) {
                exceptionFactory.throwForbidden(ErrorConstant.GROUP_HAVE_CHILDREN);
            }
        } else {
            List<User> users = userRepository.findByGroupId(group.getId());
            if (ObjectUtils.isNotEmpty(users)) {
                exceptionFactory.throwForbidden(ErrorConstant.Group_ALREADY_USER_USE, users.get(0).getName());
            }
        }
        loggerConverter.save("删除了'"+group.getName()+"'群组");
        groupRepository.delete(group);
    }
}
