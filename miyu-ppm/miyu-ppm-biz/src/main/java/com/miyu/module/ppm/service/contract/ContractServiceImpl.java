package com.miyu.module.ppm.service.contract;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.miyu.module.ppm.controller.admin.company.vo.CompanySaveReqVO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderProductDO;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDO;
import com.miyu.module.ppm.dal.dataobject.contractpaymentscheme.ContractPaymentSchemeDO;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDO;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDetailDO;
import com.miyu.module.ppm.dal.mysql.contract.ContractOrderMapper;
import com.miyu.module.ppm.dal.mysql.contractpayment.ContractPaymentMapper;
import com.miyu.module.ppm.dal.mysql.contractpaymentscheme.ContractPaymentSchemeMapper;
import com.miyu.module.ppm.dal.mysql.purchaserequirement.PurchaseRequirementDetailMapper;
import com.miyu.module.ppm.dal.mysql.purchaserequirement.PurchaseRequirementMapper;
import com.miyu.module.ppm.dal.redis.no.ContractNoRedisDAO;
import com.miyu.module.ppm.enums.common.ContractAuditStatusEnum;
import com.miyu.module.ppm.enums.common.ContractStatusEnum;
import com.miyu.module.ppm.service.companyproduct.CompanyProductService;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.miyu.module.ppm.controller.admin.contract.vo.*;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.contract.ContractMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;
import static com.miyu.module.ppm.enums.LogRecordConstants.*;

