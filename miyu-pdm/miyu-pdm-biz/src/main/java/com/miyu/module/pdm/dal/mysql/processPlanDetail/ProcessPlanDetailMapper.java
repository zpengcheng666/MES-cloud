package com.miyu.module.pdm.dal.mysql.processPlanDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomRespVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessPlanDetailRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.ProcessPlanDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProcessPlanDetailMapper extends BaseMapperX<ProcessPlanDetailDO> {

    @Select("<script>" +
            " SELECT pb.part_version_id AS partVersionId," +
            " pm.part_number AS partNumber, " +
            " pm.part_name AS partName ," +
            " pv.part_version AS partVersion " +
            " FROM pdm_proj_part_bom pb " +
            " LEFT JOIN capp_process_task t ON pb.project_code = t.project_code AND pb.part_version_id = t.part_version_id " +
            " LEFT JOIN pdm_part_version pv ON pb.part_version_id = pv.id " +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id " +
            " WHERE 1=1 AND pb.deleted=0 " +
            " AND t.status='5' " +
            " <if test='projectCode != null and projectCode != \"\" '>" +
            " AND pb.project_code = #{projectCode}" +
            " </if>" +
            "</script>")
    List<ProcessPlanDetailDO> getPartListByProcessPlanDetailId(@Param("projectCode") String projectCode);

    @Select("<script>" +
            " SELECT" +
            " cp.process_code AS processCode," +
            " cp.process_scheme_code AS processSchemeCode," +
            " cv.process_name AS processName," +
            " cv.process_version AS processVersion," +
            " cv.id AS processVersionId," +
            " cv.process_instance_id AS processInstanceId," +
            " ct.project_code AS projectCode," +
            " rp.product_number AS productNumber," +
            " o.customized_index AS customizedIndex," +
            " o.rootproduct_id AS rootProductId ," +
            " cp.part_version_id AS partVersionId ," +
            " cv.property AS property," +
            " ct.reviewer AS reviewer," +
            " ct.reviewed_by AS reviewedBy," +
            " ct.deadline AS deadline," +
            " pm.part_number AS partNumber," +
            " pm.part_name AS partName," +
            " IFNULL(cv.status, '0') AS status" +
            " FROM capp_process_version cv" +
            " LEFT JOIN capp_process cp ON cv.process_id = cp.id" +
            " LEFT JOIN capp_process_task ct ON cp.part_version_id = ct.part_version_id" +
            " LEFT JOIN capp_pbom_process_version pppv ON pppv.process_version_id = cv.id" +
            " LEFT JOIN pdm_part_version pv ON ct.part_version_id = pv.id" +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " LEFT JOIN pdm_root_product rp ON pm.root_product_id = rp.id" +
            " LEFT JOIN pdm_data_object o ON pm.root_product_id = o.rootproduct_id AND o.std_data_object = 'PartInstance'" +
            " WHERE 1=1 AND cv.deleted=0 AND cp.deleted=0 AND pppv.deleted=0" +
            " AND cv.is_valid=0 " +
            "<if test='projectCode != null and projectCode != \"\"'>" +
            " and ct.project_code = #{projectCode}" +
            "</if>" +
            "<if test='partVersionId != null and partVersionId != \"\"'>" +
            " and ct.part_version_id = #{partVersionId}" +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            " and cv.status = #{status}" +
            "</if>" +
            "</script>")
    List<ProcessPlanDetailRespVO> selectProcessPlanDetailList(@Param("projectCode") String projectCode, @Param("partVersionId") String partVersionId, @Param("status") String status);

    @Select("<script>" +
            " SELECT" +
            " cp.process_code AS processCode," +
            " cp.process_scheme_code AS processSchemeCode," +
            " cv.process_name AS processName," +
            " cv.process_version AS processVersion," +
            " cv.id AS processVersionId," +
            " cv.process_instance_id AS processInstanceId," +
            " ct.project_code AS projectCode," +
            " rp.product_number AS productNumber," +
            " o.customized_index AS customizedIndex," +
            " o.rootproduct_id AS rootProductId ," +
            " cp.part_version_id AS partVersionId ," +
            " cv.property AS property," +
            " ct.reviewer AS reviewer," +
            " ct.reviewed_by AS reviewedBy," +
            " ct.deadline AS deadline," +
            " pm.part_number AS partNumber," +
            " pm.part_name AS partName," +
            " IFNULL(cv.status, '0') AS status" +
            " FROM capp_process_version cv" +
            " LEFT JOIN capp_process cp ON cv.process_id = cp.id" +
            " LEFT JOIN capp_process_task ct ON cp.part_version_id = ct.part_version_id" +
            " LEFT JOIN capp_pbom_process_version pppv ON pppv.process_version_id = cv.id" +
            " LEFT JOIN pdm_part_version pv ON ct.part_version_id = pv.id" +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " LEFT JOIN pdm_root_product rp ON pm.root_product_id = rp.id" +
            " LEFT JOIN pdm_data_object o ON pm.root_product_id = o.rootproduct_id AND o.std_data_object = 'PartInstance'" +
            " WHERE 1=1 AND cv.deleted=0 AND cp.deleted=0 AND pppv.deleted=0" +
            "<if test='processVersionId != null and processVersionId != \"\"'>" +
            " and cv.id = #{processVersionId}" +
            "</if>" +
            " order by cv.update_time desc" +
            "</script>")
    List<ProcessPlanDetailRespVO> selectProcessDetail(@Param("processVersionId") String processVersionId);

    @Select("<script>" +
            " SELECT * FROM ( " +
            " SELECT * FROM ( " +
            " SELECT" +
            " '0' AS type," +
            " procv.id," +
            " '0' AS parentId," +
            "  concat(" +
            " proc.process_code,'_',proc.process_scheme_code,'_',procv.process_version) AS name," +
            " '0' AS serialnum," +
            "  procv.id AS processVersionId," +
            "  proc.process_scheme_code AS processSchemeCode," +
            "  proc.id AS processId" +
            "  FROM capp_process proc" +
            "  LEFT JOIN capp_process_version procv ON procv.process_id = proc.id" +
            " where procv.deleted=0 and proc.deleted=0" +
            " ) tablea UNION" +
            " SELECT * FROM (" +
            " SELECT" +
            " '1' AS type," +
            " proce.id AS id," +
            " procv.id AS parentId," +
            " concat( proce.procedure_num, '(', proce.procedure_name, ')' ) AS name," +
            " proce.procedure_num AS serialnum," +
            " procv.id AS processVersionId," +
            " proc.process_scheme_code AS processSchemeCode," +
            " proc.id AS processId" +
            " FROM capp_procedure proce" +
            " LEFT JOIN capp_process_version procv ON procv.id = proce.process_version_id" +
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id" +
            " where proce.deleted=0 and procv.deleted=0 and proc.deleted=0" +
            " ) tabled UNION" +
            " SELECT * FROM (" +
            " SELECT" +
            " '2' AS type," +
            " step.id AS id," +
            " proce.id AS parentId," +
            " CONCAT(step.step_num, '(', IFNULL(step.step_name,''), ')' ) AS name," +
            " step.step_num AS serialnum," +
            " procv.id AS processVersionId," +
            " proc.process_scheme_code AS processSchemeCode," +
            " proc.id AS processId" +
            " FROM capp_step step" +
            " LEFT JOIN capp_procedure proce ON proce.id = step.procedure_id" +
            " LEFT JOIN capp_process_version procv ON procv.id = proce.process_version_id" +
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id" +
            " where step.deleted=0 and proce.deleted=0 and procv.deleted=0 and proc.deleted=0" +
            " ) tablec " +
            " ) tabled " +
            " WHERE 1=1  " +
            "<if test='processVersionId != null and processVersionId != \"\"'>" +
            "AND tabled.processVersionId = #{processVersionId} " +
            "</if>" +
            " ORDER BY CAST(serialnum AS UNSIGNED INT); " +
            "</script>")
    List<ProcessPlanDetailRespVO> selectProcessPlanDetailTreeList(@Param("processVersionId") String processVersionId);


    /**
     * 工艺规程数据
     */
    @Select("<script>" +
            " select distinct " +
            " cpv.id AS processVersionId ," +
            " cpv.process_name AS processName ," +
            " cpv.property ," +
            " cpv.process_version AS processVersion ," +
            " cpv.description AS description," +
            " cp.process_code AS processCode," +
            " cp.material_id AS materialId ," +
            " cp.material_number AS materialNumber ," +
            " cp.material_desg	AS materialDesg ," +
            " cp.material_code	AS materialCode ," +
            " cp.material_name	AS materialName ," +
            " cp.material_spec	AS materialSpec ," +
            " cp.material_specification AS	materialSpecification," +
            " cp.single_size AS singleSize ," +
            " cp.group_size  AS groupSize ," +
            " cp.process_route_name AS processRouteName ," +
            " cp.single_quatity AS singleQuatity ," +
            " cp.process_scheme_code AS processSchemeCode ," +
            " pm.part_number AS partNumber, " +
            " pm.part_name AS partName ," +
            " pv.part_version AS partVersion, " +
            " cp.process_condition AS processCondition " +
            " FROM capp_process_version cpv" +
            " LEFT JOIN capp_process cp ON cpv.process_id = cp.id" +
            " LEFT JOIN pdm_part_version pv ON cp.part_version_id = pv.id " +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id " +
            " where 1=1" +
            " and cpv.id = #{id}" +
            "</script>")
    ProcessPlanDetailRespVO selectProcessByPartVersionId(@Param("id") String id);

    /**
     * 工序各项数据
     */
    @Select("<script>" +
            " select" +
            " id," +
            " process_version_id AS processVersionId ," +
            " procedure_num	AS procedureNum," +
            " procedure_name AS procedureName," +
            " is_inspect AS isInspect," +
            " inspect_type AS inspectType," +
            " procedure_property AS procedureProperty," +
            " preparation_time AS preparationTime," +
            " processing_time AS processingTime," +
            " description AS description ," +
            " is_out AS isOut , " +
            " from capp_procedure where 1=1" +
            " and id = #{id} " +
            "</script>")
    ProcessPlanDetailRespVO selectProcedure(@Param("id") String id);

    /**
     * 工步各项数据
     */
    @Select("<script>" +
            "SELECT " +
            " id," +
            "step_num AS stepNum," +
            "step_name AS stepName," +
            "procedure_id AS procedureId," +
            "step_property AS stepProperty," +
            "processing_time AS processingTime," +
            "slot_number AS slotNumber," +
            "furnace_temperature AS furnaceTemperature," +
            "heat_temperature AS heatTemperature," +
            "heat_up_time AS heatUpTime," +
            "keep_heat_time AS keepHeatTime," +
            "cooling_medium AS coolingMedium," +
            "cooling_temperature AS coolingTemperature," +
            "cooling_time AS coolingTime," +
            "description  " +
            "FROM capp_step WHERE 1=1" +
            " and deleted = 0 " +
            " and id = #{id} " +
            "</script>")
    ProcessPlanDetailRespVO selectStep(@Param("id") String id);

    /**
     * 新版工艺详细设计结构树(含工序工步、工艺方案启用且状态已完成)
     */
    @Select("<script>" +
            "SELECT * FROM (" +
            " SELECT * FROM (" +
            " SELECT " +
            " 1 AS type," +
            " pb.id, " +
            " '0' AS parentId, " +
            " concat( pm.part_number,'_',pv.part_version) AS name, " +
            " pm.part_number AS serialnum, " +
            " pb.part_version_id AS partVersionId," +
            " '' AS processVersionId," +
            " pm.part_number AS partNumber," +
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM pdm_proj_part_bom pb" +
            " LEFT JOIN capp_process_task t ON pb.project_code = t.project_code AND pb.part_version_id = t.part_version_id" +
            " LEFT JOIN pdm_part_version pv ON pb.part_version_id = pv.id" +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " WHERE 1=1 AND pb.deleted=0" +
            " AND t.status = '5'" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and pb.project_code = #{projectCode} " +
            "</if>" +
            "<if test='partNumber != null and partNumber != \"\" '>" +
            " and pm.part_number like concat('%',#{partNumber},'%')" +
            "</if>" +
            " ) tableb UNION " +
            " SELECT * FROM (" +
            " SELECT " +
            " 2 AS type," +
            " pppv.process_version_id AS id, " +
            " pppv.proj_part_bom_id AS parentId, " +
            " concat( proc.process_code, '_', proc.process_scheme_code ) AS name,  " +
            " proc.process_scheme_code AS serialnum, " +
            " pppv.part_version_id AS partVersionId," +
            " procv.id AS processVersionId," +
            " proc.process_code AS partNumber," +
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM capp_pbom_process_version pppv" +
            " LEFT JOIN capp_process_version procv ON procv.id = pppv.process_version_id" +
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id" +
            " LEFT JOIN capp_process_task t ON pppv.project_code = t.project_code AND pppv.part_version_id = t.part_version_id" +
            " WHERE 1=1 AND pppv.deleted=0 AND procv.deleted=0" +
            " AND procv.is_valid=0" +
            " AND t.status = '5'" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and pppv.project_code = #{projectCode} " +
            "</if>" +
            "<if test='partNumber != null and partNumber != \"\" '>" +
            " and proc.process_code like concat('%',#{partNumber},'%')" +
            "</if>" +
            " ) tablec UNION" +
            " SELECT * FROM (" +
            " SELECT" +
            " 3 AS type," +
            " proce.id AS id," +
            " procv.id AS parentId," +
            " concat( proce.procedure_num,'_',proce.procedure_name,'(',t.reviewer,' | ', " +
            " (CASE WHEN t.status = '1' THEN '待编制'  " +
            " WHEN t.status = '2' THEN '编制中'  " +
            " WHEN t.status = '3' THEN '审批中'  " +
            " WHEN t.status = '4' THEN '审批失败' ELSE '已完成' END " +
            " ),')' ) AS name, " +
            " proce.procedure_num AS serialnum, " +
            " pppv.part_version_id AS partVersionId," +
            " proce.process_version_id AS processVersionId," +
            " proc.process_code AS partNumber," +
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM capp_procedure proce" +
            " LEFT JOIN capp_process_version procv ON procv.id = proce.process_version_id" +
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id" +
            " LEFT JOIN capp_pbom_process_version pppv ON procv.id = pppv.process_version_id" +
            " LEFT JOIN capp_process_task pt ON pppv.project_code = pt.project_code AND pppv.part_version_id = pt.part_version_id" +
            " LEFT JOIN capp_process_detail_task t ON pppv.project_code = t.project_code AND pppv.part_version_id = t.part_version_id AND procv.id = t.process_version_id AND proce.id = t.procedure_id" +
            " WHERE 1=1 AND proce.deleted=0 and procv.deleted=0 and proc.deleted=0" +
            " AND pt.status = '5'" +
            " AND t.status != '0'" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and pppv.project_code = #{projectCode} " +
            "</if>" +
            "<if test='partNumber != null and partNumber != \"\" '>" +
            " and proc.process_code like concat('%',#{partNumber},'%')" +
            "</if>" +
            "<if test='status != null '>" +
            " and t.status = #{status} " +
            "</if>" +
            "<if test='reviewedBy != null and reviewedBy != \"\" '>" +
            " and t.reviewed_by = #{reviewedBy} " +
            "</if>" +
            " ) tabled UNION" +
            " SELECT * FROM (" +
            " SELECT" +
            " 4 AS type," +
            " step.id AS id," +
            " proce.id AS parentId," +
            " CONCAT(step.step_num, '(', IFNULL(step.step_name,''), ')' ) AS name," +
            " step.step_num AS serialnum," +
            " proc.part_version_id AS partVersionId," +
            " step.process_version_id AS processVersionId," +
            " proc.process_code AS partNumber," +
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM capp_step step" +
            " LEFT JOIN capp_procedure proce ON proce.id = step.procedure_id" +
            " LEFT JOIN capp_process_version procv ON procv.id = proce.process_version_id" +
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id" +
            " LEFT JOIN capp_pbom_process_version pppv ON procv.id = pppv.process_version_id" +
            " LEFT JOIN capp_process_detail_task t ON pppv.project_code = t.project_code AND pppv.part_version_id = t.part_version_id AND procv.id = t.process_version_id AND proce.id = t.procedure_id" +
            " where step.deleted=0 and proce.deleted=0 and procv.deleted=0 and proc.deleted=0" +
            " AND t.status != '0'" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and pppv.project_code = #{projectCode} " +
            "</if>" +
            "<if test='partNumber != null and partNumber != \"\" '>" +
            " and proc.process_code like concat('%',#{partNumber},'%')" +
            "</if>" +
            "<if test='status != null '>" +
            " and t.status = #{status} " +
            "</if>" +
            "<if test='reviewedBy != null and reviewedBy != \"\" '>" +
            " and t.reviewed_by = #{reviewedBy} " +
            "</if>" +
            " ) tablee " +
            ") tablef " +
            " ORDER BY serialnum " +
            "</script>")
    List<ProjPartBomTreeRespVO> selectPartTreeList(@Param("projectCode") String projectCode, @Param("partNumber") String partNumber, @Param("status") Integer status, @Param("reviewedBy") String reviewedBy);

    /**
     * 获取单个工序工艺详细设计结构树
     */
    @Select("<script>" +
            "SELECT * FROM (" +
            " SELECT * FROM (" +
            " SELECT" +
            " 3 AS type," +
            " proce.id AS id," +
            " procv.id AS parentId," +
            " concat( proce.procedure_num,'_',proce.procedure_name,'(',t.reviewer,' | ', " +
            " (CASE WHEN t.status = '1' THEN '待编制'  " +
            " WHEN t.status = '2' THEN '编制中'  " +
            " WHEN t.status = '3' THEN '审批中'  " +
            " WHEN t.status = '4' THEN '审批失败' ELSE '已完成' END " +
            " ),')' ) AS name, " +
            " proce.procedure_num AS serialnum, " +
            " pppv.part_version_id AS partVersionId," +
            " proce.process_version_id AS processVersionId," +
            " proc.process_code AS partNumber," +
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM capp_procedure proce" +
            " LEFT JOIN capp_process_version procv ON procv.id = proce.process_version_id" +
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id" +
            " LEFT JOIN capp_pbom_process_version pppv ON procv.id = pppv.process_version_id" +
            " LEFT JOIN capp_process_task pt ON pppv.project_code = pt.project_code AND pppv.part_version_id = pt.part_version_id" +
            " LEFT JOIN capp_process_detail_task t ON pppv.project_code = t.project_code AND pppv.part_version_id = t.part_version_id AND procv.id = t.process_version_id AND proce.id = t.procedure_id" +
            " WHERE 1=1 AND proce.deleted=0 and procv.deleted=0 and proc.deleted=0" +
            " AND pt.status = '5'" +
            " AND t.status != '0'" +
            "<if test='taskId != null and taskId != \"\" '>" +
            " and t.id = #{taskId} " +
            "</if>" +
            " ) tabled UNION" +
            " SELECT * FROM (" +
            " SELECT" +
            " 4 AS type," +
            " step.id AS id," +
            " proce.id AS parentId," +
            " CONCAT(step.step_num, '(', IFNULL(step.step_name,''), ')' ) AS name," +
            " step.step_num AS serialnum," +
            " proc.part_version_id AS partVersionId," +
            " step.process_version_id AS processVersionId," +
            " proc.process_code AS partNumber," +
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM capp_step step" +
            " LEFT JOIN capp_procedure proce ON proce.id = step.procedure_id" +
            " LEFT JOIN capp_process_version procv ON procv.id = proce.process_version_id" +
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id" +
            " LEFT JOIN capp_pbom_process_version pppv ON procv.id = pppv.process_version_id" +
            " LEFT JOIN capp_process_detail_task t ON pppv.project_code = t.project_code AND pppv.part_version_id = t.part_version_id AND procv.id = t.process_version_id AND proce.id = t.procedure_id" +
            " where step.deleted=0 and proce.deleted=0 and procv.deleted=0 and proc.deleted=0" +
            " AND t.status != '0'" +
            "<if test='taskId != null and taskId != \"\" '>" +
            " and t.id = #{taskId} " +
            "</if>" +
            " ) tablee " +
            ") tablef " +
            " ORDER BY serialnum " +
            "</script>")
    List<ProjPartBomTreeRespVO> selectPartTreeListByTaskId(@Param("taskId") String taskId);

    //SQL BA
    @Select("<script>" +
            "SELECT" +
            "   cv.id AS processVersionId," +
            "   cp.process_code AS processCode," +
            "   cp.process_scheme_code AS processSchemeCode," +
            "   cv.process_name AS processName," +
            "   cv.process_version AS processVersion," +
            "   pm.part_number AS partNumber," +
            "   pm.part_name AS partName," +
            "   pv.part_version AS partVersion" +
            " FROM capp_process_version cv" +
            " LEFT JOIN capp_process cp ON cv.process_id = cp.id" +
            " LEFT JOIN pdm_part_version pv ON cp.part_version_id = pv.id" +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " WHERE 1=1 AND cv.deleted=0 AND cp.deleted=0 AND cv.is_valid=0 AND cv.status = '3'" +
            "<if test='partNumber != null and partNumber != \"\"'>" +
            "   AND pm.part_number LIKE CONCAT('%', #{partNumber}, '%')" +
            "</if>" +
            "</script>")
    List<ProcessPlanDetailRespVO> getProcessPlanList(@Param("partNumber") String partNumber);
}
