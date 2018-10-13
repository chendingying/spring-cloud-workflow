package com.spring.cloud.form.resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.spring.cloud.common.constant.CoreConstant;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.common.utils.ObjectUtils;
import com.spring.cloud.form.constant.ErrorConstant;
import com.spring.cloud.form.domain.ByteArray;
import com.spring.cloud.form.domain.FormDefinition;
import com.spring.cloud.form.domain.RunByteArray;
import com.spring.cloud.form.inherit.InheritRunByteArray;
import com.spring.cloud.form.repository.ByteArrayRepository;
import com.spring.cloud.form.repository.FormDefinitionRepository;
import com.spring.cloud.form.repository.RunByteArrayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by CDZ on 2018/9/15.
 */
@RestController
public class FormRunByteArrayResource  extends BaseResource {
    @Autowired
    RunByteArrayRepository runByteArrayRepository;
    @Autowired
    FormDefinitionRepository formDefinitionRepository;

    @Autowired
    ByteArrayRepository byteArrayRepository;

    private RunByteArray getRunByteArrayFromRequest(Integer id) {
        RunByteArray runByteArray = runByteArrayRepository.findOne(id);
        if (runByteArray == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return runByteArray;
    }

    /**
     * 查询流程任务下的表单
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping("/form-definition/json")
    @ResponseStatus(HttpStatus.OK)
    public ObjectNode getFormdefinitionJson(@RequestParam Map<String, String> requestParams) throws Exception {
        RunByteArray runByteArray = null;
        if (ObjectUtils.isNotEmpty(requestParams.get("processInstanceId"))) {
            runByteArray = runByteArrayRepository.findByProcInstId(requestParams.get("processInstanceId"));
        }
        ObjectNode resultNode = objectMapper.createObjectNode();
        if (runByteArray == null) {
            FormDefinition formDefinition = null;
            ByteArray byteArray = null;
            if (ObjectUtils.isNotEmpty(requestParams.get("formKey"))) {
                formDefinition = formDefinitionRepository.findByKey(requestParams.get("formKey"));
                byteArray = byteArrayRepository.findOne(formDefinition.getDeploySourceId());
            }
            if (byteArray == null) {
                resultNode.putPOJO("json", objectMapper.createArrayNode().toString());
            } else {
                resultNode.putPOJO("json", new String(byteArray.getContentByte(), CoreConstant.DEFAULT_CHARSET));
            }
            resultNode.putPOJO("bytearrayId",0);
        } else {
            resultNode.putPOJO("json", new String(runByteArray.getContentByte(), CoreConstant.DEFAULT_CHARSET));
            resultNode.putPOJO("bytearrayId",runByteArray.getId());
        }
        return resultNode;
    }

    /**
     * 添加流程任务下的表单
     * @throws Exception
     */
    @PostMapping("/form-definition/json")
    public void saveFormdefinitionJson(@RequestBody InheritRunByteArray inheritRunByteArray) throws Exception{
        RunByteArray runByteArray = new RunByteArray();
        if(inheritRunByteArray.getId() != 0){
            runByteArray = getRunByteArrayFromRequest(inheritRunByteArray.getId());
        }
        runByteArray.setTableKey(inheritRunByteArray.getTableKey());
        runByteArray.setProcInstId(inheritRunByteArray.getProcInstId());
        runByteArray.setContentByte(inheritRunByteArray.getEditorJson().getBytes(CoreConstant.DEFAULT_CHARSET));
        runByteArrayRepository.save(runByteArray);
    }

}
