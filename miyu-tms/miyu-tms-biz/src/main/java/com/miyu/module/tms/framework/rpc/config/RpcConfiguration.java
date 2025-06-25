package com.miyu.module.tms.framework.rpc.config;

import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import com.miyu.cloud.mcs.api.McsManufacturingControlApi;
import com.miyu.module.mcc.api.encodingrule.EncodingRuleApi;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialtype.MaterialTypeApi;
import com.miyu.module.wms.api.mateiral.MaterialConfigApi;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.warehouse.WarehouseLocationApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {AdminUserApi.class,
        MaterialStockApi.class,
        MaterialConfigApi.class,
        WarehouseLocationApi.class,
        McsManufacturingControlApi.class,
        MaterialMCCApi.class,
        MaterialTypeApi.class,
        EncodingRuleApi.class})
public class RpcConfiguration {
}
