package com.spring.cloud.flow.rest.task.resource;

import com.spring.cloud.flow.constant.TableConstant;
import org.flowable.task.api.Task;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务转办接口
 * @author cdy
 * @create 2018/9/5
 */
@RestController
public class TaskAssignResource extends BaseTaskResource {

    @PutMapping(value = "/tasks/{taskId}/assign/{assignee}",name = "任务转办")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public void assignTask(@PathVariable("taskId") String taskId, @PathVariable("assignee") String assignee) {
        Task task = getTaskFromRequest(taskId);
        if(TableConstant.ASSIGNEE_NOBODY.equals(assignee)) {
            taskService.setAssignee(taskId,null);
        }else {
            taskService.setAssignee(task.getId(),assignee);
        }
        loggerConverter.save("转办了任务 '" +  task.getName() +"'");
    }
}
