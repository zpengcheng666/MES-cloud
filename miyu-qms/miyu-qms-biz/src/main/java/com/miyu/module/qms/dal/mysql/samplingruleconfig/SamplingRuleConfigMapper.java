package com.miyu.module.qms.dal.mysql.samplingruleconfig;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.qms.dal.dataobject.inspectionscheme.InspectionSchemeDO;
import com.miyu.module.qms.dal.dataobject.samplingrule.SamplingRuleDO;
import com.miyu.module.qms.dal.dataobject.samplingruleconfig.SamplingRuleConfigDO;
import com.miyu.module.qms.dal.dataobject.samplingstandard.SamplingStandardDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.samplingruleconfig.vo.*;

/**
 * 抽样规则（检验抽样方案） Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SamplingRuleConfigMapper extends BaseMapperX<SamplingRuleConfigDO> {

    default PageResult<SamplingRuleConfigDO> selectPage(SamplingRuleConfigPageReqVO reqVO) {

        MPJLambdaWrapperX<SamplingRuleConfigDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(SamplingStandardDO.class, SamplingStandardDO::getId, SamplingRuleConfigDO::getSamplingStandardId)
                .selectAs(SamplingStandardDO::getName, SamplingRuleConfigDO::getSamplingStandardName)
                .selectAll(SamplingRuleConfigDO.class);


        return selectPage(reqVO, wrapperX
                .eqIfPresent(SamplingRuleConfigDO::getSampleSizeCode, reqVO.getSampleSizeCode())
                .eqIfPresent(SamplingRuleConfigDO::getSamplingRuleType, reqVO.getSamplingRuleType())
                .eqIfPresent(SamplingRuleConfigDO::getSamplingStandardId, reqVO.getSamplingStandardId())
                .eqIfPresent(SamplingRuleConfigDO::getAcceptanceQualityLimit, reqVO.getAcceptanceQualityLimit())
                .orderByDesc(SamplingRuleConfigDO::getId));
    }


    default List<SamplingRuleConfigDO> getSamplingRuleConfigList(String code, String standardId, Integer samplingRuleType) {

        return selectList(new MPJLambdaWrapperX<SamplingRuleConfigDO>().eqIfPresent(SamplingRuleConfigDO::getSampleSizeCode, code)
                .eqIfPresent(SamplingRuleConfigDO::getSamplingStandardId, standardId)
                .eqIfPresent(SamplingRuleConfigDO::getSamplingRuleType, samplingRuleType));

    }

}