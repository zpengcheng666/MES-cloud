package com.miyu.module.ppm.convert.contractconsignmentreturn;


import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentRespVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailRespVO;
import com.miyu.module.ppm.controller.admin.shippingreturn.vo.ShippingReturnRespVO;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturn.ShippingReturnDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturndetail.ShippingReturnDetailDO;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Mapper
public interface ContractConsignmentReturnConvert {

    ContractConsignmentReturnConvert INSTANCE = Mappers.getMapper(ContractConsignmentReturnConvert.class);





    default List<PurchaseConsignmentDetailRespVO> convertList(List<ConsignmentDetailDO> list,
                                                              Map<Long, AdminUserRespDTO> userMap,
                                                              Map<String, ContractOrderDO> orderMap,
                                                              Map<String, MaterialConfigRespDTO> map,
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

            return vo;
        });
    }




}
