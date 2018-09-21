package com.spring.cloud.flow.rest.log.resource;

import com.spring.cloud.common.utils.TokenUserIdUtils;
import org.flowable.engine.FormService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created by CDZ on 2018/9/14.
 */
public class LogResource {
    @Autowired
    HistoryService historyService;
    @Autowired
    FormService formService;
    @Autowired
    RepositoryService repositoryService;

    /***
     * 查询当前用户启动了多少个流程（充当流程开启者）后续优化
     */
    @GetMapping(value = "/qidongzhe")
    public void qidongzhe(){
        TokenUserIdUtils tokenUserIdUtils = new TokenUserIdUtils();
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().startedBy(tokenUserIdUtils.tokenUserId()).list();
    }

    /**
     * 根据标识查询到工作流引用哪个表单（开始节点）
     */
    @GetMapping(value = "/test")
    public void a(){
        String processDefinitionId = "审批:1:62504";
        String expectedFormKey = formService.getStartFormData(processDefinitionId).getFormKey();
        System.out.println(expectedFormKey);
    }

    /**
     * 根据任务Id查询到工作流引用的哪个表单（任务节点）
     */
    @GetMapping(value = "/b")
    public void b(){
        String expectedFormKey = formService.getTaskFormData("67510").getFormKey();
        System.out.println(expectedFormKey);

    }
}
