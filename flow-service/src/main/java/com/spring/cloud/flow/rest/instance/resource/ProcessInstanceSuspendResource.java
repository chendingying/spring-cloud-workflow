package com.spring.cloud.flow.rest.instance.resource;

import com.spring.cloud.flow.constant.ErrorConstant;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ProcessInstanceSuspendResource extends BaseProcessInstanceResource {
	
	@PutMapping(value = "/process-instances/{processInstanceId}/suspend", name="流程实例挂起")
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional
	public void suspendProcessInstance(@PathVariable String processInstanceId) {
		ProcessInstance processInstance = getProcessInstanceFromRequest(processInstanceId);
		    
		if (processInstance.isSuspended()) {
			exceptionFactory.throwConflict(ErrorConstant.INSTANCE_ALREADY_SUSPEND, processInstance.getId());
		}
		runtimeService.suspendProcessInstanceById(processInstance.getId());
		loggerConverter.save("挂起了流程实例 '"+ processInstance.getProcessDefinitionName() + "'");
	}
}
