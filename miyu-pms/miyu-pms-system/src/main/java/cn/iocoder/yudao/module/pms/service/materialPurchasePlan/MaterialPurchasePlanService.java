package cn.iocoder.yudao.module.pms.service.materialPurchasePlan;

import cn.iocoder.yudao.module.pms.dal.dataobject.plan.*;

import java.util.List;

public interface MaterialPurchasePlanService {

    /**
     * 发起设备采购
     * @param type
     * @param userId
     * @param list
     */
    public boolean startDevicePurchase(Integer type,Long userId, List<PlanDemandDeviceDO> list);

    /**
     * 发起工装采购(工装和物料名一样,得区分一下)
     * @param type
     * @param userId
     * @param list
     */
    public boolean startMaterialPurchase(Integer type,Long userId, List<PlanDemandMaterialDO> list);

    /**
     * 发起刀具采购(完整的)
     * @param type
     * @param userId
     * @param list
     */
    public boolean startCombinationPurchase(Integer type,Long userId, List<PlanCombinationDO> list);

    /**
     * 刀具采购(分)
     * @param type
     * @param userId
     * @param list
     * @return
     */
    public boolean startCutterPurchase(Integer type,Long userId, List<PlanDemandCutterDO> list);

    /**
     * 刀柄采购(分)
     * @param type
     * @param userId
     * @param list
     * @return
     */
    public boolean startHiltPurchase(Integer type,Long userId, List<PlanDemandHiltDO> list);

    /**
     * 物料采购(加工用的毛坯那种物料)
     * @param type
     * @param userId
     * @param list
     * @return
     */
    public boolean startMiterialConfigPurchase(Integer type,Long userId, List<PlanPurchaseMaterialDO> list);
}
