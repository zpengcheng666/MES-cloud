package cn.iocoder.yudao.module.pms.controller.admin.materialPurchasePlan.vo;

import cn.iocoder.yudao.module.pms.dal.dataobject.plan.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "发起采购需要的参数")
@Data
public class StartPurchaseReqVO {

    private Long userId;
    //申请人
    private String applicant;
    //申请部门
    private String applicationDepartment;
    //申请时间
    private LocalDateTime applicationDate;

    private Integer type;

    //采购设备(已合并)
    private List<PlanDemandDeviceDO> demandDeviceDOList;
    //采购刀具
    private List<PlanDemandCutterDO> demandCutterDOList;
    //采购刀柄
    private List<PlanDemandHiltDO> demandHiltDOList;
    //采购工装(已合并)
    private List<PlanDemandMaterialDO> demandMaterialDOList;
    //物料采购
    private List<PlanPurchaseMaterialDO> purchaseMaterialDOList;
    //采购刀具
    private List<PlanCombinationDO> combinationDOList;
}
