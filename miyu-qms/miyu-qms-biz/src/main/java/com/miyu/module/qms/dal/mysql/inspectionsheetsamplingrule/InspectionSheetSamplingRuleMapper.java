package com.miyu.module.qms.dal.mysql.inspectionsheetsamplingrule;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.qms.dal.dataobject.inspectionsheetsamplingrule.InspectionSheetSamplingRuleDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.inspectionsheetsamplingrule.vo.*;

/**
 * 检验单抽样规则（检验抽样方案）关系 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface InspectionSheetSamplingRuleMapper extends BaseMapperX<InspectionSheetSamplingRuleDO> {

    default PageResult<InspectionSheetSamplingRuleDO> selectPage(InspectionSheetSamplingRulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InspectionSheetSamplingRuleDO>()
                .betweenIfPresent(InspectionSheetSamplingRuleDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(InspectionSheetSamplingRuleDO::getInspectionSheetSchemeId, reqVO.getInspectionSheetSchemeId())
                .eqIfPresent(InspectionSheetSamplingRuleDO::getInspectionSchemeItemId, reqVO.getInspectionSchemeItemId())
                .eqIfPresent(InspectionSheetSamplingRuleDO::getSamplingRuleConfigId, reqVO.getSamplingRuleConfigId())
                .eqIfPresent(InspectionSheetSamplingRuleDO::getSamplingStandardId, reqVO.getSamplingStandardId())
                .orderByDesc(InspectionSheetSamplingRuleDO::getId));
    }

}