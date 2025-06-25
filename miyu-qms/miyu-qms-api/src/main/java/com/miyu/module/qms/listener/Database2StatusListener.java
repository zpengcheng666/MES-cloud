package com.miyu.module.qms.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.qms.api.database.DatabaseApi;
import com.miyu.module.qms.enums.ApiConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 质量目标预计划管理资料库审批的结果的监听器实现类
 *
 * @author Zhangyunfei
 */
@Component
public class Database2StatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private DatabaseApi databseApi;

    @Override
    public String getProcessDefinitionKey() {
        return ApiConstants.DATABASE2_AUDIT_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        databseApi.updateDatabaseAuditStatus(event.getBusinessKey(), event.getStatus());
    }

}
