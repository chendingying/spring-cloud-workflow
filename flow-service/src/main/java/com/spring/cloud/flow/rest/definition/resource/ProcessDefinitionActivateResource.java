package com.spring.cloud.flow.rest.definition.resource;

import com.spring.cloud.flow.constant.ErrorConstant;
import com.spring.cloud.flow.rest.definition.ProcessDefinitionActionRequest;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 流程定义激活接口
 * @author cdy
 * @create 2018/9/4
 */
@RestController
public class ProcessDefinitionActivateResource extends BaseProcessDefinitionResource {

	@PutMapping(value = "/process-definitions/{processDefinitionId}/activate", name = "流程定义激活")
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional
	public void activateProcessDefinition(@PathVariable String processDefinitionId, @RequestBody(required = false) ProcessDefinitionActionRequest actionRequest) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
		
		if (!processDefinition.isSuspended()) {
			exceptionFactory.throwConflict(ErrorConstant.DEFINITION_ALREADY_ACTIVE, processDefinition.getId());
		}

		if (actionRequest == null) {
			repositoryService.activateProcessDefinitionById(processDefinitionId);
		} else {
			repositoryService.activateProcessDefinitionById(processDefinition.getId(), actionRequest.isIncludeProcessInstances(), actionRequest.getDate());
		}
		loggerConverter.save("激活了流程定义 '"+processDefinition.getName()+"'");
	}
}
