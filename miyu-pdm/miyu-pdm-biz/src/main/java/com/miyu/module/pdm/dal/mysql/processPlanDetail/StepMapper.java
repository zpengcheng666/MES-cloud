package com.miyu.module.pdm.dal.mysql.processPlanDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.StepDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface StepMapper extends BaseMapperX<StepDO> {



    @Select(" SELECT"+
            " id,"+
            " process_version_id,"+
            " procedure_id,"+
            " step_num,"+
            " step_property,"+
            " description,"+
            " creator,"+
            " updater,"+
            " create_time,"+
            " update_time,"+
            " deleted"+
            " FROM capp_step"+
            "  WHERE"+
            " deleted = 0"+
            " AND"+
            " procedure_id =  #{procedureId}"+
            " AND step_num = #{stepNum}" )
    StepDO selectByProcedureIDAndNumAndName(@Param("procedureId") String procedureId,
                                                  @Param("stepNum") String stepNum);
    @Select(" SELECT"+
            " id,"+
            " process_version_id,"+
            " procedure_id,"+
            " step_num,"+
            " step_property,"+
            " description,"+
            " creator,"+
            " updater,"+
            " create_time,"+
            " update_time,"+
            " deleted"+
            " FROM capp_step"+
            "  WHERE"+
            " deleted = 0"+
            " AND"+
            " procedure_id =  #{procedureId}"+
            " and step_num = #{stepNum}" )
    StepDO updateByProcedureIDAndNumAndName(@Param("procedureId") String procedureId,
                                            @Param("stepNum") String stepNum);

    default String selectCountByProcedureId(String procedureId) {
        return String.valueOf(selectCount(StepDO::getProcedureId, procedureId));
    }

    default Long selectCountByCategoryId(Long categoryId) {
        return selectCount(StepDO::getCategoryId, categoryId);
    }

}
