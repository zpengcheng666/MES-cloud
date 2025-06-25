package com.miyu.module.pdm.dal.mysql.processTask;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.part.vo.AddPartRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskRespVO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessTaskDO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Mapper
public interface ProcessTaskMapper extends BaseMapperX<ProcessTaskDO> {


    @Update("<script>" +
            "update capp_process_task set project_status = 1 where project_code=#{projectCode}"+
            "</script>")
    void updateProjectstatus(@Param("projectCode") String projectCode);

    @Update("<script>" +
            "UPDATE capp_process_task SET deleted=1 " +
            "WHERE project_code= #{projectCode} AND part_number = #{partNumber} " +
            "AND part_name = #{partName} AND process_condition = #{processCondition}" +
            "</script>")
    int deleteProcessMessage(@Param("projectCode") String projectCode,
                      @Param("partNumber") String partNumber,
                      @Param("partName") String partName,
                      @Param("processCondition") String processCondition);



    @Select("<script>" +
            " SELECT pv.id AS partVersionId FROM pdm_part_version pv "+
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id "+
            " WHERE 1=1 "+
            "<if test='partNumber != null and partNumber != \"\" '>" +
            "AND pm.part_number = #{partNumber}" +
            "</if>" +
            "<if test='processCondition != null and processCondition !=\"\" '>" +
            "AND pm.process_condition = #{processCondition}" +
            "</if>" +
            " order by pm.update_time desc" +
            "</script>")
    List<ProcessTaskDO> selectPTaskList(@Param("partNumber") String partNumber, @Param("processCondition") String processCondition);

    @Insert("INSERT INTO capp_process_task(id,project_code,part_number,part_name,process_condition,process_preparation_time,part_version_id) VALUES(#{id},#{projectCode},#{partNumber},#{partName},#{processCondition},#{processPreparationTime},#{partVersionId})")
    int insertProcessTask(@Param("id") String id,
                          @Param("projectCode") String projectCode,
                          @Param("partNumber") String partNumber,
                          @Param("partName") String partName,
                          @Param("processCondition") String processCondition,
                          @Param("partVersionId") String partVersionId,
                          @Param("processPreparationTime") LocalDateTime processPreparationTime);


    default List<ProcessTaskDO> selectList(ProcessTaskReqVO reqVO) {
        LambdaQueryWrapperX<ProcessTaskDO> queryWrapper = new LambdaQueryWrapperX<ProcessTaskDO>()
                .likeIfPresent(ProcessTaskDO::getProjectCode, reqVO.getProjectCode())
                .orderByDesc(ProcessTaskDO::getProjectCode);
        return selectList(queryWrapper);
    }

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
            "t.process_instance_id  AS processInstanceId,"+
            "t.approval_status  AS approvalStatus," +
            "t.reviewed_by AS reviewedBy,"+
            "t.reviewer,"+
            "t.deadline,"+
            "IFNULL( t.status, '0' ) AS status,"+
            "IFNULL( t.id, '' ) AS taskId FROM pdm_proj_part_bom pb"+
            " LEFT JOIN capp_process_task t ON pb.project_code = t.project_code AND pb.part_version_id = t.part_version_id"+
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
    List<ProcessTaskRespVO> selectProcessTaskList(@Param("projectCode") String projectCode, @Param("partNumber") String partNumber, @Param("status") String status);


    @Select("<script>" +
                   "SELECT A.* FROM ("+
                   "SELECT t.part_version_id AS partVersionId,"+
                   "t.project_code AS projectCode,"+
                   "pm.part_number AS partNumber,"+
                   "pm.part_name AS partName,"+
                   "t.process_condition AS processCondition,"+
                   "t.process_preparation_time AS processPreparationTime,"+
                   "pv.part_version AS partVersion,"+
                   "pm.root_product_id AS rootProductId,"+
                   "rp.product_number AS productNumber,"+
                   "o.customized_index AS customizedIndex,"+
                   "t.reviewed_by AS reviewedBy,"+
                   "t.reviewer,"+
                   "t.deadline,"+
                   "IFNULL( t.status, '0' ) AS status,"+
                   "IFNULL( t.id, '' ) AS taskId FROM capp_process_task t"+
                   " LEFT JOIN pdm_part_version pv ON t.part_version_id = pv.id"+
                   " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id"+
                   " LEFT JOIN pdm_root_product rp ON pm.root_product_id = rp.id" +
                   " LEFT JOIN pdm_data_object o ON pm.root_product_id = o.rootproduct_id AND o.std_data_object = 'PartInstance'"+
                   " WHERE 1=1 AND t.deleted=0 AND t.project_status=0"+
                   "<if test='projectCode != null and projectCode != \"\" '>" +
                   " and t.project_code like concat('%',#{projectCode},'%')" +
                   "</if>" +
                   "<if test='partNumber != null and partNumber != \"\" '>" +
                   " and pm.part_number like concat('%',#{partNumber},'%')" +
                   "</if>" +
                   ") A"+
                   " WHERE 1=1 AND (A.status = '0' or A.status = '1') "+
                   "<if test='status != null and status != \"\"'>" +
                   " and A.status = #{status}" +
                   "</if>" +
                   " ORDER BY A.status,A.deadline"+
                   "</script>")
    List<ProcessTaskRespVO> selectPartListByProjectCodeNew(@Param("projectCode") String projectCode, @Param("partNumber") String partNumber, @Param("status") String status);

    @Select("<script>" +
            "SELECT part_version_id FROM capp_process_task WHERE part_version_id = #{partVersionId} AND deleted = 0"+
            "</script>")
    List<AddPartRespVO> selectCptByPvId(@Param("partVersionId") String partVersionId);
}

