package com.spring.cloud.flow.rest.instance.resource;

import com.spring.cloud.flow.constant.ErrorConstant;
import com.spring.cloud.flow.rest.instance.*;
import com.spring.cloud.flow.rest.variable.RestVariable;
import com.spring.cloud.common.model.Authentication;
import com.spring.cloud.common.resource.PageResponse;
import com.spring.cloud.common.utils.ObjectUtils;
import com.spring.cloud.common.utils.TokenUserIdUtils;
import org.flowable.engine.IdentityService;
import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.impl.HistoricProcessInstanceQueryProperty;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.flowable.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;
import org.flowable.engine.impl.persistence.entity.data.HistoricActivityInstanceDataManager;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程实例接口
 * 
 * @author wengwh
 * @date 2018年4月23日
 */
@RestController
public class ProcessInstanceResource extends BaseProcessInstanceResource {
	@Autowired
	IdentityService identityService;

	private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();

	static {
		allowedSortProperties.put("id", HistoricProcessInstanceQueryProperty.PROCESS_INSTANCE_ID_);
		allowedSortProperties.put("processDefinitionId", HistoricProcessInstanceQueryProperty.PROCESS_DEFINITION_ID);
		allowedSortProperties.put("processDefinitionKey", HistoricProcessInstanceQueryProperty.PROCESS_DEFINITION_KEY);
		allowedSortProperties.put("businessKey", HistoricProcessInstanceQueryProperty.BUSINESS_KEY);
		allowedSortProperties.put("startTime", HistoricProcessInstanceQueryProperty.START_TIME);
		allowedSortProperties.put("endTime", HistoricProcessInstanceQueryProperty.END_TIME);
		allowedSortProperties.put("duration", HistoricProcessInstanceQueryProperty.DURATION);
		allowedSortProperties.put("tenantId", HistoricProcessInstanceQueryProperty.TENANT_ID);
	}

