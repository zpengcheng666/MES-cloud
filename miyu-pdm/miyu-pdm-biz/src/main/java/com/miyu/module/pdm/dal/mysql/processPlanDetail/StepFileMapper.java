package com.miyu.module.pdm.dal.mysql.processPlanDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcedureFileRespVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcedureFileSaveReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.StepFileRespVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.StepFileSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureFileDO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.StepFileDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StepFileMapper extends BaseMapperX<StepFileDO> {
    @Select("SELECT id,sketch_code, sketch_url FROM capp_step_file WHERE process_version_id = #{processVersionId} AND procedure_id = #{procedureId} AND step_id = #{stepId} AND deleted = 0")
    List<StepFileRespVO> getStepFilesByIds(@Param("processVersionId") String processVersionId, @Param("procedureId") String procedureId, @Param("stepId") String stepId);

}
