package com.miyu.module.pdm.dal.mysql.processSupplement;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessPlanDetailRespVO;
import com.miyu.module.pdm.controller.admin.processSupplement.vo.ProcessSupplementPageReqVO;
import com.miyu.module.pdm.dal.dataobject.master.PartMasterDO;
import com.miyu.module.pdm.dal.dataobject.process.ProcessDO;
import com.miyu.module.pdm.dal.dataobject.processSupplement.ProcessSupplementDO;
import com.miyu.module.pdm.dal.dataobject.processVersion.ProcessVersionDO;
import com.miyu.module.pdm.dal.dataobject.version.PartVersionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProcessSupplementMapper extends BaseMapperX<ProcessSupplementDO> {
    default PageResult<ProcessSupplementDO> selectPage(ProcessSupplementPageReqVO reqVO) {
        MPJLambdaWrapperX<ProcessSupplementDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ProcessVersionDO.class, ProcessVersionDO::getId, ProcessSupplementDO::getProcessVersionId)
                .leftJoin(ProcessDO.class, ProcessDO::getId, ProcessVersionDO::getProcessId)
                .leftJoin(PartVersionDO.class, PartVersionDO::getId, ProcessDO::getPartVersionId)
                .leftJoin(PartMasterDO.class, PartMasterDO::getId, PartVersionDO::getPartMasterId)
                .select(ProcessDO::getProcessCode)
                .select(PartMasterDO::getPartName)
                .select(ProcessDO::getProcessCondition)
                .selectAll(ProcessSupplementDO.class);
        return selectPage(reqVO, wrapperX
                .likeIfPresent(ProcessSupplementDO::getProjectCode, reqVO.getProjectCode())
                .likeIfPresent(ProcessSupplementDO::getPartNumber, reqVO.getPartNumber())
                .likeIfPresent(ProcessSupplementDO::getProcessCodeSupplement, reqVO.getProcessCodeSupplement())
                .eqIfPresent(ProcessSupplementDO::getStatus, reqVO.getStatus())
                .orderByAsc(ProcessSupplementDO::getStatus));
    }

    default ProcessSupplementDO selectByProcessCodeSupplement(String processCodeSupplement) {
        return selectOne(ProcessSupplementDO::getProcessCodeSupplement, processCodeSupplement);
    }

    @Select("<script>" +
            " SELECT * FROM ( " +
            " SELECT * FROM ( " +
            " SELECT" +
            " '0' AS type," +
            " ps.id," +
            " '0' AS parentId," +
            " ps.process_code_supplement AS name," +
            " '0' AS serialnum," +
            "  ps.id AS processVersionId" +
            "  FROM capp_process_supplement ps" +
            " where ps.deleted=0" +
            " ) tablea UNION" +
            " SELECT * FROM (" +
            " SELECT" +
            " '1' AS type," +
            " proce.id AS id," +
            " proce.process_version_id AS parentId," +
            " concat( proce.procedure_num, '(', proce.procedure_name, ')' ) AS name," +
            " proce.procedure_num AS serialnum," +
            " proce.process_version_id AS processVersionId" +
            " FROM capp_procedure proce" +
            " LEFT JOIN capp_process_supplement ps ON ps.id = proce.process_version_id" +
            " where proce.deleted=0 and ps.deleted=0" +
            " ) tabled UNION" +
            " SELECT * FROM (" +
            " SELECT" +
            " '2' AS type," +
            " step.id AS id," +
            " step.procedure_id AS parentId," +
            " CONCAT(step.step_num, '(', IFNULL(step.step_name,''), ')' ) AS name," +
            " step.step_num AS serialnum," +
            " step.process_version_id AS processVersionId" +
            " FROM capp_step step" +
            " LEFT JOIN capp_procedure proce ON proce.id = step.procedure_id" +
            " LEFT JOIN capp_process_supplement ps ON ps.id = proce.process_version_id" +
            " where step.deleted=0 and proce.deleted=0 and ps.deleted=0" +
            " ) tablec " +
            " ) tabled " +
            " WHERE 1=1  " +
            "<if test='processVersionId != null and processVersionId != \"\"'>" +
            "AND tabled.processVersionId = #{processVersionId} " +
            "</if>" +
            " ORDER BY serialnum " +
            "</script>")
    List<ProcessPlanDetailRespVO> selectProcessSupplementTreeList(@Param("processVersionId") String processVersionId);
}
