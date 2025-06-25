package com.miyu.module.wms.dal.mysql.carrytask;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.carrytask.vo.*;

/**
 * 搬运任务 Mapper
 *
 * @author 技术部长
 */
@Mapper
public interface CarryTaskMapper extends BaseMapperX<CarryTaskDO> {

    default PageResult<CarryTaskDO> selectPage(CarryTaskPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CarryTaskDO>()
                .betweenIfPresent(CarryTaskDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CarryTaskDO::getTaskCode, reqVO.getTaskCode())
                .eqIfPresent(CarryTaskDO::getTaskStatus, reqVO.getTaskStatus())
                .eqIfPresent(CarryTaskDO::getTaskType, reqVO.getTaskType())
                .eqIfPresent(CarryTaskDO::getTaskContent, reqVO.getTaskContent())
                .eqIfPresent(CarryTaskDO::getTaskDescription, reqVO.getTaskDescription())
                .eqIfPresent(CarryTaskDO::getAgvId, reqVO.getAgvId())
                .eqIfPresent(CarryTaskDO::getOrderIds, reqVO.getOrderIds())
                .orderByDesc(CarryTaskDO::getId));
    }

    default List<CarryTaskDO> selectByTaskStatus(Integer taskStatus){
        MPJLambdaWrapperX<CarryTaskDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(CarryTaskDO::getTaskStatus, taskStatus)
                .selectAll(CarryTaskDO.class);
        return selectList(wrapperX);
    }

    default List<CarryTaskDO> selectUnfinishedCarryTask(){
        MPJLambdaWrapperX<CarryTaskDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.ne(CarryTaskDO::getTaskStatus, DictConstants.WMS_CARRY_TASK_STATUS_FINISHED)
                .ne(CarryTaskDO::getTaskStatus, DictConstants.WMS_CARRY_TASK_STATUS_CANCEL)
                .selectAll(CarryTaskDO.class);
        return selectList(wrapperX);
    }


    default List<CarryTaskDO> getUnfinishedCarryTaskByReflectStockId(String reflectStockId){
        MPJLambdaWrapperX<CarryTaskDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.ne(CarryTaskDO::getTaskStatus, DictConstants.WMS_CARRY_TASK_STATUS_FINISHED)
                .ne(CarryTaskDO::getTaskStatus, DictConstants.WMS_CARRY_TASK_STATUS_CANCEL)
                .eq(CarryTaskDO::getReflectStockId, reflectStockId)
                .selectAll(CarryTaskDO.class);
        return selectList(wrapperX);
    }
}