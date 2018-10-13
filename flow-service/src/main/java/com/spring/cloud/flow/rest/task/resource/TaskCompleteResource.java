package com.spring.cloud.flow.rest.task.resource;

import com.spring.cloud.flow.constant.ErrorConstant;
import com.spring.cloud.common.model.Authentication;
import com.spring.cloud.flow.rest.task.TaskCompleteRequest;
import com.spring.cloud.flow.rest.variable.RestVariable;
import com.spring.cloud.identity.domain.User;
import com.spring.cloud.identity.repository.UserRepository;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author cdy
 * @create 2018/9/5
 */
@RestController
public class TaskCompleteResource extends BaseTaskResource {

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    UserRepository userRepository;

    @PutMapping(value = "/tasks/{taskId}/complete",name = "任务完成")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional(propagation = Propagation.REQUIRED)
    public void completeTask(@PathVariable String taskId, @RequestBody(required = false)TaskCompleteRequest taskCompleteRequest) throws Exception {

        Task task = getTaskFromRequest(taskId);
        if(task.getAssignee() == null) {
            taskService.setAssignee(taskId, Authentication.getUserId());
        }

        Map<String,Object> completeVariables = new HashMap<String,Object>();
        if(taskCompleteRequest != null && taskCompleteRequest.getVariables() != null) {
            for(RestVariable variable : taskCompleteRequest.getVariables()) {
                if(variable.getName() == null) {
                    exceptionFactory.throwIllegalArgument(ErrorConstant.PARAM_NOT_FOUND,"变量名称");
                }
                completeVariables.put(variable.getName(),restResponseFactory.getVariableValue(variable));
            }
        }
        if(task.getDelegationState() != null && task.getDelegationState().equals(DelegationState.PENDING)) {
            if(completeVariables.isEmpty()) {
                taskService.resolveTask(taskId);
            }else {
                taskService.resolveTask(taskId,completeVariables);

            }
        }else {
            if(completeVariables.isEmpty()) {
                try{
                    taskService.complete(taskId);
                }catch (Exception e){
                }
            }else {
                if(completeVariables.get("submitType").equals("n")){
                    // 查找所有并行任务节点，同时驳回
                    List<Task> taskList = findTaskListByKey(findProcessInstanceByTaskId(
                            taskId).getId(), findTaskById(taskId).getTaskDefinitionKey());
                    for (Task t : taskList) {
                        taskService.complete(t.getId(),completeVariables);
                    }
                }else{
                    BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
                    Process process = bpmnModel.getProcesses().get(0);
                    Collection<UserTask> flowElements = process.findFlowElementsOfType(UserTask.class);
                    for (UserTask userTask : flowElements) {
                        List<String> list = new ArrayList<String>();
                        System.out.println(userTask.getId());
                        list = userTask.getCandidateUsers();
                        if(list.size() != 0){
                            completeVariables.put(userTask.getName(),list);
                        }
                    }
                    taskService.complete(taskId,completeVariables);
                }
            }
        }
        loggerConverter.save("完成了任务 '" + task.getName() + "'");


    }

    /**
     * 根据流程实例ID和任务key值查询所有同级任务集合
     *
     * @param processInstanceId
     * @param key
     * @return
     */
    private List<Task> findTaskListByKey(String processInstanceId, String key) {
        return taskService.createTaskQuery().processInstanceId(
                processInstanceId).taskDefinitionKey(key).list();
    }

    /**
     * 根据任务ID获取对应的流程实例
     *
     * @param taskId
     *            任务ID
     * @return
     * @throws Exception
     */
    private ProcessInstance findProcessInstanceByTaskId(String taskId)
            throws Exception {
        // 找到流程实例
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery().processInstanceId(
                        findTaskById(taskId).getProcessInstanceId())
                .singleResult();
        if (processInstance == null) {
            throw new Exception("流程实例未找到!");
        }
        return processInstance;
    }

    /**
     * 根据任务ID获得任务实例
     *
     * @param taskId
     *            任务ID
     * @return
     * @throws Exception
     */
    private TaskEntity findTaskById(String taskId) throws Exception {
        TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(
                taskId).singleResult();
        if (task == null) {
            throw new Exception("任务实例未找到!");
        }
        return task;
    }



}






























