package com.miyu.module.ppm.convert.shipping;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.ppm.api.shipping.dto.WmsOutBoundInfoDTO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ContractOrderRespVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingRespVO;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
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
public interface ShippingConvert {

    ShippingConvert INSTANCE = Mappers.getMapper(ShippingConvert.class);

    default List<ShippingRespVO> convertList(List<ShippingDO> list, Map<Long, AdminUserRespDTO> userMap, Map<String, ContractDO> contractMap, Map<String, CompanyDO> companyMap, Map<String, PmsApprovalDto> pmsApprovalDtoMap) {
        return CollectionUtils.convertList(list, shippingDO ->
        {
            ShippingRespVO shippingRespVO = BeanUtils.toBean(shippingDO, ShippingRespVO.class);
            if (StringUtils.isNotBlank(shippingDO.getConsigner()))
                MapUtils.findAndThen(userMap, Long.valueOf(shippingDO.getConsigner()), a -> shippingRespVO.setConsigner(a.getNickname()));
            ContractDO contractRespDTO = contractMap.get(shippingDO.getContractId());
            if (contractRespDTO != null) {
                shippingRespVO.setContractNo(contractRespDTO.getNumber()).setContractName(contractRespDTO.getName());
                MapUtils.findAndThen(companyMap, contractRespDTO.getParty(), a -> shippingRespVO.setCompanyName(a.getName()));

            }
            MapUtils.findAndThen(pmsApprovalDtoMap, shippingRespVO.getProjectId(), a -> shippingRespVO.setProjectName(a.getProjectName()).setProjectCode(a.getProjectCode()));

            return shippingRespVO;
        });

    }


    default List<ContractOrderRespVO> convertOrderList(List<ContractOrderDO> list, Map<String, MaterialConfigRespDTO> map, Map<String, BigDecimal> numberMap, Map<String, BigDecimal> returnNumberMap) {
        return CollectionUtils.convertList(list, orderRespDTO ->
        {
            ContractOrderRespVO orderRespVO = BeanUtils.toBean(orderRespDTO, ContractOrderRespVO.class);

            MapUtils.findAndThen(map, orderRespDTO.getMaterialId(), a -> orderRespVO.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()));
            BigDecimal number = numberMap.get(orderRespDTO.getId());
            if (number == null) {
                orderRespVO.setFinishQuantity(new BigDecimal(0));
            } else {
                orderRespVO.setFinishQuantity(number);
            }
            BigDecimal returnNumber = returnNumberMap.get(orderRespDTO.getId());
            if (returnNumber == null) {
                orderRespVO.setReturnQuantity(new BigDecimal(0));
            } else {
                orderRespVO.setReturnQuantity(returnNumber);
            }

            orderRespVO.setChooseQuantity(new BigDecimal(0));
            orderRespVO.setRemainingQuantity(orderRespVO.getQuantity().subtract(orderRespVO.getFinishQuantity()).add(orderRespVO.getReturnQuantity()));

            return orderRespVO;
        });

    }


    default List<OrderReqDTO> convertMaterial(List<ShippingDetailDO> detailDOS,ShippingDO shippingDO) {
        return CollectionUtils.convertList(detailDOS, detailDO ->
        {
            OrderReqDTO orderRespVO = new OrderReqDTO();
            orderRespVO.setOrderNumber(shippingDO.getNo());
            orderRespVO.setOrderType(DictConstants.WMS_ORDER_TYPE_SALE_OUT);
            orderRespVO.setMaterialStockId(detailDO.getMaterialStockId());
            orderRespVO.setQuantity(detailDO.getOutboundAmount().intValue());
            //目标仓库
            orderRespVO.setTargetWarehouseId("");
            return orderRespVO;
        });
    }

}
