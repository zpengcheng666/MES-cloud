package com.miyu.module.pdm.dal.mysql.process;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcedureFileRespVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcedureFileSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureFileDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProcedureFileMapper extends BaseMapperX<ProcedureFileDO> {

    @Select("SELECT id,process_version_id,procedure_id,sketch_code, sketch_url FROM capp_procedure_file WHERE process_version_id = #{processVersionId} AND procedure_id = #{procedureId} AND deleted = 0")
    List<ProcedureFileRespVO> getProcedureFilesByIds(@Param("processVersionId") String processVersionId, @Param("procedureId") String procedureId);

}

