package com.spring.cloud.identity.response;

import com.spring.cloud.common.model.ObjectMap;
import com.spring.cloud.common.utils.ObjectUtils;
import com.spring.cloud.identity.domain.Menu;
import com.spring.cloud.identity.constant.TableConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单数据转换表
 * @author cdy
 * @create 2018/9/5
 */
public class MenuConverter {
    public static List<ObjectMap> convertMultiSelect(List<Menu> menus, List<Menu> roleMenus) {
        List<ObjectMap> menuList = new ArrayList<ObjectMap>();
        for (Menu menu : menus) {
            if (menu.getType() == TableConstant.MENU_TYPE_CHILD) {
                continue;
            }
            List<ObjectMap> children = new ArrayList<ObjectMap>();
            for (Menu childMenu : menus) {
                if (menu.getId().equals(childMenu.getParentId())) {
                    if (ObjectUtils.isNotEmpty(roleMenus) && roleMenus.contains(childMenu)) {
                        children.add(ObjectMap.of("id", childMenu.getId(), "name", childMenu.getName(), "selected", true));
                    } else {
                        children.add(ObjectMap.of("id", childMenu.getId(), "name", childMenu.getName(), "selected", false));
                    }
                }
            }
            if (ObjectUtils.isNotEmpty(children)) {
                menuList.add(ObjectMap.of("id", menu.getId(), "name", menu.getName(), "group", true));
                menuList.addAll(children);
                menuList.add(ObjectMap.of("group", false));
            }
        }
        return menuList;
    }

    public static List<ObjectMap> convertUserMenus(List<Menu> parentMenus, List<Menu> childMenus) {
        List<ObjectMap> menuList = new ArrayList<ObjectMap>();
        for (Menu menu : parentMenus) {
            List<ObjectMap> childList = new ArrayList<ObjectMap>();
            for (Menu childMenu : childMenus) {
                if (menu.getId().equals(childMenu.getParentId())) {
                    childList.add(convertMenuMap(childMenu));
                }
            }
            if (ObjectUtils.isNotEmpty(childList)) {
                ObjectMap menuMap = convertMenuMap(menu);
                menuMap.put("children", childList);
                menuList.add(menuMap);
            }
        }
        return menuList;
    }

    private static ObjectMap convertMenuMap(Menu menu) {
        ObjectMap objectMap = new ObjectMap();
        objectMap.put("id", menu.getId());
        objectMap.put("name", menu.getName());
        objectMap.put("path", menu.getRoute());
        objectMap.put("icon", menu.getIcon());
        return objectMap;
    }
}
