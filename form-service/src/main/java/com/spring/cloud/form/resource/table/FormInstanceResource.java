package com.spring.cloud.form.resource.table;

import com.spring.cloud.form.constant.ErrorConstant;
import com.spring.cloud.form.domain.FormDefinition;
import com.spring.cloud.form.domain.FormInstance;
import com.spring.cloud.form.repository.FormInstanceRepository;
import com.spring.cloud.common.jpa.Criteria;
import com.spring.cloud.common.jpa.Restrictions;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.common.resource.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author cdy
 * @create 2018/9/12
 */
@RestController
public class FormInstanceResource extends BaseResource {

    @Autowired
    FormInstanceRepository formInstanceRepository;
    private FormInstance getFormInstanceFromRequest(Integer id) {
        FormInstance formInstance = formInstanceRepository.findOne(id);
        if (formInstance == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return formInstance;
    }

    @GetMapping(value = "/form-instance")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormDefinition(@RequestParam Map<String, String> requestParams) {
        Criteria<FormInstance> criteria = new Criteria<FormInstance>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("tableId", requestParams.get("tableId")));
        criteria.add(Restrictions.like("key", requestParams.get("key")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(formInstanceRepository.findAll(criteria, getPageable(requestParams)));
    }

    @GetMapping(value = "/form-instance/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormInstance getFormInstance(@PathVariable Integer id) {
        return getFormInstanceFromRequest(id);
    }

    @PutMapping(value = "/form-instance/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormInstance updateFormInstance(@PathVariable Integer id, @RequestBody FormInstance formInstanceRequest) {
        FormInstance formInstance = getFormInstanceFromRequest(id);
        formInstance.setTableRelationId(formInstanceRequest.getTableRelationId());
        formInstance.setFormDefinitionId(formInstanceRequest.getFormDefinitionId());
        formInstance.setSuspensionState(formInstanceRequest.getSuspensionState());
        formInstance.setRelationTable(formInstanceRequest.getRelationTable());
        formInstance.setRev(formInstanceRequest.getRev());
        return formInstanceRepository.save(formInstance);
    }

    @DeleteMapping(value = "/form-instance/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFormInstance(@PathVariable Integer id) {
        FormInstance formInstance = getFormInstanceFromRequest(id);
        formInstanceRepository.delete(formInstance);
    }

    //启动实例
    @PostMapping(value = "/form-instance")
    public void startFormInstance(@RequestBody FormDefinition formDefinition){
        FormInstance formInstance = new FormInstance();
        formInstance.setTableRelationId(formDefinition.getTableId());
        formInstance.setFormDefinitionId(formDefinition.getId());
        formInstance.setSuspensionState(formDefinition.getSuspensionState());
        formInstance.setRev(formDefinition.getRev());
        formInstance.setRelationTable("");
        formInstanceRepository.save(formInstance);
    }
}
