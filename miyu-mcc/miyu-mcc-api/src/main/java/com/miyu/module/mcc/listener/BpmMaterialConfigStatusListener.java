package com.miyu.module.mcc.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.enums.ApiConstants;
import com.miyu.module.ppm.api.contract.ContractApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 合同审批的结果的监听器实现类
 *
 * @author Zhangyunfei
 */
@Component
@Slf4j
public class BpmMaterialConfigStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Override
    public String getProcessDefinitionKey() {
        return ApiConstants.MATERIAL_CONFIG_AUDIT;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        log.info("回调物料审批");
        materialMCCApi.updateAudit(event.getBusinessKey(), event.getStatus());
    }

}
