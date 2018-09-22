package com.spring.cloud.flow.rest.instance.resource;

import com.spring.cloud.flow.constant.ErrorConstant;
import com.spring.cloud.flow.rest.variable.RestVariable;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 流程变量接口
 * 
 * @author wengwh
 * @date 2018年4月23日
 */
@RestController
public class ProcessInstanceVariableResource extends BaseProcessInstanceResource {

	@GetMapping(value = "/process-instances/{processInstanceId}/variables", name = "获取流程实例变量")
	public List<RestVariable> getExecutionVariables(@PathVariable String processInstanceId) {
		HistoricProcessInstance processInstance = getHistoricProcessInstanceFromRequest(processInstanceId);
		if (processInstance.getEndTime() == null) {
			Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());
			return restResponseFactory.createRestVariables(variables);
		} else {
			List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).list();
			return restResponseFactory.createRestVariables(historicVariableInstances);
		}
	}

	@PostMapping(value = "/process-instances/{processInstanceId}/variables", name = "创建流程实例变量")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional
	public void createExecutionVariable(@PathVariable String processInstanceId, @RequestBody RestVariable restVariable) {
		ProcessInstance processInstance = getProcessInstanceFromRequest(processInstanceId);
		if (restVariable.getName() == null) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.INSTANCE_VAR_NAME_NOT_FOUND);
		}
		runtimeService.setVariable(processInstance.getId(), restVariable.getName(), restResponseFactory.getVariableValue(restVariable));
		loggerConverter.save("创建了流程实例变量 '" + processInstance.getName() + "'");
	}

	@DeleteMapping(value = "/process-instances/{processInstanceId}/variables/{variableName}", name = "删除流程实例变量")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Transactional
	public void deleteExecutionVariable(@PathVariable String processInstanceId, @PathVariable("variableName") String variableName) {
		ProcessInstance processInstance = getProcessInstanceFromRequest(processInstanceId);
		runtimeService.removeVariable(processInstance.getId(), variableName);
		loggerConverter.save("删除了流程实例变量 '" + processInstance.getProcessDefinitionName() + "'");
	}
}
