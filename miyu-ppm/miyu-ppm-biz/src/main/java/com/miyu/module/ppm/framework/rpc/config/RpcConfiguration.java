package com.miyu.module.ppm.framework.rpc.config;

import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.PmsOrderApi;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.ip.AreaApi;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import com.miyu.module.mcc.api.encodingrule.EncodingRuleApi;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.qms.api.inspectionscheme.InspectionSchemeApi;
import com.miyu.module.qms.api.inspectionsheet.InspectionSheetApi;
import com.miyu.module.qms.api.inspectionsheetcreatetask.InspectionSheetTaskApi;
import com.miyu.module.qms.api.unqualifiedmaterial.UnqualifiedMaterialApi;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.WarehouseApi;
import com.miyu.module.wms.api.order.OrderApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {AdminUserApi.class,
        DeptApi.class,
        AreaApi.class,
        BpmProcessInstanceApi.class,
        OrderApi.class,
        InspectionSchemeApi.class,
        InspectionSheetApi.class,
        InspectionSheetTaskApi.class,
        PmsApi.class,
        PmsOrderApi.class,
        MaterialMCCApi.class,
        PmsOrderMaterialRelationApi.class,
        MaterialStockApi.class,
        UnqualifiedMaterialApi.class,
        EncodingRuleApi.class,
        WarehouseApi.class
})
public class RpcConfiguration {
}
