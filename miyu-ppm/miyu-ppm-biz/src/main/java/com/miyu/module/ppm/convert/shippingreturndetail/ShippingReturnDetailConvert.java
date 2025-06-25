package com.miyu.module.ppm.convert.shippingreturndetail;


import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailRetraceDTO;
import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDetailRetraceDTO;
import com.miyu.module.ppm.controller.admin.shippingreturndetail.vo.ShippingReturnDetailRespVO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturndetail.ShippingReturnDetailDO;
import com.miyu.module.ppm.enums.shippingreturn.ShippingReturnTypeEnum;
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
public interface ShippingReturnDetailConvert {

    ShippingReturnDetailConvert INSTANCE = Mappers.getMapper(ShippingReturnDetailConvert.class);



    default List<ShippingReturnDetailRespVO> convertList(List<ShippingReturnDetailDO> list, Map<Long, AdminUserRespDTO> userMap, Map<String, MaterialConfigRespDTO> map) {
        return CollectionUtils.convertList(list, shippingReturnDetailDO ->
        {

            ShippingReturnDetailRespVO vo = BeanUtils.toBean(shippingReturnDetailDO, ShippingReturnDetailRespVO.class);
            if (StringUtils.isNotBlank(shippingReturnDetailDO.getInboundBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(shippingReturnDetailDO.getInboundBy()), a -> vo.setInboundBy(a.getNickname()));
            if (StringUtils.isNotBlank(shippingReturnDetailDO.getSignedBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(shippingReturnDetailDO.getSignedBy()), a -> vo.setSignedBy(a.getNickname()));

            MapUtils.findAndThen(map, shippingReturnDetailDO.getMaterialConfigId(), a -> vo.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand())
                    .setMaterialTypeName(a.getMaterialTypeName()).setMaterialParentTypeName(a.getMaterialParentTypeName()).setMaterialConfigId(shippingReturnDetailDO.getMaterialConfigId())
            );

            return vo;
        });

    }



    default List<ShippingReturnDetailRetraceDTO> convertList1(List<ShippingReturnDetailDO> list, Map<Long, AdminUserRespDTO> userMap, Map<String, ContractRespDTO> contractMap, Map<String, CompanyRespDTO> companyMap) {
        return CollectionUtils.convertList(list, shippingDetailDO ->
        {

            ShippingReturnDetailRetraceDTO vo = BeanUtils.toBean(shippingDetailDO, ShippingReturnDetailRetraceDTO.class);
            if (StringUtils.isNotBlank(shippingDetailDO.getInboundBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(shippingDetailDO.getInboundBy()), a -> vo.setInboundBy(a.getNickname()));
            if (StringUtils.isNotBlank(shippingDetailDO.getSignedBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(shippingDetailDO.getSignedBy()), a -> vo.setSignedBy(a.getNickname()));

            vo.setReturnTypeName(shippingDetailDO.getReturnType().intValue() ==1?"返修":(shippingDetailDO.getReturnType().intValue()==2?"换货":"退货退款"));
            MapUtils.findAndThen(contractMap, shippingDetailDO.getContractId(), a -> vo.setContractNo(a.getNumber()).setContractName(a.getName()).setCompanyName(companyMap.get(a.getParty()).getName()));

            return vo;
        });

    }
}
