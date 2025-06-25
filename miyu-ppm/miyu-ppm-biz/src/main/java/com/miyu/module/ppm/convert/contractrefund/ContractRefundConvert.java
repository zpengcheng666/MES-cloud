package com.miyu.module.ppm.convert.contractrefund;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.ppm.controller.admin.contractrefund.vo.ContractRefundRespVO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.contractrefund.ContractRefundDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturn.ShippingReturnDO;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContractRefundConvert {

    ContractRefundConvert INSTANCE = Mappers.getMapper(ContractRefundConvert.class);

    default List<ContractRefundRespVO> convertList(List<ContractRefundDO> list,Map<String, ContractRespDTO> contractMap, Map<String, CompanyRespDTO> companyMap, Map<String, ConsignmentDO> shippingReturnMap) {
        return CollectionUtils.convertList(list, contractRefundDO ->
        {
            ContractRefundRespVO contractRefundRespVO = BeanUtils.toBean(contractRefundDO, ContractRefundRespVO.class);

            ContractRespDTO contractRespDTO = contractMap.get(contractRefundDO.getContractId());
            if (contractRespDTO != null) {
                contractRefundRespVO.setContractNo(contractRespDTO.getNumber()).setContractName(contractRespDTO.getName());
                MapUtils.findAndThen(companyMap, contractRespDTO.getParty(), a -> contractRefundRespVO.setCompanyName(a.getName()));

            }
            if (!StringUtils.isEmpty(contractRefundDO.getShippingReturnId())){
                MapUtils.findAndThen(shippingReturnMap, contractRefundRespVO.getShippingReturnId(), a -> contractRefundRespVO.setShippingReturnNo(a.getNo()));

            }
            return contractRefundRespVO;
        });

    }


}
