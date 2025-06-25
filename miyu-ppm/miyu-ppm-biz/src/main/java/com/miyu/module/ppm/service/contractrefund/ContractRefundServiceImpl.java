package com.miyu.module.ppm.service.contractrefund;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.enums.contractrefund.ContractRefundEnum;
import com.miyu.module.ppm.enums.shipping.DMAuditStatusEnum;
import com.miyu.module.ppm.api.contract.PaymentApi;
import com.miyu.module.ppm.api.contract.dto.ContractPaymentDTO;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import com.miyu.module.ppm.controller.admin.contractrefund.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractrefund.ContractRefundDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.contractrefund.ContractRefundMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ApiConstants.REFUND_PROCESS_KEY;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;
import static com.miyu.module.ppm.enums.LogRecordConstants.*;

/**
 * 合同退款 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ContractRefundServiceImpl implements ContractRefundService {

    @Resource
    private ContractRefundMapper contractRefundMapper;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;
    @Resource
    private PaymentApi paymentApi;
    @Override
    public String createContractRefund(ContractRefundSaveReqVO createReqVO) {
        // 插入
        validateContractRefundPay(createReqVO);
        ContractRefundDO contractRefund = BeanUtils.toBean(createReqVO, ContractRefundDO.class);
        contractRefundMapper.insert(contractRefund);
        // 返回
        return contractRefund.getId();
    }

    @Override
    public String createContractRefundAndSubmit(@Valid ContractRefundSaveReqVO createReqVO) {
        // 插入
        validateContractRefundPay(createReqVO);
        ContractRefundDO contractRefund = BeanUtils.toBean(createReqVO, ContractRefundDO.class);
        contractRefundMapper.insert(contractRefund);


        // 2. 创建发货审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(REFUND_PROCESS_KEY).setBusinessKey(String.valueOf(contractRefund.getId())).setVariables(variables)).getCheckedData();

        // 3. 更新发货工作流编号

        contractRefund.setProcessInstanceId(processInstanceId)
                .setStatus(DMAuditStatusEnum.PROCESS.getStatus()).setRefundStatus(ContractRefundEnum.PROCESS.getStatus());

        contractRefundMapper.updateById(contractRefund);


        // 返回
        return contractRefund.getId();
    }

    @Override
    public void updateContractRefund(ContractRefundSaveReqVO updateReqVO) {
        // 校验存在
        validateContractRefundExists(updateReqVO.getId());
        validateContractRefundPay(updateReqVO);
        // 更新
        ContractRefundDO updateObj = BeanUtils.toBean(updateReqVO, ContractRefundDO.class);
        contractRefundMapper.updateById(updateObj);
    }

    @Override
    public void updateContractRefundAndSubmit(@Valid ContractRefundSaveReqVO updateReqVO) {
        // 校验存在
        validateContractRefundExists(updateReqVO.getId());
        validateContractRefundPay(updateReqVO);
        // 更新
        ContractRefundDO updateObj = BeanUtils.toBean(updateReqVO, ContractRefundDO.class);


        // 2. 创建发货审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(REFUND_PROCESS_KEY).setBusinessKey(String.valueOf(updateObj.getId())).setVariables(variables)).getCheckedData();

        // 3. 更新发货工作流编号

        updateObj.setProcessInstanceId(processInstanceId)
                .setStatus(DMAuditStatusEnum.PROCESS.getStatus()).setRefundStatus(ContractRefundEnum.PROCESS.getStatus());

        contractRefundMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = SHIPPING_REFUND_CLUE_TYPE, subType = SHIPPING_REFUND_SUBMIT_SUB_TYPE, bizNo = "{{#id}}",
            success = SHIPPING_REFUND_SUBMIT_SUCCESS)
    public void submitContractRefund(String id, Long userId) {
        // 1. 校验发货单是否在审批
        ContractRefundDO contractRefundDO = validateContractRefundExists(id);
        if (!(ObjUtil.equals(contractRefundDO.getStatus(), DMAuditStatusEnum.DRAFT.getStatus()) || ObjUtil.equals(contractRefundDO.getStatus(), DMAuditStatusEnum.REJECT.getStatus()))) {
            throw exception(SHIPPING_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 2. 创建发货审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(REFUND_PROCESS_KEY).setBusinessKey(String.valueOf(id)).setVariables(variables)).getCheckedData();

        // 3. 更新发货工作流编号
        contractRefundMapper.updateById(new ContractRefundDO().setId(id).setProcessInstanceId(processInstanceId)
                .setStatus(DMAuditStatusEnum.PROCESS.getStatus()).setRefundStatus(ContractRefundEnum.PROCESS.getStatus()));

        // 4. 记录日志
        LogRecordContext.putVariable("no", contractRefundDO.getNo());
    }

    @Override
    public void updateContractRefundStatus(String id, Integer refundStatus) {
        validateContractRefundExists(id);
        contractRefundMapper.updateById(new ContractRefundDO().setId(id).setRefundStatus(refundStatus));
    }

    @Override
    public void deleteContractRefund(String id) {
        // 校验存在
        validateContractRefundExists(id);
        // 删除
        contractRefundMapper.deleteById(id);
    }

    private ContractRefundDO validateContractRefundExists(String id) {

        ContractRefundDO contractRefundDO = contractRefundMapper.selectById(id);
        if (contractRefundDO == null) {
            throw exception(CONTRACT_REFUND_NOT_EXISTS);
        }
        return contractRefundDO;
    }

    private void validateContractRefundPay(ContractRefundSaveReqVO  vo) {


        BigDecimal value = new BigDecimal(0);
        if (value.compareTo(vo.getRefundPrice())==0){
            throw exception(CONTRACT_REFUND_NOT_NULL);
        }

            //获取合同下所有的退款单
        List<ContractRefundDO> dos = contractRefundMapper.selectListByContract("contract_id",vo.getContractId());
        BigDecimal bigDecimal = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(dos)){

            for (ContractRefundDO contractRefundDO :dos){
                bigDecimal.add(contractRefundDO.getRefundPrice());
            }
        }

        //获取合同已付款项
        CommonResult<List<ContractPaymentDTO>> dtos= paymentApi.getContractPaymentByContractIds(Lists.newArrayList(vo.getContractId()));

        BigDecimal payment = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(dtos.getCheckedData())){

            for (ContractPaymentDTO dto : dtos.getCheckedData()){
                payment.add(dto.getAmount());
            }
        }
        if (bigDecimal.compareTo(payment)>0){
            throw exception(CONTRACT_REFUND_OUT);
        }
    }





    @Override
    public ContractRefundDO getContractRefund(String id) {
        return contractRefundMapper.selectById(id);
    }

    @Override
    public PageResult<ContractRefundDO> getContractRefundPage(ContractRefundPageReqVO pageReqVO) {
        return contractRefundMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateRefundProcessInstanceStatus(String id, Integer status) {
        ContractRefundDO  contractRefundDO = validateContractRefundExists(id);
        contractRefundDO.setStatus(status);
        if (DMAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            contractRefundDO.setRefundStatus(ContractRefundEnum.REFUNDING.getStatus());
        }


        if (DMAuditStatusEnum.REJECT.getStatus().equals(status)) {
            contractRefundDO.setRefundStatus(ContractRefundEnum.REJECT.getStatus());
        }
        contractRefundMapper.updateById(contractRefundDO);
    }

    @Override
    public List<ContractRefundDO> getContractRefundByShippingReturn(List<String> shippingReturnIds) {
        return contractRefundMapper.getContractRefundByShippingReturn(shippingReturnIds);
    }

    @Override
    public List<ContractRefundDO> getContractRefundByContractId(String contractId) {
        return contractRefundMapper.getContractRefundByContractId(contractId);
    }

}