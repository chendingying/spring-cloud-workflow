package com.spring.cloud.flow.listener;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.List;

/**
 * Created by CDZ on 2018/9/14.
 */
public class TaskListenerImpl implements TaskListener {

    /**用来指定任务的办理人*/
    @Override
    public void notify(DelegateTask delegateTask) {
//        //指定个人任务的办理人，也可以指定组任务的办理人
//        String executeInstanceId = delegateTask.getProcessInstanceId();
//
//        String nextLevel="Nobody";
//
////		IntershipBeanImpl intershipbeanimpl = new IntershipBeanImpl();
////		//个人任务：通过类去查询数据库，将下一个任务的办理人查询获取，然后通过setAssignee()的方法指定任务的办理人
////		String assignee = intershipbeanimpl.findAssigneeByExecutionID("itoo_internship", executeInstanceId);
//
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//
//        //根据流程实例查询任务列表[
//        List<Task> listtask = processEngine.getTaskService().createTaskQuery().executionId(executeInstanceId).list();
//
//        if(listtask.size() > 0 && listtask!=null){
//
//            String lastassignee = listtask.get(0).getAssignee();   //取得ID
//
//            String prcessName = listtask.get(0).getProcessDefinitionId();
//
//            String[] processDefinitionid =  prcessName.split(":");
//            String processDefId = processDefinitionid[0];          //就去intershipone
//
//            List<HistoricTaskInstance> list = processEngine.getHistoryService().createHistoricTaskInstanceQuery().executionId(executeInstanceId).list();
//
//        }
//
//
//        System.out.println(nextLevel);

//        delegateTask.setAssignee(nextLevel);

        delegateTask.setAssignee("5");
    }
}
