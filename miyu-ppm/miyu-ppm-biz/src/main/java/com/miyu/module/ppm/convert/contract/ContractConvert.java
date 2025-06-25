package com.miyu.module.ppm.convert.contract;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.ppm.controller.admin.contract.vo.ContractRespVO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContractConvert {

    ContractConvert INSTANCE = Mappers.getMapper(ContractConvert.class);

    default List<ContractRespVO> convertList(List<ContractDO> list, Map<Long, AdminUserRespDTO> userMap) {
        return CollectionUtils.convertList(list, contractDO ->
                {
                    ContractRespVO contractRespVO = BeanUtils.toBean(contractDO, ContractRespVO.class);
                    if (StringUtils.isNotBlank(contractDO.getSelfContact()))
                        MapUtils.findAndThen(userMap, Long.valueOf(contractDO.getSelfContact()), a -> contractRespVO.setSelfContactName(a.getNickname()));
//                    if (StringUtils.isNotBlank(ContractDO.getUserId()))
//                        MapUtils.findAndThen(userMap, Long.valueOf(warehouseDO.getCreator()), a -> warehouseRespVO.setCreator(a.getNickname()));
//                    if (StringUtils.isNotBlank(ContractDO.getUserId()))
//                        MapUtils.findAndThen(userMap, Long.valueOf(warehouseDO.getUpdater()), a -> warehouseRespVO.setUpdater(a.getNickname()));
                    return contractRespVO;
                });
                /*convert(warehouseDO,
                userMap.get(StringUtils.isBlank(warehouseDO.getUserId())?null:Long.valueOf(warehouseDO.getUserId())),
                userMap.get(StringUtils.isBlank(warehouseDO.getCreator())?null:Long.valueOf(warehouseDO.getCreator())),
                userMap.get(StringUtils.isBlank(warehouseDO.getUpdater())?null:Long.valueOf(warehouseDO.getUpdater()))));*/
    }

//    default ContractRespVO convert(ContractDO warehouseDO, AdminUserRespDTO user, AdminUserRespDTO creator, AdminUserRespDTO updater) {
//        ContractRespVO warehouseRespVO = BeanUtils.toBean(warehouseDO, ContractRespVO.class);
//        if (user != null) {
//            warehouseRespVO.setNickName(user.getNickname());
//        }
//        if (creator != null) {
//            warehouseRespVO.setCreator(creator.getNickname());
//        }
//        if (updater != null) {
//            warehouseRespVO.setUpdater(updater.getNickname());
//        }
//        return warehouseRespVO;
//    }

}
