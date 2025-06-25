package com.miyu.module.pdm.dal.mysql.process;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessPlanDetailRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcedureRespVO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureDO;
import com.miyu.module.pdm.dal.dataobject.process.ProcessDO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessDetailDO;
import com.miyu.module.pdm.dal.dataobject.processVersion.ProcessVersionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.Collection;
import java.util.List;

@Mapper
public interface ProcedureMapper extends BaseMapperX<ProcedureDO> {
    default Long selectCountByProcedureName(String procedureName) {
        return selectCount(ProcedureDO::getProcedureName, procedureName);
    }

    default Long selectCountByTypicalId(String typicalId) {
        return selectCount(ProcedureDO::getTypicalId, typicalId);
    }

    default List<ProcedureDO> selectByProcedureNumAndProcessVersionId(String ProcedureNum, String processVersionId) {
        return selectList(ProcedureDO::getProcedureNum, ProcedureNum, ProcedureDO::getProcessVersionId, processVersionId);
    }


    @Select(" SELECT * FROM capp_procedure "+
            " WHERE process_version_id = #{processVersionId} ")
    List<ProcessPlanDetailRespVO> selectByProcedureListByProcessVersionId(@Param("processVersionId") String processVersionId);


    default List<ProcedureDO> selectProcedureListByIds(Collection<String> ids){

        MPJLambdaWrapperX<ProcedureDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ProcessVersionDO.class,ProcessVersionDO::getId,ProcedureDO::getProcessVersionId)
                .leftJoin(ProcessDO.class,ProcessDO::getId,ProcessVersionDO::getProcessId)
                .selectAs(ProcessVersionDO::getProcessName,ProcedureDO ::getProcessName)
                .selectAs(ProcessDO ::getProcessCode,ProcedureDO ::getProcessCode)
                .selectAll(ProcedureDO.class);
        wrapperX.inIfPresent(ProcedureDO ::getId, ids);

        return selectList(wrapperX);

    }
}

