package com.miyu.module.ppm.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.ppm.enums.ApiConstants;
import com.miyu.module.ppm.api.company.CompanyApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 供应商审批的结果的监听器实现类
 *
 * @author zhp
 */
@Component
public class DMCompanyStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private CompanyApi companyApi;

    @Override
    public String getProcessDefinitionKey() {
        return ApiConstants.COMPANY_AUDIT_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        companyApi.updateCompanyAuditStatus(event.getBusinessKey(), event.getStatus());
    }

}
