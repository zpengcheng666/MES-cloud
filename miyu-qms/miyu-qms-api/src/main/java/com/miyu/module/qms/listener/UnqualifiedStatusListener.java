package com.miyu.module.qms.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.qms.api.unqualifiedmaterial.UnqualifiedMaterialApi;
import com.miyu.module.qms.enums.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 不合格品审批的结果的监听器实现类
 *
 * @author Zhangyunfei
 */
@Component
@Slf4j
public class UnqualifiedStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private UnqualifiedMaterialApi unqualifiedMaterialApi;

    @Override
    public String getProcessDefinitionKey() {
        return ApiConstants.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        log.info("审批回调成功");
        unqualifiedMaterialApi.updateUnqualifiedAuditStatus(event.getBusinessKey(), event.getStatus());
    }
}
