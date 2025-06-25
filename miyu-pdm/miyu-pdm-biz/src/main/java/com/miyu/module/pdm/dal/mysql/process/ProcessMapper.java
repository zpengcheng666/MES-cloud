package com.miyu.module.pdm.dal.mysql.process;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.process.ProcessDO;
import com.miyu.module.pdm.dal.dataobject.processVersion.ProcessVersionDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProcessMapper extends BaseMapperX<ProcessDO> {
    @Select("SELECT a.id, a.process_id, a.process_name, a.process_version, a.description, a.property, "
            + "b.part_version_id,pm.part_number, b.process_code,b.material_id,b.material_number,b.material_desg,b.material_code,b.material_name,"
            + "b.material_specification, b.process_scheme_code, b.single_size, b.group_size, b.process_route_name, b.single_quatity,a.is_valid, a.status, a.process_change_id "
            + "FROM capp_process_version a "
            + "LEFT JOIN capp_process b ON b.id = a.process_id "
            + "LEFT JOIN pdm_part_version pv ON pv.id = b.part_version_id "
            + "LEFT JOIN pdm_part_master pm ON pm.id = pv.part_master_id "
            + "WHERE a.id = #{id}")
    ProcessRespVO selectCappProcessById(@Param("id") String id);

}

