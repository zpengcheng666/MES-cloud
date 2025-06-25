package com.miyu.module.pdm.dal.mysql.feasibilityTask;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskRespVO;
import com.miyu.module.pdm.controller.admin.part.vo.AddPartRespVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityTask.FeasibilityTaskDO;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface FeasibilityTaskMapper extends BaseMapperX<FeasibilityTaskDO> {

//    default int deleteMessage(PushReqDTO dto) {
//        return delete(Wrappers.lambdaUpdate(FeasibilityTaskDO.class)
//                .eq(FeasibilityTaskDO::getProjectCode, dto.getProjectCode())
//                .eq(FeasibilityTaskDO::getPartNumber, dto.getPartNumber())
//                .eq(FeasibilityTaskDO::getPartName, dto.getPartName())
//                .eq(FeasibilityTaskDO::getProcessCondition, dto.getProcessCondition()));
//    }
@Update("<script>" +
        "update capp_feasibility_task set project_status = 1 where project_code=#{projectCode}"+
        "</script>")
    void updateProjectstatus(@Param("projectCode") String projectCode);


@Update("<script>" +
            "UPDATE capp_feasibility_task SET deleted=1 " +
            "WHERE project_code= #{projectCode} AND part_number = #{partNumber} " +
            "AND part_name = #{partName} AND process_condition = #{processCondition}" +
            "</script>")
    int deleteMessage(@Param("projectCode") String projectCode,
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
        List<FeasibilityTaskDO> selectTaskList(@Param("partNumber") String partNumber, @Param("processCondition")  String processCondition);

        @Insert("INSERT INTO capp_feasibility_task(id,project_code,part_number,part_name,process_condition,part_version_id) VALUES(#{id},#{projectCode},#{partNumber},#{partName},#{processCondition},#{partVersionId})")
        int insertFeasibilityTask(@Param("id") String id,
                                  @Param("projectCode") String projectCode,
                                  @Param("partNumber") String partNumber,
                                  @Param("partName") String partName,
                                  @Param("processCondition") String processCondition,
                                  @Param("partVersionId") String partVersionId);

    default List<FeasibilityTaskDO> selectList(FeasibilityTaskReqVO reqVO) {
        LambdaQueryWrapperX<FeasibilityTaskDO> queryWrapper = new LambdaQueryWrapperX<FeasibilityTaskDO>()
                .likeIfPresent(FeasibilityTaskDO::getProjectCode, reqVO.getProjectCode())
                .orderByDesc(FeasibilityTaskDO::getProjectCode);
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
            "t.reviewed_by AS reviewedBy,"+
            "t.reviewer,"+
            "t.deadline,"+
            "IFNULL( t.status, '0' ) AS status,"+
            "IFNULL( t.id, '' ) AS taskId FROM pdm_proj_part_bom pb"+
            " LEFT JOIN capp_feasibility_task t ON pb.project_code = t.project_code AND pb.part_version_id = t.part_version_id"+
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
    List<FeasibilityTaskRespVO> selectFeasibilityTaskList(@Param("projectCode") String projectCode, @Param("partNumber") String partNumber, @Param("status") String status);

    @Select("<script>" +
            "SELECT A.* FROM ("+
            "SELECT t.part_version_id AS partVersionId,"+
            "t.project_code AS projectCode,"+
            "pm.part_number AS partNumber,"+
            "pm.part_name AS partName,"+
            "t.process_condition AS processCondition,"+
            "pv.part_version AS partVersion,"+
            "pm.root_product_id AS rootProductId,"+
            "rp.product_number AS productNumber,"+
            "o.customized_index AS customizedIndex,"+
            "t.reviewed_by AS reviewedBy,"+
            "t.reviewer,"+
            "t.deadline,"+
            "IFNULL( t.status, '0' ) AS status,"+
            "IFNULL( t.id, '' ) AS taskId FROM capp_feasibility_task t"+
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
    List<FeasibilityTaskRespVO> selectPartListByProjectCodeNew(@Param("projectCode") String projectCode, @Param("partNumber") String partNumber, @Param("status") String status);

    @Select("<script>" +
            "SELECT part_version_id FROM capp_feasibility_task WHERE part_version_id = #{partVersionId} AND deleted = 0"+
            "</script>")
    List<AddPartRespVO> selectCftByPvId(@Param("partVersionId") String partVersionId);
}
