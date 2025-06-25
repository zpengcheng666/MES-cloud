package cn.iocoder.yudao.module.bpm.framework.rpc.config;

import cn.iocoder.yudao.module.pms.api.assessment.AssessmentApi;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.PostApi;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.permission.RoleApi;
import cn.iocoder.yudao.module.system.api.sms.SmsSendApi;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;

import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.pdm.api.processChange.ProcessChangeApi;
import com.miyu.module.pdm.api.processPlan.ProcessPlanApi;
import com.miyu.module.pdm.api.processPlanDetail.ProcessPlanDetailApi;
import com.miyu.module.pdm.api.processSupplement.ProcessSupplementApi;
import com.miyu.module.pdm.api.toolingApply.ToolingApplyApi;
import com.miyu.module.pdm.api.toolingDetail.ToolingDetailApi;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.consignmentReturn.ConsignmentReturnApi;
import com.miyu.module.ppm.api.contracrefund.ContractRefundApi;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.InvoiceApi;
import com.miyu.module.ppm.api.contract.PaymentApi;
import com.miyu.module.ppm.api.purchaseConsignment.PurchaseConsignmentApi;
import com.miyu.module.pdm.api.feasibilityDetail.FeasibilityDetailApi;
import com.miyu.module.ppm.api.purchaseRequirement.RequirementApi;
import com.miyu.module.ppm.api.shipping.ShippingApi;
import com.miyu.module.ppm.api.shippingreturn.ShippingReturnApi;
import com.miyu.module.qms.api.unqualifiedmaterial.UnqualifiedMaterialApi;
import com.miyu.cloud.mcs.api.McsDistributionApplicationApi;
import com.miyu.cloud.dms.api.maintainapplication.MaintainApplicationApi;
import com.miyu.cloud.mcs.api.McsDistributionApplicationApi;
import com.miyu.cloud.dms.api.maintainapplication.MaintainApplicationApi;
import com.miyu.module.qms.enums.InspectionSheetStatusEnum;
import com.miyu.module.tms.api.ToolConfigApi;
import com.miyu.module.wms.api.mateiral.MaterialConfigApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {
        RoleApi.class,
        DeptApi.class,
        PostApi.class,
        AdminUserApi.class,
        SmsSendApi.class,
        DictDataApi.class,
        ShippingApi.class,
        ContractApi.class,
        ShippingReturnApi.class,
        PurchaseConsignmentApi.class,
        ContractRefundApi.class,
        PaymentApi.class,
        InvoiceApi.class,
        CompanyApi.class,
        ConsignmentReturnApi.class,
        FeasibilityDetailApi.class,
        ProcessChangeApi.class,
        ProcessSupplementApi.class,
        ToolingApplyApi.class,
        ToolingDetailApi.class,
        RequirementApi.class,
        UnqualifiedMaterialApi.class,
        ProcessPlanApi.class,
        ProcessPlanDetailApi.class,
        McsDistributionApplicationApi.class,
        MaintainApplicationApi.class,
        PmsApi.class,
        AssessmentApi.class,
        MaterialConfigApi.class,
        MaterialMCCApi.class,
        ToolConfigApi.class,
})
public class RpcConfiguration {
}
