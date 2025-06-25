package com.miyu.module.qms.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.qms.api.database.DatabaseApi;
import com.miyu.module.qms.enums.ApiConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 质量改进管理审批的结果的监听器实现类
 *
 * @author Zhangyunfei
 */
@Component
public class Database5StatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private DatabaseApi databseApi;

    @Override
    public String getProcessDefinitionKey() {
        return ApiConstants.DATABASE5_AUDIT_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        databseApi.updateDatabaseAuditStatus(event.getBusinessKey(), event.getStatus());
    }

}
