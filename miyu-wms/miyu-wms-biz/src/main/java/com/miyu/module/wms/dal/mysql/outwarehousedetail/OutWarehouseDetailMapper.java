package com.miyu.module.wms.dal.mysql.outwarehousedetail;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.outwarehousedetail.vo.*;

/**
 * 出库详情 Mapper
 *
 * @author Qianjy
 */
@Mapper
public interface OutWarehouseDetailMapper extends BaseMapperX<OutWarehouseDetailDO> {

    default PageResult<OutWarehouseDetailDO> selectPage(OutWarehouseDetailPageReqVO reqVO) {
        MPJLambdaWrapperX<OutWarehouseDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, OutWarehouseDetailDO::getMaterialConfigId)
                .leftJoin(MaterialStockDO.class, "realM", on -> on.eq(MaterialStockDO::getId, OutWarehouseDetailDO::getMaterialStockId))
                .leftJoin(MaterialStockDO.class, "choose", on -> on.eq(MaterialStockDO::getId, OutWarehouseDetailDO::getChooseStockId))
                .leftJoin(WarehouseDO.class, WarehouseDO::getId, OutWarehouseDetailDO::getStartWarehouseId)
                .leftJoin(WarehouseDO.class, "tw", on -> on.eq(WarehouseDO::getId, OutWarehouseDetailDO::getTargetWarehouseId))
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAs("realM.bar_code",OutWarehouseDetailDO::getRealBarCode)
                .selectAs("choose.bar_code",OutWarehouseDetailDO::getChooseBarCode)
                .selectAs("tw.warehouse_code",OutWarehouseDetailDO::getTargetWarehouseCode)
                .select(WarehouseDO::getWarehouseCode)
                .selectAll(OutWarehouseDetailDO.class);
        return selectPage(reqVO, wrapperX
                .betweenIfPresent(OutWarehouseDetailDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(OutWarehouseDetailDO::getOrderNumber, reqVO.getOrderNumber())
                .eqIfPresent(OutWarehouseDetailDO::getOutType, reqVO.getOutType())
                .eqIfPresent(OutWarehouseDetailDO::getOutState, reqVO.getOutState())
                .eqIfPresent(OutWarehouseDetailDO::getStartWarehouseId, reqVO.getStartWarehouseId())
                .likeIfPresent(OutWarehouseDetailDO::getBatchNumber, reqVO.getBatchNumber())
                .eqIfPresent(OutWarehouseDetailDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(OutWarehouseDetailDO::getMaterialStockId, reqVO.getMaterialStockId())
                .orderByDesc(OutWarehouseDetailDO::getId));
    }

    default List<OutWarehouseDetailDO> selectOutWarehouseDetailGroupByOrderNumberList(Integer outstate){
        return selectList(new LambdaQueryWrapperX<OutWarehouseDetailDO>().eq(OutWarehouseDetailDO::getOutState, outstate).groupBy(OutWarehouseDetailDO::getOrderNumber).select(OutWarehouseDetailDO::getOrderNumber));
    }

    default List<OutWarehouseDetailDO> selectOutWarehouseDetailListByOrderNumber(String orderNumber,Integer outState){
        MPJLambdaWrapperX<OutWarehouseDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStockDO.class, MaterialStockDO::getId, OutWarehouseDetailDO::getMaterialStockId)
                .eq(OutWarehouseDetailDO::getOutState, outState)
                .selectAs(MaterialStockDO::getBarCode, OutWarehouseDetailDO::getRealBarCode)
                .selectAll(OutWarehouseDetailDO.class);
        return selectList(wrapperX.eq(OutWarehouseDetailDO::getOrderNumber, orderNumber));
    }

