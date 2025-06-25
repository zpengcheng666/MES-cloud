package com.miyu.module.wms.convert.outwarehousedetail;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.controller.admin.home.vo.InOutWarehouseRespVO;
import com.miyu.module.wms.controller.admin.outwarehousedetail.vo.OutWarehouseDetailRespVO;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.UNKNOWN_STATUS;

@Mapper
public interface OutWarehouseDetailConvert {

    OutWarehouseDetailConvert INSTANCE = Mappers.getMapper(OutWarehouseDetailConvert.class);

    default List<OutWarehouseDetailRespVO> convertList(List<OutWarehouseDetailDO> list, Map<Long, AdminUserRespDTO> userMap) {
        return CollectionUtils.convertList(list, OutWarehouseDetailDO ->
        {
            OutWarehouseDetailRespVO outWarehouseDetailRespVO = BeanUtils.toBean(OutWarehouseDetailDO, OutWarehouseDetailRespVO.class);
            if (StringUtils.isNotBlank(OutWarehouseDetailDO.getCreator()))
                MapUtils.findAndThen(userMap, Long.valueOf(OutWarehouseDetailDO.getCreator()), a -> outWarehouseDetailRespVO.setCreator(a.getNickname()));
            return outWarehouseDetailRespVO;
        });

    }

