package com.spring.cloud.form.resource;

import com.spring.cloud.common.jpa.Criteria;
import com.spring.cloud.common.jpa.Restrictions;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.common.resource.PageResponse;
import com.spring.cloud.form.constant.ErrorConstant;
import com.spring.cloud.form.domain.FormField;
import com.spring.cloud.form.repository.FormFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 数据表资源类
 * @author cdy
 * @create 2018/9/5
 */
@RestController
public class FormFieIdResource extends BaseResource {
    @Autowired
    private FormFieldRepository formFieldRepository;

    private FormField getFormFieldFromRequest(Integer id) {
        FormField formField = formFieldRepository.findOne(id);
        if (formField == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return formField;
    }

    @GetMapping(value = "/form-fields")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormFields(@RequestParam Map<String, String> requestParams) {
        Criteria<FormField> criteria = new Criteria<FormField>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("tableId", requestParams.get("tableId")));
        criteria.add(Restrictions.like("key", requestParams.get("key")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(formFieldRepository.findAll(criteria, getPageable(requestParams)));
    }

    @GetMapping(value = "/form-fields/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormField getFormField(@PathVariable Integer id) {
        return getFormFieldFromRequest(id);
    }

    @PostMapping("/form-fields")
    @ResponseStatus(HttpStatus.CREATED)
    public FormField createFormField(@RequestBody FormField formFieldRequest) {
        return formFieldRepository.save(formFieldRequest);
    }

    @PutMapping(value = "/form-fields/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormField updateFormField(@PathVariable Integer id, @RequestBody FormField formFieldRequest) {
        FormField formField = getFormFieldFromRequest(id);
        formField.setName(formFieldRequest.getName());
        formField.setType(formFieldRequest.getType());
        formField.setRemark(formFieldRequest.getRemark());
        formField.setTenantId(formFieldRequest.getTenantId());
        return formFieldRepository.save(formField);
    }

    @DeleteMapping(value = "/form-fields/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFormField(@PathVariable Integer id) {
        FormField formField = getFormFieldFromRequest(id);
        formFieldRepository.delete(formField);
    }
}