    default List<OutWarehouseDetailDO> selectWaitOutWarehouseOutWarehouseDetailList(){
        return selectList(new MPJLambdaWrapperX<OutWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, OutWarehouseDetailDO::getChooseStockId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, OutWarehouseDetailDO::getMaterialConfigId)
                .eq(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_1)
                .selectAs(MaterialStockDO::getBarCode, OutWarehouseDetailDO::getChooseBarCode)
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAll(OutWarehouseDetailDO.class)
                .orderByAsc(OutWarehouseDetailDO::getNeedTime));
    }



    default List<OutWarehouseDetailDO> selectOutWarehouseDetailByMaterialStockIdsAndState(Collection<String> materialStockIds, Integer outstate){
        return selectList(new MPJLambdaWrapperX<OutWarehouseDetailDO>()
                .eq(OutWarehouseDetailDO::getOutState, outstate)
                .in(OutWarehouseDetailDO::getMaterialStockId, materialStockIds)
                .selectAll(OutWarehouseDetailDO.class));
    }

    default List<OutWarehouseDetailDO> selectOutWarehouseDetail(String warehouseId) {
        MPJLambdaWrapperX<OutWarehouseDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_1)
                .eq(OutWarehouseDetailDO::getStartWarehouseId, warehouseId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId,
                        OutWarehouseDetailDO::getMaterialConfigId)
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, OutWarehouseDetailDO::getChooseStockId)
                .leftJoin(WarehouseDO.class, WarehouseDO::getId, OutWarehouseDetailDO::getStartWarehouseId)
                .leftJoin(WarehouseDO.class, "tw", on -> on.eq(WarehouseDO::getId, OutWarehouseDetailDO::getTargetWarehouseId))
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAs(MaterialStockDO::getBarCode, OutWarehouseDetailDO::getChooseBarCode)
                .select(WarehouseDO::getWarehouseCode)
                .selectAs("tw.warehouse_code",OutWarehouseDetailDO::getTargetWarehouseCode)
                .selectAll(OutWarehouseDetailDO.class);
        return selectList(wrapperX);
    }

    default int updateBatchNotFinishByChooseStockIds(Collection<String> allMaterialStockOnTrayIds, Integer updateState){
        LambdaUpdateWrapper<OutWarehouseDetailDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(OutWarehouseDetailDO::getChooseStockId, allMaterialStockOnTrayIds)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .set(OutWarehouseDetailDO::getOutState, updateState);

        // 如果是已完成状态，则需要更新签收人和签收时间
        if(DictConstants.WMS_ORDER_DETAIL_STATUS_4.equals(updateState)){
            Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
            if(loginUserId != null){
                wrapper.set(OutWarehouseDetailDO::getSigner, String.valueOf(loginUserId));
                wrapper.set(OutWarehouseDetailDO::getSignTime, LocalDateTime.now());
            }

        }
        return update(wrapper);
    }

    default int updateBatchNotFinish(Collection<String> allMaterialStockOnTrayIds, Integer updateState){
        LambdaUpdateWrapper<OutWarehouseDetailDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(OutWarehouseDetailDO::getMaterialStockId, allMaterialStockOnTrayIds)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .set(OutWarehouseDetailDO::getOutState, updateState);

        // 如果是已完成状态，则需要更新签收人和签收时间
        if(DictConstants.WMS_ORDER_DETAIL_STATUS_4.equals(updateState)){
            Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
            if(loginUserId != null){
                wrapper.set(OutWarehouseDetailDO::getSigner, String.valueOf(loginUserId));
                wrapper.set(OutWarehouseDetailDO::getSignTime, LocalDateTime.now());
            }
        }
        return update(wrapper);
    }

    default int updateBatchNotFinish(Collection<String> allMaterialStockOnTrayIds, Integer updateState, String targetWarehouseId){
        if(updateState == null && StringUtils.isBlank(targetWarehouseId)){
            return 0;
        }
        LambdaUpdateWrapper<OutWarehouseDetailDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(OutWarehouseDetailDO::getMaterialStockId, allMaterialStockOnTrayIds)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_5);
                if(updateState != null)wrapper.set(OutWarehouseDetailDO::getOutState, updateState);
                if(StringUtils.isNotBlank(targetWarehouseId))wrapper.set(OutWarehouseDetailDO::getTargetWarehouseId, targetWarehouseId);

        // 如果是已完成状态，则需要更新签收人和签收时间
        if(DictConstants.WMS_ORDER_DETAIL_STATUS_4.equals(updateState)){
            Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
            if(loginUserId != null){
                wrapper.set(OutWarehouseDetailDO::getSigner, String.valueOf(loginUserId));
                wrapper.set(OutWarehouseDetailDO::getSignTime, LocalDateTime.now());
            }

        }
        return update(wrapper);
    }


    default List<OutWarehouseDetailDO> selectBatchOutOrderList(List<String> outOrderNumbers){
        return selectList(new MPJLambdaWrapperX<OutWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, OutWarehouseDetailDO::getMaterialStockId)
                .in(OutWarehouseDetailDO::getOrderNumber, outOrderNumbers)
                .selectAs(MaterialStockDO::getBarCode, OutWarehouseDetailDO::getRealBarCode)
                .selectAll(OutWarehouseDetailDO.class));
    }

    default List<OutWarehouseDetailDO> checkMaterialStockOrderExistsByBarCodes(String orderNumber, Collection<String> barCodes){
        MPJLambdaWrapperX<OutWarehouseDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStockDO.class, MaterialStockDO::getId, OutWarehouseDetailDO::getMaterialStockId)
                .eq(OutWarehouseDetailDO::getOrderNumber, orderNumber)
                .in(MaterialStockDO::getBarCode, barCodes)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .selectAll(OutWarehouseDetailDO.class);
        return selectList(wrapperX);
    }

    default int setOperatorInBatchOutWarehouseDetail(Set<String> orderIds){
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        if(loginUserId == null){
            return 0;
        }
        LambdaUpdateWrapper<OutWarehouseDetailDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(OutWarehouseDetailDO::getId, orderIds)
                .set(OutWarehouseDetailDO::getOperator, loginUserId)
                .set(OutWarehouseDetailDO::getOperateTime, LocalDateTime.now());
        return update(wrapper);
    }

    default int setSignerInBatchOutWarehouseDetail(Set<String> orderIds){
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        LambdaUpdateWrapper<OutWarehouseDetailDO> wrapper = new LambdaUpdateWrapper<>();
        if(loginUser == null){
            return 0;
        }
        wrapper.in(OutWarehouseDetailDO::getId, orderIds)
                .set(OutWarehouseDetailDO::getSigner, loginUser.getId())
                .set(OutWarehouseDetailDO::getSignTime, LocalDateTime.now());
        return update(wrapper);
    }

    default List<OutWarehouseDetailDO> selectOutOrderListByOrderTypeAndBatchNumber(Integer outType, String batchNumber){
        return selectList(OutWarehouseDetailDO::getOutType, outType, OutWarehouseDetailDO::getBatchNumber, batchNumber);
    }

    default List<OutWarehouseDetailDO> selectNotFinishedOutWarehouseDetailListByAreaId(String areaId){
        return selectList(new MPJLambdaWrapperX<OutWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, OutWarehouseDetailDO::getMaterialStockId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .eq(WarehouseLocationDO::getWarehouseAreaId, areaId)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .selectAll(OutWarehouseDetailDO.class));
    }

    default List<OutWarehouseDetailDO> selectOutWarehouseDetailByChooseBarCode(String barCode){
        return selectList(new MPJLambdaWrapperX<OutWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, OutWarehouseDetailDO::getChooseStockId)
                .eq(MaterialStockDO::getBarCode, barCode)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .selectAll(OutWarehouseDetailDO.class));
    }


    default List<OutWarehouseDetailDO> selectFinishOutWarehouseDetailList(LocalDateTime[] createTimeRange){
        return selectList(new MPJLambdaWrapperX<OutWarehouseDetailDO>()
                .betweenIfPresent(OutWarehouseDetailDO::getCreateTime, createTimeRange)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, OutWarehouseDetailDO::getMaterialConfigId)
                .eq(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAll(OutWarehouseDetailDO.class));
    }

    default List<OutWarehouseDetailDO> selectNotFinishedOutWarehouseDetailList(){
        return selectList(new MPJLambdaWrapperX<OutWarehouseDetailDO>()
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getId, OutWarehouseDetailDO::getMaterialStockId)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                .selectAs(MaterialStockDO::getBarCode, OutWarehouseDetailDO::getRealBarCode)
                .selectAll(OutWarehouseDetailDO.class));
    }
}
