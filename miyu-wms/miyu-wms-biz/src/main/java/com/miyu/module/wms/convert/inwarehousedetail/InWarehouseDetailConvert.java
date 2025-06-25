package com.miyu.module.wms.convert.inwarehousedetail;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.controller.admin.home.vo.InOutWarehouseRespVO;
import com.miyu.module.wms.controller.admin.inwarehousedetail.vo.InWarehouseDetailRespVO;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.MATERIAL_STOCK_NO_MOVE_PERMISSION;
import static com.miyu.module.wms.enums.ErrorCodeConstants.UNKNOWN_STATUS;

@Mapper
public interface InWarehouseDetailConvert {

    InWarehouseDetailConvert INSTANCE = Mappers.getMapper(InWarehouseDetailConvert.class);

    default List<InWarehouseDetailRespVO> convertList(List<InWarehouseDetailDO> list, Map<Long, AdminUserRespDTO> userMap) {
        return CollectionUtils.convertList(list, inWarehouseDetailDO ->
        {
            InWarehouseDetailRespVO inWarehouseDetailRespVO = BeanUtils.toBean(inWarehouseDetailDO, InWarehouseDetailRespVO.class);
            if (StringUtils.isNotBlank(inWarehouseDetailDO.getCreator()))
                MapUtils.findAndThen(userMap, Long.valueOf(inWarehouseDetailDO.getCreator()), a -> inWarehouseDetailRespVO.setCreator(a.getNickname()));
            return inWarehouseDetailRespVO;
        });

    }

