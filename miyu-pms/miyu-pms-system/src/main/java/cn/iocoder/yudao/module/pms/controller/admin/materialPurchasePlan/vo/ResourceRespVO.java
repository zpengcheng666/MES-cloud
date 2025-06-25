package cn.iocoder.yudao.module.pms.controller.admin.materialPurchasePlan.vo;

import cn.iocoder.yudao.module.pms.dal.dataobject.plan.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 物料采购计划-资源列表(设备,刀具,工装)")
@Data
public class ResourceRespVO {
    //设备
    private List<PlanDeviceDO> deviceDOList;
    //TODO 刀具
    private List<PlanCombinationDO> combinationDOList;
    //工装
    private List<PlanMaterialDO> materialDOList;

    //采购设备
    private List<PlanDemandDeviceDO> demandDeviceDOList;
    //采购刀具
    private List<PlanDemandCutterDO> demandCutterDOList;
    //采购刀柄
    private List<PlanDemandHiltDO> demandHiltDOList;
    //采购工装
    private List<PlanDemandMaterialDO> demandMaterialDOList;
    //物料采购
    private List<PlanPurchaseMaterialDO> purchaseMaterialDOList;
}
