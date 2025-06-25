package com.miyu.module.ppm.service.consignmentrefund;

import java.util.*;
import javax.validation.*;

import com.miyu.module.ppm.controller.admin.consignmentrefund.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentrefund.ConsignmentRefundDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 采购退款单 Service 接口
 *
 * @author 芋道源码
 */
public interface ConsignmentRefundService {

    /**
     * 创建采购退款单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createConsignmentRefund(@Valid ConsignmentRefundSaveReqVO createReqVO);

    /**
     * 创建采购退款单并提交审核
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createConsignmentRefundAndSubmit(@Valid ConsignmentRefundSaveReqVO createReqVO);

    /**
     * 更新采购退款单
     *
     * @param updateReqVO 更新信息
     */
    void updateConsignmentRefund(@Valid ConsignmentRefundSaveReqVO updateReqVO);

    /**
     * 更新采购退款单并提交审核
     *
     * @param updateReqVO 更新信息
     */
    void updateConsignmentRefundAndSubmit(@Valid ConsignmentRefundSaveReqVO updateReqVO);

    /***
     * 提交退款审批
     * (同时更新退款状态为审批中)
     * @param id
     * @param userId
     */
    void submitConsignmentRefund(String id, Long userId);

    /***
     * 退款完成确认
     * @param id
     * @param refundStatus
     */
    void updateConsignmentRefundStatus(String id,Integer refundStatus);


    /**
     * 删除采购退款单
     *
     * @param id 编号
     */
    void deleteConsignmentRefund(String id);

    /**
     * 获得采购退款单
     *
     * @param id 编号
     * @return 采购退款单
     */
    ConsignmentRefundDO getConsignmentRefund(String id);

    /**
     * 获得采购退款单分页
     *
     * @param pageReqVO 分页查询
     * @return 采购退款单分页
     */
    PageResult<ConsignmentRefundDO> getConsignmentRefundPage(ConsignmentRefundPageReqVO pageReqVO);

    /**
     * 合同主键获取退款集合
     * @param id
     * @return
     */
    List<ConsignmentRefundDO> getConsignmentRefundListByContractId(String id);
}