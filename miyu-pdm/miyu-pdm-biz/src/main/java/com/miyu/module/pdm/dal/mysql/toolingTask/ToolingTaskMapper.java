package com.miyu.module.pdm.dal.mysql.toolingTask;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingApplyReqVO;
import com.miyu.module.pdm.controller.admin.toolingTask.vo.ToolingTaskReqVO;
import com.miyu.module.pdm.controller.admin.toolingTask.vo.ToolingTaskRespVO;
import com.miyu.module.pdm.dal.dataobject.toolingTask.ToolingTaskDO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * 工装申请信息 Mapper
 *
 */
@Mapper
public interface ToolingTaskMapper extends BaseMapperX<ToolingTaskDO> {
    default List<ToolingTaskDO> selectList(ToolingTaskReqVO reqVO) {
        LambdaQueryWrapperX<ToolingTaskDO> queryWrapper = new LambdaQueryWrapperX<ToolingTaskDO>()
                .likeIfPresent(ToolingTaskDO::getToolingCode, reqVO.getToolingCode())
                .orderByDesc(ToolingTaskDO::getToolingCode);
        return selectList(queryWrapper);
    }

    /**
     * 工装设计任务列表
     * @param toolingCode
     * @param toolingNumber
     * @param toolingName
     * @param status
     * @return
     */
    @Select("<script>" +
            "SELECT A.* FROM ("+
            "SELECT ta.tooling_code AS toolingCode,"+
            "ta.id AS id,"+
            "ta.apply_id AS applyId,"+
            "ca.name AS name,"+
            "ta.tooling_number AS toolingNumber,"+
            "ta.tooling_name AS toolingName,"+
            "ta.apply_name AS applyName,"+
            "ta.require_time AS requireTime,"+
            "t.reviewed_by AS reviewedBy,"+
            "t.tooling_apply_id AS toolingApplyId,"+
            "t.reviewer AS reviewer,"+
            "t.deadline  AS deadline,"+
            "t.id  AS tid,"+
            "IFNULL( t.status, '0' ) AS status  FROM pdm_tooling_apply ta "+
            "LEFT JOIN pdm_tooling_task t ON t.tooling_apply_id = ta.id "+
            "LEFT JOIN pdm_tooling_category ca ON ca.id = ta.category_id"+
            " WHERE 1=1 AND ta.deleted=0 AND ta.status=3"+
            "<if test='toolingCode != null and toolingCode != \"\" '>" +
            " and ta.tooling_code like concat('%',#{toolingCode},'%')" +
            "</if>" +
            "<if test='toolingNumber != null and toolingNumber != \"\" '>" +
            " and ta.tooling_number like concat('%',#{toolingNumber},'%')" +
            "</if>" +
            "<if test='toolingName != null and toolingName != \"\" '>" +
            " and ta.tooling_name like concat('%',#{toolingName},'%')" +
            "</if>" +
            ") A"+
            " WHERE 1=1 AND (A.status = '0' or A.status = '1') "+
            "<if test='status != null and status != \"\"'>" +
            " and A.status = #{status}" +
            "</if>" +
            " ORDER BY A.status DESC"+
            "</script>")
    List<ToolingTaskRespVO> selectToolingTaskList(@Param("toolingCode") String toolingCode, @Param("toolingNumber") String toolingNumber, @Param("toolingName") String toolingName, @Param("status") String status);

    /**
     * 根据id查询工装详细设计列表
     * @param
     * @return
     */
    @Select("<script>" +
            "SELECT ta.tooling_code AS toolingCode,"+
            "ta.id AS id,"+
            "ta.apply_id AS applyId,"+
            "ta.tooling_number AS toolingNumber,"+
            "ta.customized_index AS customizedIndex,"+
            "ta.tooling_name AS toolingName,"+
            "ta.apply_name AS applyName,"+
            "ta.require_time AS requireTime,"+
            "ca.name AS name,"+
            "t.reviewed_by AS reviewedBy,"+
            "t.status AS status,"+
            "t.tooling_apply_id AS toolingApplyId,"+
            "t.process_instance_id AS processInstanceId,"+
            "t.reviewer AS reviewer,"+
            "t.deadline  AS deadline,"+
            "t.id  AS tid "+
            "FROM pdm_tooling_apply ta "+
            "LEFT JOIN pdm_tooling_task t ON t.tooling_apply_id = ta.id "+
            "LEFT JOIN pdm_tooling_category ca ON ca.id = ta.category_id"+
            " WHERE 1=1 AND ta.deleted=0 AND t.status!=0"+
            "<if test='toolingCode != null and toolingCode != \"\" '>" +
            " and ta.tooling_code like concat('%',#{toolingCode},'%')" +
            "</if>" +
            "<if test='toolingNumber != null and toolingNumber != \"\" '>" +
            " and ta.tooling_number like concat('%',#{toolingNumber},'%')" +
            "</if>" +
            "<if test='toolingName != null and toolingName != \"\" '>" +
            " and ta.tooling_name like concat('%',#{toolingName},'%')" +
            "</if>" +
            "<if test='status != null and status != \"\" '>" +
            " and t.status like concat('%',#{status},'%')" +
            "</if>" +
            " ORDER BY ta.status DESC"+
            "</script>")
    List<ToolingTaskRespVO> selectToolingDetailList(@Param("toolingCode") String toolingCode, @Param("toolingNumber") String toolingNumber, @Param("toolingName") String toolingName, @Param("status") String status);

