package com.spring.cloud.flow.rest;


import com.spring.cloud.flow.constant.TableConstant;
import com.spring.cloud.flow.rest.common.IdentityResponse;
import com.spring.cloud.flow.rest.definition.ProcessDefinitionResponse;
import com.spring.cloud.flow.rest.instance.HistoricProcessInstanceResponse;
import com.spring.cloud.flow.rest.instance.ProcessInstanceDetailResponse;
import com.spring.cloud.flow.rest.instance.ProcessInstanceResponse;
import com.spring.cloud.flow.rest.instance.ProcessInstanceStartResponse;
import com.spring.cloud.flow.rest.model.ModelResponse;
import com.spring.cloud.flow.rest.task.HistoricTaskResponse;
import com.spring.cloud.flow.rest.task.TaskResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.cloud.common.utils.ObjectUtils;
import com.spring.cloud.flow.rest.task.TaskDetailResponse;
import com.spring.cloud.flow.rest.variable.*;
import org.flowable.engine.IdentityService;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.IdentityLinkInfo;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.identitylink.service.IdentityLinkType;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * rest 接口返回结果工厂类
 * @author cdy
 * @create 2018/9/4
 */
@Component
public  class RestResponseFactory {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IdentityService identityService;

    protected List<RestVariableConverter> variableConverters = new ArrayList<RestVariableConverter>();

    public RestResponseFactory() {initializeVariableConverters();}

    protected void initializeVariableConverters() {
        variableConverters.add(new StringRestVariableConverter());
        variableConverters.add(new IntegerRestVariableConverter());
        variableConverters.add(new LongRestVariableConverter());
        variableConverters.add(new ShortRestVariableConverter());
        variableConverters.add(new DoubleRestVariableConverter());
        variableConverters.add(new BooleanRestVariableConverter());
        variableConverters.add(new DateRestVariableConverter());
        variableConverters.add(new ListRestVariableConverter());
    }

    public Object getVariableValue(RestVariable restVariable) {
        Object value = null;

        if(restVariable.getType() != null) {
            RestVariableConverter converter = null;
            for(RestVariableConverter conv : variableConverters) {
                if(conv.getRestTypeName().equals(restVariable.getType())) {
                    converter = conv;
                    break;
                }
            }
            if (converter == null) {
                throw new FlowableIllegalArgumentException("Variable '" + restVariable.getName() + "' has unsupported type: '" + restVariable.getType() + "'.");
            }
            value = converter.getVariableValue(restVariable);

        } else {
            value = restVariable.getValue();
        }
        return value;
    }

    public List<RestVariable> createRestVariables(Map<String, Object> variables) {
        List<RestVariable> result = new ArrayList<RestVariable>();
        RestVariableConverter converter = null;
        for (Map.Entry<String, Object> pair : variables.entrySet()) {
            Object value = pair.getValue();
            if(value == null) {
                continue;
            }
            for (RestVariableConverter c : variableConverters) {
                if (c.getVariableType().isAssignableFrom(value.getClass())) {
                    converter = c;
                    break;
                }
            }

            RestVariable restVariable = new RestVariable();
            restVariable.setName(pair.getKey());
            if (converter != null) {
                restVariable.setType(converter.getRestTypeName());
                converter.convertVariableValue(value, restVariable);
            }else {
                restVariable.setType(value.getClass().getName());
                restVariable.setValue(value);
            }

            result.add(restVariable);
        }
        return result;
    }

    public List<RestVariable> createRestVariables(List<HistoricVariableInstance> historicVariableInstances) {
        List<RestVariable> result = new ArrayList<RestVariable>();
        RestVariableConverter converter = null;
        for (HistoricVariableInstance pair : historicVariableInstances) {

            for (RestVariableConverter c : variableConverters) {
                if (c.getRestTypeName().equals(pair.getVariableTypeName())) {
                    converter = c;
                    break;
                }
            }

            RestVariable restVariable = new RestVariable();
            restVariable.setName(pair.getVariableName());
            if (converter != null) {
                restVariable.setType(converter.getRestTypeName());
                converter.convertVariableValue(pair.getValue(), restVariable);
            }else {
                restVariable.setType(pair.getVariableTypeName());
                restVariable.setValue(pair.getValue());
            }

            result.add(restVariable);
        }
        return result;
    }

