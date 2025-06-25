package com.miyu.module.tms.dal.mysql.assembletask;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.tms.dal.dataobject.assembletask.AssembleTaskDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.tms.controller.admin.assembletask.vo.*;

/**
 * 刀具装配任务 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface AssembleTaskMapper extends BaseMapperX<AssembleTaskDO> {

    default PageResult<AssembleTaskDO> selectPage(AssembleTaskPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AssembleTaskDO>()
                .betweenIfPresent(AssembleTaskDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AssembleTaskDO::getOperator, reqVO.getOperator())
                .eqIfPresent(AssembleTaskDO::getOrderNumber, reqVO.getOrderNumber())
                .eqIfPresent(AssembleTaskDO::getTargetLocation, reqVO.getTargetLocation())
                .eqIfPresent(AssembleTaskDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(AssembleTaskDO::getStatus, reqVO.getStatus())
                .orderByDesc(AssembleTaskDO::getId));
    }

}