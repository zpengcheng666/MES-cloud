package com.miyu.module.ppm.convert.inboundexceptionhandling;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.ppm.controller.admin.inboundexceptionhandling.vo.InboundExceptionHandlingRespVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingRespVO;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.inboundexceptionhandling.InboundExceptionHandlingDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExceptionHandlingConvert {

    ExceptionHandlingConvert INSTANCE = Mappers.getMapper(ExceptionHandlingConvert.class);

    default List<InboundExceptionHandlingRespVO> convertList(List<InboundExceptionHandlingDO> list,  Map<String, ContractDO> contractMap, Map<String, CompanyDO> companyMap, Map<String, PmsApprovalDto> pmsApprovalDtoMap) {
        return CollectionUtils.convertList(list, shippingDO ->
        {
            InboundExceptionHandlingRespVO shippingRespVO = BeanUtils.toBean(shippingDO, InboundExceptionHandlingRespVO.class);
             ContractDO contractRespDTO = contractMap.get(shippingDO.getContractId());
            if (contractRespDTO != null) {
                shippingRespVO.setContractNo(contractRespDTO.getNumber()).setContractName(contractRespDTO.getName());
                MapUtils.findAndThen(companyMap, contractRespDTO.getParty(), a -> shippingRespVO.setCompanyName(a.getName()));

            }
            MapUtils.findAndThen(pmsApprovalDtoMap, shippingRespVO.getProjectId(), a -> shippingRespVO.setProjectName(a.getProjectName()).setProjectCode(a.getProjectCode()));

            return shippingRespVO;
        });

    }

}
