package com.miyu.module.ppm.service.contractinvoice;

import java.util.*;
import javax.validation.*;

import com.miyu.module.ppm.controller.admin.contractinvoice.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractinvoice.ContractInvoiceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.ppm.dal.dataobject.contractinvoice.ContractInvoiceDetailDO;

/**
 * 购销合同发票 Service 接口
 *
 * @author Zhangyunfei
 */
public interface ContractInvoiceService {

    /**
     * 创建购销合同发票
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createContractInvoice(@Valid ContractInvoiceSaveReqVO createReqVO);

    /**
     * 更新购销合同发票
     *
     * @param updateReqVO 更新信息
     */
    void updateContractInvoice(@Valid ContractInvoiceSaveReqVO updateReqVO);

    /**
     * 删除购销合同发票
     *
     * @param id 编号
     */
    void deleteContractInvoice(String id);

    /**
     * 获得购销合同发票
     *
     * @param id 编号
     * @return 购销合同发票
     */
    ContractInvoiceDO getContractInvoice(String id);

    /**
     * 获得购销合同发票分页
     *
     * @param pageReqVO 分页查询
     * @return 购销合同发票分页
     */
    PageResult<ContractInvoiceDO> getContractInvoicePage(ContractInvoicePageReqVO pageReqVO);

    // ==================== 子表（购销合同发票表详细） ====================

    /**
     * 获得购销合同发票表详细列表
     *
     * @param invoiceId 合同发票ID
     * @return 购销合同发票表详细列表
     */
    List<ContractInvoiceDetailDO> getContractInvoiceDetailListByInvoiceId(String invoiceId);

    /**
     * 合同主键查询发票集合
     * @param contractId
     * @return
     */
    List<ContractInvoiceDO> getContractInvoiceByContractId(String contractId);

    /**
     * 合同发票审批
     * @param id
     * @param processKey
     * @param loginUserId
     */
    void submitContractInvoice(String id, String processKey, Long loginUserId);

    /**
     * 创建并提交合同发票审批
     * @param createReqVO
     * @return
     */
    void createAndSubmitContractInvoice(ContractInvoiceSaveReqVO createReqVO);

    /**
     * 合同审批更新审批状态
     * @param bussinessKey
     * @param status
     */
    void updateContractInvoiceAuditStatus(String bussinessKey, Integer status);
}