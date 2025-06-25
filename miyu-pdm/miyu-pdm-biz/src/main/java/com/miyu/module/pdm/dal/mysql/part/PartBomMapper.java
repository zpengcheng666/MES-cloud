package com.miyu.module.pdm.dal.mysql.part;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.pdm.dal.dataobject.bom.PartBomDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PartBomMapper extends BaseMapperX<PartBomDO> {

    @Select("SELECT "
            + "b.id, "
            + "a.datapackage_bom_id, "
            + "a.id, "
            + "a.file_id, "
            + "f.file_name as fileName, "
            + "f.file_type as fileType, "
            + "f.vault_url "
            + "FROM pdm_datapackage_bom b "
            + "LEFT JOIN pdm_receive_attachment a ON b.id = a.datapackage_bom_id "
            + "LEFT JOIN pdm_file f ON a.file_id = f.id "
            + "WHERE b.part_version_id = #{partVersionId}")
    List<Map<String, Object>> selectByBom(@Param("partVersionId") String partVersionId);

    @Select("SELECT id FROM pdm_datapackage_bom WHERE part_version_id = #{partVersionId} order by update_time desc")
    List<String> getIdByPartVersionId(@Param("partVersionId") String partVersionId);

}
