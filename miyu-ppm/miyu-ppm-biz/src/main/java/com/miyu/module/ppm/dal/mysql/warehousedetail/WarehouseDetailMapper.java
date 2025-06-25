package com.miyu.module.ppm.dal.mysql.warehousedetail;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.warehousedetail.WarehouseDetailDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.warehousedetail.vo.*;

/**
 * 入库详情表 对应仓库库存 来源WMS Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface WarehouseDetailMapper extends BaseMapperX<WarehouseDetailDO> {

    default PageResult<WarehouseDetailDO> selectPage(WarehouseDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarehouseDetailDO>()
                .eqIfPresent(WarehouseDetailDO::getOrderNumber, reqVO.getOrderNumber())
                .eqIfPresent(WarehouseDetailDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(WarehouseDetailDO::getInstate, reqVO.getInstate())
                .eqIfPresent(WarehouseDetailDO::getBatchNumber, reqVO.getBatchNumber())
                .eqIfPresent(WarehouseDetailDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(WarehouseDetailDO::getMaterialStockId, reqVO.getMaterialStockId())
                .eqIfPresent(WarehouseDetailDO::getMaterialUnit, reqVO.getMeterialUnit())
                .eqIfPresent(WarehouseDetailDO::getSignedAmount, reqVO.getSignedAmount())
                .eqIfPresent(WarehouseDetailDO::getMaterialNumber, reqVO.getMaterialNumber())
                .eqIfPresent(WarehouseDetailDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(WarehouseDetailDO::getWarehouseCode, reqVO.getWarehouseCode())
                .likeIfPresent(WarehouseDetailDO::getMaterialName, reqVO.getMaterialName())
                .eqIfPresent(WarehouseDetailDO::getMaterialProperty, reqVO.getMaterialProperty())
                .eqIfPresent(WarehouseDetailDO::getMaterialType, reqVO.getMaterialType())
                .eqIfPresent(WarehouseDetailDO::getMaterialManage, reqVO.getMaterialManage())
                .eqIfPresent(WarehouseDetailDO::getMaterialSpecification, reqVO.getMaterialSpecification())
                .eqIfPresent(WarehouseDetailDO::getMaterialBrand, reqVO.getMaterialBrand())
                .eqIfPresent(WarehouseDetailDO::getConsignedAmount, reqVO.getConsignedAmount())
                .eqIfPresent(WarehouseDetailDO::getQuantity, reqVO.getQuantity())
                .orderByDesc(WarehouseDetailDO::getId));
    }

    /**
     * 查询退货单明细对应的入库详情
     */
    default List<WarehouseDetailDO> queryWareHouseList(String consignmentReturnId){
        MPJLambdaWrapperX<WarehouseDetailDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(ConsignmentDO.class, ConsignmentDO::getNo,WarehouseDetailDO::getOrderNumber)
                .leftJoin(ConsignmentReturnDetailDO.class,ConsignmentReturnDetailDO::getNo, ConsignmentDO::getNo)
                .eq(ConsignmentReturnDetailDO::getConsignmentReturnId,consignmentReturnId)
                .selectAll(WarehouseDetailDO.class);
        return selectList(wrapper);
    }

    /**
     * 查询收货单对应的产品信息
     */
    default List<WarehouseDetailDO> queryWareHouseListByConsignmentReturnNo(String consignmentReturnNo){
        MPJLambdaWrapperX<WarehouseDetailDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.eq(WarehouseDetailDO::getOrderNumber,consignmentReturnNo)
                .selectAll(WarehouseDetailDO.class);
        return selectList(wrapper);
    }

}