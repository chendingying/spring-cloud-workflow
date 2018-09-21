package com.spring.cloud.identity.resource;

import com.spring.cloud.common.jpa.Criteria;
import com.spring.cloud.common.jpa.Restrictions;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.common.resource.PageResponse;
import com.spring.cloud.common.utils.ObjectUtils;
import com.spring.cloud.identity.domain.Menu;
import com.spring.cloud.identity.repository.RoleRepository;
import com.spring.cloud.identity.constant.ErrorConstant;
import com.spring.cloud.identity.constant.TableConstant;
import com.spring.cloud.identity.domain.Role;
import com.spring.cloud.identity.repository.MenuRepository;
import com.spring.cloud.identity.repository.RoleMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 菜单资源控制类
 * @author cdy
 * @create 2018/9/5
 */
@RestController
public class MenuResource extends BaseResource {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMenuRepository roleMenuRepository;

    private Menu getMenuFromRequest(Integer id) {
        Menu menu = menuRepository.findOne(id);
        if (menu == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.MENU_NOT_FOUND);
        }
        return menu;
    }

    @GetMapping(value = "/menus")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getMenus(@RequestParam Map<String, String> requestParams) {
        Criteria<Menu> criteria = new Criteria<Menu>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("parentId", requestParams.get("parentId")));
        criteria.add(Restrictions.eq("status", requestParams.get("status")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(menuRepository.findAll(criteria, getPageable(requestParams)));
    }

    @GetMapping(value = "/menus/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Menu getMenu(@PathVariable Integer id) {
        return getMenuFromRequest(id);
    }

    @PostMapping("/menus")
    @ResponseStatus(HttpStatus.CREATED)
    public Menu createMenu(@RequestBody Menu menuRequest) {
        return menuRepository.save(menuRequest);
    }

    @PutMapping(value = "/menus/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Menu updateMenu(@PathVariable Integer id, @RequestBody Menu menuRequest) {
        Menu menu = getMenuFromRequest(id);
        menu.setName(menuRequest.getName());
        menu.setStatus(menuRequest.getStatus());
        menu.setIcon(menuRequest.getIcon());
        menu.setOrder(menuRequest.getOrder());
        menu.setParentId(menuRequest.getParentId());
        menu.setType(menuRequest.getType());
        menu.setRoute(menuRequest.getRoute());
        menu.setRemark(menuRequest.getRemark());
        menu.setTenantId(menuRequest.getTenantId());
        return menuRepository.save(menu);
    }

    @PutMapping(value = "/menus/{id}/switch")
    @ResponseStatus(value = HttpStatus.OK)
    public Menu switchStatus(@PathVariable Integer id) {
        Menu menu = getMenuFromRequest(id);
        if (menu.getStatus() == TableConstant.MENU_STATUS_NORMAL) {
            menu.setStatus(TableConstant.MENU_STATUS_STOP);
        } else {
            menu.setStatus(TableConstant.MENU_STATUS_NORMAL);
        }
        return menuRepository.save(menu);
    }

    @GetMapping(value = "/menus/{id}/roles")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Role> getMenuRoles(@PathVariable Integer id) {
        return roleRepository.findByMenuId(id);
    }

    @DeleteMapping(value = "/menus/{id}/roles/{roleId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteMenuRole(@PathVariable Integer id, @PathVariable(value = "roleId") Integer roleId) {
        roleMenuRepository.deleteByMenuIdAndRoleId(id, roleId);
    }

    @DeleteMapping(value = "/menus/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable Integer id) {
        Menu menu = getMenuFromRequest(id);
        if (menu.getType() == TableConstant.MENU_TYPE_PARENT) {
            List<Menu> children = menuRepository.findByParentId(menu.getId());
            if (ObjectUtils.isNotEmpty(children)) {
                exceptionFactory.throwForbidden(ErrorConstant.MENU_HAVE_CHILDREN);
            }
        } else {
            List<Role> roles = roleRepository.findByMenuId(menu.getId());
            if (ObjectUtils.isNotEmpty(roles)) {
                exceptionFactory.throwForbidden(ErrorConstant.MENU_ALREADY_ROLE_USE, roles.get(0).getName());
            }
        }
        menuRepository.delete(menu);
    }
}
