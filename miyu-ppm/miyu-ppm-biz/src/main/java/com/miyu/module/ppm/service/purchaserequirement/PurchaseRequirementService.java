package com.miyu.module.ppm.service.purchaserequirement;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.purchaserequirement.vo.*;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDetailDO;

/**
 * 采购申请主 Service 接口
 *
 * @author Zhangyunfei
 */
public interface PurchaseRequirementService {

    /**
     * 创建采购申请主
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createPurchaseRequirement(@Valid PurchaseRequirementSaveReqVO createReqVO);

    /**
     * 更新采购申请主
     *
     * @param updateReqVO 更新信息
     */
    void updatePurchaseRequirement(@Valid PurchaseRequirementSaveReqVO updateReqVO);

    /**
     * 删除采购申请主
     *
     * @param id 编号
     */
    void deletePurchaseRequirement(String id);

    /**
     * 获得采购申请主
     *
     * @param id 编号
     * @return 采购申请主
     */
    PurchaseRequirementDO getPurchaseRequirement(String id);

    /**
     * 获得采购申请主分页
     *
     * @param pageReqVO 分页查询
     * @return 采购申请主分页
     */
    PageResult<PurchaseRequirementDO> getPurchaseRequirementPage(PurchaseRequirementPageReqVO pageReqVO);
    PageResult<PurchaseRequirementDetailDO> getPurchaseRequirementDetailPage(PurchaseRequirementPageReqVO pageReqVO);

    /**
     * 获取采购申请详细
     * @param id
     * @return
     */
    List<PurchaseRequirementDetailDO> getPurchaseRequirementDetailListByRequirementId(String id);

    /**
     * 获取采购申请详细
     * @param reqVO
     * @return
     */
    List<PurchaseRequirementDetailDO> getPurchaseRequirementDetailList(PurchaseRequirementDetailReqVO reqVO);

    /**
     * 提交采购申请审批
     * @param id
     * @param loginUserId
     */
    void submitRequirement(String id, Long loginUserId);

    /**
     * 创建并提交采购审批
     * @param createReqVO
     */
    void createAndSubmitPurchaseRequirement(PurchaseRequirementSaveReqVO createReqVO);

    /**
     * 更新采购审批状态
     * @param bussinessKey
     * @param status
     */
    void updateRequirementAuditStatus(String bussinessKey, Integer status);

    /**
     * PMS 提交采购申请
     * @param bean
     */
    String createPurchaseRequirementPMS(PurchaseRequirementSaveReqVO bean);
}
