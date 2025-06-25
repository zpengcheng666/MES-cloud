package com.miyu.module.pdm.dal.mysql.processDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomRespVO;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ResourceSelectedReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessDetailReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.FeasibilityDetailDO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ProcessDetailMapper extends BaseMapperX<ProcessDetailDO> {
    @Select("<script>" +
            "SELECT A.* FROM (" +
            "SELECT pb.id, pb.part_version_id AS partVersionId, pm.part_number as partNumber, pm.part_name as partName, pv.part_version as partVersion, " +
            " pm.root_product_id AS rootProductId, rp.product_number AS productNumber,o.customized_index AS customizedIndex, " +
            " t.reviewed_by AS reviewedBy,t.reviewer,t.deadline,t.status" +
            " FROM pdm_proj_part_bom pb " +
            " LEFT JOIN capp_process_task t ON pb.project_code = t.project_code AND pb.part_version_id = t.part_version_id " +
            " LEFT JOIN pdm_part_version pv ON pb.part_version_id = pv.id " +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " LEFT JOIN pdm_root_product rp ON pm.root_product_id = rp.id" +
            " LEFT JOIN pdm_data_object o ON pm.root_product_id = o.rootproduct_id and o.std_data_object = 'PartInstance'" +
            " WHERE 1=1 AND pb.deleted=0" +
            " AND t.status != '0'" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and pb.project_code = #{projectCode} " +
            "</if>" +
            "<if test='partNumber != null and partNumber != \"\" '>" +
            " and pm.part_number like concat('%',#{partNumber},'%')" +
            "</if>" +
            "<if test='reviewedBy != null and reviewedBy != \"\" '>" +
            " and t.reviewed_by = #{reviewedBy} " +
            "</if>" +
            " ) A WHERE 1=1 " +
            "<if test='status != null '>" +
            " and A.status = #{status} " +
            "</if>" +
            "</script>")
    List<ProjPartBomRespVO> selectPartList(@Param("projectCode") String projectCode, @Param("partNumber") String partNumber, @Param("status") Integer status, @Param("reviewedBy") String reviewedBy);

    @Select("<script>" +
            "SELECT * FROM (" +
            " SELECT * FROM (" +
            " SELECT " +
            " 1 AS type," +
            " pb.id, " +
            " '0' AS parentId, " +
            " concat( pm.part_number,'_',pv.part_version,'(',t.reviewer,' | ', " +
            " (CASE WHEN t.status = '1' THEN '待编制'  " +
            " WHEN t.status = '2' THEN '编制中'  " +
            " WHEN t.status = '3' THEN '审批中'  " +
            " WHEN t.status = '4' THEN '审批失败' ELSE '已完成' END " +
            " ),')' ) AS name, " +
            " pm.part_number AS serialnum, " +
            " pb.part_version_id AS partVersionId," +
            " '' AS source," +
            " pm.part_number AS partNumber," +
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM pdm_proj_part_bom pb" +
            " LEFT JOIN capp_process_task t ON pb.project_code = t.project_code AND pb.part_version_id = t.part_version_id" +
            " LEFT JOIN pdm_part_version pv ON pb.part_version_id = pv.id" +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " WHERE 1=1 AND pb.deleted=0" +
            " AND t.status != '0'" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and pb.project_code = #{projectCode} " +
            "</if>" +
            "<if test='partNumber != null and partNumber != \"\" '>" +
            " and pm.part_number like concat('%',#{partNumber},'%')" +
            "</if>" +
            "<if test='status != null '>" +
            " and t.status = #{status} " +
            "</if>" +
            "<if test='reviewedBy != null and reviewedBy != \"\" '>" +
            " and t.reviewed_by = #{reviewedBy} " +
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
            " pppv.source," +
            " proc.process_code AS partNumber," +
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM capp_pbom_process_version pppv" +
            " LEFT JOIN capp_process_version procv ON procv.id = pppv.process_version_id" +
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id" +
            " LEFT JOIN capp_process_task t ON pppv.project_code = t.project_code AND pppv.part_version_id = t.part_version_id" +
            " WHERE 1=1 AND pppv.deleted=0 AND procv.deleted=0" +
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
            " ) tablec UNION" +
            " SELECT * FROM ("+
            " SELECT"+
            " 3 AS type,"+
            " proce.id AS id,"+
            " procv.id AS parentId,"+
            " concat( proce.procedure_num, '(', proce.procedure_name, ')' ) AS name,"+
            " proce.procedure_num AS serialnum, " +
            " pppv.part_version_id AS partVersionId,"+
            " pppv.source," +
            " proc.process_code AS partNumber,"+
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " '' AS taskId,'' AS processInstanceId" +
            " FROM capp_procedure proce"+
            " LEFT JOIN capp_process_version procv ON procv.id = proce.process_version_id"+
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id"+
            " LEFT JOIN capp_pbom_process_version pppv ON procv.id = pppv.process_version_id"+
            " LEFT JOIN capp_process_task t ON pppv.project_code = t.project_code AND pppv.part_version_id = t.part_version_id"+
            " WHERE 1=1 AND proce.deleted=0 and procv.deleted=0 and proc.deleted=0" +
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
            " ) tabled "+
            ") tablef " +
            " ORDER BY serialnum "+
            "</script>")
    List<ProjPartBomTreeRespVO> selectPartTreeList(@Param("projectCode") String projectCode, @Param("partNumber") String partNumber, @Param("status") Integer status, @Param("reviewedBy") String reviewedBy);


    @Select("<script>" +
            "SELECT * FROM (" +
            " SELECT * FROM (" +
            " SELECT " +
            " 1 AS type," +
            " pb.id, " +
            " '0' AS parentId, " +
            " concat( pm.part_number,'_',pv.part_version,'(',t.reviewer,' | ', " +
            " (CASE WHEN t.status = '1' THEN '待编制'  " +
            " WHEN t.status = '2' THEN '编制中'  " +
            " WHEN t.status = '3' THEN '审批中'  " +
            " WHEN t.status = '4' THEN '审批失败' ELSE '已完成' END " +
            " ),')' ) AS name, " +
            " pm.part_number AS serialnum, " +
            " pb.part_version_id AS partVersionId," +
            " '' AS source," +
            " pm.part_number AS partNumber," +
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM pdm_proj_part_bom pb" +
            " LEFT JOIN capp_process_task t ON pb.project_code = t.project_code AND pb.part_version_id = t.part_version_id" +
            " LEFT JOIN pdm_part_version pv ON pb.part_version_id = pv.id" +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " WHERE 1=1 AND pb.deleted=0" +
            " AND t.status != '0'" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and pb.project_code = #{projectCode} " +
            "</if>" +
            "<if test='partVersionId != null and partVersionId != \"\" '>" +
            " and pb.part_version_id = #{partVersionId} " +
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
            " pppv.source," +
            " proc.process_code AS partNumber," +
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM capp_pbom_process_version pppv" +
            " LEFT JOIN capp_process_version procv ON procv.id = pppv.process_version_id" +
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id" +
            " LEFT JOIN capp_process_task t ON pppv.project_code = t.project_code AND pppv.part_version_id = t.part_version_id" +
            " WHERE 1=1 AND pppv.deleted=0 AND procv.deleted=0" +
            " AND t.status != '0'" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and pppv.project_code = #{projectCode} " +
            "</if>" +
            "<if test='partVersionId != null and partVersionId != \"\" '>" +
            " and proc.part_version_id = #{partVersionId} " +
            "</if>" +
            " ) tablec UNION" +
            " SELECT * FROM ("+
            " SELECT"+
            " 3 AS type,"+
            " proce.id AS id,"+
            " procv.id AS parentId,"+
            " concat( proce.procedure_num, '(', proce.procedure_name, ')' ) AS name,"+
            " proce.procedure_num AS serialnum, " +
            " pppv.part_version_id AS partVersionId,"+
            " '' AS source,"+
            " proc.process_code AS partNumber,"+
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " '' AS taskId,'' AS processInstanceId" +
            " FROM capp_procedure proce"+
            " LEFT JOIN capp_process_version procv ON procv.id = proce.process_version_id"+
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id"+
            " LEFT JOIN capp_pbom_process_version pppv ON procv.id = pppv.process_version_id"+
            " LEFT JOIN capp_process_task t ON pppv.project_code = t.project_code AND pppv.part_version_id = t.part_version_id"+
            " WHERE 1=1 AND proce.deleted=0 and procv.deleted=0 and proc.deleted=0" +
            " AND t.status != '0'" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and pppv.project_code = #{projectCode} " +
            "</if>" +
            "<if test='partVersionId != null and partVersionId != \"\" '>" +
            " and proc.part_version_id = #{partVersionId} " +
            "</if>" +
            " ) tabled "+
            ") tablef " +
            " ORDER BY serialnum "+
            "</script>")
    List<ProjPartBomTreeRespVO> selectPartTreeListByPartVersionId(@Param("projectCode") String projectCode, @Param("partVersionId") String partVersionId);
    default int deleteByProjectCode(ResourceSelectedReqVO reqVO) {
        return delete(Wrappers.lambdaUpdate(ProcessDetailDO.class)
                .eq(ProcessDetailDO::getProjectCode, reqVO.getProjectCode())
                .eq(ProcessDetailDO::getPartVersionId, reqVO.getPartVersionId())
                .eq(ProcessDetailDO::getProcessVersionId, reqVO.getProcessVersionId())
                .eq(ProcessDetailDO::getResourcesType,reqVO.getResourcesType()));

    }
    @Select("<script>" +
            "SELECT pb.project_code AS projectCode, pb.id, pb.part_version_id AS partVersionId, pm.part_number as partNumber, pm.part_name as partName, pv.part_version as partVersion, " +
            " pm.root_product_id AS rootProductId, rp.product_number AS productNumber,o.customized_index AS customizedIndex, " +
            " t.reviewed_by AS reviewedBy,t.reviewer,t.deadline,t.status,t.id AS taskId,t.process_instance_id AS processInstanceId, " +
            " d.is_pass AS isPass,d.reason " +
            " FROM pdm_proj_part_bom pb " +
            " LEFT JOIN capp_process_task t ON pb.project_code = t.project_code AND pb.part_version_id = t.part_version_id " +
            " LEFT JOIN pdm_part_version pv ON pb.part_version_id = pv.id " +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " LEFT JOIN pdm_root_product rp ON pm.root_product_id = rp.id" +
            " LEFT JOIN pdm_data_object o ON pm.root_product_id = o.rootproduct_id and o.std_data_object = 'PartInstance'" +
            " LEFT JOIN capp_feasibility_result d ON d.project_code = t.project_code and d.part_version_id = t.part_version_id" +
            " WHERE 1=1" +
            "<if test='taskId != null and taskId != \"\" '>" +
            " and t.id = #{taskId} " +
            "</if>" +
            "</script>")
    ProjPartBomRespVO selectPartDetail(@Param("taskId") String taskId);
    default int deleteByResourceId(ProcessDetailReqVO reqVO) {
        return delete(Wrappers.lambdaUpdate(ProcessDetailDO.class)
                .eq(ProcessDetailDO::getProjectCode, reqVO.getProjectCode())
                .eq(ProcessDetailDO::getPartVersionId, reqVO.getPartVersionId())
                .eq(ProcessDetailDO::getResourcesType, reqVO.getResourcesType())
                .eq(ProcessDetailDO::getProcessVersionId, reqVO.getProcessVersionId())
                .eq(ProcessDetailDO::getResourcesTypeId,reqVO.getResourcesTypeId()));

    }

    default List<ProcessDetailDO> selectResourceList(ResourceSelectedReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProcessDetailDO>()
                .eq(ProcessDetailDO::getProjectCode, reqVO.getProjectCode())
                .eq(ProcessDetailDO::getPartVersionId, reqVO.getPartVersionId())
                .eq(ProcessDetailDO::getProcessVersionId, reqVO.getProcessVersionId())
                .eqIfPresent(ProcessDetailDO::getResourcesType, reqVO.getResourcesType())
                .orderByDesc(ProcessDetailDO::getId));
    }

    @Select("<script>" +
            "SELECT * FROM (" +
            " SELECT * FROM (" +
            " SELECT " +
            " 1 AS type," +
            " t.id, " +
            " '0' AS parentId, " +
            " concat( t.project_code,'_',pm.part_number,'_',t.process_condition,'(',t.reviewer,' | ', " +
            " (CASE WHEN t.status = '1' THEN '待编制'  " +
            " WHEN t.status = '2' THEN '编制中'  " +
            " WHEN t.status = '3' THEN '审批中'  " +
            " WHEN t.status = '4' THEN '审批失败' ELSE '已完成' END " +
            " ),')' ) AS name, " +
            " t.project_code AS serialnum, " +
            " t.part_version_id AS partVersionId," +
            " '' AS source," +
            " pm.part_number AS partNumber," +
            " '' AS processVersionId," +
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " t.project_code AS projectCode,t.process_condition AS processCondition, " +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM capp_process_task t" +
            " LEFT JOIN pdm_part_version pv ON t.part_version_id = pv.id" +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " WHERE 1=1 AND t.deleted=0" +
            " AND t.status != '0'" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and t.project_code like concat('%',#{projectCode},'%') " +
            "</if>" +
            "<if test='partNumber != null and partNumber != \"\" '>" +
            " and pm.part_number like concat('%',#{partNumber},'%')" +
            "</if>" +
            "<if test='status != null '>" +
            " and t.status = #{status} " +
            "</if>" +
            "<if test='reviewedBy != null and reviewedBy != \"\" '>" +
            " and t.reviewed_by = #{reviewedBy} " +
            "</if>" +
            "<if test='projectStatus != null '>" +
            " and t.project_status = #{projectStatus} " +
            "</if>" +
            "<if test='partVersionId != null and partVersionId != \"\" '>" +
            " and pv.id = #{partVersionId} " +
            "</if>" +
            " ) tableb UNION " +
            " SELECT * FROM (" +
            " SELECT " +
            " 2 AS type," +
            " pppv.process_version_id AS id, " +
            " pppv.process_task_id AS parentId, " +
            " concat( proc.process_code, '_', proc.process_scheme_code, '_', procv.process_version ) AS name,  " +
            " proc.process_scheme_code AS serialnum, " +
            " pppv.part_version_id AS partVersionId," +
            " pppv.source," +
            " pm.part_number AS partNumber," +
            " procv.id AS processVersionId," +
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " t.project_code AS projectCode,t.process_condition AS processCondition, " +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM capp_pbom_process_version pppv" +
            " LEFT JOIN capp_process_version procv ON procv.id = pppv.process_version_id" +
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id" +
            " LEFT JOIN capp_process_task t ON pppv.project_code = t.project_code AND pppv.part_version_id = t.part_version_id" +
            " LEFT JOIN pdm_part_version pv ON t.part_version_id = pv.id" +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " WHERE 1=1 AND pppv.deleted=0 AND procv.deleted=0" +
            " AND t.status != '0'" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and pppv.project_code like concat('%',#{projectCode},'%') " +
            "</if>" +
            "<if test='partNumber != null and partNumber != \"\" '>" +
            " and pm.part_number like concat('%',#{partNumber},'%')" +
            "</if>" +
            "<if test='status != null '>" +
            " and t.status = #{status} " +
            "</if>" +
            "<if test='reviewedBy != null and reviewedBy != \"\" '>" +
            " and t.reviewed_by = #{reviewedBy} " +
            "</if>" +
            "<if test='projectStatus != null '>" +
            " and t.project_status = #{projectStatus} " +
            "</if>" +
            "<if test='partVersionId != null and partVersionId != \"\" '>" +
            " and proc.part_version_id = #{partVersionId} " +
            "</if>" +
            " ) tablec " +
            ") tablef " +
            " ORDER BY serialnum "+
            "</script>")
    List<ProjPartBomTreeRespVO> selectPartTreeListNew(@Param("projectCode") String projectCode, @Param("partNumber") String partNumber, @Param("status") Integer status, @Param("reviewedBy") String reviewedBy, @Param("projectStatus") Integer projectStatus, @Param("partVersionId") String partVersionId);

    @Select("<script>" +
            " SELECT * FROM (" +
            " SELECT " +
            " 2 AS type," +
            " pppv.process_version_id AS id, " +
            " pppv.process_task_id AS parentId, " +
            " concat( proc.process_code, '_', proc.process_scheme_code, '_', procv.process_version ) AS name,  " +
            " proc.process_scheme_code AS serialnum, " +
            " pppv.part_version_id AS partVersionId," +
            " pppv.source," +
            " pm.part_number AS partNumber," +
            " procv.id AS processVersionId," +
            " t.status," +
            " t.reviewed_by AS reviewedBy," +
            " t.project_code AS projectCode,t.process_condition AS processCondition, " +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM capp_pbom_process_version pppv" +
            " LEFT JOIN capp_process_version procv ON procv.id = pppv.process_version_id" +
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id" +
            " LEFT JOIN capp_process_task t ON pppv.project_code = t.project_code AND pppv.part_version_id = t.part_version_id" +
            " LEFT JOIN pdm_part_version pv ON t.part_version_id = pv.id" +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " WHERE 1=1 AND pppv.deleted=0 AND procv.deleted=0" +
            " AND t.status = '5'" +
            "<if test='projectCodes != null and projectCodes != \"\" '>" +
            " and pppv.project_code in (${projectCodes})" +
            "</if>" +
            " ) tablec " +
            " ORDER BY serialnum "+
            "</script>")
    List<ProjPartBomTreeRespVO> selectProcessListByProjectCodes(@Param("projectCodes") String projectCodes);

    @Select("<script>" +
            "SELECT * FROM (" +
            " SELECT * FROM ("+
            " SELECT"+
            " 3 AS type,"+
            " proce.id AS id,"+
            " procv.id AS parentId,"+
            " concat( proce.procedure_num, '(', proce.procedure_name, ')' ) AS name,"+
            " proce.procedure_num AS serialnum, " +
            " '' AS partVersionId,"+
            " '' AS source," +
            " proc.process_code AS partNumber,"+
            " procv.id AS processVersionId," +
            " '' AS status," +
            " '' AS reviewedBy," +
            " '' AS projectCode,'' AS processCondition, " +
            " '' AS taskId,'' AS processInstanceId" +
            " FROM capp_procedure proce"+
            " LEFT JOIN capp_process_version procv ON procv.id = proce.process_version_id"+
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id"+
            " WHERE 1=1 AND proce.deleted=0 and procv.deleted=0 and proc.deleted=0" +
            "<if test='processVersionId != null and processVersionId != \"\" '>" +
            " and procv.id = #{processVersionId} " +
            "</if>" +
            " ) tabled "+
            ") tablef " +
            " ORDER BY serialnum "+
            "</script>")
    List<ProjPartBomTreeRespVO> selectProcedureListByProcessVersionId(@Param("processVersionId") String processVersionId);

    @Select("<script>" +
            "SELECT * FROM (" +
            " SELECT * FROM (" +
            " SELECT" +
            " 4 AS type," +
            " step.id AS id," +
            " proce.id AS parentId," +
            " CONCAT(step.step_num, '(', IFNULL(step.step_name,''), ')' ) AS name," +
            " step.step_num AS serialnum," +
            " proc.part_version_id AS partVersionId,"+
            " '' AS source," +
            " proc.process_code AS partNumber,"+
            " procv.id AS processVersionId," +
            " '' AS status," +
            " '' AS reviewedBy," +
            " '' AS projectCode,'' AS processCondition, " +
            " '' AS taskId,'' AS processInstanceId" +
            " FROM capp_step step" +
            " LEFT JOIN capp_procedure proce ON proce.id = step.procedure_id" +
            " LEFT JOIN capp_process_version procv ON procv.id = proce.process_version_id" +
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id" +
            " where step.deleted=0 and proce.deleted=0 and procv.deleted=0 and proc.deleted=0" +
            "<if test='procedureId != null and procedureId != \"\" '>" +
            " and proce.id = #{procedureId} " +
            "</if>" +
            " ) tablee " +
            ") tablef " +
            " ORDER BY serialnum "+
            "</script>")
    List<ProjPartBomTreeRespVO> selectStepListByProcedureId(@Param("procedureId") String procedureId);

    @Select("<script>" +
            "SELECT t.project_code AS projectCode, t.id, t.part_version_id AS partVersionId, pm.part_number as partNumber, pm.part_name as partName, t.process_condition as processCondition, pv.part_version as partVersion, " +
            " pm.root_product_id AS rootProductId, rp.product_number AS productNumber,o.customized_index AS customizedIndex, " +
            " t.reviewed_by AS reviewedBy,t.reviewer,t.deadline,t.process_preparation_time,t.status,t.id AS taskId,t.process_instance_id AS processInstanceId, " +
            " d.is_pass AS isPass,d.reason,ft.id AS feasibilityTaskId " +
            " FROM capp_process_task t " +
            " LEFT JOIN pdm_part_version pv ON t.part_version_id = pv.id " +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " LEFT JOIN pdm_root_product rp ON pm.root_product_id = rp.id" +
            " LEFT JOIN pdm_data_object o ON pm.root_product_id = o.rootproduct_id and o.std_data_object = 'PartInstance'" +
            " LEFT JOIN capp_feasibility_result d ON d.project_code = t.project_code and d.part_version_id = t.part_version_id" +
            " LEFT JOIN capp_feasibility_task ft ON ft.project_code = t.project_code and ft.part_version_id = t.part_version_id" +
            " WHERE 1=1" +
            "<if test='taskId != null and taskId != \"\" '>" +
            " and t.id = #{taskId} " +
            "</if>" +
            "</script>")
    ProjPartBomRespVO selectPartDetailNew(@Param("taskId") String taskId);


        @Select("<script>" +
            "SELECT t.project_code AS projectCode, t.id, t.part_version_id AS partVersionId, pm.part_number as partNumber, pm.part_name as partName, t.process_condition as processCondition, pv.part_version as partVersion, " +
            " pm.root_product_id AS rootProductId, rp.product_number AS productNumber,o.customized_index AS customizedIndex, " +
            " t.reviewed_by AS reviewedBy,t.reviewer,t.deadline,t.process_preparation_time,t.status,t.id AS taskId,t.process_instance_id AS processInstanceId, " +
            " d.is_pass AS isPass,d.reason,ft.id AS feasibilityTaskId " +
            " FROM capp_process_task t " +
            " LEFT JOIN pdm_part_version pv ON t.part_version_id = pv.id " +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " LEFT JOIN pdm_root_product rp ON pm.root_product_id = rp.id" +
            " LEFT JOIN pdm_data_object o ON pm.root_product_id = o.rootproduct_id and o.std_data_object = 'PartInstance'" +
            " LEFT JOIN capp_feasibility_result d ON d.project_code = t.project_code and d.part_version_id = t.part_version_id" +
            " LEFT JOIN capp_feasibility_task ft ON ft.project_code = t.project_code and ft.part_version_id = t.part_version_id" +
            " WHERE 1=1 and t.status='1'" +
            "<if test='reviewedBy != null and reviewedBy != \"\" '>" +
            " AND t.reviewed_by = #{reviewedBy} " +
            "</if>" +
            "</script>")
    List<ProjPartBomRespVO> selectPartDetailNewHome(@Param("reviewedBy") String reviewedBy);
}
