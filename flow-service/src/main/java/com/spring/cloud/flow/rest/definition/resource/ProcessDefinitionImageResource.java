package com.spring.cloud.flow.rest.definition.resource;

import com.spring.cloud.flow.constant.ErrorConstant;
import org.apache.commons.io.IOUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
public class ProcessDefinitionImageResource extends BaseProcessDefinitionResource {

	@GetMapping(value = "/process-definitions/{processDefinitionId}/image", name = "流程定义流程图")
	public ResponseEntity<byte[]> getProcessDefinitionImage(@PathVariable String processDefinitionId) {
		//act_re_deployment-->act_ge_bytearray(两条数据：流程图片的文件数据和流程的描述文件数据)--> act_re_procdef(流程定义表)
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
		//获取流程图
		InputStream imageStream = repositoryService.getProcessDiagram(processDefinition.getId());
		if (imageStream == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.DEFINITION_IMAGE_NOT_FOUND, processDefinition.getId());
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.IMAGE_PNG);
		try {
			return new ResponseEntity<byte[]>(IOUtils.toByteArray(imageStream), responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			exceptionFactory.throwDefinedException(ErrorConstant.DEFINITION_IMAGE_READ_ERROR, e.getMessage());
		}
		return null;
	}

}
