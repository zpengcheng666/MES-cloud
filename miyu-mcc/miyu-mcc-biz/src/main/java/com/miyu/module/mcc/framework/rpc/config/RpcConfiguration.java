package com.miyu.module.mcc.framework.rpc.config;

import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.PostApi;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.companyProduct.CompanyProductApi;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.InvoiceApi;
import com.miyu.module.ppm.api.contract.PaymentApi;
import com.miyu.module.wms.api.mateiral.MaterialConfigApi;
import com.miyu.module.wms.api.order.OrderApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {AdminUserApi.class,MaterialConfigApi.class,BpmProcessInstanceApi.class})
public class RpcConfiguration {
}
