package com.spring.cloud.identity.resource;

import com.spring.cloud.common.jpa.Criteria;
import com.spring.cloud.common.jpa.Restrictions;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.common.resource.PageResponse;
import com.spring.cloud.common.utils.ObjectUtils;
import com.spring.cloud.identity.constant.ErrorConstant;
import com.spring.cloud.identity.constant.TableConstant;
import com.spring.cloud.identity.domain.Group;
import com.spring.cloud.identity.domain.Menu;
import com.spring.cloud.identity.domain.ModuleType;
import com.spring.cloud.identity.repository.ModuleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by CDZ on 2018/9/18.
 */
@RestController
public class ModuleTypeResource extends BaseResource {

    @Autowired
    ModuleTypeRepository moduleTypeRepository;

    private ModuleType getModuleTypeRequest(Integer id){
        ModuleType moduleType = moduleTypeRepository.findOne(id);
        if(moduleType == null){
            exceptionFactory.throwObjectNotFound(ErrorConstant.MENU_NOT_FOUND);
        }
        return moduleType;
    }

    @PostMapping(value = "/moduleTypes")
    public ModuleType save(@RequestBody ModuleType moduleType){
        return moduleTypeRepository.save(moduleType);
    }

    @GetMapping(value = "/moduleTypes")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getModuleTypes(@RequestParam Map<String, String> requestParams){
        Criteria<ModuleType> criteria = new Criteria<ModuleType>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("parentId", requestParams.get("parentId")));
        criteria.add(Restrictions.eq("status", requestParams.get("status")));
        criteria.add(Restrictions.like("moduleType", requestParams.get("moduleType")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        criteria.add(Restrictions.eq("status", requestParams.get("status")));
        return createPageResponse(moduleTypeRepository.findAll(criteria, getPageable(requestParams)));
    }


    @GetMapping(value = "/moduleTypes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ModuleType getModuleTypes(@PathVariable Integer id){
        return getModuleTypeRequest(id);
    }

    @GetMapping(value = "/moduleTypes/parentId/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ModuleType> listModuleTypes(@PathVariable Integer id){return moduleTypeRepository.findByParentId(id);}

    @DeleteMapping(value = "/moduleTypes/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteModuleType(@PathVariable Integer id){
        ModuleType moduleType = getModuleTypeRequest(id);
        if(moduleType.getParentId() == TableConstant.MODULE_TYPE_PARNET_ID){
            List<ModuleType> list = moduleTypeRepository.findByParentId(moduleType.getId());
            if (ObjectUtils.isNotEmpty(list) || list.size() > 0) {
                exceptionFactory.throwForbidden(ErrorConstant.MODULETYPE_HAVE_CHILDREN);
            }
        }
        moduleTypeRepository.delete(moduleType);
    }

    @PutMapping(value = "/moduleTypes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ModuleType updateModuleType(@PathVariable Integer id,@RequestBody ModuleType moduleTypeRequest){
       ModuleType moduleType =  getModuleTypeRequest(id);
        moduleType.setModuleType(moduleTypeRequest.getModuleType());
        moduleType.setParentId(moduleTypeRequest.getParentId());
        moduleType.setRemark(moduleTypeRequest.getRemark());
        moduleType.setStatus(moduleTypeRequest.getStatus());
        moduleType.setTenantId(moduleTypeRequest.getTenantId());
        return moduleTypeRepository.save(moduleType);
    }

    //修改类型状态
    @PutMapping(value = "/moduleTypes/{id}/switch")
    @ResponseStatus(value = HttpStatus.OK)
    public ModuleType switchStatus(@PathVariable Integer id) {
        ModuleType moduleType = getModuleTypeRequest(id);
        if (moduleType.getStatus() == TableConstant.MODULE_TYPE_STATUS_NORMAL) {
            moduleType.setStatus(TableConstant.MODULE_TYPE_STATUS_STOP);
        } else {
            moduleType.setStatus(TableConstant.MODULE_TYPE_STATUS_NORMAL);
        }
        return moduleTypeRepository.save(moduleType);
    }
}
