package com.miyu.module.ppm.convert.purchaseConsignmentDetail;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.controller.admin.consignmentreturndetail.vo.ConsignmentReturnDetailSaveReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailRespVO;
import com.miyu.module.ppm.dal.dataobject.companyproduct.CompanyProductDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.warehousedetail.WarehouseDetailDO;
import com.miyu.module.qms.api.dto.UnqualifiedMaterialRespDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface PurchaseConsignmentDetailConvert {

    PurchaseConsignmentDetailConvert INSTANCE = Mappers.getMapper(PurchaseConsignmentDetailConvert.class);

    default List<PurchaseConsignmentDetailRespVO> convertList(List<ConsignmentDetailDO> list, Map<Long, AdminUserRespDTO> userMap, Map<String, ContractOrderDO> orderMap, Map<String, MaterialConfigRespDTO> map, Map<String, BigDecimal> numberMap) {
        return CollectionUtils.convertList(list, purchaseConsignmentDetailDO ->
        {
            PurchaseConsignmentDetailRespVO vo = BeanUtils.toBean(purchaseConsignmentDetailDO, PurchaseConsignmentDetailRespVO.class);
//            if (StringUtils.isNotBlank(purchaseConsignmentDetailDO.getOutboundBy()))
//                MapUtils.findAndThen(userMap, Long.valueOf(purchaseConsignmentDetailDO.getOutboundBy()), a -> vo.setOutboundBy(a.getNickname()));
            if (StringUtils.isNotBlank(purchaseConsignmentDetailDO.getSignedBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(purchaseConsignmentDetailDO.getSignedBy()), a -> vo.setSignedBy(a.getNickname()));

            MapUtils.findAndThen(orderMap, purchaseConsignmentDetailDO.getOrderId(), a -> vo.setMaterialConfigId(a.getMaterialId()).setQuantity(a.getQuantity()));
            MapUtils.findAndThen(map, vo.getMaterialConfigId(), a -> vo.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialType(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setMaterialTypeName(a.getMaterialTypeName())
            .setMaterialParentTypeName(a.getMaterialParentTypeName()));
            BigDecimal number = numberMap.get(vo.getOrderId());
            if (number == null) {
                vo.setFinishQuantity(new BigDecimal(0));
            } else {
                vo.setFinishQuantity(number);
            }

            return vo;
        });
    }


    default List<PurchaseConsignmentDetailRespVO> convertListInfo(List<ConsignmentInfoDO> list, Map<Long, AdminUserRespDTO> userMap, Map<String, ContractOrderDO> orderMap, Map<String, MaterialConfigRespDTO> map, Map<String, BigDecimal> numberMap) {
        return CollectionUtils.convertList(list, purchaseConsignmentDetailDO ->
        {
            PurchaseConsignmentDetailRespVO vo = BeanUtils.toBean(purchaseConsignmentDetailDO, PurchaseConsignmentDetailRespVO.class);
//            if (StringUtils.isNotBlank(purchaseConsignmentDetailDO.getOutboundBy()))
//                MapUtils.findAndThen(userMap, Long.valueOf(purchaseConsignmentDetailDO.getOutboundBy()), a -> vo.setOutboundBy(a.getNickname()));
            if (StringUtils.isNotBlank(purchaseConsignmentDetailDO.getSignedBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(purchaseConsignmentDetailDO.getSignedBy()), a -> vo.setSignedBy(a.getNickname()));

            MapUtils.findAndThen(orderMap, purchaseConsignmentDetailDO.getOrderId(), a -> vo.setMaterialConfigId(a.getMaterialId()).setQuantity(a.getQuantity()));
            MapUtils.findAndThen(map, vo.getMaterialConfigId(), a -> vo.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialType(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setMaterialTypeName(a.getMaterialTypeName())
                    .setMaterialParentTypeName(a.getMaterialParentTypeName()));
            BigDecimal number = numberMap.get(vo.getOrderId());
            if (number == null) {
                vo.setFinishQuantity(new BigDecimal(0));
            } else {
                vo.setFinishQuantity(number);
            }

            return vo;
        });
    }



    default List<PurchaseConsignmentDetailRespVO> convertDetailList(List<ConsignmentDetailDO> list, Map<String, MaterialConfigRespDTO> map, Map<String, ContractOrderRespDTO> orderMap) {
        return CollectionUtils.convertList(list, purchaseConsignmentDetailDO ->
        {

            PurchaseConsignmentDetailRespVO vo = BeanUtils.toBean(purchaseConsignmentDetailDO, PurchaseConsignmentDetailRespVO.class);
            vo.setChooseQuantity(new BigDecimal(0));
            MapUtils.findAndThen(orderMap, purchaseConsignmentDetailDO.getOrderId(), a -> vo.setMaterialConfigId(a.getMaterialId()).setQuantity(a.getQuantity()));
            MapUtils.findAndThen(map, vo.getMaterialConfigId(), a -> vo.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialType(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand())
                    .setMaterialConfigId(a.getId()).setMaterialTypeName(a.getMaterialTypeName())
                    .setMaterialParentTypeName(a.getMaterialParentTypeName()));

            vo.setRemainingQuantity(vo.getQuantity().subtract(vo.getFinishQuantity()== null? new BigDecimal(0):vo.getFinishQuantity()));
            return vo;
        });

    }

    default List<PurchaseConsignmentDetailRespVO> convertPurchaseConsignmentDetailList(List<ConsignmentDetailDO> list , Map<String, ContractOrderRespDTO> numberMap , Map<String , WarehouseDetailDO> warehouseMap ) {
        return CollectionUtils.convertList(list, PurchaseConsignmentDetailDO -> {
            PurchaseConsignmentDetailRespVO vo = BeanUtils.toBean(PurchaseConsignmentDetailDO, PurchaseConsignmentDetailRespVO.class);
            MapUtils.findAndThen(numberMap, PurchaseConsignmentDetailDO.getMaterialConfigId(), a -> vo.setMaterialConfigId(a.getMaterialId()));
            MapUtils.findAndThen(warehouseMap, PurchaseConsignmentDetailDO.getMaterialConfigId(), a -> vo.setMaterialNumber(a.getMaterialNumber()).setMaterialName(a.getMaterialName()).setMaterialProperty(a.getMaterialProperty()).setMaterialType(a.getMaterialType()).
                    setMaterialUnit(a.getMaterialUnit()).setBarCode(a.getBarCode()).setBatchNumber(a.getBatchNumber()).setMaterialBrand(a.getMaterialBrand()).setMaterialSpecification(a.getMaterialSpecification()).setMaterialStockId(a.getMaterialStockId())
                    .setSignedAmount(a.getSignedAmount()).setBatchNumber(a.getBatchNumber()).setConsignedAmount(new BigDecimal(0)).setQuantity(a.getQuantity())
            );
            return vo;
        });
    }


    default List<ConsignmentReturnDetailSaveReqVO> convertPurchaseConsignmentReturnList(List<ConsignmentReturnDetailDO> detailDOS , Map<String, MaterialConfigRespDTO> configRespDTOMap ) {
        return CollectionUtils.convertList(detailDOS, ConsignmentReturnDetailDO -> {
            ConsignmentReturnDetailSaveReqVO vo = BeanUtils.toBean(ConsignmentReturnDetailDO, ConsignmentReturnDetailSaveReqVO.class);
            MapUtils.findAndThen(configRespDTOMap, ConsignmentReturnDetailDO.getMaterialConfigId(), a -> vo.setMaterialNumber(a.getMaterialNumber()).setMaterialName(a.getMaterialName()).
                    setMaterialUnit(a.getMaterialUnit()).setMaterialBrand(a.getMaterialBrand()).setMaterialSpecification(a.getMaterialSpecification())

            );
            return vo;
        });
    }



    default List<PurchaseConsignmentDetailRespVO> convertListForQms(List<ConsignmentDetailDO> list, Map<Long, AdminUserRespDTO> userMap, Map<String, MaterialConfigRespDTO> map, Map<String, BigDecimal> numberMap, Map<String, CompanyProductDO> productDOMap, Map<String, ContractOrderRespDTO> orderMap) {
        return CollectionUtils.convertList(list, purchaseConsignmentDetailDO ->
        {
            PurchaseConsignmentDetailRespVO vo = BeanUtils.toBean(purchaseConsignmentDetailDO, PurchaseConsignmentDetailRespVO.class);
//            if (StringUtils.isNotBlank(purchaseConsignmentDetailDO.getOutboundBy()))
//                MapUtils.findAndThen(userMap, Long.valueOf(purchaseConsignmentDetailDO.getOutboundBy()), a -> vo.setOutboundBy(a.getNickname()));

            MapUtils.findAndThen(orderMap, purchaseConsignmentDetailDO.getOrderId(), a -> vo.setMaterialConfigId(a.getMaterialId()).setQuantity(a.getQuantity()));

            if (StringUtils.isNotBlank(purchaseConsignmentDetailDO.getSignedBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(purchaseConsignmentDetailDO.getSignedBy()), a -> vo.setSignedBy(a.getNickname()));

            MapUtils.findAndThen(map, vo.getMaterialConfigId(), a -> vo.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialType(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setMaterialTypeName(a.getMaterialTypeName())
                    .setMaterialParentTypeName(a.getMaterialParentTypeName()));

            MapUtils.findAndThen(productDOMap, vo.getMaterialConfigId(), a -> vo.setQualityCheck(a.getQualityCheck()));
            if (vo.getQualityCheck() == null){
                vo.setQualityCheck(2);
            }
            BigDecimal number = numberMap.get(vo.getOrderId());
            if (number == null) {
                vo.setFinishQuantity(new BigDecimal(0));
            } else {
                vo.setFinishQuantity(number);
            }

            return vo;
        });
    }



    default List<PurchaseConsignmentDetailRespVO> convertList(List<ConsignmentDetailDO> list,  Map<String, MaterialConfigRespDTO> map) {
        return CollectionUtils.convertList(list, purchaseConsignmentDetailDO ->
        {
            PurchaseConsignmentDetailRespVO vo = BeanUtils.toBean(purchaseConsignmentDetailDO, PurchaseConsignmentDetailRespVO.class);
//
            MapUtils.findAndThen(map, vo.getMaterialConfigId(), a -> vo.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialType(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setMaterialTypeName(a.getMaterialTypeName())
                    .setMaterialParentTypeName(a.getMaterialParentTypeName()));

            return vo;
        });
    }




    default List<PurchaseConsignmentDetailRespVO> convertList(Map<String,ConsignmentDetailDO> consignmentDetailDOMap, Map<String, MaterialConfigRespDTO> map, List<MaterialStockRespDTO> respDTOS,Map<String, UnqualifiedMaterialRespDTO> respDTOMap) {
        return CollectionUtils.convertList(respDTOS, purchaseConsignmentDetailDO ->
        {
            ConsignmentDetailDO detailDO = consignmentDetailDOMap.get(purchaseConsignmentDetailDO.getBarCode());
            PurchaseConsignmentDetailRespVO vo = BeanUtils.toBean(detailDO, PurchaseConsignmentDetailRespVO.class);
            vo.setMaterialStatus(purchaseConsignmentDetailDO.getMaterialStatus());
            MapUtils.findAndThen(map, vo.getMaterialConfigId(), a -> vo.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialType(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setMaterialTypeName(a.getMaterialTypeName())
                    .setMaterialParentTypeName(a.getMaterialParentTypeName()));
            MapUtils.findAndThen(respDTOMap,purchaseConsignmentDetailDO.getBarCode(),a -> vo.setDefectiveLevel(a.getDefectiveLevel()).setHandleMethod(a.getHandleMethod()));
            return vo;
        });
    }





}
