package com.miyu.module.wms.framework.rpc.config;

import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import com.miyu.cloud.mcs.api.McsManufacturingControlApi;
import com.miyu.module.ppm.api.purchaseConsignment.PurchaseConsignmentApi;
import com.miyu.module.ppm.api.shipping.ShippingApi;
import com.miyu.module.mcc.api.encodingrule.EncodingRuleApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {AdminUserApi.class, McsManufacturingControlApi.class, PurchaseConsignmentApi.class, ShippingApi.class, EncodingRuleApi.class})
public class RpcConfiguration {
}