/**
 * 购销合同 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class ContractServiceImpl implements ContractService {

    @Resource
    private ContractMapper contractMapper;

    @Resource
    private ContractOrderMapper contractOrderMapper;

    @Resource
    private ContractPaymentSchemeMapper contractPaymentSchemeMapper;

    @Resource
    private CompanyProductService productService;

    @Resource
    private PurchaseRequirementMapper purchaseRequirementMapper;

    @Resource
    private PurchaseRequirementDetailMapper purchaseRequirementDetailMapper;

    @Resource
    private ContractNoRedisDAO noRedisDAO;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;


    @Resource
    private ContractPaymentMapper contractPaymentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createContract(ContractSaveReqVO createReqVO) {
        // 采购校验产品的有效性
        if("1".equals(createReqVO.getType())){
            validateContractProducts(createReqVO.getProducts());
        }
        // 生成序号
        // String no = noRedisDAO.generate(ContractNoRedisDAO.CONTRACT_NO_PREFIX);
        String no = createReqVO.getNumber();
        // 转化订单列表
        List<ContractOrderDO> contractOrders = convertList(createReqVO.getProducts(), o -> BeanUtils.toBean(o, ContractOrderDO.class));
        // 转换付款计划列表
        List<ContractPaymentSchemeDO> contractSchemes = convertList(createReqVO.getPaymentSchemes(), o -> BeanUtils.toBean(o, ContractPaymentSchemeDO.class));

        IntStream.range(0, contractSchemes.size())
                .mapToObj(index -> contractSchemes.get(index)
                        .setNumber(createReqVO.getNumber()+ "_" + (index + 1)))
                .collect(Collectors.toList());

        // 验证合同编号是否存在
        if (contractMapper.selectByNo(no).size()>0) {
            throw exception(CONTRACT_NO_EXISTS);
        }

        // 如果订单关联了采购申请明细
        // 验证订单数量不能大于所关联采购申请明细的数量
        List<PurchaseRequirementDetailDO> requirementDetailList = validContractOrderQuantity(createReqVO.getProducts());

        // 验证合同金额和付款计划金额是否相等
        validateContractPaymentSchemeAmount(createReqVO.getProducts(), createReqVO.getPaymentSchemes());

        // 插入
        ContractDO contract = BeanUtils.toBean(createReqVO, ContractDO.class).setNumber(no).setContractStatus(ContractStatusEnum.DRAFT.getStatus()).setStatus(ContractAuditStatusEnum.DRAFT.getStatus()).setNumber(no);
        contractMapper.insert(contract);
        // 插入订单
        createContractOrderList(contract.getId(), contractOrders);
        // 插入付款计划
        createContractPaymentSchemeList(contract.getId(), contractSchemes);
        // 更新采购申请状态
        updateRequirementStatus(requirementDetailList);
        // 返回
        return contract.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CONTRACT_TYPE, subType = CONTRACT_UPDATE_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CONTRACT_UPDATE_SUCCESS)
    public void updateContract(ContractSaveReqVO updateReqVO) {
        // 校验存在
        ContractDO contract = validateContractExists(updateReqVO.getId());
        // 只有草稿，可以编辑；
        if (!ObjectUtils.equalsAny(contract.getStatus(), ContractAuditStatusEnum.DRAFT.getStatus())) {
            throw exception(CONTRACT_UPDATE_FAIL_NOT_DRAFT);
        }

        // 采购校验产品有效性
        if("1".equals(updateReqVO.getType())){
            validateContractProducts(updateReqVO.getProducts());
        }

        // 验证合同金额和付款计划金额是否相等
        validateContractPaymentSchemeAmount(updateReqVO.getProducts(), updateReqVO.getPaymentSchemes());

        // 转化订单列表
        List<ContractOrderDO> contractOrders = convertList(updateReqVO.getProducts(), o -> BeanUtils.toBean(o, ContractOrderDO.class));
        // 转换付款计划列表
        List<ContractPaymentSchemeDO> contractSchemes = convertList(updateReqVO.getPaymentSchemes(), o -> BeanUtils.toBean(o, ContractPaymentSchemeDO.class));
        IntStream.range(0, contractSchemes.size())
                .mapToObj(index -> contractSchemes.get(index)
                        .setNumber(updateReqVO.getNumber()+ "_" + (index + 1)))
                .collect(Collectors.toList());

        // 更新合同
        ContractDO updateObj = BeanUtils.toBean(updateReqVO, ContractDO.class);
        contractMapper.updateById(updateObj);

        // 更新合同订单
        updateContractOrderList(updateReqVO.getId(), contractOrders);
        // 更新付款计划
        updateContractPaymentSchemeList(contract.getId(), contractSchemes);



        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(contract, ContractSaveReqVO.class));
        LogRecordContext.putVariable("contract", contract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContract(String id) {
        // 校验存在
        ContractDO contract = validateContractExists(id);
        // 只有草稿，可以删除；
        if (!ObjectUtils.equalsAny(contract.getStatus(), ContractAuditStatusEnum.DRAFT.getStatus())) {
            throw exception(CONTRACT_UPDATE_FAIL_NOT_DRAFT);
        }
        // 删除合同
        contractMapper.deleteById(contract.getId());
        // 删除订单
        contractOrderMapper.deleteByContractId(contract.getId());
        // 删除计划
        contractPaymentSchemeMapper.deleteByContractId(contract.getId());
    }

    /**
     * 验证合同是否存在
     * @param id
     * @return
     */
    private ContractDO validateContractExists(String id) {
        ContractDO contract = contractMapper.selectById(id);
        if (contract == null) {
            throw exception(CONTRACT_NOT_EXISTS);
        }
        return contract;
    }

    /**
     * 验证产品是否存在
     * @param list
     * @return
     */
    private void validateContractProducts(List<ContractSaveReqVO.Product> list) {
        // 校验产品存在
        productService.validProductList(convertSet(list, ContractSaveReqVO.Product::getMaterialId));
    }

    /**
     * 验证订单数量不能大于所关联采购申请明细的数量
     * 返回已完全匹配的采购申请明细集合
     */
    private List<PurchaseRequirementDetailDO> validContractOrderQuantity(List<ContractSaveReqVO.Product> list){

        // 采购申请已完成集合
        List<PurchaseRequirementDetailDO> finishList = new ArrayList<>();
        // 过滤关联采购申请的订单
        list = list.stream().filter(a -> StringUtils.isNotBlank(a.getRequirementDetailId())).collect(Collectors.toList());
        // 计算关联采购申请的
        Map<String, BigDecimal> sumMap = list.stream()
                .collect(Collectors.groupingBy(
                        ContractSaveReqVO.Product::getRequirementDetailId,
                        Collectors.reducing(BigDecimal.ZERO, ContractSaveReqVO.Product::getQuantity, BigDecimal::add)
                ));

        // 获取采购申请明细集合
        List<String> requirementDetailIds = convertList(list, obj -> obj.getRequirementDetailId());
        requirementDetailIds = requirementDetailIds.stream().distinct().collect(Collectors.toList());
        if(requirementDetailIds.size() == 0){
            return new ArrayList<>();
        }
        // 获取关联当前采购申请的所有订单
        List<ContractOrderDO> contractOrderList = contractOrderMapper.selectListByRequirementId(requirementDetailIds);

        // 将当前关联采购申请的订单采购数量与历史关联当前采购申请的数量求和
        for (ContractOrderDO order : contractOrderList) {
            sumMap.put(order.getRequirementDetailId(), sumMap.get(order.getRequirementDetailId()).add(order.getQuantity()));
        }

        List<PurchaseRequirementDetailDO> requirementDetailList = purchaseRequirementDetailMapper.selectList(PurchaseRequirementDetailDO::getId, requirementDetailIds);

        for(PurchaseRequirementDetailDO detail : requirementDetailList){
            // 所有关联当前采购申请的总数量 大于 当前采购申请总数量
            if(detail.getRequiredQuantity().compareTo(sumMap.get(detail.getId())) < 0){
                throw exception(PURCHASE_REQUIREMENT_DETAIL_QUANTITY_ERROR, detail.getNumber());
            }
            // 当前采购申请数量已完全匹配
            else if(detail.getRequiredQuantity().compareTo(sumMap.get(detail.getId())) == 0){
                finishList.add(detail);
            }
        }

        return finishList;
    }

    /**
     * 验证付款计划金额与合同金额是否相等
     * @param products
     * @param paymentSchemes
     */
    private void validateContractPaymentSchemeAmount(List<ContractSaveReqVO.Product> products, List<ContractSaveReqVO.PaymentScheme> paymentSchemes) {
        // 合同含税总金额
        BigDecimal contractAmount = products.stream().reduce(BigDecimal.ZERO, (a, b) -> {
            return a.add(b.getTaxPrice().multiply(b.getQuantity()));
        }, BigDecimal::add);

        // 付款计划总金额
        BigDecimal schemeAmount = paymentSchemes.stream().map(ContractSaveReqVO.PaymentScheme::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 付款计划总金额不等于合同总金额
        if(contractAmount.compareTo(schemeAmount) != 0){
            throw exception(CONTRACT_PAYMENT_SCHEME_AMOUNT_ERROR);
        }
    }


    @Override
    public ContractDO getContract(String id) {
        return contractMapper.selectById(id);
    }

    @Override
    public PageResult<ContractDO> getContractPage(ContractPageReqVO pageReqVO) {
        return contractMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ContractDO> getContractListByType(Collection<String> types) {
        return contractMapper.selectListByType(types);
    }

    @Override
    public List<ContractDO> getContractListByIds(Collection<String> ids) {
        return contractMapper.selectListByIds(ids);
    }

    /**
     * 更新合同状态
     * @param updateReqVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContractStatus(ContractUpdateReqVO updateReqVO) {
        // 校验存在
        ContractDO contract = validateContractExists(updateReqVO.getId());
        // 审批状态已完成 可以终止
        if (!ObjectUtils.equalsAny(contract.getStatus(), ContractAuditStatusEnum.APPROVE.getStatus())) {
            throw exception(CONTRACT_STATUS_UPDATE_FAIL, "审批状态不是已完成");
        }

        // 合同状态执行中，可以终止；
        if (!ObjectUtils.equalsAny(contract.getContractStatus(), ContractStatusEnum.PROCESS.getStatus()) ) {
            throw exception(CONTRACT_STATUS_UPDATE_FAIL, "合同状态不是进行中");
        }
        // 更新
        ContractDO updateObj = BeanUtils.toBean(updateReqVO, ContractDO.class);
        contractMapper.updateById(updateObj);
    }


    /**
     * 合同主键获取合同信息
     * @param id
     * @return
     */
    @Override
    public ContractDO getContractById(String id) {
        return contractMapper.getContractById(id);
    }

    @Override
    public ContractDO getContractInfoByNumber(ContractReqVO reqVO) {
        return contractMapper.selectListByNumber(reqVO);
    }


    // ==================== 子表（合同订单） ====================

    @Override
    public List<ContractOrderDO> getContractOrderListByContractId(String contractId) {
        return contractOrderMapper.selectListByContractId(contractId);
    }

    @Override
    public List<ContractOrderDO> getContractOrderListByContractIds(List<String> contractIds) {
        MPJLambdaWrapperX<ContractOrderDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.inIfPresent(ContractOrderDO::getContractId, contractIds);
        return contractOrderMapper.selectList(wrapper);
    }

    private void createContractOrderList(String contractId, List<ContractOrderDO> list) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(item -> item.setContractId(contractId));
            contractOrderMapper.insertBatch(list);
        }
    }

    /**
     * 采购申请详情ID获取采购合同集合
     * @param requirementDetailId
     * @return
     */
    @Override
    public List<ContractOrderDO> getContractOrderListByRequirementDetailId(String requirementDetailId) {
        return contractOrderMapper.selectListByRequirementDetailId(requirementDetailId);
    }

    @Override
    public List<ContractPaymentDO> getContractPaymentListByContractIds(List<String> contractIds) {
        MPJLambdaWrapperX<ContractPaymentDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.inIfPresent(ContractPaymentDO::getContractId,contractIds);
        return contractPaymentMapper.selectList(wrapper);
    }


