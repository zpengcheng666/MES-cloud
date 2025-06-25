package cn.iocoder.yudao.module.pms.listener;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.enums.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 工艺方案审批的监听器实现类
 *
 * @author Liuy
 */
@Component
@Slf4j
public class PmsApprovalStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private PmsApi pmsApi;

    @Override
    protected String getProcessDefinitionKey() {
        return ApiConstants.Project_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        log.info("审批回调成功");
        //更新审批状态
//        processPlanApi.updateProcessTaskStatus(event.getBusinessKey(), event.getStatus());
        pmsApi.updateProcessStatus(event.getBusinessKey(), event.getStatus()).getCheckedData();
    }
}
