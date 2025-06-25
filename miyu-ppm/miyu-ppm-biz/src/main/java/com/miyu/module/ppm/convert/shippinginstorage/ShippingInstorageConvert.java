package com.miyu.module.ppm.convert.shippinginstorage;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentRespVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ContractOrderRespVO;
import com.miyu.module.ppm.controller.admin.shippinginstorage.vo.ShippingInstorageRespVO;
import com.miyu.module.ppm.controller.admin.shippinginstoragedetail.vo.ShippingInstorageDetailRespVO;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippinginstorage.ShippingInstorageDO;
import com.miyu.module.ppm.dal.dataobject.shippinginstoragedetail.ShippingInstorageDetailDO;
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
public interface ShippingInstorageConvert {

    ShippingInstorageConvert INSTANCE = Mappers.getMapper(ShippingInstorageConvert.class);

    default List<PurchaseConsignmentRespVO> convertList(List<ConsignmentDO> list, Map<Long, AdminUserRespDTO> userMap, Map<String, ContractDO> contractMap, Map<String, CompanyDO> companyMap,
                                                        Map<String, PmsApprovalDto> projectMap) {
        return CollectionUtils.convertList(list, shippingDO ->
        {
            PurchaseConsignmentRespVO shippingRespVO = BeanUtils.toBean(shippingDO, PurchaseConsignmentRespVO.class);
            if (StringUtils.isNotBlank(shippingDO.getConsignedBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(shippingDO.getConsignedBy()), a -> shippingRespVO.setConsignedBy(a.getNickname()));
            ContractDO contractRespDTO = contractMap.get(shippingDO.getContractId());
            if (contractRespDTO != null) {
                shippingRespVO.setContractNo(contractRespDTO.getNumber()).setContractName(contractRespDTO.getName());

            }
            MapUtils.findAndThen(companyMap, shippingDO.getCompanyId(), a -> shippingRespVO.setCompanyName(a.getName()));
            MapUtils.findAndThen(projectMap, shippingDO.getProjectId(), a -> shippingRespVO.setProjectName(a.getProjectName()));

            return shippingRespVO;
        });

    }


    default List<ShippingInstorageDetailRespVO> convertList(List<ShippingInstorageDetailDO> list, Map<String, MaterialConfigRespDTO> materialMap) {
        return CollectionUtils.convertList(list, shippingDO ->
        {
            ShippingInstorageDetailRespVO shippingRespVO = BeanUtils.toBean(shippingDO, ShippingInstorageDetailRespVO.class);

            MapUtils.findAndThen(materialMap, shippingDO.getMaterialId(), a -> shippingRespVO.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setMaterialTypeName(a.getMaterialTypeName()).setMaterialParentTypeName(a.getMaterialParentTypeName()));

            return shippingRespVO;
        });

    }

}
