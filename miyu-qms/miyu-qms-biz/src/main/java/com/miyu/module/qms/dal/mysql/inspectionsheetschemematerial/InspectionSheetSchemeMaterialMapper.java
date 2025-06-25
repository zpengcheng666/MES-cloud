package com.miyu.module.qms.dal.mysql.inspectionsheetschemematerial;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.qms.controller.admin.analysis.vo.AnalysisReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetMaterialReqVO;
import com.miyu.module.qms.dal.dataobject.inspectionsheet.InspectionSheetDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import com.miyu.module.qms.dal.dataobject.unqualifiedmaterial.UnqualifiedMaterialDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo.*;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 检验单产品 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface InspectionSheetSchemeMaterialMapper extends BaseMapperX<InspectionSheetSchemeMaterialDO> {

    default PageResult<InspectionSheetSchemeMaterialDO> selectPage(InspectionSheetSchemeMaterialPageReqVO reqVO) {
        MPJLambdaWrapperX<InspectionSheetSchemeMaterialDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSheetSchemeDO.class, InspectionSheetSchemeDO::getId, InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId)
                .selectAs(InspectionSheetSchemeDO::getMaterialNumber, InspectionSheetSchemeMaterialDO::getMaterialNumber)
                .selectAs(InspectionSheetSchemeDO::getMaterialName, InspectionSheetSchemeMaterialDO::getMaterialName)
                .selectAll(InspectionSheetSchemeMaterialDO.class)
                .eq(reqVO.getMaterialNumber() != null, InspectionSheetSchemeDO::getMaterialNumber, reqVO.getMaterialNumber());

        return selectPage(reqVO, wrapperX
                .betweenIfPresent(InspectionSheetSchemeMaterialDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(InspectionSheetSchemeMaterialDO::getContent, reqVO.getContent())
                .eqIfPresent(InspectionSheetSchemeMaterialDO::getInspectionResult, reqVO.getInspectionResult())
                .eqIfPresent(InspectionSheetSchemeMaterialDO::getMaterialId, reqVO.getMaterialId())
                .eqIfPresent(InspectionSheetSchemeMaterialDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(InspectionSheetSchemeMaterialDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(InspectionSheetSchemeMaterialDO::getBatchNumber, reqVO.getBatchNumber())
                .eqIfPresent(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, reqVO.getInspectionSheetSchemeId())
                .notIn(reqVO.getExcludeIds() != null && reqVO.getExcludeIds().size() > 0, InspectionSheetSchemeMaterialDO::getId, reqVO.getExcludeIds())
                .orderByDesc(InspectionSheetSchemeMaterialDO::getId));
    }

    default int deleteByInspectionSheetSchemeId(String inspectionSheetSchemeId) {
        return delete(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, inspectionSheetSchemeId);
    }


    /**
     * 物料条码和批次号获得检验单信息
     * @param reqVO
     * @return
     */
    default List<InspectionSheetSchemeMaterialDO> getInspectionSheetInfoMaterial(InspectionSheetMaterialReqVO reqVO) {
        MPJLambdaWrapperX<InspectionSheetSchemeMaterialDO> wrapperX = new MPJLambdaWrapperX<>();

        wrapperX.leftJoin(InspectionSheetSchemeDO.class, InspectionSheetSchemeDO::getId, InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId)
                .leftJoin(InspectionSheetDO.class, InspectionSheetDO::getId, InspectionSheetSchemeDO::getInspectionSheetId)
                .selectAs(InspectionSheetSchemeDO::getMaterialNumber, InspectionSheetSchemeMaterialDO::getMaterialNumber)
//                .selectAs(InspectionSheetSchemeDO::getMaterialCode, InspectionSheetSchemeMaterialDO::getMaterialCode)
//                .selectAs(InspectionSheetSchemeDO::getMaterialName, InspectionSheetSchemeMaterialDO::getMaterialName)
//                .selectAs(InspectionSheetSchemeDO::getMaterialProperty, InspectionSheetSchemeMaterialDO::getMaterialProperty)
//                .selectAs(InspectionSheetSchemeDO::getMaterialType, InspectionSheetSchemeMaterialDO::getMaterialType)
//                .selectAs(InspectionSheetSchemeDO::getMaterialSpecification, InspectionSheetSchemeMaterialDO::getMaterialSpecification)
//                .selectAs(InspectionSheetSchemeDO::getMaterialBrand, InspectionSheetSchemeMaterialDO::getMaterialBrand)
//                .selectAs(InspectionSheetSchemeDO::getMaterialUnit, InspectionSheetSchemeMaterialDO::getMaterialUnit)
                .selectAs(InspectionSheetSchemeDO::getAssignmentId, InspectionSheetSchemeMaterialDO::getAssignmentId)
                .selectAs(InspectionSheetSchemeDO::getAssignmentDate, InspectionSheetSchemeMaterialDO::getAssignmentDate)
                .selectAs(InspectionSheetSchemeDO::getTechnologyId, InspectionSheetSchemeMaterialDO::getTechnologyId)
                .selectAs(InspectionSheetSchemeDO::getProcessId, InspectionSheetSchemeMaterialDO::getProcessId)
                .selectAs(InspectionSheetDO::getSheetNo, InspectionSheetSchemeMaterialDO::getSheetNo)
                .selectAs(InspectionSheetDO::getSheetName, InspectionSheetSchemeMaterialDO::getSheetName)
                .selectAll(InspectionSheetSchemeMaterialDO.class);

        return selectList(wrapperX.eqIfPresent(InspectionSheetSchemeMaterialDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(InspectionSheetSchemeMaterialDO::getBatchNumber, reqVO.getBatchNumber()));
    }

    default List<InspectionSheetSchemeMaterialDO> getInspectionMaterialListBySchemeIds(List<String> schemeIds) {
        MPJLambdaWrapperX<InspectionSheetSchemeMaterialDO> wrapperX = new MPJLambdaWrapperX<>();

        wrapperX.leftJoin(InspectionSheetSchemeDO.class, InspectionSheetSchemeDO::getId, InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId)
                .leftJoin(InspectionSheetDO.class, InspectionSheetDO::getId, InspectionSheetSchemeDO::getInspectionSheetId)
                .selectAs(InspectionSheetDO::getRecordNumber, InspectionSheetSchemeMaterialDO::getRecordNumber)
                .selectAll(InspectionSheetSchemeMaterialDO.class)
                .in(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, schemeIds);

        return selectList(wrapperX);
    }

    @Select("SELECT b.id, a.bar_code_check, b.handle_method, b.defective_level, " +
            "(SELECT GROUP_CONCAT( d.NAME ) " +
            "FROM qms_unqualified_registration c " +
            "LEFT JOIN qms_defective_code d ON d.id = c.defective_code " +
            "WHERE " +
            "c.unqualified_material_id = b.id " +
            "and c.deleted = 0) as defectiveCode " +
            "FROM " +
            "qms_inspection_sheet_scheme_material a " +
            "LEFT JOIN qms_unqualified_material b ON b.scheme_material_id = a.id " +
            "AND b.inspection_sheet_scheme_id = a.inspection_sheet_scheme_id " +
            "WHERE " +
            "ifnull(ifnull(a.spec_inspection_result, a.mutual_inspection_result), a.inspection_result) = 2 " +
            "AND a.inspection_sheet_scheme_id = #{schemeId}")
   List<InspectionSheetSchemeMaterialDO> selectUnqualifiedMaterialDefectiveListBySchemeId(String schemeId);


    default List<InspectionSheetSchemeMaterialDO> getMaterialsByAnalysis(AnalysisReqVO vo){

        MPJLambdaWrapperX<InspectionSheetSchemeMaterialDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(UnqualifiedMaterialDO.class, UnqualifiedMaterialDO::getSchemeMaterialId, InspectionSheetSchemeMaterialDO::getId)
                .selectAs(UnqualifiedMaterialDO::getHandleMethod, InspectionSheetSchemeMaterialDO::getHandleMethod)
                .selectAll(InspectionSheetSchemeMaterialDO.class);

        return selectList(wrapperX.betweenIfPresent(InspectionSheetSchemeMaterialDO::getUpdateTime,vo.getQueryTime())
                .eqIfPresent(InspectionSheetSchemeMaterialDO::getMaterialConfigId,vo.getMaterialConfigId())
                .eqIfPresent(InspectionSheetSchemeMaterialDO::getBatchNumber,vo.getBatchNumber())
                .isNotNull(UnqualifiedMaterialDO::getHandleMethod)
        );
    }

    @Select("SELECT a.* " +
            "FROM " +
            "qms_inspection_sheet_scheme_material a " +
            "WHERE " +
            "a.inspection_sheet_scheme_id = #{schemeId}" +
            "AND ifnull(ifnull(a.spec_inspection_result, a.mutual_inspection_result), a.inspection_result) = 2 " +
            "AND a.deleted = 0")
    List<InspectionSheetSchemeMaterialDO> getUnqualifiedMaterialListBySchemeId(String schemeId);
}
