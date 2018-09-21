package com.spring.cloud.flow.rest.definition.resource;


import com.spring.cloud.flow.constant.ErrorConstant;
import com.spring.cloud.common.utils.ObjectUtils;
import org.apache.commons.io.IOUtils;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.List;

/**
 * 获取流程定义XML接口
 * @author cdy
 * @create 2018/9/4
 */
@RestController
public class ProcessDefinitionXmlResource extends BaseProcessDefinitionResource {

	@GetMapping(value = "/process-definitions/{processDefinitionId}/xml", name = "获取流程定义XML")
	public ResponseEntity<byte[]> getProcessDefinitionXml(@PathVariable String processDefinitionId) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
		String deploymentId = processDefinition.getDeploymentId();
		String resourceId = processDefinition.getResourceName();
		if (deploymentId == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.DEFINITION_DEPLOY_NOT_FOUND, processDefinitionId);
		}
		if (resourceId == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.DEFINITION_RESOURCE_NOT_FOUND, processDefinitionId);
		}

		Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
		if (deployment == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.DEPLOY_NOT_FOUND, deploymentId);
		}

		List<String> resourceList = repositoryService.getDeploymentResourceNames(deploymentId);
		if (ObjectUtils.isEmpty(resourceList) || !resourceList.contains(resourceId)) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.DEPLOY_RESOURCE_NOT_FOUND, deploymentId, resourceId);
		}
		InputStream resourceStream = repositoryService.getResourceAsStream(deploymentId, resourceId);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.TEXT_XML);
		try {
			return new ResponseEntity<byte[]>(IOUtils.toByteArray(resourceStream), responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("获取流程定义XML信息异常",e);
			exceptionFactory.throwDefinedException(ErrorConstant.DEFINITION_XML_READ_ERROR, e.getMessage());
		}
		return null;
		
	}

}
