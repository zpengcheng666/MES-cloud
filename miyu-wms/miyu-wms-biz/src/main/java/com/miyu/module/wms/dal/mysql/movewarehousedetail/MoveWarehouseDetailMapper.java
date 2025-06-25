package com.miyu.module.wms.dal.mysql.movewarehousedetail;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.movewarehousedetail.vo.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 库存移动详情 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface MoveWarehouseDetailMapper extends BaseMapperX<MoveWarehouseDetailDO> {

    default PageResult<MoveWarehouseDetailDO> selectPage(MoveWarehouseDetailPageReqVO reqVO) {
        MPJLambdaWrapperX<MoveWarehouseDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MoveWarehouseDetailDO::getMaterialConfigId)
                .leftJoin(MaterialStockDO.class, "realM", on->on.eq(MaterialStockDO::getId, MoveWarehouseDetailDO::getMaterialStockId))
                .leftJoin(MaterialStockDO.class, "choose", on->on.eq(MaterialStockDO::getId, MoveWarehouseDetailDO::getChooseStockId))
                .leftJoin(WarehouseDO.class, WarehouseDO::getId, MoveWarehouseDetailDO::getStartWarehouseId)
                .leftJoin(WarehouseDO.class, "tw", on -> on.eq(WarehouseDO::getId, MoveWarehouseDetailDO::getTargetWarehouseId))
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAs("realM.bar_code",MoveWarehouseDetailDO::getRealBarCode)
                .selectAs("choose.bar_code",MoveWarehouseDetailDO::getChooseBarCode)
                .selectAs(WarehouseDO::getWarehouseCode, MoveWarehouseDetailDO::getStartWarehouseCode)
                .selectAs("tw.warehouse_code", MoveWarehouseDetailDO::getTargetWarehouseCode)
                .selectAll(MoveWarehouseDetailDO.class);
        return selectPage(reqVO, wrapperX
                .betweenIfPresent(MoveWarehouseDetailDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MoveWarehouseDetailDO::getOrderNumber, reqVO.getOrderNumber())
                .eqIfPresent(MoveWarehouseDetailDO::getMoveType, reqVO.getMoveType())
                .eqIfPresent(MoveWarehouseDetailDO::getMoveState, reqVO.getMoveState())
                .eqIfPresent(MoveWarehouseDetailDO::getStartWarehouseId, reqVO.getStartWarehouseId())
                .eqIfPresent(MoveWarehouseDetailDO::getTargetWarehouseId, reqVO.getTargetWarehouseId())
                .eqIfPresent(MoveWarehouseDetailDO::getBatchNumber, reqVO.getBatchNumber())
                .eqIfPresent(MoveWarehouseDetailDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(MoveWarehouseDetailDO::getMaterialStockId, reqVO.getMaterialStockId())
                .orderByDesc(MoveWarehouseDetailDO::getId));
    }


    default List<MoveWarehouseDetailDO> selectMoveWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds, Integer moveState){
        return selectList(new MPJLambdaWrapperX<MoveWarehouseDetailDO>()
               .in(MoveWarehouseDetailDO::getMaterialStockId, materialStockIds)
               .eqIfPresent(MoveWarehouseDetailDO::getMoveState, moveState));
    }

    default List<MoveWarehouseDetailDO> selectMoveWarehouseDetailListByInWarehouseIds(List<String> inWarehouseIds){
        return selectList(new MPJLambdaWrapperX<MoveWarehouseDetailDO>()
               .in(MoveWarehouseDetailDO::getStartWarehouseId, inWarehouseIds));
    }

    default List<MoveWarehouseDetailDO> selectWaitMoveWarehouseMoveWarehouseDetailList(){
        return selectList(new MPJLambdaWrapperX<MoveWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, MoveWarehouseDetailDO::getChooseStockId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MoveWarehouseDetailDO::getMaterialConfigId)
                .eq(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_1)
                .selectAs(MaterialStockDO::getBarCode, MoveWarehouseDetailDO::getChooseBarCode)
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAll(MoveWarehouseDetailDO.class));
    }


    default List<MoveWarehouseDetailDO> selectMoveWarehouseDetailByMaterialStockIdsAndState(Collection<String> materialStockIds, Integer moveState){
        return selectList(new MPJLambdaWrapperX<MoveWarehouseDetailDO>()
               .in(MoveWarehouseDetailDO::getMaterialStockId, materialStockIds)
               .eq(MoveWarehouseDetailDO::getMoveState, moveState));
    }

    default int updateMoveWarehouseDetailStateByMaterialStockId(String containerStockId, Integer updateState){
        return update(new LambdaUpdateWrapper<MoveWarehouseDetailDO>()
                .set(MoveWarehouseDetailDO::getMoveState, updateState)
                .eq(MoveWarehouseDetailDO::getMaterialStockId, containerStockId));
    }

    default List<MoveWarehouseDetailDO> selectNotFinishMoveDetailList(){
        return selectList(new MPJLambdaWrapperX<MoveWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, MoveWarehouseDetailDO::getMaterialStockId)
                .eq(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_1).or()
                .eq(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_2).or()
                .eq(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_3)
                .select(MaterialStockDO::getTotality)
                .selectAll(MoveWarehouseDetailDO.class));
    }

    default int updateBatchNotFinishByChooseStockIds(Collection<String> allMaterialStockOnTrayIds, Integer updateState){
        LambdaUpdateWrapper<MoveWarehouseDetailDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(MoveWarehouseDetailDO::getChooseStockId, allMaterialStockOnTrayIds)
                .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .set(MoveWarehouseDetailDO::getMoveState, updateState);
        // 如果是已完成状态，则需要更新签收人和签收时间
        if(DictConstants.WMS_ORDER_DETAIL_STATUS_4.equals(updateState)){
            Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
            if(loginUserId != null){wrapper.set(MoveWarehouseDetailDO::getSigner, String.valueOf(loginUserId));
                wrapper.set(MoveWarehouseDetailDO::getSignTime, LocalDateTime.now());
            }

        }
        return update(wrapper);
    }

    default int updateBatchNotFinish(Collection<String> allMaterialStockOnTrayIds, Integer updateState){
        LambdaUpdateWrapper<MoveWarehouseDetailDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(MoveWarehouseDetailDO::getMaterialStockId, allMaterialStockOnTrayIds)
                .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .set(MoveWarehouseDetailDO::getMoveState, updateState);
        // 如果是已完成状态，则需要更新签收人和签收时间
        if(DictConstants.WMS_ORDER_DETAIL_STATUS_4.equals(updateState)){
            Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
            if(loginUserId != null){
                wrapper.set(MoveWarehouseDetailDO::getSigner, String.valueOf(loginUserId));
                wrapper.set(MoveWarehouseDetailDO::getSignTime, LocalDateTime.now());
            }

        }
        return update(wrapper);
    }

    default List<MoveWarehouseDetailDO> selectBatchMoveOrderList(List<String> moveOrderNumbers){
        return selectList(new MPJLambdaWrapperX<MoveWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, MoveWarehouseDetailDO::getMaterialStockId)
                .in(MoveWarehouseDetailDO::getOrderNumber, moveOrderNumbers)
                .selectAs(MaterialStockDO::getBarCode, MoveWarehouseDetailDO::getRealBarCode)
                .selectAll(MoveWarehouseDetailDO.class));

    }

    default int setOperatorInBatchMoveWarehouseDetail(Set<String> orderIds){
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if(loginUser == null){
            return 0;
        }
        LambdaUpdateWrapper<MoveWarehouseDetailDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(MoveWarehouseDetailDO::getId, orderIds)
                .set(MoveWarehouseDetailDO::getOperator, loginUser.getId())
                .set(MoveWarehouseDetailDO::getOperateTime, LocalDateTime.now());
        return update(wrapper);
    }


    default int setSignerInBatchMoveWarehouseDetail(Set<String> orderIds){
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if(loginUser == null){
            return 0;
        }
        LambdaUpdateWrapper<MoveWarehouseDetailDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(MoveWarehouseDetailDO::getId, orderIds)
                .set(MoveWarehouseDetailDO::getSigner, loginUser.getId())
                .set(MoveWarehouseDetailDO::getSignTime, LocalDateTime.now());
        return update(wrapper);
    }


    default List<MoveWarehouseDetailDO> selectMoveOrderListByOrderTypeAndBatchNumber(Integer moveType, String batchNumber){
       return selectList(MoveWarehouseDetailDO::getMoveType, moveType, MoveWarehouseDetailDO::getBatchNumber, batchNumber);
    }

    default List<MoveWarehouseDetailDO> selectNotFinishedMoveWarehouseDetailListByAreaId(String areaId){
        return selectList(new MPJLambdaWrapperX<MoveWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, MoveWarehouseDetailDO::getMaterialStockId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .eq(WarehouseLocationDO::getWarehouseAreaId, areaId)
                .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .selectAll(MoveWarehouseDetailDO.class));
    }

    default List<MoveWarehouseDetailDO> selectMoveWarehouseDetailByChooseBarCode(String barCode){
        return selectList(new MPJLambdaWrapperX<MoveWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, MoveWarehouseDetailDO::getChooseStockId)
                .eq(MaterialStockDO::getBarCode, barCode)
                .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .selectAll(MoveWarehouseDetailDO.class));
    }

    default List<MoveWarehouseDetailDO> selectNotFinishedMoveWarehouseDetailList(){
        return selectList(new MPJLambdaWrapperX<MoveWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, MoveWarehouseDetailDO::getMaterialStockId)
                .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .selectAs(MaterialStockDO::getBarCode, MoveWarehouseDetailDO::getRealBarCode)
                .selectAll(MoveWarehouseDetailDO.class));
    }
}