    default List<InWarehouseDetailDO> convertList(List<OrderReqDTO> list) {
        return list.stream().map(orderReqDTO -> {
            InWarehouseDetailDO inWarehouseDetailDO = new InWarehouseDetailDO();
            inWarehouseDetailDO.setOrderNumber(orderReqDTO.getOrderNumber());
            Integer orderType = orderReqDTO.getOrderType();
            if (DictConstants.WMS_ORDER_TYPE_PURCHASE_IN == orderType) {
                inWarehouseDetailDO.setInType(DictConstants.WMS_IN_WAREHOUSE_TYPE_2);
            } else if (DictConstants.WMS_ORDER_TYPE_OUTSOURCE_IN == orderType) {
                inWarehouseDetailDO.setInType(DictConstants.WMS_IN_WAREHOUSE_TYPE_3);
            } else if (DictConstants.WMS_ORDER_TYPE_PRODUCE_IN == orderType) {
                inWarehouseDetailDO.setInType(DictConstants.WMS_IN_WAREHOUSE_TYPE_4);
            } else if (DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_MATERIAL_IN == orderType) {
                inWarehouseDetailDO.setInType(DictConstants.WMS_IN_WAREHOUSE_TYPE_5);
            } else if (DictConstants.WMS_ORDER_TYPE_CHECK_IN == orderType) {
                inWarehouseDetailDO.setInType(DictConstants.WMS_IN_WAREHOUSE_TYPE_6);
            } else if (DictConstants.WMS_ORDER_TYPE_OTHER_IN == orderType) {
                inWarehouseDetailDO.setInType(DictConstants.WMS_IN_WAREHOUSE_TYPE_7);
            } else if (DictConstants.WMS_ORDER_TYPE_MATERIAL_IN == orderType) {
                inWarehouseDetailDO.setInType(DictConstants.WMS_IN_WAREHOUSE_TYPE_8);
            } else if (DictConstants.WMS_ORDER_TYPE_PROFIT_IN == orderType) {
                inWarehouseDetailDO.setInType(DictConstants.WMS_IN_WAREHOUSE_TYPE_9);
            } else {
                return null;
            }
            if(orderReqDTO.getOrderStatus() != null){
                inWarehouseDetailDO.setInState(orderReqDTO.getOrderStatus());
            }else {
                inWarehouseDetailDO.setInState(DictConstants.WMS_ORDER_DETAIL_STATUS_6);
            }
            inWarehouseDetailDO.setChooseStockId(orderReqDTO.getChooseStockId());
            inWarehouseDetailDO.setMaterialStockId(orderReqDTO.getMaterialStockId());
            if(StringUtils.isNotBlank(orderReqDTO.getMaterialConfigId()))inWarehouseDetailDO.setMaterialConfigId(orderReqDTO.getMaterialConfigId());
            inWarehouseDetailDO.setQuantity(orderReqDTO.getQuantity());
            inWarehouseDetailDO.setStartWarehouseId(orderReqDTO.getStartWarehouseId());
            inWarehouseDetailDO.setTargetWarehouseId(orderReqDTO.getTargetWarehouseId());
            return inWarehouseDetailDO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    default List<OrderReqDTO> convertList2(List<InWarehouseDetailDO> inWarehouseDetailDOS){
        return inWarehouseDetailDOS.stream().map(inWarehouseDetailDO -> {
            OrderReqDTO orderReqDTO = new OrderReqDTO();
            orderReqDTO.setOrderNumber(inWarehouseDetailDO.getOrderNumber());
            orderReqDTO.setOrderType(inWarehouseDetailDO.getInType());
            orderReqDTO.setOrderStatus(inWarehouseDetailDO.getInState());
            Integer orderType = inWarehouseDetailDO.getInType();
            if (DictConstants.WMS_IN_WAREHOUSE_TYPE_2 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_PURCHASE_IN);
            } else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_3 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_OUTSOURCE_IN);
            } else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_4 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_PRODUCE_IN);
            } else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_5 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_MATERIAL_IN);
            } else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_6 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_CHECK_IN);
            } else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_7 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_OTHER_IN);
            } else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_8 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_MATERIAL_IN);
            } else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_9 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_PROFIT_IN);
            } else {
                return null;
            }
            orderReqDTO.setChooseStockId(inWarehouseDetailDO.getChooseStockId());
            orderReqDTO.setMaterialStockId(inWarehouseDetailDO.getMaterialStockId());
            orderReqDTO.setRealBarCode(inWarehouseDetailDO.getRealBarCode());
            orderReqDTO.setMaterialConfigId(inWarehouseDetailDO.getMaterialConfigId());
            orderReqDTO.setQuantity(inWarehouseDetailDO.getQuantity());
            orderReqDTO.setBatchNumber(inWarehouseDetailDO.getBatchNumber());
            orderReqDTO.setStartWarehouseId(inWarehouseDetailDO.getStartWarehouseId());
            orderReqDTO.setTargetWarehouseId(inWarehouseDetailDO.getTargetWarehouseId());
            orderReqDTO.setOrderStatus(inWarehouseDetailDO.getInState());
            orderReqDTO.setOperator(inWarehouseDetailDO.getOperator());
            orderReqDTO.setOperateTime(inWarehouseDetailDO.getOperateTime());
            orderReqDTO.setSigner(inWarehouseDetailDO.getSigner());
            orderReqDTO.setSignTime(inWarehouseDetailDO.getSignTime());
            return orderReqDTO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    default Integer convertInType(Integer orderType){
        if (DictConstants.WMS_ORDER_TYPE_PURCHASE_IN == orderType) {
            return DictConstants.WMS_IN_WAREHOUSE_TYPE_2;
        } else if (DictConstants.WMS_ORDER_TYPE_OUTSOURCE_IN == orderType) {
            return DictConstants.WMS_IN_WAREHOUSE_TYPE_3;
        } else if (DictConstants.WMS_ORDER_TYPE_PRODUCE_IN == orderType) {
            return DictConstants.WMS_IN_WAREHOUSE_TYPE_4;
        } else if (DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_MATERIAL_IN == orderType) {
            return DictConstants.WMS_IN_WAREHOUSE_TYPE_5;
        } else if (DictConstants.WMS_ORDER_TYPE_CHECK_IN == orderType) {
            return DictConstants.WMS_IN_WAREHOUSE_TYPE_6;
        } else if (DictConstants.WMS_ORDER_TYPE_OTHER_IN == orderType) {
            return DictConstants.WMS_IN_WAREHOUSE_TYPE_7;
        } else if (DictConstants.WMS_ORDER_TYPE_MATERIAL_IN == orderType) {
            return DictConstants.WMS_IN_WAREHOUSE_TYPE_8;
        } else if (DictConstants.WMS_ORDER_TYPE_PROFIT_IN == orderType) {
            return DictConstants.WMS_IN_WAREHOUSE_TYPE_9;
        }else {
            return null;
        }
    }

    default List<InOutWarehouseRespVO> convertToHomeList(List<InWarehouseDetailDO> inWarehouseDetailDOS, Map<String, String> warehouseIdNameMap){
        return inWarehouseDetailDOS.stream().map(inWarehouseDetailDO -> {
            InOutWarehouseRespVO inOutWarehouseRespVO = new InOutWarehouseRespVO();
            inOutWarehouseRespVO.setOrderNumber(inWarehouseDetailDO.getOrderNumber());
            inOutWarehouseRespVO.setOrderType(convertStringType(inWarehouseDetailDO.getInType()));
            inOutWarehouseRespVO.setMaterielNumber(inWarehouseDetailDO.getMaterialNumber());
            inOutWarehouseRespVO.setBarCode(inWarehouseDetailDO.getChooseBarCode());
            inOutWarehouseRespVO.setStartWarehouseName(warehouseIdNameMap.get(inWarehouseDetailDO.getStartWarehouseId()));
            inOutWarehouseRespVO.setTargetWarehouseName(warehouseIdNameMap.get(inWarehouseDetailDO.getTargetWarehouseId()));
            return inOutWarehouseRespVO;
        }).collect(Collectors.toList());
    }


    default String convertStringType(Integer orderType){
        if (DictConstants.WMS_IN_WAREHOUSE_TYPE_1 == orderType) {
            return "自建出库";
        }else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_2 == orderType) {
            return "采购入库";
        }else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_3 == orderType) {
            return "外协入库";
        }else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_4 == orderType) {
             return "生产入库";
        }else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_5 == orderType) {
             return "采购退货入库";
        }else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_6 == orderType) {
             return "检验入库";
        }else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_7 == orderType) {
             return "其他入库";
        }else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_8 == orderType) {
             return "原材料入库";
        }else if (DictConstants.WMS_IN_WAREHOUSE_TYPE_9 == orderType) {
             return "盘盈入库";
        }else {
            throw exception(UNKNOWN_STATUS);
        }
    }
}
