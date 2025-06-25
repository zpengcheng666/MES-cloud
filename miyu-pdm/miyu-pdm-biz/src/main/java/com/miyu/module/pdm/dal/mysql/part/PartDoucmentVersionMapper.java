package com.miyu.module.pdm.dal.mysql.part;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.pdm.dal.dataobject.document.PartDocumentVersionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PartDoucmentVersionMapper extends BaseMapperX<PartDocumentVersionDO> {
    @Select("SELECT dm.document_name AS documentName, dm.document_type AS documentType, dv.document_version AS documentVersion " +
            "FROM pdm_part_version pv " +
            "LEFT JOIN pdm_document_version dv ON pv.document_version_id = dv.id " +
            "LEFT JOIN pdm_document_master dm ON dv.document_master_id = dm.id " +
            "WHERE pv.id = #{partVersionId}")
    PartDocumentVersionDO getDocumentInfoByPartVersionId(String partVersionId);

    @Select("<script>" +
            "SELECT vault_url "+
            "FROM pdm_file "+
            " WHERE 1=1 "+
            "<if test='fileName != null and fileName != \"\"'>" +
            " and file_name = #{fileName}" +
            "</if>" +
            "<if test='fileType != null and fileType != \"\"'>" +
            " and file_type = #{fileType}" +
            "</if>" +
            " order by update_time desc limit 1 "+
            "</script>")
    String getModelUrl(@Param("fileName") String fileName, @Param("fileType") String fileType);
}