//    private void updateContractOrderList(String contractId, List<ContractOrderDO> list) {
//        deleteContractOrderByContractId(contractId);
//        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
//        createContractOrderList(contractId, list);
//    }


    private void updateContractOrderList(String contractId, List<ContractOrderDO> newList) {
        List<ContractOrderDO> oldList = contractOrderMapper.selectListByContractId(contractId);
        List<List<ContractOrderDO>> diffList = diffList(oldList, newList, // id 不同，就认为是不同的记录
                (oldVal, newVal) -> oldVal.getId().equals(newVal.getId()));
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            diffList.get(0).forEach(o -> o.setContractId(contractId));
            contractOrderMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            contractOrderMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            contractOrderMapper.deleteBatchIds(convertSet(diffList.get(2), ContractOrderDO::getId));
        }
    }

    private void deleteContractOrderByContractId(String contractId) {
        contractOrderMapper.deleteByContractId(contractId);
    }

    /**
     * 获取产品的历史价格（最高、最低、平均、最新）
     * @param productIds
     * @return
     */
    @Override
    public Map<String, ContractOrderProductDO> getContractProductPriceHis(Collection<String> productIds) {

        if (CollUtil.isEmpty(productIds)) {
            return Collections.emptyMap();
        }

        return contractOrderMapper.selectListByProductId(productIds);
    }

    /**
     * 提交合同审批
     * @param id
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitContract(String id, String processKey, Long userId) {
        // 1. 校验合同是否在审批
        ContractDO contract = validateContractExists(id);
        if (ObjUtil.notEqual(contract.getStatus(), ContractAuditStatusEnum.DRAFT.getStatus())) {
            throw exception(CONTRACT_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 2. 创建合同审批流程实例
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(processKey).setBusinessKey(String.valueOf(id)).setVariables(new HashMap<>())).getCheckedData();

        // 3. 更新合同工作流编号
        contractMapper.updateById(new ContractDO().setId(id).setProcessInstanceId(processInstanceId)
                .setStatus(ContractAuditStatusEnum.PROCESS.getStatus()));
    }

    @Override
    public void updateContractAuditStatus(String bussinessKey, Integer status) {
        // 1.1 校验合同是否存在
        ContractDO contract = validateContractExists(bussinessKey);
        // 1.2 只有审批中，可以更新审批结果
        if (ObjUtil.notEqual(contract.getStatus(), ContractAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(CONTRACT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }

        ContractDO contractDO = new ContractDO().setId(bussinessKey).setStatus(status);
        // 审批完成 更新合同状态为执行中
        if(ObjectUtils.equalsAny(status, ContractAuditStatusEnum.APPROVE.getStatus())){
            contractDO.setContractStatus(ContractStatusEnum.PROCESS.getStatus());
        }
        // 更新合同审批结果
        contractMapper.updateById(contractDO);
    }

    /**
     * 创建并提交合同审批
     * @param createReqVO
     * @return
     */
    @Override
    @Transactional
    public void createAndSubmitContract(ContractSaveReqVO createReqVO) {
        String id = createContract(createReqVO);
        submitContract(id, createReqVO.getProcessKey(), getLoginUserId());
    }


    // ==================== 子表（合同付款计划） ====================

    /**
     * 合同主键获取合同付款计划
     * @param contractId
     * @return
     */
    @Override
    public List<ContractPaymentSchemeDO> getContractPaymentSchemeListByContractId(String contractId) {
        return contractPaymentSchemeMapper.selectListByContractId(contractId);
    }

    /**
     * 创建合同付款计划
     * @param contractId
     * @param list
     * @return
     */
    @Override
    public void createContractPaymentSchemeList(String contractId, List<ContractPaymentSchemeDO> list) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(item -> item.setContractId(contractId));
            contractPaymentSchemeMapper.insertBatch(list);
        }
    }

    /**
     * 更新采购申请状态
     */
    private void updateRequirementStatus(List<PurchaseRequirementDetailDO> requirementDetailList) {

        if(requirementDetailList.size() > 0){
            // 失效当前采购明细
            requirementDetailList.forEach(o -> o.setIsValid(0));
            purchaseRequirementDetailMapper.updateBatch(requirementDetailList);
        }

        for(PurchaseRequirementDetailDO detail : requirementDetailList) {
            // 获取有效的采购明细
            List<PurchaseRequirementDetailDO> validList = purchaseRequirementDetailMapper.selectList(PurchaseRequirementDetailDO::getRequirementId, detail.getRequirementId(), PurchaseRequirementDetailDO::getIsValid, 1);
            if(validList.size() == 0){
                PurchaseRequirementDO upd = new PurchaseRequirementDO().setId(detail.getRequirementId()).setIsValid(0);
                purchaseRequirementMapper.updateById(upd);
            }
        }
    }

    /**
     * 更新合同付款计划
     * @param contractId
     * @param newList
     */
    private void updateContractPaymentSchemeList(String contractId, List<ContractPaymentSchemeDO> newList) {
        List<ContractPaymentSchemeDO> oldList = contractPaymentSchemeMapper.selectListByContractId(contractId);
        List<List<ContractPaymentSchemeDO>> diffList = diffList(oldList, newList, // id 不同，就认为是不同的记录
                (oldVal, newVal) -> oldVal.getId().equals(newVal.getId()));
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            diffList.get(0).forEach(o -> o.setContractId(contractId));
            contractPaymentSchemeMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            contractPaymentSchemeMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            contractPaymentSchemeMapper.deleteBatchIds(convertSet(diffList.get(2), ContractPaymentSchemeDO::getId));
        }
    }

    private void deleteCContractPaymentSchemeByContractId(String contractId) {
        contractPaymentSchemeMapper.deleteByContractId(contractId);
    }
}
