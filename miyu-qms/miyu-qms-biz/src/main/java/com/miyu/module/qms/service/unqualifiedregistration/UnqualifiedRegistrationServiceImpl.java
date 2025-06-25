package com.miyu.module.qms.service.unqualifiedregistration;

import com.miyu.module.qms.controller.admin.analysis.vo.AnalysisReqVO;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.miyu.module.qms.dal.dataobject.inspectionsheet.InspectionSheetDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import com.miyu.module.qms.dal.dataobject.unqualifiedmaterial.UnqualifiedMaterialDO;
import com.miyu.module.qms.dal.mysql.inspectionsheet.InspectionSheetMapper;
import com.miyu.module.qms.dal.mysql.inspectionsheetscheme.InspectionSheetSchemeMapper;
import com.miyu.module.qms.dal.mysql.unqualifiedmaterial.UnqualifiedMaterialMapper;
import com.miyu.module.qms.enums.UnqualifiedAuditStatusEnum;
import com.miyu.module.qms.service.inspectionsheetschemematerial.InspectionSheetSchemeMaterialService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.qms.controller.admin.unqualifiedregistration.vo.*;
import com.miyu.module.qms.dal.dataobject.unqualifiedregistration.UnqualifiedRegistrationDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.unqualifiedregistration.UnqualifiedRegistrationMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.miyu.module.qms.enums.ApiConstants.PROCESS_KEY;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 不合格品登记 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class UnqualifiedRegistrationServiceImpl implements UnqualifiedRegistrationService {

    @Resource
    private UnqualifiedRegistrationMapper unqualifiedRegistrationMapper;

    @Resource
    private InspectionSheetMapper inspectionSheetMapper;

    @Resource
    private InspectionSheetSchemeMapper inspectionSheetSchemeMapper;

    @Resource
    private InspectionSheetSchemeMaterialService inspectionSheetSchemeMaterialService;

    @Resource
    private UnqualifiedMaterialMapper unqualifiedMaterialMapper;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    public String createUnqualifiedRegistration(UnqualifiedRegistrationSaveReqVO createReqVO) {
        // 插入
        UnqualifiedRegistrationDO unqualifiedRegistration = BeanUtils.toBean(createReqVO, UnqualifiedRegistrationDO.class);
        unqualifiedRegistrationMapper.insert(unqualifiedRegistration);
        // 返回
        return unqualifiedRegistration.getId();
    }

    @Override
    public void updateUnqualifiedRegistration(UnqualifiedRegistrationSaveReqVO updateReqVO) {
        // 校验存在
        validateUnqualifiedRegistrationExists(updateReqVO.getId());
        // 更新
        UnqualifiedRegistrationDO updateObj = BeanUtils.toBean(updateReqVO, UnqualifiedRegistrationDO.class);
        unqualifiedRegistrationMapper.updateById(updateObj);
    }

    @Override
    public void deleteUnqualifiedRegistration(String id) {
        // 校验存在
        validateUnqualifiedRegistrationExists(id);
        // 删除
        unqualifiedRegistrationMapper.deleteById(id);
    }

    private void validateUnqualifiedRegistrationExists(String id) {
        if (unqualifiedRegistrationMapper.selectById(id) == null) {
            throw exception(UNQUALIFIED_REGISTRATION_NOT_EXISTS);
        }
    }

    @Override
    public UnqualifiedRegistrationDO getUnqualifiedRegistration(String id) {
        return unqualifiedRegistrationMapper.selectById(id);
    }

    @Override
    public PageResult<UnqualifiedRegistrationDO> getUnqualifiedRegistrationPage(UnqualifiedRegistrationPageReqVO pageReqVO) {
        return unqualifiedRegistrationMapper.selectPage(pageReqVO);
    }

    /**
     * 批量保存不合格品登记
     * @param createReqVO
     * @return
     */
    @Override
    public void saveUnqualifiedRegistrationBatch(UnqualifiedRegistrationSaveReqVO createReqVO) {

        List<UnqualifiedRegistrationSaveReqVO.Registration> list = createReqVO.getRegistrations();

        // 验证不合格产品重复
        validMaterialDuplicate(list);

        // 验证不合格数总数不能大于总检测数
        // Integer sumValue = getSumValue(list, UnqualifiedRegistrationSaveReqVO.Registration::getQuantity, Integer::sum);
        // 主键获取检验单任务
        InspectionSheetSchemeDO sheetScheme = inspectionSheetSchemeMapper.selectById(createReqVO.getInspectionSheetSchemeId());

        // 缺陷数量不能大于总不合格数
        // 不合格数 = 检测数- 合格数
        if(list.size() > (sheetScheme.getQuantity() - sheetScheme.getQualifiedQuantity())){
            throw exception(UNQUALIFIED_REGISTRATION_NUMBER_ERROR);
        }

        // 保存不合格产品
        List<UnqualifiedMaterialDO> unqualifiedList = BeanUtils.toBean(list, UnqualifiedMaterialDO.class, vo -> {
            vo.setSchemeMaterialId(vo.getSchemeMaterialId());
        });
        unqualifiedMaterialMapper.insertOrUpdateBatch(unqualifiedList);

        // 保存不合格产品
        List<UnqualifiedRegistrationDO> registrationDOList = new ArrayList<>();
        for(UnqualifiedMaterialDO o : unqualifiedList) {
            if(o.getDefectiveCode().size() > 0){
                o.getDefectiveCode().stream().forEach(id -> {
                            UnqualifiedRegistrationDO registrationDO = new UnqualifiedRegistrationDO();
                            registrationDO.setUnqualifiedMaterialId(o.getId());
                            registrationDO.setDefectiveCode(id);
                            registrationDO.setInspectionSheetSchemeId(sheetScheme.getId());
                            registrationDOList.add(registrationDO);
                        }
                );
            }
        }

        if(registrationDOList.size() > 0){
            unqualifiedRegistrationMapper.delete(UnqualifiedRegistrationDO::getInspectionSheetSchemeId, createReqVO.getInspectionSheetSchemeId());
            unqualifiedRegistrationMapper.insertOrUpdateBatch(registrationDOList);
        }
    }

    /**
     * 验证产品重复
     * @param details
     */
    private void validMaterialDuplicate(List<UnqualifiedRegistrationSaveReqVO.Registration> details){
        Map<String, Long> counts = details.stream()
                .collect(Collectors.groupingBy(item -> item.getSchemeMaterialId(), Collectors.counting()));
        if(counts.entrySet().stream().allMatch(entry -> entry.getValue() > 1)){
            throw exception(UNQUALIFIED_MATERIAL_DUPLICATE);
        }
    }

    /**
     * 检验任务ID获得不合格产品登记集合
     * @param id
     * @return
     */
    @Override
    public List<UnqualifiedMaterialDO> getUnqualifiedRegistrationListBySheetSchemeId(String id) {

        List<UnqualifiedMaterialDO> list = unqualifiedMaterialMapper.selectListBySheetSchemeId(id);
        // 未填写过不合格品登记
        // 默认显示检验不合格的产品
        if(list.size() == 0){
            List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetSchemeMaterialService.getUnqualifiedMaterialListBySchemeId(id);
            materialList.stream().forEach(o -> {
                UnqualifiedMaterialDO material = new UnqualifiedMaterialDO();
                material.setSchemeMaterialId(o.getId());
                material.setInspectionSheetSchemeId(id);
                list.add(material);
            });
        }
        return list;
    }

    @Override
    public List<UnqualifiedRegistrationDO> getDefectives(AnalysisReqVO vo) {
        return unqualifiedRegistrationMapper.getDefectives(vo);
    }

    /**
     * 批量保存不合格品登记并提交审核
     * @param createReqVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAndAuditUnqualifiedRegistrationBatch(UnqualifiedRegistrationSaveReqVO createReqVO) {
        saveUnqualifiedRegistrationBatch(createReqVO);
        // 查询检验任务
        InspectionSheetSchemeDO scheme = inspectionSheetSchemeMapper.selectById(createReqVO.getInspectionSheetSchemeId());
        // 查询检验单
        InspectionSheetDO sheet = inspectionSheetMapper.selectById(scheme.getInspectionSheetId());
        // 启动审批流程
        // 2. 创建合同审批流程实例
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("selfInspection", scheme.getSelfInspection() == null ? 0 : scheme.getSelfInspection());
        processInstanceVariables.put("sourceType", sheet.getSourceType());
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(PROCESS_KEY).setBusinessKey(createReqVO.getInspectionSheetSchemeId()).setVariables(processInstanceVariables)).getCheckedData();

        // 更新检验任务
        InspectionSheetSchemeDO updSheetScheme = new InspectionSheetSchemeDO();
        updSheetScheme.setId(createReqVO.getInspectionSheetSchemeId());
        updSheetScheme.setProcessInstanceId(processInstanceId);
        // 更新状态 审批中
        updSheetScheme.setProcessStatus(UnqualifiedAuditStatusEnum.PROCESS.getStatus());
        inspectionSheetSchemeMapper.updateById(updSheetScheme);
    }
}
