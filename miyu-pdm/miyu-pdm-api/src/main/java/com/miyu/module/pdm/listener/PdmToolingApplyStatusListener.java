package com.miyu.module.pdm.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.pdm.api.toolingApply.ToolingApplyApi;
import com.miyu.module.pdm.enums.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class PdmToolingApplyStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private ToolingApplyApi toolingApplyApi;

    @Override
    protected String getProcessDefinitionKey() {
        return ApiConstants.TOOLING_APPLY_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        log.info("审批回调成功");
        //更新审批状态
        toolingApplyApi.updateToolingApplyStatus(event.getBusinessKey(), event.getStatus());
    }

}
