package com.miyu.module.qms.dal.mysql.unqualifiedregistration;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.qms.controller.admin.analysis.vo.AnalysisReqVO;
import com.miyu.module.qms.dal.dataobject.defectivecode.DefectiveCodeDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;
import com.miyu.module.qms.dal.dataobject.unqualifiedregistration.UnqualifiedRegistrationDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.unqualifiedregistration.vo.*;

import java.util.List;

/**
 * 不合格品登记 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface UnqualifiedRegistrationMapper extends BaseMapperX<UnqualifiedRegistrationDO> {

    default PageResult<UnqualifiedRegistrationDO> selectPage(UnqualifiedRegistrationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UnqualifiedRegistrationDO>()
                .betweenIfPresent(UnqualifiedRegistrationDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(UnqualifiedRegistrationDO::getDefectiveCode, reqVO.getDefectiveCode())
                .eqIfPresent(UnqualifiedRegistrationDO::getInspectionSheetSchemeId, reqVO.getInspectionSheetSchemeId())
                .orderByDesc(UnqualifiedRegistrationDO::getId));
    }


    default List<UnqualifiedRegistrationDO> getDefectives(AnalysisReqVO vo) {
        MPJLambdaWrapperX<UnqualifiedRegistrationDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(DefectiveCodeDO.class, DefectiveCodeDO::getId, UnqualifiedRegistrationDO::getDefectiveCode)
                .leftJoin(InspectionSheetSchemeDO.class, InspectionSheetSchemeDO::getId, UnqualifiedRegistrationDO::getInspectionSheetSchemeId)
                .ne(InspectionSheetSchemeDO::getSchemeType,1)
                .selectAs(DefectiveCodeDO::getName, UnqualifiedRegistrationDO::getName)
                .selectAs(InspectionSheetSchemeDO::getBatchNumber, UnqualifiedRegistrationDO::getBatchNumber)
                .selectAs(InspectionSheetSchemeDO::getSchemeType, UnqualifiedRegistrationDO::getSchemeType)
                .selectAs(InspectionSheetSchemeDO::getMaterialConfigId, UnqualifiedRegistrationDO::getMaterialConfigId);

        if (!org.springframework.util.StringUtils.isEmpty(vo.getBatchNumber())){
            wrapperX.eq(InspectionSheetSchemeDO::getBatchNumber,vo.getBatchNumber());
        }
        if (!StringUtils.isEmpty(vo.getMaterialConfigId())){
            wrapperX.eq(InspectionSheetSchemeDO::getMaterialConfigId,vo.getMaterialConfigId());
        }
        wrapperX.selectAll(UnqualifiedRegistrationDO.class);
        return selectList(wrapperX
                .betweenIfPresent(UnqualifiedRegistrationDO::getCreateTime, vo.getQueryTime()));

    }
}