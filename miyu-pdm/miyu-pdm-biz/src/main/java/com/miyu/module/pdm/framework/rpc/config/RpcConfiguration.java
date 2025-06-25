package com.miyu.module.pdm.framework.rpc.config;

import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import com.miyu.cloud.dms.api.devicetype.DeviceTypeApi;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.qms.api.inspectionscheme.InspectionSchemeApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {AdminUserApi.class, BpmProcessInstanceApi.class, PmsApi.class, DeviceTypeApi.class, MaterialMCCApi.class, InspectionSchemeApi.class})
public class RpcConfiguration {
}
