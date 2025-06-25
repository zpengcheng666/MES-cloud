package com.miyu.module.pdm.dal.mysql.ToolingDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingDetailTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.toolingDeatail.ToolingDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface ToolingDetailMapper extends BaseMapperX<ToolingDetailDO> {

    @Select("SELECT " +
            "a.id as piid, " +
            "a.name as instanceNumber, " +
            "a.rootproduct_id as rootProductId, " +
            "a.part_version_id as partVersionId, " +
            "a.parent_id as parentPiid, " +
            "a.customized_index as customizedIndex, " +
            "a.serial_number as serialNumber, " +
            "a.vmatrix01 as vmatrix01, " +
            "a.vmatrix02 as vmatrix02, " +
            "a.vmatrix03 as vmatrix03, " +
            "a.vmatrix04 as vmatrix04, " +
            "a.vmatrix05 as vmatrix05, " +
            "a.vmatrix06 as vmatrix06, " +
            "a.vmatrix07 as vmatrix07, " +
            "a.vmatrix08 as vmatrix08, " +
            "a.vmatrix09 as vmatrix09, " +
            "a.vmatrix10 as vmatrix10, " +
            "a.vmatrix11 as vmatrix11, " +
            "a.vmatrix12 as vmatrix12, " +
            "a.type as type, " +
            "c.part_number as partNumber, " +
            "c.part_name as partName, " +
            "b.part_version as partVersion, " +
            "b.document_version_id as documentVersionId, " +
            "f.vault_url as vaultUrl " +
            " FROM ${tableName_PI} a " +
            " LEFT JOIN ${tableName_PV} b ON a.part_version_id = b.id " +
            " LEFT JOIN ${tableName_PM} c ON b.part_master_id = c.id " +
            " LEFT JOIN ${tableName_DR} e ON b.document_version_id = e.document_version_id " +
            " LEFT JOIN ${tableName_DF} f ON e.file_id = f.id " +
            "  where a.rootproduct_id = #{rootproductId}")
    List<ToolingDetailTreeRespVO> selectTreeList(@Param("tableName_PI") String tableName_PI,@Param("tableName_PV") String tableName_PV,@Param("tableName_PM") String tableName_PM,@Param("tableName_DR") String tableName_DR,@Param("tableName_DF") String tableName_DF,@Param("rootproductId") String rootproductId);

}
