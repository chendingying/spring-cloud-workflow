package com.spring.cloud.flow.rest.instance.resource;

import com.spring.cloud.flow.constant.ErrorConstant;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ProcessInstanceActivateResource extends BaseProcessInstanceResource {

	@PutMapping(value = "/process-instances/{processInstanceId}/activate", name="流程实例激活")
	@ResponseStatus(value = HttpStatus.OK)
	public void activateProcessInstance(@PathVariable String processInstanceId) {

		ProcessInstance processInstance = getProcessInstanceFromRequest(processInstanceId);
		    
		if (!processInstance.isSuspended()) {
			exceptionFactory.throwConflict(ErrorConstant.INSTANCE_ALREADY_ACTIVE, processInstance.getId());
		}

		runtimeService.activateProcessInstanceById(processInstance.getId());
	}
}
