package com.miyu.module.qms.dal.mysql.inspectionsheet;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.qms.dal.dataobject.inspectionsheet.InspectionSheetDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import com.miyu.module.qms.enums.InspectionSheetStatusEnum;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.*;

/**
 * 检验单 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface InspectionSheetMapper extends BaseMapperX<InspectionSheetDO> {

    default PageResult<InspectionSheetDO> selectPage(InspectionSheetPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InspectionSheetDO>()
                .betweenIfPresent(InspectionSheetDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(InspectionSheetDO::getSheetName, reqVO.getSheetName())
                .eqIfPresent(InspectionSheetDO::getSheetNo, reqVO.getSheetNo())
                .eqIfPresent(InspectionSheetDO::getStatus, reqVO.getStatus())
                .eqIfPresent(InspectionSheetDO::getHeader, reqVO.getHeader())
                .betweenIfPresent(InspectionSheetDO::getBeginTime, reqVO.getBeginTime())
                .betweenIfPresent(InspectionSheetDO::getEndTime, reqVO.getEndTime())
                .orderByDesc(InspectionSheetDO::getId));
    }

    default InspectionSheetDO getInspectionSheetInfo(InspectionSheetReqVO reqVO) {
        MPJLambdaWrapperX<InspectionSheetDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSheetSchemeDO.class, InspectionSheetSchemeDO::getInspectionSheetId, InspectionSheetDO::getId)
                //.leftJoin(InspectionSheetSchemeMaterialDO.class, InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, InspectionSheetSchemeDO::getInspectionSchemeId)
                .leftJoin(InspectionSheetSchemeMaterialDO.class, on -> on.eq(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, InspectionSheetSchemeDO::getId).eq(InspectionSheetSchemeMaterialDO::getId, reqVO.getSheetSchemeMaterialId()))
                .eq(InspectionSheetSchemeDO::getId, reqVO.getSheetSchemeId())
                .selectAs(InspectionSheetSchemeMaterialDO::getMaterialId, InspectionSheetDO::getMaterialId)
                .selectAs(InspectionSheetSchemeMaterialDO::getBarCode, InspectionSheetDO::getBarCode)
//                .selectAs(InspectionSheetSchemeMaterialDO::getInspectionResult, InspectionSheetDO::getInspectionResult)
//                .selectAs(InspectionSheetSchemeMaterialDO::getMutualInspectionResult, InspectionSheetDO::getInspectionResult)
//                .selectAs(InspectionSheetSchemeMaterialDO::getSpecInspectionResult, InspectionSheetDO::getInspectionResult)
                .selectAs("ifnull(ifnull(t2.spec_inspection_result, t2.mutual_inspection_result), t2.inspection_result)", InspectionSheetDO::getInspectionResult)
                .selectAs(InspectionSheetSchemeMaterialDO::getContent, InspectionSheetDO::getContent)
                .selectAs(InspectionSheetSchemeDO::getMaterialConfigId, InspectionSheetDO::getMaterialConfigId)
                .selectAs(InspectionSheetSchemeDO::getMaterialNumber, InspectionSheetDO::getMaterialNumber)
                .selectAs(InspectionSheetSchemeDO::getSchemeType, InspectionSheetDO::getSchemeType)
                .selectAs(InspectionSheetSchemeDO::getInspectionSheetType, InspectionSheetDO::getInspectionSheetType)
                .selectAs(InspectionSheetSchemeDO::getAssignmentType, InspectionSheetDO::getAssignmentType)
                .selectAs(InspectionSheetSchemeDO::getAssignmentId, InspectionSheetDO::getAssignmentId)
                .selectAs(InspectionSheetSchemeDO::getAssignmentDate, InspectionSheetDO::getAssignmentDate)
                .selectAs(InspectionSheetSchemeDO::getStatus, InspectionSheetDO::getSchemeStatus)
                .selectAs(InspectionSheetSchemeDO::getSelfInspection, InspectionSheetDO::getSelfInspection)
                .selectAs(InspectionSheetSchemeDO::getSelfAssignmentStatus, InspectionSheetDO::getSelfAssignmentStatus)
                .selectAs(InspectionSheetSchemeDO::getTechnologyId, InspectionSheetDO::getTechnologyId)
                .selectAs(InspectionSheetSchemeDO::getProcessId, InspectionSheetDO::getProcessId)
                .selectAll(InspectionSheetDO.class);


        return selectOne(wrapperX);

    }

    default InspectionSheetDO getInspectionSheetInfoBySchemeId(String schemeId) {
        MPJLambdaWrapperX<InspectionSheetDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSheetSchemeDO.class, InspectionSheetSchemeDO::getInspectionSheetId, InspectionSheetDO::getId)
                .eq(InspectionSheetSchemeDO::getId, schemeId)
                .selectAs(InspectionSheetSchemeDO::getSchemeType, InspectionSheetDO::getSchemeType)
                .selectAs(InspectionSheetSchemeDO::getInspectionSheetType, InspectionSheetDO::getInspectionSheetType)
                .selectAs(InspectionSheetSchemeDO::getAssignmentType, InspectionSheetDO::getAssignmentType)
                .selectAs(InspectionSheetSchemeDO::getAssignmentId, InspectionSheetDO::getAssignmentId)
                .selectAs(InspectionSheetSchemeDO::getAssignmentDate, InspectionSheetDO::getAssignmentDate)
                .selectAs(InspectionSheetSchemeDO::getInspectionResult, InspectionSheetDO::getInspectionResult)
                .selectAs(InspectionSheetSchemeDO::getQualifiedQuantity, InspectionSheetDO::getQualifiedQuantity)
                .selectAs(InspectionSheetSchemeDO::getInspectionQuantity, InspectionSheetDO::getInspectionQuantity)
                .selectAs(InspectionSheetSchemeDO::getQuantity, InspectionSheetDO::getQuantity)
                .selectAs(InspectionSheetSchemeDO::getPassRule, InspectionSheetDO::getPassRule)
                .selectAs(InspectionSheetSchemeDO::getMaterialNumber, InspectionSheetDO::getMaterialNumber)
//                .selectAs(InspectionSheetSchemeDO::getMaterialCode, InspectionSheetDO::getMaterialCode)
                .selectAs(InspectionSheetSchemeDO::getMaterialName, InspectionSheetDO::getMaterialName)
//                .selectAs(InspectionSheetSchemeDO::getMaterialProperty, InspectionSheetDO::getMaterialProperty)
//                .selectAs(InspectionSheetSchemeDO::getMaterialType, InspectionSheetDO::getMaterialType)
//                .selectAs(InspectionSheetSchemeDO::getMaterialSpecification, InspectionSheetDO::getMaterialSpecification)
//                .selectAs(InspectionSheetSchemeDO::getMaterialBrand, InspectionSheetDO::getMaterialBrand)
//                .selectAs(InspectionSheetSchemeDO::getMaterialUnit, InspectionSheetDO::getMaterialUnit)
                .selectAll(InspectionSheetDO.class);
        return selectOne(wrapperX);

    }
}