    default List<OutWarehouseDetailDO> convertList(List<OrderReqDTO> list) {
        return list.stream().map(orderReqDTO -> {
            OutWarehouseDetailDO outWarehouseDetailDO = new OutWarehouseDetailDO();
            outWarehouseDetailDO.setOrderNumber(orderReqDTO.getOrderNumber());
            Integer orderType = orderReqDTO.getOrderType();
            if (DictConstants.WMS_ORDER_TYPE_SALE_OUT == orderType) {
                outWarehouseDetailDO.setOutType(DictConstants.WMS_OUT_WAREHOUSE_TYPE_2);
            } else if (DictConstants.WMS_ORDER_TYPE_OUTSOURCE_OUT == orderType) {
                outWarehouseDetailDO.setOutType(DictConstants.WMS_OUT_WAREHOUSE_TYPE_3);
            } else if (DictConstants.WMS_ORDER_TYPE_PRODUCE_OUT == orderType) {
                outWarehouseDetailDO.setOutType(DictConstants.WMS_OUT_WAREHOUSE_TYPE_4);
            } else if (DictConstants.WMS_ORDER_TYPE_CHECK_OUT == orderType) {
                outWarehouseDetailDO.setOutType(DictConstants.WMS_OUT_WAREHOUSE_TYPE_5);
            } else if (DictConstants.WMS_ORDER_TYPE_DAMAGE_OUT == orderType) {
                outWarehouseDetailDO.setOutType(DictConstants.WMS_OUT_WAREHOUSE_TYPE_6);
            } else if (DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT == orderType) {
                outWarehouseDetailDO.setOutType(DictConstants.WMS_OUT_WAREHOUSE_TYPE_7);
            } else if (DictConstants.WMS_ORDER_TYPE_TRANSFER_OUT == orderType) {
                outWarehouseDetailDO.setOutType(DictConstants.WMS_OUT_WAREHOUSE_TYPE_8);
            } else if (DictConstants.WMS_ORDER_TYPE_OTHER_OUT == orderType) {
                outWarehouseDetailDO.setOutType(DictConstants.WMS_OUT_WAREHOUSE_TYPE_9);
            }  else if (DictConstants.WMS_ORDER_TYPE_LOSS_OUT == orderType) {
                outWarehouseDetailDO.setOutType(DictConstants.WMS_OUT_WAREHOUSE_TYPE_10);
            } else {
                return null;
            }
            if(orderReqDTO.getOrderStatus() != null){
                outWarehouseDetailDO.setOutState(orderReqDTO.getOrderStatus());
            }else {
                outWarehouseDetailDO.setOutState(DictConstants.WMS_ORDER_DETAIL_STATUS_6);
            }
            if(Objects.isNull(orderReqDTO.getNeedTime())) {
                orderReqDTO.setNeedTime(LocalDateTime.now());
            }
            outWarehouseDetailDO.setNeedTime(orderReqDTO.getNeedTime());
            outWarehouseDetailDO.setMaterialStockId(orderReqDTO.getMaterialStockId());
            outWarehouseDetailDO.setChooseStockId(orderReqDTO.getChooseStockId());
            outWarehouseDetailDO.setQuantity(orderReqDTO.getQuantity());
            outWarehouseDetailDO.setSignLocationId(orderReqDTO.getSignLocationId());
            if(StringUtils.isNotBlank(orderReqDTO.getStartWarehouseId()))outWarehouseDetailDO.setStartWarehouseId(orderReqDTO.getStartWarehouseId());
            outWarehouseDetailDO.setTargetWarehouseId(orderReqDTO.getTargetWarehouseId());
            return outWarehouseDetailDO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    default List<OrderReqDTO> convertList2(List<OutWarehouseDetailDO> outWarehouseDetailDOS){
        return outWarehouseDetailDOS.stream().map(outWarehouseDetailDO -> {
            OrderReqDTO orderReqDTO = new OrderReqDTO();
            orderReqDTO.setOrderNumber(outWarehouseDetailDO.getOrderNumber());
            Integer orderType = outWarehouseDetailDO.getOutType();
            if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_2 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_SALE_OUT);
            } else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_3 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_OUTSOURCE_OUT);
            } else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_4 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_PRODUCE_OUT);
            } else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_5 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_CHECK_OUT);
            } else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_6 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_DAMAGE_OUT);
            } else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_7 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT);
            } else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_8 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_TRANSFER_OUT);
            } else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_9 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_OTHER_OUT);
            }else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_10 == orderType) {
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_LOSS_OUT);
            }else {
                return null;
            }
            orderReqDTO.setChooseStockId(outWarehouseDetailDO.getChooseStockId());
            orderReqDTO.setMaterialStockId(outWarehouseDetailDO.getMaterialStockId());
            orderReqDTO.setRealBarCode(outWarehouseDetailDO.getRealBarCode());
            orderReqDTO.setMaterialConfigId(outWarehouseDetailDO.getMaterialConfigId());
            orderReqDTO.setQuantity(outWarehouseDetailDO.getQuantity());
            orderReqDTO.setBatchNumber(outWarehouseDetailDO.getBatchNumber());
            orderReqDTO.setStartWarehouseId(outWarehouseDetailDO.getStartWarehouseId());
            orderReqDTO.setTargetWarehouseId(outWarehouseDetailDO.getTargetWarehouseId());
            orderReqDTO.setSignLocationId(outWarehouseDetailDO.getSignLocationId());
            orderReqDTO.setOrderStatus(outWarehouseDetailDO.getOutState());
            orderReqDTO.setOperator(outWarehouseDetailDO.getOperator());
            orderReqDTO.setOperateTime(outWarehouseDetailDO.getOperateTime());
            orderReqDTO.setSigner(outWarehouseDetailDO.getSigner());
            orderReqDTO.setSignTime(outWarehouseDetailDO.getSignTime());
            return orderReqDTO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    default Integer convertOutType(Integer orderType){
        if (DictConstants.WMS_ORDER_TYPE_SALE_OUT == orderType) {
            return DictConstants.WMS_OUT_WAREHOUSE_TYPE_2;
        } else if (DictConstants.WMS_ORDER_TYPE_OUTSOURCE_OUT == orderType) {
            return DictConstants.WMS_OUT_WAREHOUSE_TYPE_3;
        } else if (DictConstants.WMS_ORDER_TYPE_PRODUCE_OUT == orderType) {
            return DictConstants.WMS_OUT_WAREHOUSE_TYPE_4;
        } else if (DictConstants.WMS_ORDER_TYPE_CHECK_OUT == orderType) {
            return DictConstants.WMS_OUT_WAREHOUSE_TYPE_5;
        } else if (DictConstants.WMS_ORDER_TYPE_DAMAGE_OUT == orderType) {
            return DictConstants.WMS_OUT_WAREHOUSE_TYPE_6;
        } else if (DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT == orderType) {
            return DictConstants.WMS_OUT_WAREHOUSE_TYPE_7;
        } else if (DictConstants.WMS_ORDER_TYPE_TRANSFER_OUT == orderType) {
            return DictConstants.WMS_OUT_WAREHOUSE_TYPE_8;
        } else if (DictConstants.WMS_ORDER_TYPE_OTHER_OUT == orderType) {
            return DictConstants.WMS_OUT_WAREHOUSE_TYPE_9;
        }  else if (DictConstants.WMS_ORDER_TYPE_LOSS_OUT == orderType) {
            return DictConstants.WMS_OUT_WAREHOUSE_TYPE_10;
        } else {
            return null;
        }
    }

    default List<InOutWarehouseRespVO> convertToHomeList(List<OutWarehouseDetailDO> outWarehouseDetailDOS, Map<String, String> warehouseIdNameMap){
        return outWarehouseDetailDOS.stream().map(outWarehouseDetailDO -> {
            InOutWarehouseRespVO inOutWarehouseRespVO = new InOutWarehouseRespVO();
            inOutWarehouseRespVO.setOrderNumber(outWarehouseDetailDO.getOrderNumber());
            inOutWarehouseRespVO.setOrderType(convertStringType(outWarehouseDetailDO.getOutType()));
            inOutWarehouseRespVO.setMaterielNumber(outWarehouseDetailDO.getMaterialNumber());
            inOutWarehouseRespVO.setBarCode(outWarehouseDetailDO.getChooseBarCode());
            inOutWarehouseRespVO.setStartWarehouseName(warehouseIdNameMap.get(outWarehouseDetailDO.getStartWarehouseId()));
            inOutWarehouseRespVO.setTargetWarehouseName(warehouseIdNameMap.get(outWarehouseDetailDO.getTargetWarehouseId()));
            return inOutWarehouseRespVO;
        }).collect(Collectors.toList());
    }


    default String convertStringType(Integer orderType){
        if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_1 == orderType) {
            return "自建出库";
        }else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_2 == orderType) {
            return "销售出库";
        }else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_3 == orderType) {
            return "外协出库";
        }else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_4 == orderType) {
            return "生产出库";
        }else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_5 == orderType) {
            return "检验出库";
        }else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_6 == orderType) {
            return "报损出库";
        }else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_7 == orderType) {
            return "采购退货出库";
        }else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_8 == orderType) {
            return "调拨出库";
        }else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_9 == orderType) {
            return "其他出库";
        }else if (DictConstants.WMS_OUT_WAREHOUSE_TYPE_10 == orderType) {
            return "盘亏出库";
        }else {
            throw exception(UNKNOWN_STATUS);
        }
    }
}
