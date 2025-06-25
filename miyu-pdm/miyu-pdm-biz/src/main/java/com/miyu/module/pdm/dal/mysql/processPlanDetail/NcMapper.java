package com.miyu.module.pdm.dal.mysql.processPlanDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.*;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureFileDO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.NcDO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.ProcessVersionNcDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NcMapper extends BaseMapperX<NcDO> {

    @Select(" SELECT cn.id AS id," +
            " cpvn.nc_id AS ncId, " +
            " cn.nc_name AS ncName, " +
            " cn.nc_url AS ncUrl "+
            " FROM capp_nc cn "+
            " LEFT JOIN capp_process_version_nc cpvn ON cn.id = cpvn.nc_id "+
            " WHERE 1 = 1 "+
            " AND cpvn.step_id = #{stepId} "+
            " AND cpvn.procedure_id = #{procedureId} "+
            " AND cpvn.process_version_id = #{processVersionId} "+
            " AND cpvn.deleted = 0 "+
            " AND cn.deleted = 0 " )
    List<NcRespVO> getNcByIds(@Param("stepId") String stepId, @Param("procedureId") String procedureId,@Param("processVersionId") String processVersionId);

    default int deleteNcById(NcReqVO reqVO) {
        return delete(Wrappers.lambdaUpdate(NcDO.class)
                .eq(NcDO::getId, reqVO.getNcId()));
    }

}
