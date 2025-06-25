package cn.iocoder.yudao.module.bpm.framework.flowable.core.listener.demo.exection;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.alibaba.fastjson.JSON;
import com.miyu.module.tms.api.ToolConfigApi;
import com.miyu.module.tms.api.dto.ToolConfigSaveReqDTO;
import com.miyu.module.wms.api.mateiral.MaterialConfigApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigSaveReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.task.api.Task;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants.PARAM_NOT_NULL;

/**
 * 类型为 class 的 ExecutionListener 监听器示例
 *
 * @author 芋道源码
 */

@Component
@Slf4j
class WmsMaterialConfigClassExecutionListener implements JavaDelegate {

    @Resource
    private TaskService taskService;

    @Resource
    private MaterialConfigApi materialConfigApi;

    @Resource
    private ToolConfigApi toolConfigApi;

    @Override
    public void execute(DelegateExecution execution) {
        log.info("[execute][execution({}) 被调用！变量有：{}]", execution.getId(),
                execution.getCurrentFlowableListener().getFieldExtensions());

        Task task = taskService.createTaskQuery().executionId(execution.getId()).singleResult();
        Map<String, VariableInstance> variableInstances = taskService.getVariableInstances(task.getId());
        VariableInstance a = variableInstances.get("wmsMaterialConfig");
        MaterialConfigSaveReqDTO materialConfigSaveReqDTO = JSON.parseObject(JSON.toJSONString(a.getCachedValue()),MaterialConfigSaveReqDTO.class);
        // 物料类型为刀具
        if("DJ".equals(materialConfigSaveReqDTO.getMaterialType())){
            ToolConfigSaveReqDTO toolConfigSaveReqDTO = JSON.parseObject(JSON.toJSONString(a.getCachedValue()),ToolConfigSaveReqDTO.class);
            toolConfigSaveReqDTO.setMaterialConfigId(materialConfigSaveReqDTO.getId());
            toolConfigSaveReqDTO.setToolType(materialConfigSaveReqDTO.getMaterialType());
            CommonResult<Boolean> booleanCommonResult = toolConfigApi.createToolConfig(toolConfigSaveReqDTO);
            if(!booleanCommonResult.isSuccess()){
                throw exception(PARAM_NOT_NULL);
            }
        }
        else {
            CommonResult<Boolean> booleanCommonResult = materialConfigApi.otherUpdateMaterialConfig(materialConfigSaveReqDTO);
            if(!booleanCommonResult.isSuccess()){
                throw exception(PARAM_NOT_NULL);
            }
        }
    }

}
