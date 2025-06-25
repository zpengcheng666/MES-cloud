package com.miyu.module.tms.dal.mysql.toolinfo;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.tms.controller.admin.assembletask.vo.AssembleTaskPageReqVO;
import com.miyu.module.tms.dal.dataobject.assembletask.AssembleTaskDO;
import com.miyu.module.tms.dal.dataobject.toolconfig.ToolConfigDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolBalanceDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolBaseDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.tms.controller.admin.toolinfo.vo.*;

/**
 * 刀组信息 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface ToolInfoMapper extends BaseMapperX<ToolInfoDO> {

    default PageResult<ToolInfoDO> selectPage(ToolInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ToolInfoDO>()
                .betweenIfPresent(ToolInfoDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ToolInfoDO::getMaterialStockId, reqVO.getMaterialStockId())
                .eqIfPresent(ToolInfoDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(ToolInfoDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ToolInfoDO::getAssembleTaskId, reqVO.getAssembleTaskId())
                .orderByDesc(ToolInfoDO::getId));
    }

//    default PageResult<ToolInfoDO> selectAssembleTaskRecordPage(AssembleTaskPageReqVO reqVO){
//        MPJLambdaWrapperX<ToolInfoDO> wrapperX = new MPJLambdaWrapperX<>();
//        wrapperX.leftJoin(AssembleRecordDO.class, AssembleRecordDO::getToolInfoId, ToolInfoDO::getId)
//                .leftJoin(AssembleTaskDO.class,AssembleTaskDO::getId, ToolInfoDO::getAssembleTaskId)
//                .leftJoin(ToolBaseDO.class,ToolBaseDO::getToolInfoId,ToolInfoDO::getId)
//                .select(AssembleTaskDO::getOrderNumber)
//                .select(AssembleTaskDO::getDistributionDeadline)
//                .select(AssembleTaskDO::getMinimumTime)
//                .select(AssembleTaskDO::getTargetLocation)
//                .select(ToolBaseDO::getDiameter)
//                .select(ToolBaseDO::getRAngle)
//                .select(ToolBaseDO::getRatedLife)
//                .select(ToolBaseDO::getTotalLength)
//                .select(ToolBaseDO::getRemainLife)
//                .select(ToolInfoDO::getStatus)
//                .select(ToolInfoDO::getMaterialConfigId)
//                .select(ToolInfoDO::getMaterialStockId)
//                .groupBy(ToolInfoDO::getId)
//                .groupBy(AssembleTaskDO::getOrderNumber)
//                .groupBy(AssembleTaskDO::getDistributionDeadline)
//                .groupBy(AssembleTaskDO::getMinimumTime)
//                .groupBy(AssembleTaskDO::getTargetLocation)
//                .groupBy(ToolBaseDO::getDiameter)
//                .groupBy(ToolBaseDO::getRAngle)
//                .groupBy(ToolBaseDO::getRatedLife)
//                .groupBy(ToolBaseDO::getTotalLength)
//                .groupBy(ToolBaseDO::getRemainLife)
//                .groupBy(ToolInfoDO::getStatus)
//                .groupBy(ToolInfoDO::getMaterialConfigId)
//                .groupBy(ToolInfoDO::getMaterialStockId);
//
//        if(StringUtils.isNotBlank(reqVO.getId()))wrapperX.eq(ToolInfoDO::getAssembleTaskId, reqVO.getId());
//        if(StringUtils.isNotBlank(reqVO.getOrderNumber()))wrapperX.eq(AssembleTaskDO::getOrderNumber, reqVO.getOrderNumber());
//        if(StringUtils.isNotBlank(reqVO.getTargetLocation()))wrapperX.eq(AssembleTaskDO::getTargetLocation, reqVO.getTargetLocation());
//        if(StringUtils.isNotBlank(reqVO.getMaterialConfigId()))wrapperX.eq(ToolInfoDO::getMaterialConfigId, reqVO.getMaterialConfigId());
//        return selectPage(reqVO, wrapperX);
//    }

    default PageResult<ToolInfoDO> selectAssembleTaskRecordPage(AssembleTaskPageReqVO reqVO){
        MPJLambdaWrapperX<ToolInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(AssembleTaskDO.class,AssembleTaskDO::getId, ToolInfoDO::getAssembleTaskId)
                .leftJoin(ToolBaseDO.class,ToolBaseDO::getToolInfoId,ToolInfoDO::getId)
                .select(AssembleTaskDO::getOrderNumber)
                .select(AssembleTaskDO::getDistributionDeadline)
                .select(AssembleTaskDO::getMinimumTime)
                .select(AssembleTaskDO::getTargetLocation)
                .select(ToolBaseDO::getDiameter)
                .select(ToolBaseDO::getRAngle)
                .select(ToolBaseDO::getRatedLife)
                .select(ToolBaseDO::getTotalLength)
                .select(ToolBaseDO::getRemainLife)
                .select(ToolInfoDO::getStatus)
                .select(ToolInfoDO::getMaterialConfigId)
                .select(ToolInfoDO::getMaterialStockId)
                .selectAll(ToolInfoDO.class);

        if(StringUtils.isNotBlank(reqVO.getId()))wrapperX.eq(ToolInfoDO::getAssembleTaskId, reqVO.getId());
        if(StringUtils.isNotBlank(reqVO.getOrderNumber()))wrapperX.eq(AssembleTaskDO::getOrderNumber, reqVO.getOrderNumber());
        if(StringUtils.isNotBlank(reqVO.getTargetLocation()))wrapperX.eq(AssembleTaskDO::getTargetLocation, reqVO.getTargetLocation());
        if(StringUtils.isNotBlank(reqVO.getMaterialConfigId()))wrapperX.eq(ToolInfoDO::getMaterialConfigId, reqVO.getMaterialConfigId());
        return selectPage(reqVO, wrapperX);
    }


    default List<ToolInfoDO> getToolInfoById(String id){

        MPJLambdaWrapperX<ToolInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(AssembleTaskDO.class,AssembleTaskDO::getId, ToolInfoDO::getAssembleTaskId)
                .leftJoin(ToolConfigDO.class,ToolConfigDO::getMaterialConfigId,ToolInfoDO::getMaterialConfigId)
                .select(AssembleTaskDO::getOrderNumber)
                .select(AssembleTaskDO::getDistributionDeadline)
                .select(AssembleTaskDO::getMinimumTime)
                .select(ToolConfigDO::getToolName)
                .select(ToolConfigDO::getToolModel)
                .select(ToolConfigDO::getToolWeight)
                .select(ToolConfigDO::getToolTexture)
                .select(ToolConfigDO::getToolCoating)
                .select(ToolConfigDO::getRatedLife)
                .select(ToolConfigDO::getMaxSpeed)
                .select(ToolConfigDO::getLengthUpper)
                .select(ToolConfigDO::getLengthFloor)
                .select(ToolConfigDO::getHangingLengthFloor)
                .select(ToolConfigDO::getHangingLengthUpper)
                .select(ToolConfigDO::getBladeFloorDeviation)
                .select(ToolConfigDO::getBladeUpperDeviation)
                .select(ToolConfigDO::getRFloorDeviation)
                .select(ToolConfigDO::getRUpperDeviation)
                .select(ToolConfigDO::getMaterialNumber)
                .selectAll(ToolInfoDO.class);
        wrapperX.eqIfPresent(ToolInfoDO::getId, id);

        return selectList(wrapperX);

    }

}