package com.spring.cloud.flow.rest.task.resource;

import com.spring.cloud.flow.constant.ErrorConstant;
import com.spring.cloud.common.model.Authentication;
import com.spring.cloud.flow.rest.task.TaskCompleteRequest;
import com.spring.cloud.flow.rest.variable.RestVariable;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cdy
 * @create 2018/9/5
 */
@RestController
public class TaskCompleteResource extends BaseTaskResource {

    @PutMapping(value = "/tasks/{taskId}/complete",name = "任务完成")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional(propagation = Propagation.REQUIRED)
    public void completeTask(@PathVariable String taskId, @RequestBody(required = false)TaskCompleteRequest taskCompleteRequest) {

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
                taskService.complete(taskId,completeVariables);
            }
        }
        loggerConverter.save("完成了任务 '" + task.getName() + "'");
    }
}






























