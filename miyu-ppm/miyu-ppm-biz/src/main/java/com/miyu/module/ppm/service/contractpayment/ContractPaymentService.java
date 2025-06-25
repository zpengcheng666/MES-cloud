package com.miyu.module.ppm.service.contractpayment;

import java.util.*;
import javax.validation.*;

import com.miyu.module.ppm.api.contract.dto.ContractPaymentDTO;
import com.miyu.module.ppm.controller.admin.contractpayment.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDetailDO;
import com.miyu.module.ppm.dal.dataobject.contractpaymentscheme.ContractPaymentSchemeDO;

/**
 * 合同付款 Service 接口
 *
 * @author Zhangyunfei
 */
public interface ContractPaymentService {

    /**
     * 创建合同付款
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createContractPayment(@Valid ContractPaymentSaveReqVO createReqVO);

    /**
     * 更新合同付款
     *
     * @param updateReqVO 更新信息
     */
    void updateContractPayment(@Valid ContractPaymentSaveReqVO updateReqVO);

    /**
     * 删除合同付款
     *
     * @param id 编号
     */
    void deleteContractPayment(String id);

    /**
     * 获得合同付款
     *
     * @param id 编号
     * @return 合同付款
     */
    ContractPaymentDO getContractPayment(String id);

    /**
     * 获得合同付款分页
     *
     * @param pageReqVO 分页查询
     * @return 合同付款分页
     */
    PageResult<ContractPaymentDO> getContractPaymentPage(ContractPaymentPageReqVO pageReqVO);

    /**
     * 合同主键获取合同付款计划集合
     * @param reqVO
     * @return
     */
    List<ContractPaymentSchemeDO> getContractPaymentSchemeListByContractId(ContractPaymentReqVO reqVO);

    /**
     * 获取付款记录
     * @param id
     * @return
     */
    ContractPaymentDO getContractPaymentById(String id);

    /**
     * 提交合同付款审批
     * @param id
     * @param processKey
     * @param loginUserId
     */
    void submitContractPayment(String id, String processKey, Long loginUserId);

    /**
     * 合同审批更新状态
     * @param bussinessKey
     * @param status
     */
    void updateContractPaymentAuditStatus(String bussinessKey, Integer status);

    /**
     * 创建并提交合同付款审批
     * @param createReqVO
     * @return
     */
    void createAndSubmitContractPayment(ContractPaymentSaveReqVO createReqVO);

    /**
     * 通过合同id查询付款及明细
     * @param ids
     * @return
     */
    List<ContractPaymentDTO> getContractPaymentByContractIds(Collection<String> ids);

    /**
     * 合同发票获取支付单据详细
     * @param reqVO
     * @return
     */
    List<ContractPaymentDO> getList4InvoiceByPaymentId(ContractPaymentReqVO reqVO);

    // ==================== 子表（合同付款详细） ====================

    /**
     * 获得合同付款详细列表
     *
     * @param paymentId 合同ID
     * @return 合同付款详细列表
     */
    List<ContractPaymentDetailDO> getPaymentListById(String paymentId);

    /**
     * 合同主键获取支付信息集合
     * @param id
     * @return
     */
    List<ContractPaymentDTO> getContractPaymentByContractId(String id);
}
