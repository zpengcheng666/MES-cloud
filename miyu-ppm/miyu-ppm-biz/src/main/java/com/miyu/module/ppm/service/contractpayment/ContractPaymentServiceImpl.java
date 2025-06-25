package com.miyu.module.ppm.service.contractpayment;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.api.contract.dto.ContractPaymentDTO;
import com.miyu.module.ppm.api.contract.dto.ContractPaymentDetailDTO;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDetailDO;
import com.miyu.module.ppm.dal.dataobject.contractpaymentscheme.ContractPaymentSchemeDO;
import com.miyu.module.ppm.dal.mysql.contract.ContractMapper;
import com.miyu.module.ppm.dal.mysql.contractpayment.ContractPaymentDetailMapper;
import com.miyu.module.ppm.dal.mysql.contractpaymentscheme.ContractPaymentSchemeMapper;
import com.miyu.module.ppm.enums.common.PaymentAuditStatusEnum;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.contractpayment.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.contractpayment.ContractPaymentMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 合同付款 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class ContractPaymentServiceImpl implements ContractPaymentService {

    @Resource
    private ContractPaymentMapper contractPaymentMapper;

    @Resource
    private ContractPaymentSchemeMapper contractPaymentSchemeMapper;

    @Resource
    private ContractPaymentDetailMapper contractPaymentDetailMapper;

    @Resource
    private ContractMapper contractMapper;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    @Transactional
    public String createContractPayment(ContractPaymentSaveReqVO createReqVO) {
        /**
         * 拼接付款编号 合同编号+序号
         */
        // 获取当前合同支付记录集合
        List<ContractPaymentDO> paymentList = contractPaymentMapper.selectList(ContractPaymentDO::getContractId, createReqVO.getContractId());
        ContractDO contract = contractMapper.selectOne(ContractDO::getId, createReqVO.getContractId());
        ContractPaymentDO contractPayment = BeanUtils.toBean(createReqVO, ContractPaymentDO.class)
                .setNumber(contract.getNumber()+"_"+ (paymentList.size() +1))
                .setStatus(PaymentAuditStatusEnum.DRAFT.getStatus());
        // 验证实际支付金额不能大于付款计划剩余待支付金额
        validateContractPaymentAmount(createReqVO);
        // 保存付款信息
        contractPaymentMapper.insert(contractPayment);
        // 转化订单详细列表
        List<ContractPaymentDetailDO> paymentDetailList = convertList(createReqVO.getPaymentDetails(), o -> BeanUtils.toBean(o, ContractPaymentDetailDO.class));
        // 插入子表
        createContractPaymentDetailList(contractPayment.getId(), paymentDetailList);
        // 返回
        return contractPayment.getId();
    }

    @Override
    @Transactional
    public void updateContractPayment(ContractPaymentSaveReqVO updateReqVO) {
        // 校验存在
        validateContractPaymentExists(updateReqVO.getId());
        // 验证实际支付金额不能大于付款计划剩余待支付金额
        validateContractPaymentAmount(updateReqVO);
        // 更新
        ContractPaymentDO updateObj = BeanUtils.toBean(updateReqVO, ContractPaymentDO.class);
        contractPaymentMapper.updateById(updateObj);

        // 转化订单详细列表
        List<ContractPaymentDetailDO> paymentDetailList = convertList(updateReqVO.getPaymentDetails(), o -> BeanUtils.toBean(o, ContractPaymentDetailDO.class));
        // 更新子表
        updateContractPaymentDetailList(updateReqVO.getId(), paymentDetailList);
    }

    @Override
    @Transactional
    public void deleteContractPayment(String id) {
        // 校验存在
        validateContractPaymentExists(id);
        // 删除支付
        contractPaymentMapper.deleteById(id);
        // 删除支付详细
        contractPaymentDetailMapper.deleteByPaymentId(id);
    }

    private ContractPaymentDO validateContractPaymentExists(String id) {
        ContractPaymentDO payment = contractPaymentMapper.selectById(id);
        if (payment == null) {
            throw exception(CONTRACT_PAYMENT_NOT_EXISTS);
        }
        return payment;
    }

    /**
     * 验证付款详细对应付款计划金额
     * @param reqVO
     */
    private void validateContractPaymentAmount(ContractPaymentSaveReqVO reqVO) {
        // 付款计划集合
        Collection<String> ids = convertSet(reqVO.getPaymentDetails(), obj -> obj.getSchemeId());
        // 获取付款明细
        List<ContractPaymentDetailDO> detailList = contractPaymentDetailMapper.selectPaymentAmountListBySchemeId(ids, reqVO.getId());
        // list转map
        Map<String, ContractPaymentDetailDO> detailMap = detailList.stream()
                .collect(Collectors.toMap(obj -> obj.getSchemeId(), obj -> obj));
        // 获取付款计划
        List<ContractPaymentSchemeDO> schemeList = contractPaymentSchemeMapper.selectListByIds(ids);
        Map<String, ContractPaymentSchemeDO> schemeMap = schemeList.stream()
                .collect(Collectors.toMap(obj -> obj.getId(), obj -> obj));
        // 遍历支付明细 验证当前支付明细对应的付款计划金额
        for (ContractPaymentSaveReqVO.PaymentDetail detail : reqVO.getPaymentDetails()) {
            // 计划支付金额
            BigDecimal schemeAmount = schemeMap.get(detail.getSchemeId()) != null ? schemeMap.get(detail.getSchemeId()).getAmount() : BigDecimal.ZERO;
            // 已支付金额
            BigDecimal paidAmount = detailMap.get(detail.getSchemeId()) != null ? detailMap.get(detail.getSchemeId()).getAmount() : BigDecimal.ZERO;
            // 当前支付金额大于当前付款计划金额
            if(detail.getAmount().compareTo(schemeAmount.subtract(paidAmount)) > 0){
                throw exception(CONTRACT_PAYMENT_AMOUNT_ERROR, schemeMap.get(detail.getSchemeId()).getNumber());
            }
        }
    }

    @Override
    public ContractPaymentDO getContractPayment(String id) {
        return contractPaymentMapper.selectById(id);
    }

    @Override
    public PageResult<ContractPaymentDO> getContractPaymentPage(ContractPaymentPageReqVO pageReqVO) {
        return contractPaymentMapper.selectPage(pageReqVO);
    }

    /**
     * 合同主键获取合同付款计划
     * @param reqVO
     * @return
     */
    @Override
    public List<ContractPaymentSchemeDO> getContractPaymentSchemeListByContractId(ContractPaymentReqVO reqVO) {
        return contractPaymentSchemeMapper.selectListPaymentSchemeByContractId(reqVO);
    }

    /**
     * 通过主键获得付款记录
     * @param id
     * @return
     */
    @Override
    public ContractPaymentDO getContractPaymentById(String id) {
        return contractPaymentMapper.getContractPaymentById(id);
    }

    /**
     * 提交合同审批
     * @param id
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitContractPayment(String id, String processKey, Long userId) {
        // 1. 校验付款是否在审批
        ContractPaymentDO payment = validateContractPaymentExists(id);
        if (ObjUtil.notEqual(payment.getStatus(), PaymentAuditStatusEnum.DRAFT.getStatus())) {
            throw exception(PAYMENT_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 2. 创建合同审批流程实例
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(processKey).setBusinessKey(String.valueOf(id)).setVariables(new HashMap<>())).getCheckedData();

        // 3. 更新合同付款工作流编号
        contractPaymentMapper.updateById(new ContractPaymentDO().setId(id).setProcessInstanceId(processInstanceId).setStatus(PaymentAuditStatusEnum.PROCESS.getStatus()));
    }

    /**
     * 更新审批状态
     * @param bussinessKey
     * @param status
     */
    @Override
    public void updateContractPaymentAuditStatus(String bussinessKey, Integer status) {
        // 1 校验付款是否存在
        ContractPaymentDO payment = validateContractPaymentExists(bussinessKey);
        // 2 只有审批中，可以更新审批结果
        if (ObjUtil.notEqual(payment.getStatus(), PaymentAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(PAYMENT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }

        ContractPaymentDO updateObj = new ContractPaymentDO().setId(bussinessKey).setStatus(status);
        // 更新付款审批结果
        contractPaymentMapper.updateById(updateObj);
    }

    /**
     * 创建并提交付款审批
     * @param createReqVO
     * @return
     */
    @Override
    @Transactional
    public void createAndSubmitContractPayment(ContractPaymentSaveReqVO createReqVO) {
        String id = createContractPayment(createReqVO);
        submitContractPayment(id, createReqVO.getProcessKey(), getLoginUserId());
    }


    /**
     * 合同发票获取支付单据详细
     * @param reqVO
     * @return
     */
    @Override
    public List<ContractPaymentDO> getList4InvoiceByPaymentId(ContractPaymentReqVO reqVO) {
        return contractPaymentMapper.selectList4InvoiceByPaymentId(reqVO);
    }

    /**
     * 通过合同id查询付款及明细
     * @param ids
     * @return
     */
    @Override
    public List<ContractPaymentDTO> getContractPaymentByContractIds(Collection<String> ids) {
        //通过合同id查询合同付款
        List<ContractPaymentDO> contractPaymentDOS = contractPaymentMapper.selectList(ContractPaymentDO::getContractId, ids);
        List<ContractPaymentDTO> contractPaymentDTOS = BeanUtils.toBean(contractPaymentDOS, ContractPaymentDTO.class, vo -> {
            //查询付款详细
            List<ContractPaymentDetailDO> contractPaymentDetailDOS = contractPaymentDetailMapper.selectListByPaymentId(vo.getId());
            List<ContractPaymentDetailDTO> contractPaymentDetailDTOS = BeanUtils.toBean(contractPaymentDetailDOS, ContractPaymentDetailDTO.class);
            vo.setPaymentDetailDTOList(contractPaymentDetailDTOS);
        });
        return contractPaymentDTOS;
    }


    /**
     * 合同主键获取支付信息集合
     * @param id
     * @return
     */
    @Override
    public List<ContractPaymentDTO> getContractPaymentByContractId(String id) {
        //通过合同id查询合同付款
        List<ContractPaymentDO> contractPaymentDOS = contractPaymentMapper.selectList(ContractPaymentDO::getContractId, id);
        List<ContractPaymentDTO> contractPaymentDTOS = BeanUtils.toBean(contractPaymentDOS, ContractPaymentDTO.class, vo -> {
            //查询付款详细
            List<ContractPaymentDetailDO> contractPaymentDetailDOS = contractPaymentDetailMapper.selectListByPaymentId(vo.getId());
            List<ContractPaymentDetailDTO> contractPaymentDetailDTOS = BeanUtils.toBean(contractPaymentDetailDOS, ContractPaymentDetailDTO.class);
            vo.setPaymentDetailDTOList(contractPaymentDetailDTOS);
        });
        return contractPaymentDTOS;
    }

    // ==================== 子表（合同付款详细） ====================

    @Override
    public List<ContractPaymentDetailDO> getPaymentListById(String paymentId) {
        return contractPaymentDetailMapper.selectListByPaymentId(paymentId);
    }


    private void createContractPaymentDetailList(String paymentId, List<ContractPaymentDetailDO> list) {
        // 过滤支付金额为0的记录
        list = list.stream().filter(a-> BigDecimal.ZERO.compareTo(a.getAmount()) < 0).collect(Collectors.toList());
        list.forEach(o -> o.setPaymentId(paymentId));
        contractPaymentDetailMapper.insertBatch(list);
    }

    private void updateContractPaymentDetailList(String paymentId, List<ContractPaymentDetailDO> list) {
        deleteContractPaymentDetailByPaymentId(paymentId);
        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createContractPaymentDetailList(paymentId, list);
    }

    private void deleteContractPaymentDetailByPaymentId(String paymentId) {
        contractPaymentDetailMapper.deleteByPaymentId(paymentId);
    }

}
