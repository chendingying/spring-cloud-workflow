package com.spring.cloud.form.resource;

import com.spring.cloud.form.domain.ByteArray;
import com.spring.cloud.form.domain.FormField;
import com.spring.cloud.form.domain.FormLayout;
import com.spring.cloud.form.repository.FormLayoutRepository;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.spring.cloud.common.constant.CoreConstant;
import com.spring.cloud.common.jpa.Criteria;
import com.spring.cloud.common.jpa.Restrictions;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.common.resource.PageResponse;
import com.spring.cloud.form.constant.ErrorConstant;
import com.spring.cloud.form.repository.ByteArrayRepository;
import com.spring.cloud.form.repository.FormDefinitionRepository;
import com.spring.cloud.form.repository.FormFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 数据表资源类
 * @author cdy
 * @create 2018/9/5
 */
@RestController
public class FormLayoutResource extends BaseResource {
    @Autowired
    private FormLayoutRepository formLayoutRepository;
    @Autowired
    private FormFieldRepository formFieldRepository;
    @Autowired
    private ByteArrayRepository byteArrayRepository;
    @Autowired
    private FormDefinitionRepository formDefinitionRepository;

    private FormLayout getFormLayoutFromRequest(Integer id) {
        FormLayout formLayout = formLayoutRepository.findOne(id);
        if (formLayout == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return formLayout;
    }

    @GetMapping(value = "/form-layouts")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormLayouts(@RequestParam Map<String, String> requestParams) {
        Criteria<FormLayout> criteria = new Criteria<FormLayout>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("tableId", requestParams.get("tableId")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(formLayoutRepository.findAll(criteria, getPageable(requestParams)));
    }

    @GetMapping(value = "/form-layouts/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormLayout getFormLayout(@PathVariable Integer id) {
        return getFormLayoutFromRequest(id);
    }

    @PostMapping("/form-layouts")
    @ResponseStatus(HttpStatus.CREATED)
    public FormLayout createFormLayout(@RequestBody FormLayout formLayoutRequest) {
        return formLayoutRepository.save(formLayoutRequest);
    }

    @PutMapping(value = "/form-layouts/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormLayout updateFormLayout(@PathVariable Integer id, @RequestBody FormLayout formLayoutRequest) {
        FormLayout formLayout = getFormLayoutFromRequest(id);
        formLayout.setName(formLayoutRequest.getName());
        formLayout.setTenantId(formLayoutRequest.getTenantId());
        return formLayoutRepository.save(formLayout);
    }

    @DeleteMapping(value = "/form-layouts/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFormLayout(@PathVariable Integer id) {
        FormLayout formLayout = getFormLayoutFromRequest(id);
        formLayoutRepository.delete(formLayout);
    }

    @GetMapping("/form-layouts/{id}/json")
    @ResponseStatus(HttpStatus.OK)
    public ObjectNode getFormLayoutJson(@PathVariable Integer id) throws Exception {
        FormLayout formLayout = getFormLayoutFromRequest(id);

        List<FormField> formFields = formFieldRepository.findByTableId(formLayout.getTableId());

        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.putPOJO("fields", formFields);

        ByteArray byteArray = byteArrayRepository.findOne(formLayout.getEditorSourceId());
        if(byteArray == null) {
            resultNode.putPOJO("json", objectMapper.createArrayNode().toString());
        }else {
            resultNode.putPOJO("json", new String(byteArray.getContentByte(), CoreConstant.DEFAULT_CHARSET));
        }

        return resultNode;
    }

    @PutMapping("/form-layouts/{id}/json")
    @ResponseStatus(HttpStatus.OK)
    public void saveFormLayoutJson(@PathVariable Integer id,@RequestBody String editorJson) throws Exception {
        FormLayout formLayout = getFormLayoutFromRequest(id);

        ByteArray byteArray = byteArrayRepository.findOne(formLayout.getEditorSourceId());
        if(byteArray == null) {
            byteArray = new ByteArray();
            byteArray.setName(formLayout.getName());
        }

        byteArray.setContentByte(editorJson.getBytes(CoreConstant.DEFAULT_CHARSET));
        byteArrayRepository.save(byteArray);

        formLayout.setEditorSourceId(byteArray.getId());
        formLayoutRepository.save(formLayout);
    }



}
