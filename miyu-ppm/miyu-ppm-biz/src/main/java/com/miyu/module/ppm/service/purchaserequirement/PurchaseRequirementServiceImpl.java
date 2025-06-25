package com.miyu.module.ppm.service.purchaserequirement;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import com.alibaba.nacos.common.utils.StringUtils;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDetailDO;
import com.miyu.module.ppm.dal.mysql.purchaserequirement.PurchaseRequirementDetailMapper;
import com.miyu.module.ppm.enums.common.RequirementAuditStatusEnum;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.purchaserequirement.vo.*;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.ppm.dal.mysql.purchaserequirement.PurchaseRequirementMapper;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ApiConstants.PURCHASE_REQUIREMENT_PROCESS_KEY;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 采购申请主 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class PurchaseRequirementServiceImpl implements PurchaseRequirementService {

    @Resource
    private PurchaseRequirementMapper purchaseRequirementMapper;

    @Resource
    private PurchaseRequirementDetailMapper purchaseRequirementDetailMapper;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    /**
     * 保存采购申请
     * @param createReqVO 创建信息
     * @return
     */
    @Override
    @Transactional
    public String createPurchaseRequirement(PurchaseRequirementSaveReqVO createReqVO) {
        String number = "P" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        // 验证采购申请明细产品重复
        validMaterialDuplicate(createReqVO.getDetails());
        // 插入采购申请
        PurchaseRequirementDO purchaseRequirement = BeanUtils.toBean(createReqVO, PurchaseRequirementDO.class).setNumber(number).setIsValid(1).setStatus(RequirementAuditStatusEnum.DRAFT.getStatus());
        purchaseRequirementMapper.insert(purchaseRequirement);
        // 物料类型id
        List<String> materialIds = convertList(createReqVO.getDetails(), PurchaseRequirementSaveReqVO.Detail::getRequiredMaterial);
        materialIds = materialIds.stream().distinct().collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> materialMap = materialMCCApi.getMaterialConfigMap(materialIds);
        // 插入采购申请详情
        if (CollUtil.isNotEmpty(createReqVO.getDetails())) {
            List<PurchaseRequirementDetailDO> details = convertList(createReqVO.getDetails(), o -> BeanUtils.toBean(o, PurchaseRequirementDetailDO.class, vo-> {
                MapUtils.findAndThen(materialMap, vo.getRequiredMaterial(), a -> vo.setNumber(number + "_" +a.getMaterialNumber()));
                vo.setRequirementId(purchaseRequirement.getId());
                vo.setIsValid(1);
            }));
            purchaseRequirementDetailMapper.insertBatch(details);
        }
        // 返回
        return purchaseRequirement.getId();
    }

    /**
     * PMS保存采购申请 单据直接进入待审核状态
     * @param createReqVO 创建信息
     * @return
     */
    @Override
    @Transactional
    public String createPurchaseRequirementPMS(PurchaseRequirementSaveReqVO createReqVO) {
        // 创建采购申请
        String id = createPurchaseRequirement(createReqVO);
        // 提交采购申请 进入待审核
        // 提交人为申请人
        if(StringUtils.isNotBlank(createReqVO.getApplicant())){
            submitRequirement(id, Long.valueOf(createReqVO.getApplicant()));
        }
        return id;
    }

    /**
     * 更新采购申请
     * @param updateReqVO 更新信息
     */
    @Override
    @Transactional
    public void updatePurchaseRequirement(PurchaseRequirementSaveReqVO updateReqVO) {
        // 校验存在
        validatePurchaseRequirementExists(updateReqVO.getId());
        // 验证采购申请明细产品重复
        validMaterialDuplicate(updateReqVO.getDetails());
        // 更新
        PurchaseRequirementDO updateObj = BeanUtils.toBean(updateReqVO, PurchaseRequirementDO.class);
        purchaseRequirementMapper.updateById(updateObj);
        // 获取采购申请
        PurchaseRequirementDO requirementDO = purchaseRequirementMapper.selectById(updateObj.getId());

        // 物料类型id
        List<String> materialIds = convertList(updateReqVO.getDetails(), PurchaseRequirementSaveReqVO.Detail::getRequiredMaterial);
        materialIds = materialIds.stream().distinct().collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> materialMap = materialMCCApi.getMaterialConfigMap(materialIds);
        // 更细采购申请详细
        if (CollUtil.isNotEmpty(updateReqVO.getDetails())) {
            List<PurchaseRequirementDetailDO> details = convertList(updateReqVO.getDetails(), o -> BeanUtils.toBean(o, PurchaseRequirementDetailDO.class, vo-> {
                MapUtils.findAndThen(materialMap, vo.getRequiredMaterial(), a -> vo.setNumber(requirementDO.getNumber() + "_" +a.getMaterialNumber()));
                vo.setIsValid(1);
            }));
            updatePurchaseRequirementDetailList(updateReqVO.getId(), details);
        }
    }

    /**
     * 删除采购申请
     * @param id 编号
     */
    @Override
    @Transactional
    public void deletePurchaseRequirement(String id) {
        // 校验存在
        validatePurchaseRequirementExists(id);
        // 删除
        purchaseRequirementMapper.deleteById(id);
        // 删除详细
        purchaseRequirementDetailMapper.delete(PurchaseRequirementDetailDO::getRequirementId, id);
    }

    private PurchaseRequirementDO validatePurchaseRequirementExists(String id) {
        PurchaseRequirementDO  requirementDO = purchaseRequirementMapper.selectById(id);
        if (requirementDO == null) {
            throw exception(PURCHASE_REQUIREMENT_NOT_EXISTS);
        }
        return requirementDO;
    }

    /**
     * 验证产品重复
     * @param details
     */
    private void validMaterialDuplicate(List<PurchaseRequirementSaveReqVO.Detail> details){
        if(details.size()>0){
            //源单类型为3是外协采购,不需要验证产品重复
            if(!(details.get(0).getSourceType()==3)){
                Map<String, Long> counts = details.stream()
                        .collect(Collectors.groupingBy(item -> item.getRequiredMaterial(), Collectors.counting()));
                if(counts.entrySet().stream().allMatch(entry -> entry.getValue() > 1)){
                    throw exception(PURCHASE_REQUIREMENT_DETAIL_MATERIAL_DUPLICATE);
                }
            }
        }
    }

    @Override
    public PurchaseRequirementDO getPurchaseRequirement(String id) {
        return purchaseRequirementMapper.selectById(id);
    }

    @Override
    public PageResult<PurchaseRequirementDO> getPurchaseRequirementPage(PurchaseRequirementPageReqVO pageReqVO) {
        // 查询当前部门
//        if(StringUtils.isNotBlank(pageReqVO.getIsCurrentDept())){
//            AdminUserRespDTO user = userApi.getUser(getLoginUserId()).getCheckedData();
//            pageReqVO.setApplicationDepartment(user.getDeptId().toString());
//        }

        return purchaseRequirementMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<PurchaseRequirementDetailDO> getPurchaseRequirementDetailPage(PurchaseRequirementPageReqVO pageReqVO) {
        return purchaseRequirementDetailMapper.selectPage(pageReqVO);
    }

    /**
     * 获取采购申请详细
     * @param id
     * @return
     */
    @Override
    public List<PurchaseRequirementDetailDO> getPurchaseRequirementDetailListByRequirementId(String id) {
        return purchaseRequirementDetailMapper.getPurchaseRequirementDetailRequirementId(id);
    }

    /**
     * 获取采购申请详细
     * @param reqVO
     * @return
     */
    @Override
    public List<PurchaseRequirementDetailDO> getPurchaseRequirementDetailList(PurchaseRequirementDetailReqVO reqVO) {
        return purchaseRequirementDetailMapper.getPurchaseRequirementDetailList(reqVO);
    }

    /**
     * 提交采购申请审批
     * @param id
     * @param userId
     */
    @Override
    public void submitRequirement(String id, Long userId) {
        // 1. 校验采购申请是否在审批
        PurchaseRequirementDO requirementDO = validatePurchaseRequirementExists(id);
        if (ObjUtil.notEqual(requirementDO.getStatus(), RequirementAuditStatusEnum.DRAFT.getStatus())) {
            throw exception(PURCHASE_REQUIREMENT_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 2. 创建采购申请审批流程实例
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(PURCHASE_REQUIREMENT_PROCESS_KEY).setBusinessKey(String.valueOf(id)).setVariables(new HashMap<>())).getCheckedData();

        // 3. 更新工作流编号
        purchaseRequirementMapper.updateById(new PurchaseRequirementDO().setId(id).setProcessInstanceId(processInstanceId).setStatus(RequirementAuditStatusEnum.PROCESS.getStatus()));
    }

    /**
     * 创建并提交采购审批
     * @param createReqVO
     */
    @Override
    public void createAndSubmitPurchaseRequirement(PurchaseRequirementSaveReqVO createReqVO) {
        String id = createPurchaseRequirement(createReqVO);
        submitRequirement(id, getLoginUserId());
    }

    /**
     * 更新采购审批状态
     * @param bussinessKey
     * @param status
     */
    @Override
    public void updateRequirementAuditStatus(String bussinessKey, Integer status) {
        // 1 校验采购申请是否存在
        PurchaseRequirementDO requirement = validatePurchaseRequirementExists(bussinessKey);
        // 2 只有审批中，可以更新审批结果
        if (ObjUtil.notEqual(requirement.getStatus(), RequirementAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(PURCHASE_REQUIREMENT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }

        PurchaseRequirementDO updateObj = new PurchaseRequirementDO().setId(bussinessKey).setStatus(status);
        // 更新付款审批结果
        purchaseRequirementMapper.updateById(updateObj);
    }

    /**
     * 更新采购申请详细
     * @param requirementId
     * @param newList
     */
    private void updatePurchaseRequirementDetailList(String requirementId, List<PurchaseRequirementDetailDO> newList) {
        List<PurchaseRequirementDetailDO> oldList = purchaseRequirementDetailMapper.selectList(PurchaseRequirementDetailDO::getRequirementId, requirementId);
        List<List<PurchaseRequirementDetailDO>> diffList = diffList(oldList, newList, // id 不同，就认为是不同的记录
                (oldVal, newVal) -> oldVal.getId().equals(newVal.getId()));
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            diffList.get(0).forEach(o -> o.setRequirementId(requirementId));
            purchaseRequirementDetailMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            purchaseRequirementDetailMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            purchaseRequirementDetailMapper.deleteBatchIds(convertSet(diffList.get(2), PurchaseRequirementDetailDO::getId));
        }
    }
}
