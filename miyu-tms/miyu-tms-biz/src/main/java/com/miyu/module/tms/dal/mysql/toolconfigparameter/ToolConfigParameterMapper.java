package com.miyu.module.tms.dal.mysql.toolconfigparameter;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.tms.dal.dataobject.toolconfigparameter.ToolConfigParameterDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.tms.controller.admin.toolconfigparameter.vo.*;

/**
 * 刀具参数信息 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface ToolConfigParameterMapper extends BaseMapperX<ToolConfigParameterDO> {

    default PageResult<ToolConfigParameterDO> selectPage(ToolConfigParameterPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ToolConfigParameterDO>()
                .eqIfPresent(ToolConfigParameterDO::getToolConfigId, reqVO.getToolConfigId())
                .likeIfPresent(ToolConfigParameterDO::getName, reqVO.getName())
                .eqIfPresent(ToolConfigParameterDO::getValue, reqVO.getValue())
                .eqIfPresent(ToolConfigParameterDO::getAbbr, reqVO.getAbbr())
                .eqIfPresent(ToolConfigParameterDO::getUnit, reqVO.getUnit())
                .eqIfPresent(ToolConfigParameterDO::getSort, reqVO.getSort())
                .eqIfPresent(ToolConfigParameterDO::getType, reqVO.getType())
                .orderByDesc(ToolConfigParameterDO::getId));
    }

}
