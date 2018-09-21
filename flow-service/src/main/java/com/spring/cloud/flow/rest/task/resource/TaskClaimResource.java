package com.spring.cloud.flow.rest.task.resource;

import org.flowable.task.api.Task;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务认领接口
 * @author cdy
 * @create 2018/9/5
 */
@RestController
public class TaskClaimResource extends BaseTaskResource {

    @PutMapping(value = "/tasks/{taskId}/claim/{claimer}",name = "任务认领")
    public void claimTask(@PathVariable("taskId") String taskId,@PathVariable("claimer") String claimer) {
        Task task = getTaskFromRequest(taskId);
        taskService.claim(task.getId(),claimer);
    }
}
