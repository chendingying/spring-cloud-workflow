package com.spring.cloud.flow.rest.definition.resource;

import org.flowable.job.api.Job;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProcessDefinitionJobResource extends BaseProcessDefinitionResource {

	@GetMapping(value = "/process-definitions/{processDefinitionId}/jobs", name = "获取流程定义定时任务")
	@ResponseStatus(value = HttpStatus.OK)
	public List<Job> activateProcessDefinition(@PathVariable String processDefinitionId) {
		List<Job> jobs = managementService.createTimerJobQuery().processDefinitionId(processDefinitionId).list();
		return jobs;
	}

	@DeleteMapping(value = "/process-definitions/{processDefinitionId}/jobs/{jobId}", name = "删除流程定义定时任务")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteJob(@PathVariable String processDefinitionId, @PathVariable String jobId) {
		managementService.deleteTimerJob(jobId);
	}
}
