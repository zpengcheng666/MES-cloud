package com.miyu.module.pdm.dal.mysql.toolingApply;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingApplyReqVO;
import com.miyu.module.pdm.dal.dataobject.toolingApply.ToolingApplyDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ToolingApplyMapper extends BaseMapperX<ToolingApplyDO> {
    default PageResult<ToolingApplyDO> selectPage(ToolingApplyReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ToolingApplyDO>()
                .likeIfPresent(ToolingApplyDO::getToolingCode, reqVO.getToolingCode())
                .likeIfPresent(ToolingApplyDO::getToolingNumber, reqVO.getToolingNumber())
                .likeIfPresent(ToolingApplyDO::getToolingName, reqVO.getToolingName())
                .eqIfPresent(ToolingApplyDO::getStatus, reqVO.getStatus()));
    }

    @Select("<script>" +
            "SELECT ap.*, ca.name ,ca.parent_id AS parentId " +
            "FROM pdm_tooling_apply ap " +
            "JOIN pdm_tooling_category ca ON ca.id = ap.category_id " +
            "WHERE  ap.deleted = 0 " +
            "AND (ap.status = '0' OR ap.status = '1' OR ap.status = '2' OR ap.status = '3') " +
            "<if test='status != null '>" +
            " AND ap.status = #{status} " +
            "</if>" +
            "<if test='toolingCode != null and toolingCode != \"\"'>" +
            "AND ap.tooling_code LIKE CONCAT('%', #{toolingCode}, '%') " +
            "</if>" +
            "<if test='toolingNumber != null and toolingNumber != \"\"'>" +
            "AND ap.tooling_number LIKE CONCAT('%', #{toolingNumber}, '%') " +
            "</if>" +
            "<if test='toolingName != null and toolingName != \"\"'>" +
            "AND ap.tooling_name LIKE CONCAT('%', #{toolingName}, '%') " +
            "</if>" +
            "</script>")
   List<ToolingApplyDO> getToolingApplyList(ToolingApplyReqVO reqVO);

    @Select("SELECT customized_index from pdm_tooling_apply where id=#{rootProductId} ")
    String selectCustomzedIndexById(String rootProductId);

    @Select( "SELECT ap.*, ca.name  " +
            "FROM pdm_tooling_apply ap " +
            "JOIN pdm_tooling_category ca ON ca.id = ap.category_id " +
            "WHERE 1=1 AND ap.deleted = 0 AND ap.id=#{id}")
    ToolingApplyReqVO selectById1(String id);


    @Select("SELECT * " +
            "FROM pdm_tooling_apply " +
            "WHERE 1=1 AND deleted = 0 OR tooling_code=#{toolingCode} OR tooling_number=#{toolingNumber} OR tooling_name=#{toolingName}")
    ToolingApplyReqVO selectBy(@Param("toolingCode") String toolingCode , @Param("toolingNumber") String toolingNumber, @Param("toolingName") String toolingName);

   @Select("SELECT * " +
           "FROM pdm_tooling_apply " +
           "WHERE 1=1 AND deleted=0 " +
           "AND tooling_number=#{toolingNumber}")
    ToolingApplyDO selectByIdAndToolingNumber(@Param("toolingNumber") String toolingNumber);
}
