package com.miyu.module.pdm.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.pdm.api.toolingApply.ToolingApplyApi;
import com.miyu.module.pdm.api.toolingDetail.ToolingDetailApi;
import com.miyu.module.pdm.enums.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class PdmToolingDetailStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private ToolingDetailApi toolingDetailApi;

    @Override
    protected String getProcessDefinitionKey() {
        return ApiConstants.TOOLING_DETAIL_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        log.info("审批回调成功");
        //更新审批状态
        toolingDetailApi.updateToolingDetailStatus(event.getBusinessKey(), event.getStatus());
    }

}
