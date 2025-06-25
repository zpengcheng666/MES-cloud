package com.miyu.module.qms.dal.mysql.inspectionsheetscheme;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miyu.module.qms.controller.admin.analysis.vo.AnalysisReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetTaskPageReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo.InspectionSchemeTerminalReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo.InspectionSheetSchemePageReqVO;
import com.miyu.module.qms.dal.dataobject.inspectionscheme.InspectionSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheet.InspectionSheetDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import com.miyu.module.qms.enums.InspectionSheetSchemeStatusEnum;
import com.miyu.module.qms.enums.InspectionSheetStatusEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 检验单方案任务计划 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface InspectionSheetSchemeMapper extends BaseMapperX<InspectionSheetSchemeDO> {

    default PageResult<InspectionSheetSchemeDO> selectPage(InspectionSheetSchemePageReqVO reqVO) {

        MPJLambdaWrapperX<InspectionSheetSchemeDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSheetDO.class, InspectionSheetDO::getId, InspectionSheetSchemeDO::getInspectionSheetId)
                .selectAs(InspectionSheetDO:: getSheetName, InspectionSheetSchemeDO::getSheetName)
                .selectAs(InspectionSheetDO:: getSheetNo, InspectionSheetSchemeDO::getSheetNo)
                .selectAs(InspectionSheetDO:: getRecordNumber, InspectionSheetSchemeDO::getRecordNumber)
                .selectAll(InspectionSheetSchemeDO.class);

        wrapperX.like(reqVO.getSheetName() != null, InspectionSheetDO::getSheetName, reqVO.getSheetName())
                .eq(reqVO.getSheetNo() != null, InspectionSheetDO::getSheetNo, reqVO.getSheetNo())
                .eq(reqVO.getStatus() != null, InspectionSheetDO::getStatus, reqVO.getStatus())
                .between(reqVO.getBeginTime() != null, InspectionSheetSchemeDO::getBeginTime, reqVO.getBeginTime() != null ? reqVO.getBeginTime()[0] : null, reqVO.getBeginTime() != null ? reqVO.getBeginTime()[1] : null)
                .between(reqVO.getEndTime() != null, InspectionSheetSchemeDO::getEndTime, reqVO.getEndTime() != null ? reqVO.getEndTime()[0] : null, reqVO.getEndTime() != null ? reqVO.getEndTime()[1] : null)
                .between(reqVO.getCreateTime() != null, InspectionSheetSchemeDO::getCreateTime, reqVO.getCreateTime() != null ? reqVO.getCreateTime()[0] : null, reqVO.getCreateTime() != null ? reqVO.getCreateTime()[1] : null)
                .eq(reqVO.getRecordNumber() != null, InspectionSheetDO::getStatus, reqVO.getRecordNumber())
                .eq(reqVO.getTechnologyId() != null, InspectionSheetSchemeDO::getTechnologyId, reqVO.getTechnologyId())
                .eq(reqVO.getSchemeType() != null, InspectionSheetSchemeDO::getSchemeType, reqVO.getSchemeType())
                .orderByDesc(InspectionSheetSchemeDO::getId);

        return selectPage(reqVO, wrapperX);
    }

    default int deleteByInspectionSheetId(String inspectionSheetId) {
        return delete(InspectionSheetSchemeDO::getInspectionSheetId, inspectionSheetId);
    }

    InspectionSheetSchemeDO selectByInspectionSheetId(String inspectionSheetId);


    default List<InspectionSheetSchemeDO> selectBySheetSchemeInfoById(String schemeId) {
        MPJLambdaWrapperX<InspectionSheetSchemeDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.select("(select count(1) from qms_inspection_sheet_scheme_material t1 where t1.inspection_sheet_scheme_id = '" + schemeId + "' and t1.deleted = 0) as quantity")
                .selectAll(InspectionSheetSchemeDO.class)
                .eq(InspectionSheetSchemeMaterialDO::getId, schemeId);
        return selectList(wrapperX);
    }

    default InspectionSheetSchemeDO getInspectionSheetInfoBySchemeId(String schemeId) {
        MPJLambdaWrapperX<InspectionSheetSchemeDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSheetDO.class, InspectionSheetDO::getId, InspectionSheetSchemeDO::getInspectionSheetId)
                .eq(InspectionSheetSchemeDO::getId, schemeId)
                .selectAs(InspectionSheetDO::getSheetName, InspectionSheetSchemeDO::getSheetName)
                .selectAs(InspectionSheetDO::getSheetNo, InspectionSheetSchemeDO::getSheetNo)
                .selectAs(InspectionSheetDO::getRecordNumber, InspectionSheetSchemeDO::getRecordNumber)
                .selectAs(InspectionSheetDO::getSourceType, InspectionSheetSchemeDO::getSourceType)
                .selectAll(InspectionSheetSchemeDO.class);
        return selectOne(wrapperX);

    }

    default List<InspectionSheetSchemeDO> getInspectionSheetSchemes(AnalysisReqVO vo) {

        MPJLambdaWrapperX<InspectionSheetSchemeDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSchemeDO.class, InspectionSchemeDO::getId, InspectionSheetSchemeDO::getInspectionSchemeId)
                .selectAs(InspectionSchemeDO::getAcceptanceQualityLimit, InspectionSheetSchemeDO::getAcceptanceQualityLimit)
                .selectAll(InspectionSheetSchemeDO.class);
        return selectList(wrapperX
                .eqIfPresent(InspectionSheetSchemeDO::getMaterialConfigId, vo.getMaterialConfigId())
                .eqIfPresent(InspectionSheetSchemeDO::getBatchNumber, vo.getBatchNumber())
                .betweenIfPresent(InspectionSheetSchemeDO::getBeginTime, vo.getQueryTime())
//                .eq(InspectionSheetSchemeDO::getSchemeType, 3)//成品检验单  TODO 排除自检单
                .isNull(InspectionSheetSchemeDO::getNeedFirstInspection)//排除首检
                .eq(InspectionSheetSchemeDO::getStatus, InspectionSheetSchemeStatusEnum.INSPECTED.getStatus())
        );
    }

    @Select("SELECT " +
            "a.id, " +
            "a.inspection_sheet_id, " +
            "b.record_number, " +
            "b.source_type, " +
            "a.quantity, " +
            "a.self_Inspection, " +
            "a.self_assignment_status, " +
            "a.scheme_type, " +
            "( SELECT bar_code FROM qms_inspection_sheet_scheme_material m WHERE m.inspection_sheet_scheme_id = a.id  and m.status is null limit 1) as barCode, " +
            "( SELECT GROUP_CONCAT(m.bar_code) FROM qms_inspection_sheet_scheme_material m WHERE m.inspection_sheet_scheme_id = a.id  and m.status is null) as barCodes, " +
            "( SELECT COUNT( 1 ) FROM qms_inspection_sheet_scheme_material m WHERE m.inspection_sheet_scheme_id = a.id and m.status is null) as toInspectionQuantity, " +
            "( SELECT COUNT( 1 ) FROM qms_inspection_sheet_scheme_material m WHERE m.inspection_sheet_scheme_id = a.id and m.status is not null) as inspectedQuantity, " +
            "( SELECT COUNT( 1 ) FROM qms_inspection_sheet_scheme_material m WHERE m.inspection_sheet_scheme_id = a.id and m.inspection_result = 1 and m.status is not null) as qualifiedQuantity, " +
            "( SELECT COUNT( 1 ) FROM qms_inspection_sheet_scheme_material m WHERE m.inspection_sheet_scheme_id = a.id and m.inspection_result = 2 and m.status is not null) AS unqualifiedQuantity " +
            "FROM " +
            "qms_inspection_sheet_scheme a " +
            "LEFT JOIN " +
            "qms_inspection_sheet b " +
            "ON " +
            "a.inspection_sheet_id = b.id " +
            "WHERE " +
            " 1=1 " +
            "AND a.deleted = 0 " +
            "AND ((a.assignment_id = #{assignmentId} and a.self_inspection is NULL) " +
//            "OR (a.assignment_id = null and a.self_inspection is NULL and a.assignment_team_id = #{deptId} and a.status = 0) " +
            "OR (a.assignment_id = #{assignmentId} and a.self_inspection = 1 and a.self_assignment_status = 0) " +
            "OR (a.mutual_assignment_id = #{assignmentId} and a.self_assignment_status = 1) " +
            "OR (a.spec_assignment_id = #{assignmentId} and a.self_assignment_status = 3)) " +
//            "AND a.process_id = #{processId}" +
            "AND (a.status = '1' or a.status = '2')")
    List<InspectionSheetSchemeDO> getInspectionSheetSchemeList4Terminal(InspectionSchemeTerminalReqVO reqVO);


    default List<InspectionSheetSchemeDO> getInspectionSheetSchemeAnalysis(AnalysisReqVO vo,Boolean process) {

        MPJLambdaWrapperX<InspectionSheetSchemeDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSchemeDO.class, InspectionSchemeDO::getId, InspectionSheetSchemeDO::getInspectionSchemeId)
                .selectAs(InspectionSchemeDO::getAcceptanceQualityLimit, InspectionSheetSchemeDO::getAcceptanceQualityLimit)
                .selectAll(InspectionSheetSchemeDO.class);
        if (process){
            wrapperX.isNotNull(InspectionSheetSchemeDO::getProcessId);
        }
        return selectList(wrapperX
                .eqIfPresent(InspectionSheetSchemeDO::getMaterialConfigId, vo.getMaterialConfigId())
                .eqIfPresent(InspectionSheetSchemeDO::getBatchNumber, vo.getBatchNumber())
                .betweenIfPresent(InspectionSheetSchemeDO::getBeginTime, vo.getQueryTime())
                .eqIfPresent(InspectionSheetSchemeDO::getSchemeType, vo.getSchemeType())
                .eq(InspectionSheetSchemeDO::getStatus, InspectionSheetSchemeStatusEnum.INSPECTED.getStatus())
        );
    }

    default List<InspectionSheetSchemeDO> getInspectionSheetSchemeAnalysisWorker(AnalysisReqVO vo) {

        MPJLambdaWrapperX<InspectionSheetSchemeDO> wrapperX = new MPJLambdaWrapperX<>();
//        wrapperX.select("( SELECT COUNT( 1 ) FROM qms_inspection_sheet_scheme_material m WHERE m.inspection_sheet_scheme_id = t.id and m.inspection_result = 1 and m.status = 1 and m.deleted = 0) as qualifiedQuantity")
//        .select("( SELECT COUNT( 1 ) FROM qms_inspection_sheet_scheme_material m WHERE m.inspection_sheet_scheme_id = t.id and m.inspection_result = 2 and m.status = 1 and m.deleted = 0) AS unqualifiedQuantity")
//        .select(InspectionSheetSchemeDO::getId)
//        .select(InspectionSheetSchemeDO::getAssignmentId)
//        .select(InspectionSheetSchemeDO::getQuantity);
        return selectList(wrapperX
                .eqIfPresent(InspectionSheetSchemeDO::getMaterialConfigId, vo.getMaterialConfigId())
                .eqIfPresent(InspectionSheetSchemeDO::getBatchNumber, vo.getBatchNumber())
                .betweenIfPresent(InspectionSheetSchemeDO::getBeginTime, vo.getQueryTime())
                .inIfPresent(InspectionSheetSchemeDO::getSchemeType, vo.getSchemeType())
                .eq(InspectionSheetSchemeDO::getStatus, InspectionSheetSchemeStatusEnum.INSPECTED.getStatus())
        );
    }

    /**
     * 查询检验任务
     * @param reqVO
     * @return
     */
    default PageResult<InspectionSheetSchemeDO> selectTaskPage(InspectionSheetTaskPageReqVO reqVO) {
        MPJLambdaWrapperX<InspectionSheetSchemeDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSheetDO.class, InspectionSheetDO::getId, InspectionSheetSchemeDO::getInspectionSheetId)
                .selectAll(InspectionSheetSchemeDO.class)
                .selectAs(InspectionSheetDO:: getSheetNo, InspectionSheetSchemeDO::getSheetNo)
                .selectAs(InspectionSheetDO:: getRecordNumber, InspectionSheetSchemeDO::getRecordNumber)
