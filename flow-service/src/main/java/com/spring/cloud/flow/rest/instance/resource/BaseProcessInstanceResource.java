package com.spring.cloud.flow.rest.instance.resource;

import com.spring.cloud.flow.constant.ErrorConstant;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.flow.rest.RestResponseFactory;
import com.spring.cloud.flow.rest.log.LoggerConverter;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseProcessInstanceResource extends BaseResource {
	@Autowired
	protected RestResponseFactory restResponseFactory;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	protected LoggerConverter loggerConverter;

	protected ProcessInstance getProcessInstanceFromRequest(String processInstanceId) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if (processInstance == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.INSTANCE_NOT_FOUND, processInstanceId);
		}
		return processInstance;
	}

	protected HistoricProcessInstance getHistoricProcessInstanceFromRequest(String processInstanceId) {
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if (historicProcessInstance == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.INSTANCE_NOT_FOUND, processInstanceId);
		}
		return historicProcessInstance;
	}
}
