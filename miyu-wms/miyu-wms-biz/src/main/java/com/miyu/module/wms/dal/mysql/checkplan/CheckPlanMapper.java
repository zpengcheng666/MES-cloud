package com.miyu.module.wms.dal.mysql.checkplan;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.module.wms.dal.dataobject.checkcontainer.CheckContainerDO;
import com.miyu.module.wms.dal.dataobject.checkplan.CheckPlanDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.checkplan.vo.*;

/**
 * 库存盘点计划 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface CheckPlanMapper extends BaseMapperX<CheckPlanDO> {

    default PageResult<CheckPlanDO> selectPage(CheckPlanPageReqVO reqVO) {
        MPJLambdaWrapperX<CheckPlanDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, CheckPlanDO::getCheckAreaId)
                .select(WarehouseAreaDO::getAreaCode)
                .selectAll(CheckPlanDO.class);
        return selectPage(reqVO, wrapperX
                .betweenIfPresent(CheckPlanDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CheckPlanDO::getCheckAreaId, reqVO.getCheckAreaId())
                .likeIfPresent(CheckPlanDO::getCheckName, reqVO.getCheckName())
                .betweenIfPresent(CheckPlanDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(CheckPlanDO::getEndTime, reqVO.getEndTime())
                .betweenIfPresent(CheckPlanDO::getCutOffTime, reqVO.getCutOffTime())
                .eqIfPresent(CheckPlanDO::getCheckUserId, reqVO.getCheckUserId())
                .eqIfPresent(CheckPlanDO::getCheckStatus, reqVO.getCheckStatus())
                .orderByDesc(CheckPlanDO::getId));
    }

    default PageResult<CheckPlanDO> selectTaskPage(CheckPlanPageReqVO reqVO){
        MPJLambdaWrapperX<CheckPlanDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, CheckPlanDO::getCheckAreaId)
                .select(WarehouseAreaDO::getAreaCode)
                .selectAll(CheckPlanDO.class);
        return selectPage(reqVO, wrapperX
                .betweenIfPresent(CheckPlanDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CheckPlanDO::getCheckAreaId, reqVO.getCheckAreaId())
                .likeIfPresent(CheckPlanDO::getCheckName, reqVO.getCheckName())
                .betweenIfPresent(CheckPlanDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(CheckPlanDO::getEndTime, reqVO.getEndTime())
                .betweenIfPresent(CheckPlanDO::getCutOffTime, reqVO.getCutOffTime())
                .eqIfPresent(CheckPlanDO::getCheckUserId, reqVO.getCheckUserId())
                .gt(CheckPlanDO::getCheckStatus, DictConstants.WMS_CHECK_STATUS_CHECK)
                .orderByDesc(CheckPlanDO::getId));
    }



    default boolean updateCheckStatus(String checkPlanId, Integer checkPlanStatus){
        LambdaUpdateWrapper<CheckPlanDO> updateWrapper = new LambdaUpdateWrapper<>();
        return update(updateWrapper.set(CheckPlanDO::getCheckStatus, checkPlanStatus).eq(CheckPlanDO::getId, checkPlanId)) > 0;
    }


    default CheckPlanDO getCheckPlanByCheckContainerId(String checkContainerId){
        MPJLambdaWrapperX<CheckPlanDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(CheckContainerDO.class, CheckContainerDO::getCheckPlanId, CheckPlanDO::getId)
                .eq(CheckContainerDO::getId, checkContainerId);
        return selectOne(wrapperX);
    }

    default List<CheckPlanDO> getNotFinishedCheckPlanByCheckAreaId(String checkAreaId){
        MPJLambdaWrapperX<CheckPlanDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(CheckPlanDO::getCheckAreaId, checkAreaId)
                .ne(CheckPlanDO::getCheckStatus, DictConstants.WMS_CHECK_STATUS_FINISHED)
                .ne(CheckPlanDO::getCheckStatus, DictConstants.WMS_CHECK_STATUS_CLOSED);
        return selectList(wrapperX);
    }

    default List<CheckPlanDO> selectNotFinishedCheckPlanAndLocked(){
        MPJLambdaWrapperX<CheckPlanDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(CheckPlanDO::getCheckLocked, true)
                .ne(CheckPlanDO::getCheckStatus, DictConstants.WMS_CHECK_STATUS_FINISHED)
                .ne(CheckPlanDO::getCheckStatus, DictConstants.WMS_CHECK_STATUS_CLOSED);
        return selectList(wrapperX);
    }
}