package cn.iocoder.yudao.module.bpm.framework.flowable.core.listener.demo.exection;

import com.miyu.module.qms.api.dto.UnqualifiedMaterialReqDTO;
import com.miyu.module.qms.api.unqualifiedmaterial.UnqualifiedMaterialApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.task.api.Task;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 类型为 class 的 ExecutionListener 监听器示例
 *
 * @author 芋道源码
 */

@Component
@Slf4j
class UnqualifiedDelegateClassExecutionListener implements JavaDelegate {

    @Resource
    private TaskService taskService;

    @Resource
    private UnqualifiedMaterialApi unqualifiedMaterialApi;


    @Override
    public void execute(DelegateExecution execution) {
        log.info("[execute][execution({}) 被调用！变量有：{}]", execution.getId(),
                execution.getCurrentFlowableListener().getFieldExtensions());
        Task task = taskService.createTaskQuery().executionId(execution.getId()).singleResult();
        Map<String, VariableInstance> variableInstances = taskService.getVariableInstances(task.getId());
        VariableInstance a = variableInstances.get("unqualifiedMaterials");
        unqualifiedMaterialApi.updateUnqualifiedMaterial((List<UnqualifiedMaterialReqDTO>) a.getCachedValue());
    }

}