    public List<ModelResponse> createModelResponseList(List<Model> models) {
        List<ModelResponse> responseList = new ArrayList<ModelResponse>();
        for (Model instance : models) {
            responseList.add(createModelResponse(instance));
        }
        return responseList;
    }

    public ModelResponse createModelResponse(Model model) {
        ModelResponse response = new ModelResponse();
        response.setCategory(model.getCategory());
        response.setCreateTime(model.getCreateTime());
        response.setId(model.getId());
        response.setKey(model.getKey());
        response.setLastUpdateTime(model.getLastUpdateTime());
        try {
            JsonNode modelNode = objectMapper.readTree(model.getMetaInfo());
            response.setDescription(modelNode.get("description").asText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setName(model.getName());
        response.setVersion(model.getVersion());
        if (model.getDeploymentId() != null) {
            response.setDeployed(true);
        } else {
            response.setDeployed(false);
        }
        response.setTenantId(model.getTenantId());
        return response;
    }

    public List<ProcessDefinitionResponse> createProcessDefinitionResponseList(List<ProcessDefinition> processDefinitions) {
        List<ProcessDefinitionResponse> responseList = new ArrayList<ProcessDefinitionResponse>();
        for (ProcessDefinition instance : processDefinitions) {
            responseList.add(createProcessDefinitionResponse(instance));
        }
        return responseList;
    }

    public ProcessDefinitionResponse createProcessDefinitionResponse(ProcessDefinition processDefinition) {
        ProcessDefinitionResponse response = new ProcessDefinitionResponse();
        response.setId(processDefinition.getId());
        response.setKey(processDefinition.getKey());
        response.setVersion(processDefinition.getVersion());
        response.setCategory(processDefinition.getCategory());
        response.setName(processDefinition.getName());
        response.setDescription(processDefinition.getDescription());
        response.setSuspended(processDefinition.isSuspended());
        response.setGraphicalNotationDefined(processDefinition.hasGraphicalNotation());
        response.setTenantId(processDefinition.getTenantId());
        return response;
    }

    public List<ProcessInstanceResponse> createProcessInstanceResponseList(List<ProcessInstance> processInstances) {
        List<ProcessInstanceResponse> responseList = new ArrayList<ProcessInstanceResponse>();
        for (ProcessInstance instance : processInstances) {
            responseList.add(createProcessInstanceResponse(instance));
        }
        return responseList;
    }

    public ProcessInstanceResponse createProcessInstanceResponse(ProcessInstance processInstance) {
        ProcessInstanceResponse result = new ProcessInstanceResponse();
        result.setId(processInstance.getId());
        result.setSuspended(processInstance.isSuspended());
        result.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        result.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        result.setProcessDefinitionName(processInstance.getProcessDefinitionName());
        result.setProcessDefinitionVersion(processInstance.getProcessDefinitionVersion());
        result.setStartTime(processInstance.getStartTime());
        result.setStartUserId(processInstance.getStartUserId());
        result.setCurrentActivityId(processInstance.getActivityId());
        result.setBusinessKey(processInstance.getBusinessKey());
        result.setTenantId(processInstance.getTenantId());
        return result;
    }

    public List<HistoricProcessInstanceResponse> createHistoricProcessInstancResponseList(List<HistoricProcessInstance> processInstances) {
        List<HistoricProcessInstanceResponse> responseList = new ArrayList<HistoricProcessInstanceResponse>();
        for (HistoricProcessInstance instance : processInstances) {
            responseList.add(createHistoricProcessInstanceResponse(instance));
        }
        return responseList;
    }

    public HistoricProcessInstanceResponse createHistoricProcessInstanceResponse(HistoricProcessInstance processInstance) {
        HistoricProcessInstanceResponse result = new HistoricProcessInstanceResponse();
        createHistoricProcessInstanceResponse(result, processInstance);
        return result;
    }

    public ProcessInstanceDetailResponse createProcessInstanceDetailResponse(HistoricProcessInstance hisProcessInstance, ProcessInstance processInstance) {
        ProcessInstanceDetailResponse result = new ProcessInstanceDetailResponse();
        createHistoricProcessInstanceResponse(result, hisProcessInstance);
        result.setStartUserName(getUserName(hisProcessInstance.getStartUserId()));
        if (processInstance != null) {
            result.setSuspended(processInstance.isSuspended());
        }
        return result;
    }

    private void createHistoricProcessInstanceResponse(HistoricProcessInstanceResponse result, HistoricProcessInstance processInstance) {
        result.setId(processInstance.getId());
        result.setBusinessKey(processInstance.getBusinessKey());
        result.setStartTime(processInstance.getStartTime());
        result.setEndTime(processInstance.getEndTime());
        result.setDurationInMillis(processInstance.getDurationInMillis());
        result.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        result.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        result.setProcessDefinitionName(processInstance.getProcessDefinitionName());
        result.setProcessDefinitionVersion(processInstance.getProcessDefinitionVersion());
        result.setStartActivityId(processInstance.getStartActivityId());
        result.setStartUserId(processInstance.getStartUserId());
        result.setSuperProcessInstanceId(processInstance.getSuperProcessInstanceId());
        result.setTenantId(processInstance.getTenantId());
    }

    public ProcessInstanceStartResponse createProcessInstanceStartResponse(ProcessInstance processInstance, List<Task> tasks) {
        ProcessInstanceStartResponse result = new ProcessInstanceStartResponse();
        result.setId(processInstance.getId());
        result.setBusinessKey(processInstance.getBusinessKey());
        result.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        result.setCurrentActivityId(processInstance.getActivityId());
        result.setTenantId(processInstance.getTenantId());
        List<Map<String, String>> taskInfo = new ArrayList<Map<String, String>>();
        for (Task task : tasks) {
            Map<String, String> taskMap = new HashMap<String, String>();
            taskMap.put("taskId", task.getId());
            taskMap.put("taskName", task.getName());
            taskMap.put("taskDefinitionKey", task.getTaskDefinitionKey());
            taskInfo.add(taskMap);
        }
        result.setTaskInfo(taskInfo);
        return result;
    }

    public List<HistoricTaskResponse> createHistoricTaskResponseList(List<HistoricTaskInstance> tasks) {
        List<HistoricTaskResponse> responseList = new ArrayList<HistoricTaskResponse>();
        for (HistoricTaskInstance instance : tasks) {
            responseList.add(createHistoricTaskResponse(instance));
        }
        return responseList;
    }

    public TaskDetailResponse createTaskDetailResponse(HistoricTaskInstance historicTaskInstance, Task taskInstance) {
        TaskDetailResponse result = new TaskDetailResponse();
        createHistoricTaskResponse(result, historicTaskInstance);
        result.setAssigneeName(getUserName(historicTaskInstance.getAssignee()));
        result.setOwnerName(getUserName(historicTaskInstance.getOwner()));
        if (taskInstance != null) {
            result.setDelegationState(taskInstance.getDelegationState());
            result.setSuspended(taskInstance.isSuspended());
        }
        return result;
    }

    public HistoricTaskResponse createHistoricTaskResponse(HistoricTaskInstance taskInstance) {
        HistoricTaskResponse result = new HistoricTaskResponse();
        createHistoricTaskResponse(result, taskInstance);
        return result;
    }

    private void createHistoricTaskResponse(HistoricTaskResponse result, HistoricTaskInstance taskInstance) {
        result.setId(taskInstance.getId());
        result.setName(taskInstance.getName());
        result.setOwner(taskInstance.getOwner());
        result.setTaskDefinitionKey(taskInstance.getTaskDefinitionKey());
        result.setAssignee(taskInstance.getAssignee());
        result.setDescription(taskInstance.getDescription());
        result.setCategory(taskInstance.getCategory());
        result.setDueDate(taskInstance.getDueDate());
        result.setFormKey(taskInstance.getFormKey());
        result.setParentTaskId(taskInstance.getParentTaskId());
        result.setPriority(taskInstance.getPriority());
        result.setProcessDefinitionId(taskInstance.getProcessDefinitionId());
        result.setTenantId(taskInstance.getTenantId());
        result.setProcessInstanceId(taskInstance.getProcessInstanceId());
        result.setDurationInMillis(taskInstance.getDurationInMillis());
        result.setStartTime(taskInstance.getStartTime());
        result.setEndTime(taskInstance.getEndTime());
        result.setClaimTime(taskInstance.getClaimTime());
        result.setWorkTimeInMillis(taskInstance.getWorkTimeInMillis());
    }

    public List<TaskResponse> createTaskResponseList(List<Task> tasks) {
        List<TaskResponse> responseList = new ArrayList<TaskResponse>();
        for (Task instance : tasks) {
            responseList.add(createTaskResponse(instance));
        }
        return responseList;
    }

    public TaskResponse createTaskResponse(Task taskInstance) {
        TaskResponse result = new TaskResponse();
        createTaskResponse(result, taskInstance);
        return result;
    }

    private void createTaskResponse(TaskResponse result, Task taskInstance) {
        result.setId(taskInstance.getId());
        result.setName(taskInstance.getName());
        result.setOwner(taskInstance.getOwner());
        result.setTaskDefinitionKey(taskInstance.getTaskDefinitionKey());
        result.setCreateTime(taskInstance.getCreateTime());
        result.setAssignee(taskInstance.getAssignee());
        result.setDescription(taskInstance.getDescription());
        result.setDueDate(taskInstance.getDueDate());
        result.setDelegationState(taskInstance.getDelegationState());
        result.setFormKey(taskInstance.getFormKey());
        result.setParentTaskId(taskInstance.getParentTaskId());
        result.setPriority(taskInstance.getPriority());
        result.setSuspended(taskInstance.isSuspended());
        result.setTenantId(taskInstance.getTenantId());
        result.setCategory(taskInstance.getCategory());
        result.setProcessDefinitionId(taskInstance.getProcessDefinitionId());
        result.setProcessInstanceId(taskInstance.getProcessInstanceId());
    }

    public List<IdentityResponse> createTaskIdentityResponseList(List<HistoricIdentityLink> historicIdentityLinks) {
        List<IdentityResponse> responseList = new ArrayList<IdentityResponse>();
        for (HistoricIdentityLink identityLink : historicIdentityLinks) {
            if (IdentityLinkType.CANDIDATE.equals(identityLink.getType())) {
                responseList.add(createIdentityResponse(identityLink));
            }
        }
        return responseList;
    }

    public List<IdentityResponse> createIdentityResponseList(List<IdentityLink> identityLinks) {
        List<IdentityResponse> responseList = new ArrayList<IdentityResponse>();
        for (IdentityLinkInfo identityLink : identityLinks) {
            responseList.add(createIdentityResponse(identityLink));
        }
        return responseList;
    }

    public IdentityResponse createIdentityResponse(IdentityLinkInfo identityLink) {
        IdentityResponse identityResponse = new IdentityResponse();
        if (identityLink.getGroupId() != null) {
            identityResponse.setType(TableConstant.IDENTITY_GROUP);
            identityResponse.setIdentityId(identityLink.getGroupId());
            identityResponse.setIdentityName(getGroupName(identityLink.getGroupId()));
        } else if (identityLink.getUserId() != null) {
            identityResponse.setType(TableConstant.IDENTITY_USER);
            identityResponse.setIdentityId(identityLink.getUserId());
            identityResponse.setIdentityName(getUserName(identityLink.getUserId()));
        }
        return identityResponse;
    }

    private String getUserName(String userId) {
        if(ObjectUtils.isEmpty(userId)) {
            return null;
        }
        User user = identityService.createUserQuery().userId(userId).singleResult();
        if (user != null) {
            return user.getFirstName();
        }
        return null;
    }

    private String getGroupName(String groupId) {
        if(ObjectUtils.isEmpty(groupId)) {
            return null;
        }
        Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
        if (group != null) {
            return group.getName();
        }
        return null;
    }



























}
