package com.miyu.module.pdm.dal.mysql.feasibilityDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.FeasibilityDetailReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomRespVO;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ResourceSelectedReqVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.FeasibilityDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FeasibilityDetailMapper extends BaseMapperX<FeasibilityDetailDO> {

    @Select("<script>" +
            "SELECT A.* FROM (" +
            "SELECT pb.id, pb.part_version_id AS partVersionId, pm.part_number as partNumber, pm.part_name as partName, pv.part_version as partVersion, " +
            " pm.root_product_id AS rootProductId, rp.product_number AS productNumber,o.customized_index AS customizedIndex, " +
            " t.reviewed_by AS reviewedBy,t.reviewer,t.deadline,t.status,t.id AS taskId,t.process_instance_id AS processInstanceId, " +
            " d.is_pass AS isPass " +
            " FROM pdm_proj_part_bom pb " +
            " LEFT JOIN capp_feasibility_task t ON pb.project_code = t.project_code AND pb.part_version_id = t.part_version_id " +
            " LEFT JOIN pdm_part_version pv ON pb.part_version_id = pv.id " +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " LEFT JOIN pdm_root_product rp ON pm.root_product_id = rp.id" +
            " LEFT JOIN pdm_data_object o ON pm.root_product_id = o.rootproduct_id and o.std_data_object = 'PartInstance'" +
            " LEFT JOIN capp_feasibility_result d ON d.project_code = t.project_code and d.part_version_id = t.part_version_id" +
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
            "SELECT pb.project_code AS projectCode, pb.id, pb.part_version_id AS partVersionId, pm.part_number as partNumber, pm.part_name as partName, pv.part_version as partVersion, " +
            " pm.root_product_id AS rootProductId, rp.product_number AS productNumber,o.customized_index AS customizedIndex, " +
            " t.reviewed_by AS reviewedBy,t.reviewer,t.deadline,t.status,t.id AS taskId,t.process_instance_id AS processInstanceId, " +
            " d.is_pass AS isPass,d.reason " +
            " FROM pdm_proj_part_bom pb " +
            " LEFT JOIN capp_feasibility_task t ON pb.project_code = t.project_code AND pb.part_version_id = t.part_version_id " +
            " LEFT JOIN pdm_part_version pv ON pb.part_version_id = pv.id " +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " LEFT JOIN pdm_root_product rp ON pm.root_product_id = rp.id" +
            " LEFT JOIN pdm_data_object o ON pm.root_product_id = o.rootproduct_id and o.std_data_object = 'PartInstance'" +
            " LEFT JOIN capp_feasibility_result d ON d.project_code = t.project_code and d.part_version_id = t.part_version_id" +
            " WHERE 1=1" +
            "<if test='taskId != null and taskId != \"\" '>" +
            " and (t.id = #{taskId} or pb.id = #{taskId}) " +
            "</if>" +
            "</script>")
    ProjPartBomRespVO selectPartDetail(@Param("taskId") String taskId);

    default int deleteByProjectCode(ResourceSelectedReqVO reqVO) {
        return delete(Wrappers.lambdaUpdate(FeasibilityDetailDO.class)
                .eq(FeasibilityDetailDO::getProjectCode, reqVO.getProjectCode())
                .eq(FeasibilityDetailDO::getPartVersionId, reqVO.getPartVersionId())
                .eq(FeasibilityDetailDO::getResourcesType, reqVO.getResourcesType()));
    }

    default int deleteByResourceId(FeasibilityDetailReqVO reqVO) {
        return delete(Wrappers.lambdaUpdate(FeasibilityDetailDO.class)
                .eq(FeasibilityDetailDO::getProjectCode, reqVO.getProjectCode())
                .eq(FeasibilityDetailDO::getPartVersionId, reqVO.getPartVersionId())
                .eq(FeasibilityDetailDO::getResourcesType, reqVO.getResourcesType())
                .eq(FeasibilityDetailDO::getResourcesTypeId, reqVO.getResourcesTypeId()));
    }

    default List<FeasibilityDetailDO> selectResourceList(ResourceSelectedReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<FeasibilityDetailDO>()
                .eq(FeasibilityDetailDO::getProjectCode, reqVO.getProjectCode())
                .eq(FeasibilityDetailDO::getPartVersionId, reqVO.getPartVersionId())
                .eq(FeasibilityDetailDO::getResourcesType, reqVO.getResourcesType())
                .orderByDesc(FeasibilityDetailDO::getId));
    }

    @Select("<script>" +
            "SELECT A.* FROM (" +
            "SELECT t.part_version_id AS partVersionId, pm.part_number as partNumber, pm.part_name as partName, pv.part_version as partVersion, " +
            " t.project_code AS projectCode,t.process_condition AS processCondition, " +
            " pm.root_product_id AS rootProductId, rp.product_number AS productNumber,o.customized_index AS customizedIndex, " +
            " t.reviewed_by AS reviewedBy,t.reviewer,t.deadline,t.status,t.id AS taskId,t.process_instance_id AS processInstanceId, " +
            " d.is_pass AS isPass,d.reason " +
            " FROM capp_feasibility_task t " +
            " LEFT JOIN pdm_part_version pv ON t.part_version_id = pv.id " +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " LEFT JOIN pdm_root_product rp ON pm.root_product_id = rp.id" +
            " LEFT JOIN pdm_data_object o ON pm.root_product_id = o.rootproduct_id and o.std_data_object = 'PartInstance'" +
            " LEFT JOIN capp_feasibility_result d ON d.project_code = t.project_code and d.part_version_id = t.part_version_id" +
            " WHERE 1=1 AND t.deleted=0" +
            " AND t.status != '0'" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " and t.project_code like concat('%',#{projectCode},'%') " +
            "</if>" +
            "<if test='partNumber != null and partNumber != \"\" '>" +
            " and pm.part_number like concat('%',#{partNumber},'%')" +
            "</if>" +
            "<if test='reviewedBy != null and reviewedBy != \"\" '>" +
            " and t.reviewed_by = #{reviewedBy} " +
            "</if>" +
            "<if test='projectStatus != null '>" +
            " and t.project_status = #{projectStatus} " +
            "</if>" +
            " ) A WHERE 1=1 " +
            "<if test='status != null '>" +
            " and A.status = #{status} " +
            "</if>" +
            " ORDER BY A.status,A.deadline"+
            "</script>")
    List<ProjPartBomRespVO> selectPartListNew(@Param("projectCode") String projectCode, @Param("partNumber") String partNumber, @Param("status") Integer status, @Param("reviewedBy") String reviewedBy, @Param("projectStatus") Integer projectStatus);

    @Select("<script>" +
            "SELECT t.part_version_id AS partVersionId, pm.part_number as partNumber, pm.part_name as partName, pv.part_version as partVersion, " +
            " t.project_code AS projectCode,t.process_condition AS processCondition, " +
            " pm.root_product_id AS rootProductId, rp.product_number AS productNumber,o.customized_index AS customizedIndex, " +
            " t.reviewed_by AS reviewedBy,t.reviewer,t.deadline,t.status,t.id AS taskId,t.process_instance_id AS processInstanceId, " +
            " d.is_pass AS isPass,d.reason " +
            " FROM capp_feasibility_task t " +
            " LEFT JOIN pdm_part_version pv ON t.part_version_id = pv.id " +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " LEFT JOIN pdm_root_product rp ON pm.root_product_id = rp.id" +
            " LEFT JOIN pdm_data_object o ON pm.root_product_id = o.rootproduct_id and o.std_data_object = 'PartInstance'" +
            " LEFT JOIN capp_feasibility_result d ON d.project_code = t.project_code and d.part_version_id = t.part_version_id" +
            " WHERE 1=1" +
            "<if test='taskId != null and taskId != \"\" '>" +
            " and t.id = #{taskId} " +
            "</if>" +
            "</script>")
    ProjPartBomRespVO selectPartDetailNew(@Param("taskId") String taskId);
}
