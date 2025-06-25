package com.miyu.cloud.framework.rpc.config;

import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.notify.NotifyMessageSendApi;
import cn.iocoder.yudao.module.system.api.permission.PermissionApi;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import com.miyu.cloud.mcs.restServer.api.WmsOrderApiMapping;
import com.miyu.module.qms.api.inspectionsheet.InspectionSheetApi;
import com.miyu.module.qms.api.inspectionsheetcreatetask.InspectionSheetTaskApi;
import com.miyu.module.wms.api.order.OrderApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {
        FileApi.class,
        BpmProcessInstanceApi.class,
        NotifyMessageSendApi.class,
        DictDataApi.class,
        PermissionApi.class,
        AdminUserApi.class,
        InspectionSheetApi.class,
        InspectionSheetTaskApi.class,
        OrderApi.class,
        WmsOrderApiMapping.class,
        PmsOrderMaterialRelationApi.class})
public class RpcConfiguration {
}
