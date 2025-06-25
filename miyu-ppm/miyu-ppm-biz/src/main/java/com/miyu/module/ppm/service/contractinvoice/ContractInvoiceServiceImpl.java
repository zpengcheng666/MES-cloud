package com.miyu.module.ppm.service.contractinvoice;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.contractinvoice.ContractInvoiceDetailDO;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDO;
import com.miyu.module.ppm.dal.mysql.contract.ContractOrderMapper;
import com.miyu.module.ppm.dal.mysql.contractinvoice.ContractInvoiceDetailMapper;
import com.miyu.module.ppm.dal.mysql.contractpayment.ContractPaymentMapper;
import com.miyu.module.ppm.enums.common.InvoiceAuditStatusEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.contractinvoice.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractinvoice.ContractInvoiceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.contractinvoice.ContractInvoiceMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 购销合同发票 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class ContractInvoiceServiceImpl implements ContractInvoiceService {

    @Resource
    private ContractOrderMapper contractOrderMapper;

    @Resource
    private ContractInvoiceMapper contractInvoiceMapper;

    @Resource
    private ContractInvoiceDetailMapper contractInvoiceDetailMapper;

    @Resource
    private ContractPaymentMapper contractPaymentMapper;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createContractInvoice(ContractInvoiceSaveReqVO createReqVO) {
        // 验证实际开具金额不能大于付款剩余待支付金额
        validateContractInvoiceAmount(createReqVO);
        // 插入
        ContractInvoiceDO contractInvoice = BeanUtils.toBean(createReqVO, ContractInvoiceDO.class).setStatus(InvoiceAuditStatusEnum.DRAFT.getStatus());;
        contractInvoiceMapper.insert(contractInvoice);

        // 转化订单详细列表
        List<ContractInvoiceDetailDO> invoiceDetailList = convertList(createReqVO.getInvoiceDetails(), o -> BeanUtils.toBean(o, ContractInvoiceDetailDO.class));
        // 插入子表
        createContractInvoiceDetailList(contractInvoice.getId(), invoiceDetailList);
        // 返回
        return contractInvoice.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContractInvoice(ContractInvoiceSaveReqVO updateReqVO) {
        // 校验存在
        validateContractInvoiceExists(updateReqVO.getId());
        // 验证实际开具金额不能大于付款剩余待支付金额
        validateContractInvoiceAmount(updateReqVO);
        // 更新
        ContractInvoiceDO updateObj = BeanUtils.toBean(updateReqVO, ContractInvoiceDO.class);
        contractInvoiceMapper.updateById(updateObj);

        // 更新子表
        List<ContractInvoiceDetailDO> detailList = convertList(updateReqVO.getInvoiceDetails(), o -> BeanUtils.toBean(o, ContractInvoiceDetailDO.class));
        updateContractInvoiceDetailList(updateReqVO.getId(), detailList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContractInvoice(String id) {
        // 校验存在
        validateContractInvoiceExists(id);
        // 删除
        contractInvoiceMapper.deleteById(id);

        // 删除子表
        deleteContractInvoiceDetailByInvoiceId(id);
    }

    private ContractInvoiceDO validateContractInvoiceExists(String id) {
        ContractInvoiceDO invoice = contractInvoiceMapper.selectById(id);
        if (invoice == null) {
            throw exception(CONTRACT_INVOICE_NOT_EXISTS);
        }
        return invoice;
    }

    /**
     * 验证付款详细对应付款计划金额
     *
     * @param reqVO
     */
    private void validateContractInvoiceAmount(ContractInvoiceSaveReqVO reqVO) {
        /**
         * 验证合同金额
         */
        // 获取合同订单集合
        List<ContractOrderDO> orderList = contractOrderMapper.selectList(ContractOrderDO::getContractId, reqVO.getContractId());
        // 计算订单金额总和
        BigDecimal contractAmount = getSumValue(orderList,
                order -> order.getTaxPrice() != null ? order.getTaxPrice().multiply(order.getQuantity()) : BigDecimal.ZERO,
                BigDecimal::add);

        // 获取合同已开发票金额总和
        List<ContractInvoiceDO> invoiceList  = contractInvoiceMapper.selectList(ContractInvoiceDO::getContractId, reqVO.getContractId());
        // 去掉当前发票记录
        invoiceList = invoiceList.stream().filter(o -> !o.getId().equals(reqVO.getId())).collect(Collectors.toList());
        BigDecimal invoiceAmount = getSumValue(invoiceList,
                invoice -> invoice.getAmount() != null ? invoice.getAmount() : BigDecimal.ZERO,
                BigDecimal::add);
        // 未填写过发票计算金额总和为null
        invoiceAmount = invoiceAmount == null ? BigDecimal.ZERO : invoiceAmount;
        // 加上当前记录金额
        invoiceAmount = invoiceAmount.add(reqVO.getAmount());
        // 已开发票金额总和大于合同总金额
        if(invoiceAmount.compareTo(contractAmount) > 0){
            throw exception(CONTRACT_INVOICE_SUM_AMOUNT_ERROR);
        }

        // 发票详细非必填，没填写的情况下不去校验金额
        if (reqVO.getInvoiceDetails().size() == 0) {
            return;
        }

        // 开具金额详细总和
        BigDecimal sumInvoiceAmount = getSumValue(reqVO.getInvoiceDetails(),
                detail -> detail.getAmount() != null ? detail.getAmount() : BigDecimal.ZERO,
                BigDecimal::add);

        // 验证实际开具金额总和
        if (reqVO.getAmount().compareTo(sumInvoiceAmount) != 0) {
            throw exception(CONTRACT_INVOICE_AMOUNT_ERROR);
        }

        // 付款计划集合
        Collection<String> ids = convertSet(reqVO.getInvoiceDetails(), obj -> obj.getPaymentId());
        // 获取付款明细
        List<ContractInvoiceDetailDO> detailList = contractInvoiceDetailMapper.selectInvoiceAmountListBySchemeId(ids, reqVO.getId());
        // list转map
        Map<String, ContractInvoiceDetailDO> detailMap = detailList.stream()
                .collect(Collectors.toMap(obj -> obj.getPaymentId(), obj -> obj));
        // 获取付款集合
        List<ContractPaymentDO> paymentList = contractPaymentMapper.selectListByIds(ids);

        Map<String, ContractPaymentDO> paymentMap = paymentList.stream()
                .collect(Collectors.toMap(obj -> obj.getId(), obj -> obj));
        // 遍历支付明细 验证当前支付明细对应的付款计划金额
        for (ContractInvoiceSaveReqVO.InvoiceDetail detail : reqVO.getInvoiceDetails()) {
            // 计划支付金额
            BigDecimal payAmount = paymentMap.get(detail.getPaymentId()) != null ? paymentMap.get(detail.getPaymentId()).getAmount() : BigDecimal.ZERO;
            // 已支付金额
            BigDecimal paidAmount = detailMap.get(detail.getPaymentId()) != null ? detailMap.get(detail.getPaymentId()).getAmount() : BigDecimal.ZERO;
            // 当前支付金额大于当前付款计划金额
            if (detail.getAmount().compareTo(payAmount.subtract(paidAmount)) > 0) {
                throw exception(CONTRACT_INVOICE_DETAIL_AMOUNT_ERROR, paymentMap.get(detail.getPaymentId()).getNumber());
            }
        }
    }


    @Override
    public ContractInvoiceDO getContractInvoice(String id) {
        return contractInvoiceMapper.getContractInvoiceById(id);
    }

    @Override
    public PageResult<ContractInvoiceDO> getContractInvoicePage(ContractInvoicePageReqVO pageReqVO) {
        return contractInvoiceMapper.selectPage(pageReqVO);
    }


    /**
     * 合同主键查询发票集合
     * @param contractId
     * @return
     */
    @Override
    public List<ContractInvoiceDO> getContractInvoiceByContractId(String contractId) {
        return contractInvoiceMapper.selectList(ContractInvoiceDO::getContractId, contractId);
    }

    /**
     * 提交合同审批
     * @param id
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitContractInvoice(String id, String processKey, Long userId) {
        // 1. 校验合同是否在审批
        ContractInvoiceDO invoice = validateContractInvoiceExists(id);
        if (ObjUtil.notEqual(invoice.getStatus(), InvoiceAuditStatusEnum.DRAFT.getStatus())) {
            throw exception(CONTRACT_INVOICE_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 2. 创建合同审批流程实例
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(processKey).setBusinessKey(String.valueOf(id)).setVariables(new HashMap<>())).getCheckedData();

        // 更新合同工作流编号
        contractInvoiceMapper.updateById(new ContractInvoiceDO().setId(id).setProcessInstanceId(processInstanceId)
                .setStatus(InvoiceAuditStatusEnum.PROCESS.getStatus()));
    }

    /**
     * 创建并提交合同审批
     * @param createReqVO
     * @return
     */
    @Override
    @Transactional
    public void createAndSubmitContractInvoice(ContractInvoiceSaveReqVO createReqVO) {
        String id = createContractInvoice(createReqVO);
        submitContractInvoice(id, createReqVO.getProcessKey(), getLoginUserId());
    }

    /**
     * 审批更新发票审批状态
     * @param bussinessKey
     * @param status
     */
    @Override
    public void updateContractInvoiceAuditStatus(String bussinessKey, Integer status) {
        // 校验合同是否存在
        ContractInvoiceDO invoice = validateContractInvoiceExists(bussinessKey);
        // 只有审批中，可以更新审批结果
        if (ObjUtil.notEqual(invoice.getStatus(), InvoiceAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(CONTRACT_INVOICE_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }

        ContractInvoiceDO invoiceDO = new ContractInvoiceDO().setId(bussinessKey).setStatus(status);
        // 更新合同审批结果
        contractInvoiceMapper.updateById(invoiceDO);
    }

    // ==================== 子表（购销合同发票表详细） ====================

    @Override
    public List<ContractInvoiceDetailDO> getContractInvoiceDetailListByInvoiceId(String invoiceId) {
        return contractInvoiceDetailMapper.selectListByInvoiceId(invoiceId);
    }

    private void createContractInvoiceDetailList(String invoiceId, List<ContractInvoiceDetailDO> list) {
        list.forEach(o -> o.setInvoiceId(invoiceId));
        contractInvoiceDetailMapper.insertBatch(list);
    }

    private void updateContractInvoiceDetailList(String invoiceId, List<ContractInvoiceDetailDO> list) {
        deleteContractInvoiceDetailByInvoiceId(invoiceId);
        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createContractInvoiceDetailList(invoiceId, list);
    }

    private void deleteContractInvoiceDetailByInvoiceId(String invoiceId) {
        contractInvoiceDetailMapper.deleteByInvoiceId(invoiceId);
    }

}