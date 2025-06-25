package com.miyu.module.qms.dal.mysql.samplingrule;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.qms.dal.dataobject.samplingrule.SamplingRuleDO;
import com.miyu.module.qms.dal.dataobject.samplingruleconfig.SamplingRuleConfigDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.samplingrule.vo.*;

/**
 * 抽样规则 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SamplingRuleMapper extends BaseMapperX<SamplingRuleDO> {

    default PageResult<SamplingRuleDO> selectPage(SamplingRulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SamplingRuleDO>()
                .betweenIfPresent(SamplingRuleDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(SamplingRuleDO::getSamplingStandardId, reqVO.getSamplingStandardId())
                .eqIfPresent(SamplingRuleDO::getSampleSizeCode, reqVO.getSampleSizeCode())
                .eqIfPresent(SamplingRuleDO::getMinValue, reqVO.getMinValue())
                .eqIfPresent(SamplingRuleDO::getMaxValue, reqVO.getMaxValue())
                .eqIfPresent(SamplingRuleDO::getInspectionLevelType, reqVO.getInspectionLevelType())
                .orderByDesc(SamplingRuleDO::getId));
    }


    default List<SamplingRuleDO> getSamplingRuleByStandardId(String standardId){
        MPJLambdaWrapperX<SamplingRuleDO> wrapperX = new MPJLambdaWrapperX<>();

        return selectList(wrapperX.eqIfPresent(SamplingRuleDO::getSamplingStandardId, standardId));
    }

    default List<SamplingRuleDO> getSamplingRuleByLevelType(String standardId, Integer levelType){
        MPJLambdaWrapperX<SamplingRuleDO> wrapperX = new MPJLambdaWrapperX<>();
        return selectList(wrapperX.eqIfPresent(SamplingRuleDO::getSamplingStandardId, standardId).eqIfPresent(SamplingRuleDO::getInspectionLevelType, levelType));
    }

}