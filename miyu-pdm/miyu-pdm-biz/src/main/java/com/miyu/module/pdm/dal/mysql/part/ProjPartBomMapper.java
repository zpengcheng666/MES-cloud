package com.miyu.module.pdm.dal.mysql.part;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miyu.module.pdm.controller.admin.part.vo.AddPartRespVO;
import com.miyu.module.pdm.dal.dataobject.part.NewPartDO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProjPartBomMapper extends BaseMapper<NewPartDO> {

    @Select("<script>"+
            "select * from pdm_proj_part_bom where company_id = #{companyId} AND part_version_id = #{PVID} AND deleted = 0"+
            "</script>")
    List<AddPartRespVO> selectPPB(@Param("companyId") String companyId, @Param("PVID") String PVID);

    @Insert("<script>"+
            "INSERT INTO pdm_proj_part_bom (id,company_id,company_name,part_version_id,table_name) VALUES (#{id},#{company_id},#{company_name},#{part_version_id},#{table_name})"+
            "</script>")
    int insertPPB(Map<String ,Object> ppbMap);

    @Update("<script>"+
            "UPDATE pdm_proj_part_bom SET deleted = 1 WHERE part_version_id = #{PVID}"+
            "</script>")
    void deletePPB(@Param("PVID") String PVID);
}
