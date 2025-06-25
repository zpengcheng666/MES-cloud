package com.miyu.module.wms.dal.mysql.carrytask;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.ibatis.annotations.Mapper;

/**
 * 搬运任务子表 Mapper
 *
 * @author 技术部长
 */
@Mapper
public interface CarrySubTaskMapper extends BaseMapperX<CarrySubTaskDO> {

    default List<CarrySubTaskDO> selectListByParentId(String parentId) {
        MPJLambdaWrapperX<CarrySubTaskDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStockDO.class, MaterialStockDO::getId, CarrySubTaskDO::getMaterialStockId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, CarrySubTaskDO::getLocationId)
                .select(WarehouseLocationDO::getLocationCode)
                .select(MaterialStockDO::getBarCode)
                .selectAll(CarrySubTaskDO.class)
                .eq(CarrySubTaskDO::getParentId, parentId)
                .orderByAsc(CarrySubTaskDO::getExecuteOrder);
        return selectList(wrapperX);
    }

    default List<CarrySubTaskDO> selectListByParentIds(Collection<String> parentIds) {
        return selectList(CarrySubTaskDO::getParentId, parentIds);
    }

    default int deleteByParentId(String parentId) {
        return delete(CarrySubTaskDO::getParentId, parentId);
    }

    default List<CarrySubTaskDO> selectUnfinishedCarrySubTask(){
        return selectList(new LambdaQueryWrapperX<CarrySubTaskDO>()
                .ne(CarrySubTaskDO::getTaskStatus, DictConstants.WMS_CARRY_TASK_CALL_BACK_STATUS_FINISHED)
                .ne(CarrySubTaskDO::getTaskStatus, DictConstants.WMS_CARRY_SUB_TASK_STATUS_CANCEL));
    }

    default Boolean updateByParentIdAndExecuteOrder(String parentId, Integer executeOrder, Integer taskStatus){
        LambdaUpdateWrapper<CarrySubTaskDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CarrySubTaskDO::getParentId, parentId)
               .eq(CarrySubTaskDO::getExecuteOrder, executeOrder)
               .set(CarrySubTaskDO::getTaskStatus, taskStatus);
        return update(updateWrapper) > 0;
    }
}
