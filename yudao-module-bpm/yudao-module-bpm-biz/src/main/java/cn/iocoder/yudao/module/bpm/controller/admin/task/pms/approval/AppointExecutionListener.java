package cn.iocoder.yudao.module.bpm.controller.admin.task.pms.approval;

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
import java.util.List;
import java.util.Map;

/**
 * 立项任命执行监听器
 */
@Component
@Slf4j
public class AppointExecutionListener implements JavaDelegate {
    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private TaskService taskService;

    @Resource
    private PmsListenerApi pmsListenerApi;

    /**
     * 立项负责人任命
     * @param execution
     */
    @Override
    public void execute(DelegateExecution execution) {
        Task task = taskService.createTaskQuery().executionId(execution.getId()).singleResult();
        Map<String, VariableInstance> variableInstances = taskService.getVariableInstances(task.getId());

        List<Integer> responsiblePersons = (List<Integer>) variableInstances.get("responsiblePerson").getCachedValue();
        List<Integer> projectManagers = (List<Integer>) variableInstances.get("projectManager").getCachedValue();
        String approvalId = variableInstances.get("approvalId").getTextValue();

        Integer responsiblePeople = responsiblePersons.get(0);
        Integer projectManager = projectManagers.get(0);
        Map<String,String> map = new HashMap<>();
        map.put("responsiblePerson",responsiblePeople.toString());
        map.put("projectManager",projectManager.toString());
        map.put("approvalId",approvalId);

        pmsListenerApi.appoint(map);
    }

}
