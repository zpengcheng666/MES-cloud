package com.miyu.module.ppm.service.contractrefund;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.contractrefund.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractrefund.ContractRefundDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 合同退款 Service 接口
 *
 * @author 芋道源码
 */
public interface ContractRefundService {

    /**
     * 创建合同退款
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createContractRefund(@Valid ContractRefundSaveReqVO createReqVO);

    /**
     * 创建合同退款并提交审批
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createContractRefundAndSubmit(@Valid ContractRefundSaveReqVO createReqVO);

    /**
     * 更新合同退款
     *
     * @param updateReqVO 更新信息
     */
    void updateContractRefund(@Valid ContractRefundSaveReqVO updateReqVO);

    /**
     * 更新合同退款并提交审批
     *
     * @param updateReqVO 更新信息
     */
    void updateContractRefundAndSubmit(@Valid ContractRefundSaveReqVO updateReqVO);

    /***
     * 提交退款审批
     * (同时更新退款状态为审批中)
     * @param id
     * @param userId
     */
    void submitContractRefund(String id, Long userId);

    /***
     * 更新退款状态
     * @param id
     * @param refundStatus
     */
    void updateContractRefundStatus(String id,Integer refundStatus);

    /**
     * 删除合同退款
     *
     * @param id 编号
     */
    void deleteContractRefund(String id);

    /**
     * 获得合同退款
     *
     * @param id 编号
     * @return 合同退款
     */
    ContractRefundDO getContractRefund(String id);

    /**
     * 获得合同退款分页
     *
     * @param pageReqVO 分页查询
     * @return 合同退款分页
     */
    PageResult<ContractRefundDO> getContractRefundPage(ContractRefundPageReqVO pageReqVO);


    /***
     * 更新审批状态
     * @param id
     * @param status
     */
    void updateRefundProcessInstanceStatus(String id,Integer status);

   List<ContractRefundDO> getContractRefundByShippingReturn(List<String> shippingReturnIds);


    List<ContractRefundDO> getContractRefundByContractId(String contractId);
}