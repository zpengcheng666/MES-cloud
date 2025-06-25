package com.miyu.module.ppm.service.contract;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.contract.vo.*;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderProductDO;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDO;
import com.miyu.module.ppm.dal.dataobject.contractpaymentscheme.ContractPaymentSchemeDO;

/**
 * 购销合同 Service 接口
 *
 * @author Zhangyunfei
 */
public interface ContractService {

    /**
     * 创建购销合同
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createContract(@Valid ContractSaveReqVO createReqVO);

    /**
     * 更新购销合同
     *
     * @param updateReqVO 更新信息
     */
    void updateContract(@Valid ContractSaveReqVO updateReqVO);

    /**
     * 删除购销合同
     *
     * @param id 编号
     */
    void deleteContract(String id);

    /**
     * 获得购销合同
     *
     * @param id 编号
     * @return 购销合同
     */
    ContractDO getContract(String id);

    /**
     * 获得购销合同分页
     *
     * @param pageReqVO 分页查询
     * @return 购销合同分页
     */
    PageResult<ContractDO> getContractPage(ContractPageReqVO pageReqVO);


    /**
     * 更新合同状态
     * @param updateReqVO
     */
    void updateContractStatus(ContractUpdateReqVO updateReqVO);

    // ==================== 子表（合同订单） ====================

    /**
     * 获得合同订单列表
     *
     * @param contractId 合同ID
     * @return 合同订单列表
     */
    List<ContractOrderDO> getContractOrderListByContractId(String contractId);
    List<ContractOrderDO> getContractOrderListByContractIds(List<String> contractIds);

    /**
     * 根据类型获取合同集合
     * @param types
     * @return
     */
    List<ContractDO> getContractListByType(Collection<String> types);

    /**
     * 根据合同主键获取合同集合
     * @param ids
     * @return
     */
    List<ContractDO> getContractListByIds(Collection<String> ids);


    /**
     * 获取产品历史价格
     * @param productIds
     */
    Map<String, ContractOrderProductDO> getContractProductPriceHis(Collection<String> productIds);

    /**
     * 提交合同审批
     * @param id
     * @param processKey
     * @param loginUserId
     */
    void submitContract(String id, String processKey, Long loginUserId);

    /**
     * 合同审批
     * @param bussinessKey
     * @param status
     */
    void updateContractAuditStatus(String bussinessKey, Integer status);

    /**
     * 创建并提交合同审批
     * @param createReqVO
     * @return
     */
    void createAndSubmitContract(ContractSaveReqVO createReqVO);

    /**
     * 获得合同计划集合
     * @param contractId
     * @return
     */
    List<ContractPaymentSchemeDO> getContractPaymentSchemeListByContractId(String contractId);

    /**
     * 创建合同计划
     * @param contractId
     * @param list
     * @return
     */
    void createContractPaymentSchemeList(String contractId, List<ContractPaymentSchemeDO> list);

    /**
     * 合同主键获取合同信息
     * @param id
     * @return
     */
    ContractDO getContractById(String id);


    /**
     * 合同编号获取合同信息
     * @param reqVO
     * @return
     */
    ContractDO getContractInfoByNumber(ContractReqVO reqVO);

    /**
     * 采购申请详情ID获取采购合同集合
     * @param requirementDetailId
     * @return
     */
    List<ContractOrderDO> getContractOrderListByRequirementDetailId(String requirementDetailId);



    List<ContractPaymentDO> getContractPaymentListByContractIds(List<String> contractIds);
}
