package cn.iocoder.yudao.module.bpm.controller.admin.task.pms.assessment;

import cn.iocoder.yudao.module.bpm.api.pms.PmsListenerApi;
import cn.iocoder.yudao.module.bpm.service.task.BpmProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.task.api.Task;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 战略评估执行监听器
 */
@Component
@Slf4j
public class StrategyExecutionListener implements JavaDelegate {
    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private TaskService taskService;

    @Resource
    private PmsListenerApi pmsListenerApi;

    /**
     * 主要目的是获取当前任务的执行人和执行人填写的原因
     * @param execution
     */
    @Override
    public void execute(DelegateExecution execution) {
        Task task = taskService.createTaskQuery().executionId(execution.getId()).singleResult();
        System.out.println(task);
        String assignee = task.getAssignee();
        Map<String, VariableInstance> variableInstances = taskService.getVariableInstances(task.getId());
        String reason = variableInstances.get("TASK_REASON").getTextValue();
        String assessmentId = variableInstances.get("assessmentId").getTextValue();
        String conclusion = variableInstances.get("conclusion").getTextValue();

        Map<String,String> map = new HashMap<>();
        map.put("auditor",assignee);
        map.put("suggestion",reason);
        map.put("assessmentId",assessmentId);
        map.put("conclusion",conclusion);

        pmsListenerApi.createStrategyChild(map);


    }

}
