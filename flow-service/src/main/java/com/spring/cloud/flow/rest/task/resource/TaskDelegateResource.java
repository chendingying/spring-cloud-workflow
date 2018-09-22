package com.spring.cloud.flow.rest.task.resource;

import org.flowable.task.api.Task;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务委托接口
 * @author cdy
 * @create 2018/9/5
 */
@RestController
public class TaskDelegateResource extends BaseTaskResource {

    @PutMapping(value = "/tasks/{taskId}/delegate/{delegater}",name = "任务委托")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public void delegateTask(@PathVariable("taskId") String taskId,@PathVariable("delegater") String delegater) {
        Task task = getTaskFromRequest(taskId);
        taskService.delegateTask(task.getId(),delegater);
        loggerConverter.save("委托了任务 '" + task.getName() + "'");
    }
}
