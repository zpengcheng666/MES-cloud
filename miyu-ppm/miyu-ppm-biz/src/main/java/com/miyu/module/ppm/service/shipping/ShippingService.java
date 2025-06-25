package com.miyu.module.ppm.service.shipping;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;

import com.miyu.module.ppm.api.shipping.dto.ShippingDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailRetraceDTO;
import com.miyu.module.ppm.controller.admin.home.vo.ContractAnalysisResp;
import com.miyu.module.ppm.controller.admin.shipping.vo.*;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 销售发货 Service 接口
 *
 * @author 芋道源码
 */
public interface ShippingService {

    /**
     * 创建销售发货
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createShipping(@Valid ShippingSaveReqVO createReqVO);

    /**
     * 创建销售发货并提交审批
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createShippingAndSubmit(@Valid ShippingSaveReqVO createReqVO);

    /**
     * 更新销售发货
     *
     * @param updateReqVO 更新信息
     */
    void updateShipping(@Valid ShippingSaveReqVO updateReqVO);

    /***
     * 通知WMS出库
     * @param updateReqVO
     */
    void outBoundShipping(@Valid ShippingSaveReqVO updateReqVO);
    Boolean outBoundShipping(ShippingDO shippingDO,List<ShippingDetailDO> detailDOS);

    /***
     * 更新销售发货并提交审核
     * @param updateReqVO
     */
    void updateShippingSubmit(@Valid ShippingSaveReqVO updateReqVO);

    /**
     * 删除销售发货
     *
     * @param id 编号
     */
    void deleteShipping(String id);

    /***
     * 发货单作废
     * @param id
     */
    void cancelShipping(String id);
    void confirmOut(String id);

    /**
     * 获得销售发货
     *
     * @param id 编号
     * @return 销售发货
     */
    ShippingDO getShipping(String id);

    /**
     * 获得销售发货分页
     *
     * @param pageReqVO 分页查询
     * @return 销售发货分页
     */
    PageResult<ShippingDO> getShippingPage(ShippingPageReqVO pageReqVO);

    /**
     * 通过合同id查询销售发货单及明细
     * @param ids
     * @return
     */
    List<ShippingDTO> getShippingListByContractIds(Collection<String> ids);


    List<ShippingDetailRetraceDTO> getShippingListByBarcode(String barCode);

    List<ShippingDTO> getShippingListByProjectIds(Collection<String> ids);

    List<ShippingDetailDTO> getShippingDetailListByProjectIds(Collection<String> ids);

    // ==================== 子表（销售发货明细） ====================

    /**
     * 获得销售发货明细列表
     *
     * @param shippingId 发货单ID
     * @return 销售发货明细列表
     */
    List<ShippingDetailDO> getShippingDetailListByShippingId(String shippingId);

    List<ShippingDetailDTO> getShippingByConsignmentDetailIds(Collection<String> ids);


    /***
     * 提交发货审批
     * (同时更新发货状态为审批中)
     * @param id
     * @param userId
     */
    void submitShipping(String id, Long userId);

    /***
     * 更新审批状态
     * @param id
     * @param status
     */
    void updateShippingProcessInstanceStatus(String id,Integer status);

    /***
     * 更新发货状态
     * @param id   详情ID
     * @param shippingStatus
     */
    void updateShippingStatus(String id,Integer shippingStatus);


    /***
     * 查询合同下所有有效的订单发货数
     * @param contractId
     * @return  key  订单ID  value  订单数量
     */
    Map<String, BigDecimal> getOrderMap(String contractId,List<String> detailIds,List<Integer> status);

    /***
     * 根据状态获取发货单
     * @param status
     * @return
     */
    List<ShippingDO> getShippingList(List<Integer> status);

    /***
     * 根据合同获取发货单
     * @param contractId
     * @return
     */
    List<ShippingDO> getShippingByContract(String contractId);


    ShippingDO getShippingByNo(String shippingNo);


    /**
     * 监控出库信息  更新出库单状态
     */
    void checkOutBoundInfo();


    List<ShippingDetailDO> getShippingDetailListByProjectId(String projectId,String contractId,Integer shippingType);


    List<ShippingDO> getShippings(List<String> ids);


    void updateShippingStatus(ShippingDO shippingDO);

    List<ContractAnalysisResp> getContractAnalysis(Integer type);
}
