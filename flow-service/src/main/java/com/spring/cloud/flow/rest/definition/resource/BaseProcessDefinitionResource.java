package com.spring.cloud.flow.rest.definition.resource;


import com.spring.cloud.flow.constant.ErrorConstant;
import com.spring.cloud.flow.rest.RestResponseFactory;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.flow.rest.log.LoggerConverter;
import org.flowable.engine.ManagementService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 流程定义接口基类
 * @author cdy
 * @create 2018/9/4
 */
public class BaseProcessDefinitionResource extends BaseResource {
	@Autowired
	protected RestResponseFactory restResponseFactory;
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	protected ManagementService managementService;
	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	protected LoggerConverter loggerConverter;

	protected ProcessDefinition getProcessDefinitionFromRequest(String processDefinitionId) {
		// 直接查询数据库，不查询缓存，防止出现挂起激活验证不一致
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();

		if (processDefinition == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.DEFINITION_NOT_FOUND, processDefinitionId);
		}

		return processDefinition;
	}
}
