package com.miyu.module.wms.convert.movewarehousedetail;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.controller.admin.movewarehousedetail.vo.MoveWarehouseDetailRespVO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.UNKNOWN_STATUS;


@Mapper
public interface MoveWarehouseDetailConvert {
    MoveWarehouseDetailConvert INSTANCE = Mappers.getMapper(MoveWarehouseDetailConvert.class);

    default List<MoveWarehouseDetailRespVO> convertList(List<MoveWarehouseDetailDO> list, Map<Long, AdminUserRespDTO> userMap) {
        return CollectionUtils.convertList(list,  moveWarehouseDetailDO ->
        {
            MoveWarehouseDetailRespVO moveWarehouseDetailRespVO = BeanUtils.toBean(moveWarehouseDetailDO, MoveWarehouseDetailRespVO.class);
            if (StringUtils.isNotBlank(moveWarehouseDetailDO.getCreator()))
                MapUtils.findAndThen(userMap, Long.valueOf(moveWarehouseDetailDO.getCreator()), a -> moveWarehouseDetailRespVO.setCreator(a.getNickname()));
            return moveWarehouseDetailRespVO;
        });

    }

    default List<MoveWarehouseDetailDO> convertList(List<OrderReqDTO> list) {
        return list.stream().map(orderReqDTO -> {
            MoveWarehouseDetailDO moveWarehouseDetailDO = new MoveWarehouseDetailDO();
            moveWarehouseDetailDO.setOrderNumber(orderReqDTO.getOrderNumber());
            Integer orderType = orderReqDTO.getOrderType();
            if (DictConstants.WMS_ORDER_TYPE_PRODUCE_MOVE == orderType) {
                moveWarehouseDetailDO.setMoveType(DictConstants.WMS_MOVE_WAREHOUSE_TYPE_1);
            } else if (DictConstants.WMS_ORDER_TYPE_CHECK_MOVE == orderType) {
                moveWarehouseDetailDO.setMoveType(DictConstants.WMS_MOVE_WAREHOUSE_TYPE_2);
            } else if (DictConstants.WMS_ORDER_TYPE_TRANSFER_MOVE == orderType) {
                moveWarehouseDetailDO.setMoveType(DictConstants.WMS_MOVE_WAREHOUSE_TYPE_3);
            } else {
                return null;
            }
            if(orderReqDTO.getOrderStatus() != null){
                moveWarehouseDetailDO.setMoveState(orderReqDTO.getOrderStatus());
            }else {
                moveWarehouseDetailDO.setMoveState(DictConstants.WMS_ORDER_DETAIL_STATUS_6);
            }
            moveWarehouseDetailDO.setChooseStockId(orderReqDTO.getChooseStockId());
            moveWarehouseDetailDO.setMaterialStockId(orderReqDTO.getMaterialStockId());
            moveWarehouseDetailDO.setQuantity(orderReqDTO.getQuantity());
            moveWarehouseDetailDO.setSignLocationId(orderReqDTO.getSignLocationId());
            if(StringUtils.isNotBlank(orderReqDTO.getStartWarehouseId()))moveWarehouseDetailDO.setStartWarehouseId(orderReqDTO.getStartWarehouseId());
            moveWarehouseDetailDO.setTargetWarehouseId(orderReqDTO.getTargetWarehouseId());
            return moveWarehouseDetailDO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    default List<OrderReqDTO> convertList2(List<MoveWarehouseDetailDO> list){
        return list.stream().map(moveWarehouseDetailDO -> {
            OrderReqDTO orderReqDTO = new OrderReqDTO();
            orderReqDTO.setOrderNumber(moveWarehouseDetailDO.getOrderNumber());
            Integer moveType = moveWarehouseDetailDO.getMoveType();
            if (DictConstants.WMS_MOVE_WAREHOUSE_TYPE_1 == moveType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_PRODUCE_MOVE);
            } else if (DictConstants.WMS_MOVE_WAREHOUSE_TYPE_2 == moveType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_CHECK_MOVE);
            } else if (DictConstants.WMS_MOVE_WAREHOUSE_TYPE_3 == moveType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_TRANSFER_MOVE);
            }else {
                return null;
            }
            orderReqDTO.setChooseStockId(moveWarehouseDetailDO.getChooseStockId());
            orderReqDTO.setMaterialStockId(moveWarehouseDetailDO.getMaterialStockId());
            orderReqDTO.setRealBarCode(moveWarehouseDetailDO.getRealBarCode());
            orderReqDTO.setMaterialConfigId(moveWarehouseDetailDO.getMaterialConfigId());
            orderReqDTO.setQuantity(moveWarehouseDetailDO.getQuantity());
            orderReqDTO.setBatchNumber(moveWarehouseDetailDO.getBatchNumber());
            orderReqDTO.setStartWarehouseId(moveWarehouseDetailDO.getStartWarehouseId());
            orderReqDTO.setTargetWarehouseId(moveWarehouseDetailDO.getTargetWarehouseId());
            orderReqDTO.setSignLocationId(moveWarehouseDetailDO.getSignLocationId());
            orderReqDTO.setOrderStatus(moveWarehouseDetailDO.getMoveState());
            orderReqDTO.setOperator(moveWarehouseDetailDO.getOperator());
            orderReqDTO.setOperateTime(moveWarehouseDetailDO.getOperateTime());
            orderReqDTO.setSigner(moveWarehouseDetailDO.getSigner());
            orderReqDTO.setSignTime(moveWarehouseDetailDO.getSignTime());
            return orderReqDTO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    default Integer convertOrderType(Integer type){
        if (DictConstants.WMS_MOVE_WAREHOUSE_TYPE_1 == type) {
            return DictConstants.WMS_ORDER_TYPE_PRODUCE_MOVE;
        } else if (DictConstants.WMS_MOVE_WAREHOUSE_TYPE_2 == type) {
            return DictConstants.WMS_ORDER_TYPE_CHECK_MOVE;
        } else if (DictConstants.WMS_MOVE_WAREHOUSE_TYPE_3 == type) {
           return DictConstants.WMS_ORDER_TYPE_TRANSFER_MOVE;
        }else {
            return null;
        }
    }

    default Integer convertMoveType(Integer type){
        if (DictConstants.WMS_ORDER_TYPE_PRODUCE_MOVE == type) {
            return DictConstants.WMS_MOVE_WAREHOUSE_TYPE_1;
        } else if (DictConstants.WMS_ORDER_TYPE_CHECK_MOVE == type) {
            return DictConstants.WMS_MOVE_WAREHOUSE_TYPE_2;
        } else if (DictConstants.WMS_ORDER_TYPE_TRANSFER_MOVE == type) {
            return DictConstants.WMS_MOVE_WAREHOUSE_TYPE_3;
        }else {
            return null;
        }
    }

    default String convertStringType(Integer orderType){
        if (DictConstants.WMS_MOVE_WAREHOUSE_TYPE_1 == orderType) {
            return "生产移库";
        }else if (DictConstants.WMS_MOVE_WAREHOUSE_TYPE_2 == orderType) {
            return "检验移库";
        }else if (DictConstants.WMS_MOVE_WAREHOUSE_TYPE_3 == orderType) {
            return "调拨移库";
        }else {
            throw exception(UNKNOWN_STATUS);
        }
    }
}
