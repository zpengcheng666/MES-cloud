package com.miyu.module.qms.dal.mysql.inspectionscheme;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.qms.dal.dataobject.inspectionscheme.InspectionSchemeDO;
import com.miyu.module.qms.dal.dataobject.samplingstandard.SamplingStandardDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.inspectionscheme.vo.*;

/**
 * 检验方案 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InspectionSchemeMapper extends BaseMapperX<InspectionSchemeDO> {

    default PageResult<InspectionSchemeDO> selectPage(InspectionSchemePageReqVO reqVO) {

        MPJLambdaWrapperX<InspectionSchemeDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(SamplingStandardDO.class, SamplingStandardDO::getId, InspectionSchemeDO::getSamplingStandardId)
                .selectAs(SamplingStandardDO::getName, InspectionSchemeDO::getSamplingStandardName)
                .selectAll(InspectionSchemeDO.class);

        // 物料类别不为空且物料类型集合为空
        if(reqVO.getMaterialTypeId() != null && reqVO.getMaterialConfigIds().size() == 0){
            wrapperX.apply("1=2");
        }

        return selectPage(reqVO, wrapperX
                .betweenIfPresent(InspectionSchemeDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(InspectionSchemeDO::getSchemeName, reqVO.getSchemeName())
                .likeIfPresent(InspectionSchemeDO::getSchemeNo, reqVO.getSchemeNo())
                .eqIfPresent(InspectionSchemeDO::getSchemeType, reqVO.getSchemeType())
//                .eqIfPresent(InspectionSchemeDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(InspectionSchemeDO::getInspectionLevel, reqVO.getInspectionLevel())
                .eqIfPresent(InspectionSchemeDO::getIsEffective, reqVO.getIsEffective())
                .likeIfPresent(InspectionSchemeDO::getMaterialNumber, reqVO.getMaterialNumber())
                .likeIfPresent(InspectionSchemeDO::getMaterialName, reqVO.getMaterialName())
                .eqIfPresent(InspectionSchemeDO::getTechnologyId, reqVO.getTechnologyId())
                .eqIfPresent(InspectionSchemeDO::getProcessId, reqVO.getProcessId())
                .eqIfPresent(InspectionSchemeDO::getSamplingStandardId, reqVO.getSamplingStandardId())
                .in(reqVO.getMaterialConfigIds() != null && reqVO.getMaterialConfigIds().size() > 0, InspectionSchemeDO::getMaterialConfigId, reqVO.getMaterialConfigIds())
                .orderByDesc(InspectionSchemeDO::getId));
    }

    /**
     * 检验单获取检验方案
     * @param reqVO
     * @return
     */

    default List<InspectionSchemeDO> selectScheme4InspectionSheet(InspectionSchemeReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<InspectionSchemeDO>()
                .eqIfPresent(InspectionSchemeDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(InspectionSchemeDO::getSchemeType, reqVO.getSchemeType())
                .eqIfPresent(InspectionSchemeDO::getTechnologyId, reqVO.getTechnologyId())
                .eqIfPresent(InspectionSchemeDO::getProcessId, reqVO.getProcessId())
                .eq(InspectionSchemeDO::getIsEffective, "1")
                .orderByDesc(InspectionSchemeDO::getId));
    }

}
