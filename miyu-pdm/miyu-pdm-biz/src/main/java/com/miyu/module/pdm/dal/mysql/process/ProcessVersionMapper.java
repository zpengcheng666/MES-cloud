package com.miyu.module.pdm.dal.mysql.process;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessRespVO;
import com.miyu.module.pdm.dal.dataobject.processVersion.ProcessVersionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProcessVersionMapper extends BaseMapperX<ProcessVersionDO> {
    @Select("<script>" +
            "SELECT max( a.process_version ) AS process_version, " +
            " max( p.process_scheme_code ) AS process_scheme_code " +
            " FROM capp_process_version a " +
            " LEFT JOIN capp_pbom_process_version d ON a.id = d.process_version_id " +
            " LEFT JOIN capp_process p ON p.id = a.process_id  " +
            " WHERE 1=1 AND a.deleted=0 AND d.deleted=0 AND p.deleted=0" +
            "<if test='partVersionId != null and partVersionId != \"\" '>" +
            " AND p.part_version_id = #{partVersionId} " +
            "</if>" +
            "<if test='processId != null and processId != \"\" '>" +
            " AND a.process_id = #{processId} " +
            "</if>" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " AND d.project_code = #{projectCode} " +
            "</if>" +
            "</script>")
    Map<String, Object> selectProcessVersion(@Param("partVersionId") String partVersionId,@Param("processId") String processId,@Param("projectCode") String projectCode);

    @Select("<script>" +
            "SELECT count(1) as count " +
            "FROM capp_process_version a " +
            "LEFT JOIN capp_process b ON b.id = a.process_id " +
            "WHERE a.status='3' AND a.deleted=0" +
            "<if test='partVersionId != null and partVersionId != \"\" '>" +
            " AND b.part_version_id = #{partVersionId} " +
            "</if>" +
            "<if test='processCondition != null and processCondition != \"\" '>" +
            " AND b.process_condition = #{processCondition} " +
            "</if>" +
            "</script>")
    Integer selectProcessCount(@Param("partVersionId") String partVersionId,@Param("processCondition") String processCondition);

    @Select("<script>" +
            "SELECT a.id, a.process_id, a.process_name, a.process_version, a.description, a.property, " +
            "b.part_version_id, b.process_code, b.process_scheme_code, b.single_size, b.group_size, " +
            "b.process_route_name, b.single_quatity, b.process_condition " +
            "FROM capp_process_version a " +
            "LEFT JOIN capp_process b ON b.id = a.process_id " +
            " WHERE a.status='3' AND a.deleted=0 AND b.deleted=0" +
            "<if test='partVersionId != null and partVersionId != \"\" '>" +
            " AND b.part_version_id = #{partVersionId} " +
            "</if>" +
            "<if test='processCondition != null and processCondition != \"\" '>" +
            " AND b.process_condition = #{processCondition} " +
            "</if>" +
            " ORDER BY a.process_version " +
            "</script>")
    List<ProcessRespVO> selectProcessList(@Param("partVersionId") String partVersionId,@Param("processCondition") String processCondition);

    @Select("<script>" +
            "SELECT project_code from capp_pbom_process_version a " +
            " WHERE 1=1 AND a.deleted=0" +
            "<if test='processVersionId != null and processVersionId != \"\" '>" +
            " AND a.process_version_id = #{processVersionId} " +
            "</if>" +
            "<if test='projectCode != null and projectCode != \"\" '>" +
            " AND a.project_code != #{projectCode} " +
            "</if>" +
            " order by update_time desc limit 1" +
            "</script>")
    Map<String, Object> selectPbomProcessVersion(@Param("processVersionId") String processVersionId,@Param("projectCode") String projectCode);
}
