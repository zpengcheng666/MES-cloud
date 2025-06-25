package com.miyu.module.pdm.dal.mysql.feasibilityDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcedureSchemaItemRespVO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.ProcedureSchemaItemDO;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface ProcedureSchemaItemMapper extends BaseMapperX<ProcedureSchemaItemDO> {

    @Select("SELECT a.id AS id," +
            " procedure_id AS procedureId," +
            "b.item_name AS inspectionItemName," +
            " process_version_id AS processVersionId," +
            " inspection_item_id AS inspectionItemId," +
            " number AS number," +
            " reference_type AS referenceType," +
            " largest_value AS largestValue," +
            " min_value AS minValue," +
            " a.content AS content," +
            " judgement AS judgement," +
            " acceptance_quality_limit AS acceptanceQualityLimit" +
            " FROM capp_procedure_scheme_item a " +
            "LEFT JOIN `miyu-qms`.qms_inspection_item b ON b.id = a.inspection_item_id " +
            "WHERE a.process_version_id = #{processVersionId} " +
            "AND a.procedure_id = #{procedureId} " +
            "AND a.deleted = 0")
    List<ProcedureSchemaItemRespVO> getProcedureSchemaItemByIds(@Param("processVersionId") String processVersionId, @Param("procedureId") String procedureId);

    void saveBatch(List<ProcedureSchemaItemDO> doList);

    @Delete("DELETE FROM capp_procedure_scheme_item WHERE process_version_id = #{processVersionId} AND procedure_id = #{procedureId}")
    void deleteByVersionAndProcedure(@Param("processVersionId") String processVersionId, @Param("procedureId") String procedureId);
}
