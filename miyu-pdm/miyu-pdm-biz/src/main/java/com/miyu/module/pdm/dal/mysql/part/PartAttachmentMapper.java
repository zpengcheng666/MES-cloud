package com.miyu.module.pdm.dal.mysql.part;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.pdm.controller.admin.part.vo.PartAttachmentRespVO;
import com.miyu.module.pdm.dal.dataobject.attachment.PartAttachmentDO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.NcDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PartAttachmentMapper extends BaseMapperX<PartAttachmentDO> {
    @Select("SELECT file_id FROM pdm_receive_attachment WHERE datapackage_bom_id = #{datapackageBomId}")
    List<Long> getFileIdsByDatapackageBomId(@Param("datapackageBomId") String datapackageBomId);

    @Select("SELECT a.datapackage_bom_id, a.file_id as fileId, f.file_name as fileName, f.file_type as fileType, f.vault_url as vaultUrl " +
            "FROM pdm_receive_attachment a " +
            "LEFT JOIN pdm_file f ON a.file_id = f.id " +
            "WHERE a.datapackage_bom_id = #{datapackageBomId}")
    List<Map<String, Object>> getFilesByDatapackageBomId(@Param("datapackageBomId") String datapackageBomId);

    /**
     * 根据数据包ID查询附件信息。
     *
     * @param datapackageBomId 数据包ID
     * @return 附件信息列表
     */
    @Select("<script>"+
            " SELECT * FROM ( "+
            "   SELECT "+
            "       a.id, "+
            "       a.deleted, "+
            "       a.datapackage_bom_id, "+
            "       a.file_source AS fileSource," +
            "       a.file_id AS fileId, "+
            "       f.file_name AS fileName, "+
            "       f.file_type AS fileType, "+
            "       f.vault_url AS vaultUrl "+
            "   FROM "+
            "       pdm_receive_attachment a "+
            "   LEFT JOIN pdm_file f ON a.file_id = f.id "+
            "   WHERE a.file_source = '0' "+
            "   UNION "+
            "   SELECT "+
            "       a.id, "+
            "       a.deleted, "+
            "       a.datapackage_bom_id, "+
            "       a.file_source AS fileSource," +
            "       '' AS fileId, "+
            "       a.file_name AS fileName, "+
            "       '' AS fileType, "+
            "       a.vault_url AS vaultUrl "+
            "   FROM "+
            "       pdm_receive_attachment a "+
            "   WHERE a.file_source = '1' "+
            ") t "+
            "WHERE t.datapackage_bom_id = #{datapackageBomId} AND deleted = 0 order by t.fileSource"+
            "</script>")
    List<Map<String, Object>>  findAttachmentsByDatapackageBomId(@Param("datapackageBomId") String datapackageBomId);

    default int deleteNewFile(PartAttachmentRespVO saveReqVO) {
        return delete(Wrappers.lambdaUpdate(PartAttachmentDO.class)
                .eq(PartAttachmentDO::getId, saveReqVO.getId()));
    }
}
