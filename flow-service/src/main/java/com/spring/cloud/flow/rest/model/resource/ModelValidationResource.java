package com.spring.cloud.flow.rest.model.resource;

import com.fasterxml.jackson.databind.JsonNode;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.validation.ValidationError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cdy
 * @create 2018/9/4
 */
@RestController
public class ModelValidationResource extends BaseModelResource {
    @PostMapping(value = "/models/validate", name="模型检查")
    public List<ValidationError> validate(@RequestBody JsonNode body) {
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(body);
        ProcessValidator validator = new ProcessValidatorFactory().createDefaultProcessValidator();
        List<ValidationError> errors = validator.validate(bpmnModel);
        return errors;
    }

}