    /**
     * 根据id查询工装详细设计详情
     * @param id
     * @return
     */
    @Select("<script>" +
            "SELECT ta.tooling_code AS toolingCode,"+
            "ta.id AS id,"+
            "ta.category_id AS categoryId,"+
            "ta.customized_index AS customizedIndex,"+
            "ta.quantity AS quantity,"+
            "ta.tooling_style AS toolingStyle,"+
            "ta.process_type AS processType,"+
            "ta.apply_reason AS applyReason,"+
            "ta.tooling_number AS toolingNumber,"+
            "ta.tooling_name AS toolingName,"+
            "ta.apply_name AS applyName,"+
            "ta.equipment_name AS equipmentName,"+
            "ta.equipment_number AS equipmentNumber,"+
            "ta.technical_requirement AS technicalRequirement,"+
            "ta.require_time AS requireTime,"+
            "ca.name AS name,"+
            "t.reviewed_by AS reviewedBy,"+
            "t.status AS status,"+
            "t.tooling_apply_id AS toolingApplyId,"+
            "t.reviewer AS reviewer,"+
            "t.deadline  AS deadline,"+
            "t.id  AS tid "+
            "FROM pdm_tooling_apply ta "+
            "LEFT JOIN pdm_tooling_task t ON t.tooling_apply_id = ta.id "+
            "LEFT JOIN pdm_tooling_category ca ON ca.id = ta.category_id"+
            " WHERE 1=1 AND ta.deleted=0"+
            "<if test='id != null and id != \"\"'>" +
            " and ta.id = #{id}" +
            "</if>" +
            "</script>")
    ToolingTaskRespVO selectToolingDetailById(@Param("id") String id);

    /**
     * 点击进度显示工装详细设计详情
     * @param id
     * @return
     */
    @Select("<script>" +
            "SELECT ta.tooling_code AS toolingCode,"+
            "ta.id AS id,"+
            "ta.category_id AS categoryId,"+
            "ta.quantity AS quantity,"+
            "ta.customized_index AS customizedIndex,"+
            "ta.tooling_style AS toolingStyle,"+
            "ta.process_type AS processType,"+
            "ta.apply_reason AS applyReason,"+
            "ta.tooling_number AS toolingNumber,"+
            "ta.tooling_name AS toolingName,"+
            "ta.apply_name AS applyName,"+
            "ta.equipment_name AS equipmentName,"+
            "ta.equipment_number AS equipmentNumber,"+
            "ta.technical_requirement AS technicalRequirement,"+
            "ta.require_time AS requireTime,"+
            "ca.name AS name,"+
            "t.reviewed_by AS reviewedBy,"+
            "t.status AS status,"+
            "t.tooling_apply_id AS toolingApplyId,"+
            "t.reviewer AS reviewer,"+
            "t.deadline  AS deadline,"+
            "t.id  AS tid "+
            "FROM pdm_tooling_apply ta "+
            "LEFT JOIN pdm_tooling_task t ON t.tooling_apply_id = ta.id "+
            "LEFT JOIN pdm_tooling_category ca ON ca.id = ta.category_id"+
            " WHERE 1=1 AND ta.deleted=0"+
            "<if test='id != null and id != \"\"'>" +
            " and t.id = #{id}" +
            "</if>" +
            "</script>")
    ToolingTaskReqVO selectById1(String id);

/**
 * 根据customized_index查询对应文件目录
 */
@Select("SELECT * FROM ${TableName_DF}")
List<ToolingTaskRespVO> selectFileByCustomizedIndex(@Param("TableName_DF") String TableName_DF);
}