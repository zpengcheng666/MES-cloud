package com.miyu.module.qms.service.inspectionsheetsamplingrule;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import com.miyu.module.qms.controller.admin.inspectionsheetsamplingrule.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetsamplingrule.InspectionSheetSamplingRuleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.inspectionsheetsamplingrule.InspectionSheetSamplingRuleMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 检验单抽样规则（检验抽样方案）关系 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class InspectionSheetSamplingRuleServiceImpl implements InspectionSheetSamplingRuleService {

    @Resource
    private InspectionSheetSamplingRuleMapper inspectionSheetSamplingRuleMapper;

    @Override
    public String createInspectionSheetSamplingRule(InspectionSheetSamplingRuleSaveReqVO createReqVO) {
        // 插入
        InspectionSheetSamplingRuleDO inspectionSheetSamplingRule = BeanUtils.toBean(createReqVO, InspectionSheetSamplingRuleDO.class);
        inspectionSheetSamplingRuleMapper.insert(inspectionSheetSamplingRule);
        // 返回
        return inspectionSheetSamplingRule.getId();
    }

    @Override
    public void updateInspectionSheetSamplingRule(InspectionSheetSamplingRuleSaveReqVO updateReqVO) {
        // 校验存在
        validateInspectionSheetSamplingRuleExists(updateReqVO.getId());
        // 更新
        InspectionSheetSamplingRuleDO updateObj = BeanUtils.toBean(updateReqVO, InspectionSheetSamplingRuleDO.class);
        inspectionSheetSamplingRuleMapper.updateById(updateObj);
    }

    @Override
    public void deleteInspectionSheetSamplingRule(String id) {
        // 校验存在
        validateInspectionSheetSamplingRuleExists(id);
        // 删除
        inspectionSheetSamplingRuleMapper.deleteById(id);
    }

    private void validateInspectionSheetSamplingRuleExists(String id) {
        if (inspectionSheetSamplingRuleMapper.selectById(id) == null) {
            throw exception(INSPECTION_SHEET_SAMPLING_RULE_NOT_EXISTS);
        }
    }

    @Override
    public InspectionSheetSamplingRuleDO getInspectionSheetSamplingRule(String id) {
        return inspectionSheetSamplingRuleMapper.selectById(id);
    }

    @Override
    public PageResult<InspectionSheetSamplingRuleDO> getInspectionSheetSamplingRulePage(InspectionSheetSamplingRulePageReqVO pageReqVO) {
        return inspectionSheetSamplingRuleMapper.selectPage(pageReqVO);
    }

}