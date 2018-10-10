package com.spring.cloud.form.resource.table;

import com.spring.cloud.form.constant.ErrorConstant;
import com.spring.cloud.form.domain.FormDefinition;
import com.spring.cloud.form.repository.ByteArrayRepository;
import com.spring.cloud.form.repository.FormDefinitionRepository;
import com.spring.cloud.form.repository.RunByteArrayRepository;
import com.spring.cloud.common.jpa.Criteria;
import com.spring.cloud.common.jpa.Restrictions;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.common.resource.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *
 * @author cdy
 * @create 2018/9/8
 */
@RestController
public class FormDefinitionResource extends BaseResource {

    @Autowired
    FormDefinitionRepository formDefinitionRepository;
    @Autowired
    private ByteArrayRepository byteArrayRepository;

    @Autowired
    private RunByteArrayRepository runByteArrayRepository;

    private FormDefinition getFormDefinitionFromRequest(Integer id) {
        FormDefinition formDefinition = formDefinitionRepository.findOne(id);
        if (formDefinition == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return formDefinition;
    }

    @GetMapping(value = "/form-definition")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormDefinition(@RequestParam Map<String, String> requestParams) {
        Criteria<FormDefinition> criteria = new Criteria<FormDefinition>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("tableId", requestParams.get("tableId")));
        criteria.add(Restrictions.like("key", requestParams.get("key")));
        criteria.add(Restrictions.like("category",requestParams.get("category")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(formDefinitionRepository.findAll(criteria, getPageable(requestParams)));
    }

    @GetMapping(value = "/form-definition/byte/{id}")
    public PageResponse getFormDefinitionByteArray(@PathVariable Integer id){
//        List<FormDefinition> formDefinitions =  formDefinitionRepository.findFeploySourceIdByTableId(id);
        return null;
    }

    @GetMapping(value = "/form-definition/{id}")
        @ResponseStatus(value = HttpStatus.OK)
        public FormDefinition getFormDefinition(@PathVariable Integer id) {
        return getFormDefinitionFromRequest(id);
    }

    @PutMapping(value = "/form-definition/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormDefinition updateFormDefinition(@PathVariable Integer id, @RequestBody FormDefinition formDefinitionRequest) {
        FormDefinition formDefinition = getFormDefinitionFromRequest(id);
        formDefinition.setName(formDefinitionRequest.getName());
        formDefinition.setTenantId(formDefinitionRequest.getTenantId());
        formDefinition.setKey(formDefinitionRequest.getKey());
        formDefinition.setVersion(formDefinitionRequest.getVersion());
        formDefinition.setCategory(formDefinitionRequest.getCategory());
        return formDefinitionRepository.save(formDefinition);
    }

    @DeleteMapping(value = "/form-definition/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFormDefinition(@PathVariable Integer id) {
        FormDefinition formDefinition = getFormDefinitionFromRequest(id);
        formDefinitionRepository.delete(formDefinition);
    }

}
