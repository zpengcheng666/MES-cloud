package com.miyu.module.ppm.convert.contractconsignment;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.controller.admin.contractconsignment.vo.ContractConsignmentRespVO;
import com.miyu.module.ppm.controller.admin.contractconsignmentdetail.vo.ContractConsignmentDetailRespVO;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contractconsignment.ContractConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.contractconsignmentdetail.ContractConsignmentDetailDO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContractConsignmentConvert {

    ContractConsignmentConvert INSTANCE = Mappers.getMapper(ContractConsignmentConvert.class);

    default List<ContractConsignmentRespVO> convertList(List<ContractConsignmentDO> list, Map<Long, AdminUserRespDTO> userMap, Map<String, ContractDO> contractMap, Map<String, CompanyDO> companyMap) {
        return CollectionUtils.convertList(list, purchaseConsignmentDO ->
        {
            ContractConsignmentRespVO purchaseConsignmentRespVO = BeanUtils.toBean(purchaseConsignmentDO, ContractConsignmentRespVO.class);
            if (StringUtils.isNotBlank(purchaseConsignmentDO.getConsigner()))
                MapUtils.findAndThen(userMap, Long.valueOf(purchaseConsignmentDO.getConsigner()), a -> purchaseConsignmentRespVO.setConsigner(a.getNickname()));
            ContractDO contractRespDTO  = contractMap.get(purchaseConsignmentDO.getContractId());
            if(contractRespDTO != null){
                purchaseConsignmentRespVO.setContractNo(contractRespDTO.getNumber()).setContractName(contractRespDTO.getName());
                MapUtils.findAndThen(companyMap, contractRespDTO.getParty(), a -> purchaseConsignmentRespVO.setCompanyName(a.getName()));

            }
            return purchaseConsignmentRespVO;
        });

    }


    default List<ContractConsignmentDetailRespVO> convertList(List<ContractConsignmentDetailDO> list, Map<String, MaterialConfigRespDTO> map) {
        return CollectionUtils.convertList(list, detailDO ->
        {
            ContractConsignmentDetailRespVO vo = BeanUtils.toBean(detailDO, ContractConsignmentDetailRespVO.class);
            MapUtils.findAndThen(map, detailDO.getMaterialConfigId(), a -> vo.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setMaterialTypeName(a.getMaterialTypeName()).setMaterialParentTypeName(a.getMaterialParentTypeName()));

            return vo;
        });

    }

}
