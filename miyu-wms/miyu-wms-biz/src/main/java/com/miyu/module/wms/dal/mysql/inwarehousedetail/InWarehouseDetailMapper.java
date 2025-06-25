package com.miyu.module.wms.dal.mysql.inwarehousedetail;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.inwarehousedetail.vo.*;

import javax.validation.constraints.NotNull;

/**
 * 入库详情 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface InWarehouseDetailMapper extends BaseMapperX<InWarehouseDetailDO> {

    default PageResult<InWarehouseDetailDO> selectPage(InWarehouseDetailPageReqVO reqVO) {
        MPJLambdaWrapperX<InWarehouseDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, InWarehouseDetailDO::getMaterialConfigId)
                .leftJoin(MaterialStockDO.class, "realM", on->on.eq(MaterialStockDO::getId, InWarehouseDetailDO::getMaterialStockId))
                .leftJoin(MaterialStockDO.class, "choose", on->on.eq(MaterialStockDO::getId, InWarehouseDetailDO::getChooseStockId))
                .leftJoin(WarehouseDO.class, WarehouseDO::getId, InWarehouseDetailDO::getTargetWarehouseId)
                .leftJoin(WarehouseDO.class, "tw", on -> on.eq(WarehouseDO::getId, OutWarehouseDetailDO::getStartWarehouseId))
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAs("realM.bar_code",InWarehouseDetailDO::getRealBarCode)
                .selectAs("choose.bar_code",InWarehouseDetailDO::getChooseBarCode)
                .selectAs("tw.warehouse_code",InWarehouseDetailDO::getStartWarehouseCode)
                .select(WarehouseDO::getWarehouseCode)
                .selectAll(InWarehouseDetailDO.class);
        return selectPage(reqVO, wrapperX
                .betweenIfPresent(InWarehouseDetailDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(InWarehouseDetailDO::getOrderNumber, reqVO.getOrderNumber())
                .eqIfPresent(InWarehouseDetailDO::getInType, reqVO.getInType())
                .eqIfPresent(InWarehouseDetailDO::getTargetWarehouseId, reqVO.getTargetWarehouseId())
                .eqIfPresent(InWarehouseDetailDO::getInState, reqVO.getInState())
                .eqIfPresent(InWarehouseDetailDO::getBatchNumber, reqVO.getBatchNumber())
                .eqIfPresent(InWarehouseDetailDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(InWarehouseDetailDO::getMaterialStockId, reqVO.getMaterialStockId())
                .eqIfPresent(InWarehouseDetailDO::getChooseBarCode, reqVO.getChooseStockId())
                .orderByDesc(InWarehouseDetailDO::getId));
    }

    default InWarehouseDetailDO selectInWarehouseDetailByMaterialStockIdAndState(String materialStockId, Integer inWarehouseState){
        return selectOne(new MPJLambdaWrapperX<InWarehouseDetailDO>()
                .eq(InWarehouseDetailDO::getInState, inWarehouseState)
                .eq(InWarehouseDetailDO::getMaterialStockId, materialStockId)
                .selectAll(InWarehouseDetailDO.class));

    }

    default List<InWarehouseDetailDO> selectInWarehouseDetailByMaterialStockIdsAndState(Collection<String> materialIds, Integer inWarehouseState){
        return selectList(new MPJLambdaWrapperX<InWarehouseDetailDO>()
                .eq(InWarehouseDetailDO::getInState, inWarehouseState)
                .in(InWarehouseDetailDO::getMaterialStockId, materialIds)
                .selectAll(InWarehouseDetailDO.class));

    }

    default List<InWarehouseDetailDO> selectInWarehouseDetailByOrderNumberAndMaterialConfigId(String orderNumber, String materialConfigId){
        return selectList(
                InWarehouseDetailDO::getOrderNumber, orderNumber,
                InWarehouseDetailDO::getMaterialConfigId, materialConfigId);
    }


    default List<InWarehouseDetailDO> selectFinishInWarehouseDetailList(LocalDateTime[] createTimeRange){
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime nowZero = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
//        LocalDateTime startTime = nowZero.minusDays(7);
        return selectList(new MPJLambdaWrapperX<InWarehouseDetailDO>()
                .betweenIfPresent(InWarehouseDetailDO::getCreateTime, createTimeRange)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, InWarehouseDetailDO::getMaterialConfigId)
                .eq(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAll(InWarehouseDetailDO.class));
    }

    default List<InWarehouseDetailDO> selectInWarehouseInWarehouseDetailList(Integer inWarehouseState){
        return selectList(new MPJLambdaWrapperX<InWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, InWarehouseDetailDO::getChooseStockId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, InWarehouseDetailDO::getMaterialConfigId)
                .eq(InWarehouseDetailDO::getInState, inWarehouseState)
                .selectAs(MaterialStockDO::getBarCode, InWarehouseDetailDO::getChooseBarCode)
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAll(InWarehouseDetailDO.class));
    }

    default List<InWarehouseDetailDO> selectInWarehouseDetailIds(Collection<String> inWarehouseIds){
        return selectList(new MPJLambdaWrapperX<InWarehouseDetailDO>()
                .in(InWarehouseDetailDO::getId, inWarehouseIds)
                .selectAll(InWarehouseDetailDO.class));
    }

    default int updateBatchNotFinishByChooseStockIds(Collection<String> allMaterialStockOnTrayIds, Integer updateState){
        LambdaUpdateWrapper<InWarehouseDetailDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(InWarehouseDetailDO::getChooseStockId, allMaterialStockOnTrayIds)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .set(InWarehouseDetailDO::getInState, updateState);
        return update(wrapper);
    }

    default int updateBatchNotFinish(Collection<String> allMaterialStockOnTrayIds, Integer updateState){
        LambdaUpdateWrapper<InWarehouseDetailDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(InWarehouseDetailDO::getMaterialStockId, allMaterialStockOnTrayIds)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .set(InWarehouseDetailDO::getInState, updateState);
        return update(wrapper);
    }

    default List<InWarehouseDetailDO> selectInWarehouseDetailByOrderNumberAndMaterialConfigIds(String orderNumber, Collection<String> materialConfigIds){
        MPJLambdaWrapperX<InWarehouseDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(InWarehouseDetailDO::getOrderNumber, orderNumber)
                .in(InWarehouseDetailDO::getMaterialConfigId, materialConfigIds)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .selectAll(InWarehouseDetailDO.class);
        return selectList(wrapperX);
    }

    default List<InWarehouseDetailDO> selectInWarehouseDetailByOrderNumberAndBarCodes(String orderNumber, Collection<String> barCodes){
        MPJLambdaWrapperX<InWarehouseDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStockDO.class, MaterialStockDO::getId, InWarehouseDetailDO::getMaterialStockId)
                .eq(InWarehouseDetailDO::getOrderNumber, orderNumber)
                .in(MaterialStockDO::getBarCode, barCodes)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .selectAll(InWarehouseDetailDO.class);
        return selectList(wrapperX);
    }

    default List<InWarehouseDetailDO> selectBatchInOrderList(List<String> inOrderNumbers){
        return selectList(new MPJLambdaWrapperX<InWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, InWarehouseDetailDO::getMaterialStockId)
                .in(InWarehouseDetailDO::getOrderNumber, inOrderNumbers)
                .selectAs(MaterialStockDO::getBarCode, InWarehouseDetailDO::getRealBarCode)
                .selectAll(InWarehouseDetailDO.class));

    }

    default int updateOperatorInBatchInWarehouseDetail(Set<String> orderIds){
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if(loginUser == null){
            return 0;
        }
        LambdaUpdateWrapper<InWarehouseDetailDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(InWarehouseDetailDO::getId, orderIds)
                .isNull(InWarehouseDetailDO::getOperator)
                .set(InWarehouseDetailDO::getOperator, loginUser.getId())
                .set(InWarehouseDetailDO::getOperateTime, LocalDateTime.now());
        return update(wrapper);
    }

    default int updateSignerInBatchInWarehouseDetail(Set<String> orderIds){
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        LambdaUpdateWrapper<InWarehouseDetailDO> wrapper = new LambdaUpdateWrapper<>();
        if(loginUser == null){
            return 0;
        }
        wrapper.in(InWarehouseDetailDO::getId, orderIds)
                .set(InWarehouseDetailDO::getSigner, loginUser.getId())
                .set(InWarehouseDetailDO::getSignTime, LocalDateTime.now());
        return update(wrapper);
    }

    default List<InWarehouseDetailDO> selectInOrderListByOrderTypeAndBatchNumber(Integer inType, String batchNumber){
        return selectList(InWarehouseDetailDO::getInType, inType, InWarehouseDetailDO::getBatchNumber, batchNumber);
    }

    default List<InWarehouseDetailDO> selectNotFinishedInWarehouseDetailListByAreaId(String areaId){
        return selectList(new MPJLambdaWrapperX<InWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, InWarehouseDetailDO::getMaterialStockId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .eq(WarehouseLocationDO::getWarehouseAreaId, areaId)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .selectAll(InWarehouseDetailDO.class));
    }

    default List<InWarehouseDetailDO> selectInWarehouseDetailByChooseBarCode(String barCode){
        return selectList(new MPJLambdaWrapperX<InWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, InWarehouseDetailDO::getChooseStockId)
                .eq(MaterialStockDO::getBarCode, barCode)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .selectAll(InWarehouseDetailDO.class));
    }

    default List<InWarehouseDetailDO> selectBatchNotFinishByTargetWarehouseId(String targetWarehouseId){
        MPJLambdaWrapperX<InWarehouseDetailDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.eq(InWarehouseDetailDO::getTargetWarehouseId, targetWarehouseId)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_5);
        return selectList(wrapper);
    }
}