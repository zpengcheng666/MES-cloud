package com.miyu.module.pdm.dal.mysql.part;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.pdm.controller.admin.part.vo.AddPartRespVO;
import com.miyu.module.pdm.dal.dataobject.master.PartMasterDO;
import com.miyu.module.pdm.dal.dataobject.version.PartVersionDO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface PartVersionMapper extends BaseMapperX<PartVersionDO> {


    @Select("SELECT pv.id AS partVersionId ,pv.source AS source ,pm.part_number AS partNumber, pm.part_name AS partName, pv.part_version, pm.process_condition AS processCondition " +
            "FROM pdm_part_version pv " +
            "LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id " +
            "WHERE pv.id = #{id}")
    PartVersionDO selectPartInfoById(String id);

    @Select("<script>"+
            "SELECT * FROM pdm_part_version WHERE part_master_id = #{PMID} AND part_version = #{partVersion} AND deleted = 0"+
            "</script>")
    List<AddPartRespVO> selectPV(@Param("PMID") String PMID, @Param("partVersion") String partVersion);

    @Insert("<script>"+
            "INSERT INTO pdm_part_version (id,part_master_id,part_version,table_name,source) VALUES (#{id},#{part_master_id},#{part_version},#{table_name},#{source})"+
            "</script>")
    int insertPV(Map<String ,Object> pvMap);

    @Update("<script>"+
            "UPDATE pdm_part_version SET deleted = 1 WHERE id = #{PVID}"+
            "</script>")
    void deletePV(@Param("PVID") String PVID);
}