//                .eq(InspectionSheetSchemeDO::getSelfInspection, 1)
//                .eq(InspectionSheetSchemeDO::getSelfAssignmentStatus, 2);
                .or(w -> w.eq(InspectionSheetSchemeDO::getSelfInspection, 1).eq(InspectionSheetSchemeDO::getSelfAssignmentStatus, 2))
                .or(w -> w.isNull(InspectionSheetSchemeDO::getSelfInspection).eq(InspectionSheetSchemeDO::getStatus, InspectionSheetStatusEnum.TOASSIGN.getStatus()))
                 .apply("IF(t.assignment_team_id IS NOT NULL, t.assignment_type = 2 AND t.assignment_team_id = "+ reqVO.getDeptId() +", 1=1)")
                // .or(w -> w.isNull(InspectionSheetSchemeDO::getSelfInspection).eq(InspectionSheetSchemeDO::getStatus, InspectionSheetStatusEnum.TOASSIGN.getStatus()))
                .like(reqVO.getSheetName() != null, InspectionSheetDO::getSheetName, reqVO.getSheetName())
                .eq(reqVO.getSheetNo() != null, InspectionSheetDO::getSheetNo, reqVO.getSheetNo())
                .eq(reqVO.getRecordNumber() != null, InspectionSheetDO::getRecordNumber, reqVO.getRecordNumber())
                .eq(reqVO.getTechnologyId() != null, InspectionSheetSchemeDO::getTechnologyId, reqVO.getTechnologyId())
                .eq(reqVO.getSchemeType() != null, InspectionSheetSchemeDO::getSchemeType, reqVO.getSchemeType())
                .orderByDesc(InspectionSheetDO::getId);

        return selectPage(reqVO, wrapperX);
    }

    /**
     * 查询待认领检验任务
     * @param reqVO
     * @return
     */
    default PageResult<InspectionSheetSchemeDO> selectClaimTaskList(InspectionSheetTaskPageReqVO reqVO) {
        MPJLambdaWrapperX<InspectionSheetSchemeDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSheetDO.class, InspectionSheetDO::getId, InspectionSheetSchemeDO::getInspectionSheetId)
                .selectAll(InspectionSheetSchemeDO.class)
                .selectAs(InspectionSheetDO:: getSheetNo, InspectionSheetSchemeDO::getSheetNo)
                .selectAs(InspectionSheetDO:: getRecordNumber, InspectionSheetSchemeDO::getRecordNumber)
                .or(w -> w.eq(InspectionSheetSchemeDO::getSelfInspection, 1).eq(InspectionSheetSchemeDO::getSelfAssignmentStatus, 2))
                .or(w -> w.isNull(InspectionSheetSchemeDO::getSelfInspection).eq(InspectionSheetSchemeDO::getStatus, InspectionSheetStatusEnum.TOASSIGN.getStatus()))
                .apply("IF(t.assignment_team_id IS NOT NULL, t.assignment_type = 2 AND t.assignment_team_id = "+ reqVO.getDeptId() +", 1=1)")
                .orderByDesc(InspectionSheetDO::getId);
        return selectPage(reqVO, wrapperX);
    }

    /**
     * 查询待检验任务
     * @param reqVO
     * @return
     */

    @Select("SELECT " +
            "a.*, " +
            "b.sheet_no, " +
            "b.record_number, " +
            "b.source_type " +
            "FROM " +
            "qms_inspection_sheet_scheme a " +
            "LEFT JOIN " +
            "qms_inspection_sheet b " +
            "ON " +
            "a.inspection_sheet_id = b.id " +
            "WHERE " +
            " 1=1 " +
            "AND a.deleted = 0 " +
            "AND ((a.assignment_id = #{assignmentId} and a.self_inspection is NULL) " +
//            "OR (a.assignment_id = null and a.self_inspection is NULL and a.assignment_team_id = #{deptId} and a.status = 0) " +
            "OR (a.assignment_id = #{assignmentId} and a.self_inspection = 1 and a.self_assignment_status = 0) " +
            "OR (a.mutual_assignment_id = #{assignmentId} and a.self_assignment_status = 1) " +
            "OR (a.spec_assignment_id = #{assignmentId} and a.self_assignment_status = 3)) " +
            "AND (a.status = '1' or a.status = '2')")
    IPage<InspectionSheetSchemeDO> selectInspectionTaskList(Page<?> page,  @Param("assignmentId") String assignmentId);


    /**
     * 查询不合格检验任务
     * @param reqVO
     * @return
     */
    @Select("SELECT " +
            "a.*, " +
            "b.sheet_no, " +
            "b.record_number " +
            "FROM " +
            "qms_inspection_sheet_scheme a " +
            "LEFT JOIN " +
            "qms_inspection_sheet b " +
            "ON " +
            "a.inspection_sheet_id = b.id " +
            "WHERE " +
            " 1=1 " +
            "AND a.deleted = 0 " +
            "AND (SELECT COUNT( 1 ) FROM qms_inspection_sheet_scheme_material m WHERE m.inspection_sheet_scheme_id = a.id and ifnull(ifnull(m.spec_inspection_result, m.mutual_inspection_result), m.inspection_result) = '2' and m.status = '1' and m.deleted = '0')  > 0 " +
//            "AND ((a.assignment_id = #{assignmentId} and a.self_inspection is NULL) " +
//            "OR (a.spec_assignment_id = #{assignmentId} and a.self_inspection is NOT NULL)) " +
            "AND ((a.process_status not in ('1','2'))) " +
            "AND (a.status = '3')")
    IPage<InspectionSheetSchemeDO> selectUnqualifiedTaskList(Page<?> page, @Param("assignmentId") String assignmentId);
}