	@GetMapping(value = "/process-instances", name = "流程实例查询")
	public PageResponse getProcessInstances(@RequestParam Map<String, String> requestParams) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		if (ObjectUtils.isNotEmpty(requestParams.get("processInstanceId"))) {
			query.processInstanceId(requestParams.get("processInstanceId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionName"))) {
			query.processDefinitionName(requestParams.get("processDefinitionName"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionKey"))) {
			query.processDefinitionKey(requestParams.get("processDefinitionKey"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionId"))) {
			query.processDefinitionId(requestParams.get("processDefinitionId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("businessKey"))) {
			query.processInstanceBusinessKey(requestParams.get("businessKey"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("involvedUser"))) {
			query.involvedUser(requestParams.get("involvedUser"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("finished"))) {
			boolean isFinished = ObjectUtils.convertToBoolean(requestParams.get("finished"));
			if (isFinished) {
				query.finished();
			} else {
				query.unfinished();
			}
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("superProcessInstanceId"))) {
			query.superProcessInstanceId(requestParams.get("superProcessInstanceId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("excludeSubprocesses"))) {
			query.excludeSubprocesses(ObjectUtils.convertToBoolean(requestParams.get("excludeSubprocesses")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("finishedAfter"))) {
			query.finishedAfter(ObjectUtils.convertToDatetime(requestParams.get("finishedAfter")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("finishedBefore"))) {
			query.finishedBefore(ObjectUtils.convertToDatetime(requestParams.get("finishedBefore")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("startedAfter"))) {
			query.startedAfter(ObjectUtils.convertToDatetime(requestParams.get("startedAfter")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("startedBefore"))) {
			query.startedBefore(ObjectUtils.convertToDatetime(requestParams.get("startedBefore")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("startedBy"))) {
			query.startedBy(requestParams.get("startedBy"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("tenantId"))) {
			query.processInstanceTenantIdLike(requestParams.get("tenantId"));
		}
		//只显示未完成和未删除的实例
//		query.notDeleted();
//		query.unfinished();
		return new ProcessInstancePaginateList(restResponseFactory).paginateList(getPageable(requestParams), query, allowedSortProperties);
	}

	@GetMapping(value = "/process-instances/{processInstanceId}", name = "根据ID获取流程实例")
	public ProcessInstanceDetailResponse getProcessDefinition(@PathVariable String processInstanceId) {
		ProcessInstance processInstance = null;
		HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceFromRequest(processInstanceId);
		if (historicProcessInstance.getEndTime() == null) {
			processInstance = getProcessInstanceFromRequest(processInstanceId);
		}
		return restResponseFactory.createProcessInstanceDetailResponse(historicProcessInstance, processInstance);
	}

	@PostMapping(value = "/process-instances", name = "流程实例创建")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public ProcessInstanceStartResponse startProcessInstance(@RequestBody ProcessInstanceStartRequest request) {

		if (request.getProcessDefinitionId() == null && request.getProcessDefinitionKey() == null) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.PARAM_NOT_FOUND, "processDefinitionId或者processDefinitionKey");
		}

		int paramsSet = ((request.getProcessDefinitionId() != null) ? 1 : 0) + ((request.getProcessDefinitionKey() != null) ? 1 : 0);

		if (paramsSet != 1) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.INSTANCE_START_PARAM_TO_MANY);
		}

		//判断租户是否为null
		if (request.isCustomTenantSet()) {
			if (request.getProcessDefinitionId() != null) {
				exceptionFactory.throwIllegalArgument(ErrorConstant.INSTANCE_START_TENANT_ERROR);
			}
		}

		Map<String, Object> startVariables = null;
		//判断是否有变量
		if (request.getVariables() != null) {
			startVariables = new HashMap<String, Object>();
			for (RestVariable variable : request.getVariables()) {
				if (variable.getName() == null) {
					exceptionFactory.throwIllegalArgument(ErrorConstant.PARAM_NOT_FOUND, "Variable name");
				}
				startVariables.put(variable.getName(), restResponseFactory.getVariableValue(variable));
			}
		}
		org.flowable.engine.common.impl.identity.Authentication.setAuthenticatedUserId(Authentication.getUserId());
		
		ProcessInstance instance = null;

		TokenUserIdUtils tokenUserIdUtils = new TokenUserIdUtils();
		identityService.setAuthenticatedUserId(tokenUserIdUtils.tokenUserId());

		if (request.getProcessDefinitionId() != null) {
			//启动流程
			ProcessInstance processInstance =  runtimeService.createProcessInstanceQuery().processDefinitionId(request.getProcessDefinitionId()).singleResult();
			if(processInstance == null){
				instance = runtimeService.startProcessInstanceById(request.getProcessDefinitionId(), request.getBusinessKey(), startVariables);
				loggerConverter.save("启动了流程 '" + instance.getProcessDefinitionName() + "'");
			}
		} else if (request.getProcessDefinitionKey() != null) {
			if (request.isCustomTenantSet()) {
				instance = runtimeService.startProcessInstanceByKeyAndTenantId(request.getProcessDefinitionKey(), request.getBusinessKey(), startVariables, request.getTenantId());
			} else {
				instance = runtimeService.startProcessInstanceByKey(request.getProcessDefinitionKey(), request.getBusinessKey(), startVariables);
			}
		}

		//是否自动提交任务
		if (request.isAutoCommitTask()) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).list();
			for (Task task : tasks) {
				if (ObjectUtils.isEmpty(task.getAssignee())) {
					taskService.setAssignee(task.getId(), Authentication.getUserId());
				}
				taskService.complete(task.getId());
			}
		}
		if(instance == null){
			exceptionFactory.throwIllegalArgument(ErrorConstant.INSTANCE_NOT_REPEAT,request.getProcessDefinitionId());
		}
		//创建任务
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).list();
		return restResponseFactory.createProcessInstanceStartResponse(instance, tasks);
	}

	@DeleteMapping(value = "/process-instances/{processInstanceId}", name = "流程实例删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Transactional
	public void deleteProcessInstance(@PathVariable String processInstanceId, @RequestParam(value = "deleteReason", required = false) String deleteReason, @RequestParam(value = "cascade", required = false) boolean cascade) {
		HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceFromRequest(processInstanceId);
		if (historicProcessInstance.getEndTime() != null) {
			historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
			return;
		}
		ExecutionEntity executionEntity = (ExecutionEntity) getProcessInstanceFromRequest(processInstanceId);
		if (ObjectUtils.isNotEmpty(executionEntity.getSuperExecutionId())) {
			exceptionFactory.throwForbidden(ErrorConstant.INSTANCE_HAVE_PARENT, processInstanceId);
		}

		runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
		if (cascade) {
			historyService.deleteHistoricProcessInstance(processInstanceId);
		}
		loggerConverter.save("删除了流程实例 '" +historicProcessInstance.getProcessDefinitionName()+ "'");
	}
}
