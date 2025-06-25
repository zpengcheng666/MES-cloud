package com.miyu.module.ppm.convert.shippingdetail;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailRetraceDTO;
import com.miyu.module.ppm.controller.admin.shippingdetail.vo.ShippingDetailRespVO;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface ShippingDetailConvert {

    ShippingDetailConvert INSTANCE = Mappers.getMapper(ShippingDetailConvert.class);

    default List<ShippingDetailRespVO> convertList(List<ShippingDetailDO> list, Map<Long, AdminUserRespDTO> userMap, Map<String, MaterialConfigRespDTO> map) {
        return CollectionUtils.convertList(list, shippingDetailDO ->
        {

            ShippingDetailRespVO vo = BeanUtils.toBean(shippingDetailDO, ShippingDetailRespVO.class);
            vo.setMaterialConfigId(shippingDetailDO.getMaterialConfigId());
            if (StringUtils.isNotBlank(shippingDetailDO.getOutboundBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(shippingDetailDO.getOutboundBy()), a -> vo.setOutboundBy(a.getNickname()));
//            if (StringUtils.isNotBlank(shippingDetailDO.getSignedBy()))
//                MapUtils.findAndThen(userMap, Long.valueOf(shippingDetailDO.getSignedBy()), a -> vo.setSignedBy(a.getNickname()));

           MapUtils.findAndThen(map, vo.getMaterialConfigId(), a -> vo.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setMaterialTypeName(a.getMaterialTypeName()).setMaterialParentTypeName(a.getMaterialParentTypeName()));

            return vo;
        });

    }


    default List<ShippingDetailRespVO> convertListForReturn(List<ShippingDetailDO> list,
                                                   Map<Long, AdminUserRespDTO> userMap,
                                                   Map<String, MaterialConfigRespDTO> map,
                                                   Map<String, ConsignmentDO> consignmentDOMap) {
        return CollectionUtils.convertList(list, shippingDetailDO ->
        {

            ShippingDetailRespVO vo = BeanUtils.toBean(shippingDetailDO, ShippingDetailRespVO.class);
            vo.setMaterialConfigId(shippingDetailDO.getMaterialConfigId());
            if (StringUtils.isNotBlank(shippingDetailDO.getOutboundBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(shippingDetailDO.getOutboundBy()), a -> vo.setOutboundBy(a.getNickname()));
//            if (StringUtils.isNotBlank(shippingDetailDO.getSignedBy()))
//                MapUtils.findAndThen(userMap, Long.valueOf(shippingDetailDO.getSignedBy()), a -> vo.setSignedBy(a.getNickname()));
            MapUtils.findAndThen(consignmentDOMap, shippingDetailDO.getConsignmentId(), a -> vo.setConsignmentNo(a.getNo()).setConsignmentName(a.getName()));

            MapUtils.findAndThen(map, vo.getMaterialConfigId(), a -> vo.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setMaterialTypeName(a.getMaterialTypeName()).setMaterialParentTypeName(a.getMaterialParentTypeName()));

            return vo;
        });

    }


    default List<ShippingDetailRespVO> convertDetailList(List<ShippingDetailDO> list, Map<String, MaterialConfigRespDTO> map,Map<String, ContractOrderDO> orderMap) {
        return CollectionUtils.convertList(list, shippingDetailDO ->
        {

            ShippingDetailRespVO vo = BeanUtils.toBean(shippingDetailDO, ShippingDetailRespVO.class);
            vo.setChooseQuantity(new BigDecimal(0));
            MapUtils.findAndThen(orderMap, shippingDetailDO.getOrderId(), a -> vo.setMaterialConfigId(a.getMaterialId()).setQuantity(a.getQuantity()));
            MapUtils.findAndThen(map, vo.getMaterialConfigId(), a -> vo.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand())
                    .setMaterialTypeName(a.getMaterialTypeName()).setMaterialParentTypeName(a.getMaterialParentTypeName())
                    .setMaterialConfigId(a.getId()));

            vo.setRemainingQuantity(vo.getQuantity().subtract(vo.getFinishQuantity()== null? new BigDecimal(0):vo.getFinishQuantity()));
            return vo;
        });

    }



    default List<ShippingDetailRetraceDTO> convertList(List<ShippingDetailDO> list, Map<Long, AdminUserRespDTO> userMap, Map<String, ContractRespDTO> contractMap, Map<String, CompanyDO> companyMap) {
        return CollectionUtils.convertList(list, shippingDetailDO ->
        {

            ShippingDetailRetraceDTO vo = BeanUtils.toBean(shippingDetailDO, ShippingDetailRetraceDTO.class);
            if (StringUtils.isNotBlank(shippingDetailDO.getOutboundBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(shippingDetailDO.getOutboundBy()), a -> vo.setOutboundBy(a.getNickname()));
//            if (StringUtils.isNotBlank(shippingDetailDO.getSignedBy()))
//                MapUtils.findAndThen(userMap, Long.valueOf(shippingDetailDO.getSignedBy()), a -> vo.setSignedBy(a.getNickname()));

//            MapUtils.findAndThen(contractMap, shippingDetailDO.getContractId(), a -> vo.setContractNo(a.getNumber()).setContractName(a.getName()).setCompanyName(companyMap.get(a.getParty()).getName()));
//
            return vo;
        });

    }



    default List<ShippingDetailRespVO> convertList(List<ShippingDetailDO> list, Map<String, MaterialConfigRespDTO> map) {
        return CollectionUtils.convertList(list, shippingDetailDO ->
        {

            ShippingDetailRespVO vo = BeanUtils.toBean(shippingDetailDO, ShippingDetailRespVO.class);
            MapUtils.findAndThen(map, shippingDetailDO.getMaterialConfigId(), a -> vo.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode()).setMaterialConfigId(a.getId())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setMaterialTypeName(a.getMaterialTypeName()).setMaterialParentTypeName(a.getMaterialParentTypeName()));
            return vo;
        });

    }


}
