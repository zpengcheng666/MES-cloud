package com.miyu.module.ppm.service.purchaseconsignment;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;

import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDetailDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingDTO;
import com.miyu.module.ppm.controller.admin.consignmentinfo.vo.ConsignmentInfoQueryReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 采购收货 Service 接口
 *
 * @author 芋道源码
 */
public interface PurchaseConsignmentService {

    /**
     * 创建采购收货
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    @Transactional(rollbackFor = Exception.class)
    String createPurchaseConsignmentAndSubmit(Long userId, PurchaseConsignmentSaveReqVO createReqVO);

    /**
     * 创建采购收货
     * @param userId
     * @param createReqVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    String createPurchaseConsignment(Long userId, PurchaseConsignmentSaveReqVO createReqVO);
    /**
     * 更新采购收货
     *
     * @param updateReqVO 更新信息
     */
    void updatePurchaseConsignment(@Valid PurchaseConsignmentSaveReqVO updateReqVO);


    /***
     * 更新采购收货并提交审核
     * @param updateReqVO
     */
    void updatePurchaseConsignmentSubmit(@Valid PurchaseConsignmentSaveReqVO updateReqVO);

    /**
     * 删除采购收货
     *
     * @param id 编号
     */
    void deletePurchaseConsignment(String id);

    /***
     * 发货单作废
     * @param id
     */
    void cancelPurchaseConsignment(String id);


    /**
     * 获得采购收货
     *
     * @param id 编号
     * @return 采购收货
     */
    ConsignmentDO getPurchaseConsignment(String id);

    /**
     * 获得采购收货分页
     *
     * @param pageReqVO 分页查询
     * @return 采购收货分页
     */
    PageResult<ConsignmentDO> getPurchaseConsignmentPage(PurchaseConsignmentPageReqVO pageReqVO);

    /**
     * 通过合同id查询签收明细
     * @param ids
     */
    public List<PurchaseConsignmentDTO> getConsignmentDetailByContractIds(Collection<String> ids);


    /**
     * 通过合同id查询收货单明细
     * @param id
     * @return
     */
    List<PurchaseConsignmentDTO> getConsignmentDetailByContractId(String id);

    List<PurchaseConsignmentDTO> getConsignmentListByProjectIds(Collection<String> ids);

    // ==================== 子表（收货明细） ====================

    /**
     * 获得收货明细列表
     *
     * @param consignmentId 收货单ID
     * @return 收货明细列表
     */
    List<ConsignmentDetailDO> getPurchaseConsignmentDetailListByConsignmentId(String consignmentId);

    List<ConsignmentDetailDO> getConsignmentDetailListByProjectIds(Collection<String> ids);

    List<ConsignmentDetailDO> getPurchaseDetailListByContractOrderIds(Collection<String> ids);

    List<PurchaseConsignmentDetailDTO> getPurchaseDetailListByShippingIds(Collection<String> ids);

    List<ConsignmentInfoDO> getPurchaseConsignmentInfoListByConsignmentId(String consignmentId);

    /***
     * 提交采购审批
     * (同时更新发货状态为审批中)
     * @param id
     * @param userId
     */
    void submitPurchaseConsignment(String id, Long userId);

    /**
     *  更新审批状态
     * @param id    编号
     * @param status 审批结果
     */
    void updatePurchaseConsignmentStatus(String id, Integer status);

    /***
     * 确认收货
     * @param id
     * @param consignmentStatus
     */
    void updateConsignmentStatus(String id,Integer consignmentStatus);

    /***
     * 查询合同下所有有效的订单发货数
     * @param contractId
     * @return  key  订单ID  value  订单数量
     */
    Map<String, BigDecimal> getOrderMap(String contractId, List<String> detailIds,List<Integer> status);

    /**
     * 查询收货单
     */
    List<ConsignmentDO> getConsignmentByContract(String id);

    /**
     * 根据consigment_no查询收货单信息
     */
    ConsignmentDO queryConsignmentByNo(String consignmentNo);

    /***
     * 提交质检单
     * @param updateReqVO
     */
    void updatePurchaseConsignmentQms(@Valid PurchaseConsignmentSaveReqVO updateReqVO);

    /***
     * 检查 质检状态  并更新入库以及收货单
     */
    void checkSchemeSheetResult();

    /****
     * 调用QMS质检
     */
    void checkSchemeSheet();
    void checkInBoundInfo();
    void checkInBoundInfo1() throws InterruptedException;


    public List<ConsignmentDO> getConsignmentDetailByIds(Collection<String> ids);


    void  updateConsignmentStatus(ConsignmentDO consignmentDO);



    List<ConsignmentDO> getConsignments(ConsignmentInfoQueryReqVO reqVO);
}
