package com.miyu.module.pdm.dal.mysql.processDetailTask;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProcessDetailTaskReqVO;
import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProcessDetailTaskRespVO;
import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityTask.FeasibilityTaskDO;
import com.miyu.module.pdm.dal.dataobject.processDetailTask.ProcessDetailTaskDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProcessDetailTaskMapper extends BaseMapperX<ProcessDetailTaskDO> {

    @Select("<script>" +
            "SELECT * FROM (" +
            " SELECT * FROM (" +
            " SELECT " +
            " '1' AS type," +
            " '零件' AS typeName," +
            " pb.id, " +
            " '0' AS parentId, " +
            " concat( pm.part_number,'_',pv.part_version) AS name, " +
            " pm.part_number AS serialnum, " +
            " pb.part_version_id AS partVersionId," +
            " '' AS processVersionId," +
            " '' AS status," +
            " t.reviewed_by AS reviewedBy," +
            " t.reviewer AS reviewer," +
            " t.deadline AS deadline," +
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
            " '2' AS type," +
            " '工艺方案' AS typeName," +
            " pppv.process_version_id AS id, " +
            " pppv.proj_part_bom_id AS parentId, " +
            " concat( proc.process_code, '_', proc.process_scheme_code ) AS name,  " +
            " proc.process_scheme_code AS serialnum, " +
            " pppv.part_version_id AS partVersionId," +
            " pppv.process_version_id AS processVersionId, " +
            " '' AS status," +
            " t.reviewed_by AS reviewedBy," +
            " t.reviewer AS reviewer," +
            " t.deadline AS deadline," +
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
            " SELECT * FROM ("+
            " SELECT"+
            " '3' AS type,"+
            " '工序' AS typeName," +
            " proce.id AS id,"+
            " procv.id AS parentId,"+
            " concat( proce.procedure_num, '(', proce.procedure_name, ')' ) AS name,"+
            " proce.procedure_num AS serialnum, " +
            " pppv.part_version_id AS partVersionId,"+
            " pppv.process_version_id AS processVersionId, " +
            " IFNULL( t.status, '0' ) AS status," +
            " t.reviewed_by AS reviewedBy," +
            " t.reviewer AS reviewer," +
            " t.deadline AS deadline," +
            " t.id AS taskId,t.process_instance_id AS processInstanceId" +
            " FROM capp_procedure proce"+
            " LEFT JOIN capp_process_version procv ON procv.id = proce.process_version_id"+
            " LEFT JOIN capp_process proc ON proc.id = procv.process_id"+
            " LEFT JOIN capp_pbom_process_version pppv ON procv.id = pppv.process_version_id"+
            " LEFT JOIN capp_process_task pt ON pppv.project_code = pt.project_code AND pppv.part_version_id = pt.part_version_id"+
            " LEFT JOIN capp_process_detail_task t ON pppv.project_code = t.project_code AND pppv.part_version_id = t.part_version_id AND procv.id = t.process_version_id AND proce.id = t.procedure_id"+
            " WHERE 1=1 AND proce.deleted=0 and procv.deleted=0 and proc.deleted=0" +
            " AND pt.status = '5'" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and pppv.project_code = #{projectCode} " +
            "</if>" +
            "<if test='partNumber != null and partNumber != \"\" '>" +
            " and proc.process_code like concat('%',#{partNumber},'%')" +
            "</if>" +
            " ) tabled "+
            " where (tabled.status = '0' or tabled.status = '1')" +
            "<if test='status != null '>" +
            " and tabled.status = #{status} " +
            "</if>" +
            ") tablef " +
            " ORDER BY serialnum "+
            "</script>")
    List<ProjPartBomTreeRespVO> selectPartTreeList(@Param("projectCode") String projectCode, @Param("partNumber") String partNumber, @Param("status") Integer status);



    @Select("<script>" +
            "SELECT A.* FROM ("+
            "SELECT pb.part_version_id AS partVersionId,"+
            "t.project_code AS projectCode,"+
            "pm.part_number AS partNumber,"+
            "pm.part_name AS partName,"+
            "pv.part_version AS partVersion,"+
            "pm.root_product_id AS rootProductId,"+
            "rp.product_number AS productNumber,"+
            "o.customized_index AS customizedIndex,"+
            "t.reviewed_by AS reviewedBy,"+
            "t.reviewer,"+
            "t.deadline,"+
            "IFNULL( t.status, '0' ) AS status,"+
            "IFNULL( t.id, '' ) AS taskId FROM pdm_proj_part_bom pb"+
            " LEFT JOIN capp_process_detail_task t ON pb.project_code = t.project_code AND pb.part_version_id = t.part_version_id"+
            " LEFT JOIN pdm_part_version pv ON pb.part_version_id = pv.id"+
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id"+
            " LEFT JOIN pdm_root_product rp ON pm.root_product_id = rp.id" +
            " LEFT JOIN pdm_data_object o ON pm.root_product_id = o.rootproduct_id AND o.std_data_object = 'PartInstance'"+
            " WHERE 1=1 AND pb.deleted=0"+
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and pb.project_code = #{projectCode} " +
            "</if>" +
            "<if test='partNumber != null and partNumber != \"\" '>" +
            " and pm.part_number like concat('%',#{partNumber},'%')" +
            "</if>" +
            ") A"+
            " WHERE 1=1 AND (A.status = '0' or A.status = '1') "+
            "<if test='status != null and status != \"\"'>" +
            " and A.status = #{status}" +
            "</if>" +
            " ORDER BY A.status DESC"+
            "</script>")
    List<ProcessDetailTaskRespVO> selectProcessDetailTaskList(@Param("projectCode") String projectCode, @Param("partNumber") String partNumber, @Param("status") Integer status);


}
