package com.spring.cloud.identity.response;

import com.spring.cloud.common.model.ObjectMap;
import com.spring.cloud.common.utils.ObjectUtils;
import com.spring.cloud.identity.constant.TableConstant;
import com.spring.cloud.identity.domain.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * 群组数据转换类
 * @author cdy
 * @create 2018/9/5
 */
public class GroupConverter {
    public static List<ObjectMap> convertMultiSelect(List<Group> groups, List<Group> userGroups) {
        return getChildren(TableConstant.GROUP_PARNET_ID, groups, userGroups);
    }

    private static List<ObjectMap> getChildren(int parentId, List<Group> groups, List<Group> userGroups) {
        List<ObjectMap> groupList = new ArrayList<ObjectMap>();
        for (Group child : groups) {
            if (parentId == child.getParentId()) {
                if (child.getType() == TableConstant.GROUP_TYPE_CHILD) {
                    if (ObjectUtils.isNotEmpty(userGroups) && userGroups.contains(child)) {
                        groupList.add(ObjectMap.of("id", child.getId(), "name", child.getName(), "selected", true));
                    } else {
                        groupList.add(ObjectMap.of("id", child.getId(), "name", child.getName(), "selected", false));
                    }
                } else {
                    List<ObjectMap> children = getChildren(child.getId(), groups, userGroups);
                    if (ObjectUtils.isNotEmpty(children)) {
                        groupList.add(ObjectMap.of("id", child.getId(), "name", child.getName(), "group", true));
                        groupList.addAll(children);
                        groupList.add(ObjectMap.of("group", false));
                    }
                }

            }
        }
        return groupList;
    }

}
