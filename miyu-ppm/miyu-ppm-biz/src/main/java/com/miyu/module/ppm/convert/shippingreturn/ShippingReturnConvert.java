package com.miyu.module.ppm.convert.shippingreturn;


import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentRespVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailRespVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingRespVO;
import com.miyu.module.ppm.controller.admin.shippingreturn.vo.ShippingReturnRespVO;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturn.ShippingReturnDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturndetail.ShippingReturnDetailDO;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Mapper
public interface ShippingReturnConvert {

    ShippingReturnConvert INSTANCE = Mappers.getMapper(ShippingReturnConvert.class);


    default List<PurchaseConsignmentRespVO> convertList(List<ConsignmentDO> list, Map<Long, AdminUserRespDTO> userMap, Map<String, ContractDO> contractMap, Map<String, CompanyDO> companyMap, Map<String, PmsApprovalDto> projectMap) {
        return CollectionUtils.convertList(list, shippingReturnDO ->
        {
            PurchaseConsignmentRespVO shippingReturnRespVO = BeanUtils.toBean(shippingReturnDO, PurchaseConsignmentRespVO.class);
            if (StringUtils.isNotBlank(shippingReturnDO.getConsignedBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(shippingReturnDO.getConsignedBy()), a -> shippingReturnRespVO.setConsignedBy(a.getNickname()));
            ContractDO contractRespDTO = contractMap.get(shippingReturnDO.getContractId());
            if (contractRespDTO != null) {
                shippingReturnRespVO.setContractNo(contractRespDTO.getNumber()).setContractName(contractRespDTO.getName());
                MapUtils.findAndThen(companyMap, contractRespDTO.getParty(), a -> shippingReturnRespVO.setCompanyName(a.getName()));
            }
            MapUtils.findAndThen(projectMap, shippingReturnDO.getProjectId(), a -> shippingReturnRespVO.setProjectName(a.getProjectName()));

            return shippingReturnRespVO;
        });

    }



    default List<PurchaseConsignmentDetailRespVO> convertList(List<ConsignmentDetailDO> list,
                                                              Map<Long, AdminUserRespDTO> userMap,
                                                              Map<String, ContractOrderDO> orderMap,
                                                              Map<String, MaterialConfigRespDTO> map,
                                                              Map<String, BigDecimal> numberMap,
                                                              Map<String, ShippingDO> shippingDOMap
                                                              ) {
        return CollectionUtils.convertList(list, purchaseConsignmentDetailDO ->
        {
            PurchaseConsignmentDetailRespVO vo = BeanUtils.toBean(purchaseConsignmentDetailDO, PurchaseConsignmentDetailRespVO.class);
            vo.setRowDisable(0);
//            if (StringUtils.isNotBlank(purchaseConsignmentDetailDO.getOutboundBy()))
//                MapUtils.findAndThen(userMap, Long.valueOf(purchaseConsignmentDetailDO.getOutboundBy()), a -> vo.setOutboundBy(a.getNickname()));
            if (StringUtils.isNotBlank(purchaseConsignmentDetailDO.getSignedBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(purchaseConsignmentDetailDO.getSignedBy()), a -> vo.setSignedBy(a.getNickname()));

            MapUtils.findAndThen(orderMap, purchaseConsignmentDetailDO.getOrderId(), a -> vo.setMaterialConfigId(a.getMaterialId()).setQuantity(new BigDecimal(1)));
            MapUtils.findAndThen(shippingDOMap, purchaseConsignmentDetailDO.getShippingId(), a -> vo.setShippingName(a.getName()).setShippingNo(a.getNo()));
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






    default List<ShippingReturnRespVO> convertList(List<ConsignmentDO> list,  Map<String, BigDecimal> priceMap) {
        return CollectionUtils.convertList(list, shippingReturnDO ->
        {
            ShippingReturnRespVO shippingReturnRespVO = BeanUtils.toBean(shippingReturnDO, ShippingReturnRespVO.class);
            shippingReturnRespVO.setPrice(priceMap.get(shippingReturnDO.getId()) == null?new BigDecimal(0):priceMap.get(shippingReturnDO.getId()));
            return shippingReturnRespVO;
        });

    }


    default List<OrderReqDTO> convertMaterial(List<ShippingReturnDetailDO> detailDOS, ShippingReturnDO shippingDO) {
        return CollectionUtils.convertList(detailDOS, detailDO ->
        {
            OrderReqDTO orderRespVO = new OrderReqDTO();
            orderRespVO.setOrderNumber(shippingDO.getShippingReturnNo());
            orderRespVO.setOrderType(DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_MATERIAL_IN);
            orderRespVO.setMaterialStockId(detailDO.getMaterialStockId());
            orderRespVO.setQuantity(detailDO.getSignedAmount().intValue());
            orderRespVO.setOrderStatus(1);
            //目标仓库
            orderRespVO.setTargetWarehouseId("");
            return orderRespVO;
        });
    }

}
