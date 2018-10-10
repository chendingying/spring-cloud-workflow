package com.spring.cloud.form.resource.table;

import com.spring.cloud.common.jpa.Criteria;
import com.spring.cloud.common.jpa.Restrictions;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.common.resource.PageResponse;
import com.spring.cloud.form.constant.ErrorConstant;
import com.spring.cloud.form.domain.FormFieldType;
import com.spring.cloud.form.repository.FormFieldTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by CDZ on 2018/9/21.
 */
@RestController
public class FormFieldTypeResource extends BaseResource {
    @Autowired
    FormFieldTypeRepository formFieldTypeRepository;

    private FormFieldType getFormFieldTypeFromRequest(Integer id) {
        FormFieldType formFieldType = formFieldTypeRepository.findOne(id);
        if (formFieldType == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return formFieldType;
    }

    @GetMapping(value = "/form-fieldTypes")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormFieldTypes(@RequestParam Map<String, String> requestParams) {
        Criteria<FormFieldType> criteria = new Criteria<FormFieldType>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("name", requestParams.get("name")));
        criteria.add(Restrictions.like("key", requestParams.get("key")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(formFieldTypeRepository.findAll(criteria, getPageable(requestParams)));
    }

    @GetMapping(value = "/form-fieldTypes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormFieldType getFormFieldType(@PathVariable Integer id) {
        return getFormFieldTypeFromRequest(id);
    }

    @PostMapping("/form-fieldTypes")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFormFieldType(@RequestBody FormFieldType formFieldTypeRequest) {
        FormFieldType formFieldType = formFieldTypeRepository.findByName(formFieldTypeRequest.getName());
        if(formFieldType == null){
             formFieldTypeRepository.save(formFieldTypeRequest);
        }
}

    @PutMapping(value = "/form-fieldTypes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormFieldType updateFormFieldType(@PathVariable Integer id, @RequestBody FormFieldType formFieldTypeRequest) {
        FormFieldType formFieldType = getFormFieldTypeFromRequest(id);
        formFieldType.setName(formFieldTypeRequest.getName());
        formFieldType.setRemark(formFieldTypeRequest.getRemark());
        formFieldType.setTenantId(formFieldTypeRequest.getTenantId());
        return formFieldTypeRepository.save(formFieldType);
    }

    @DeleteMapping(value = "/form-fieldTypes/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFormFieldType(@PathVariable Integer id) {
        FormFieldType formFieldType = getFormFieldTypeFromRequest(id);
        formFieldTypeRepository.delete(formFieldType);
    }
}
