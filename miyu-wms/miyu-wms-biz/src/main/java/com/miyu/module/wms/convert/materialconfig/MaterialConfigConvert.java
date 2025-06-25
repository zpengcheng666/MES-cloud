package com.miyu.module.wms.convert.materialconfig;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.wms.api.mateiral.dto.MCCMaterialConfigReqDTO;
import com.miyu.module.wms.controller.admin.materialconfig.vo.MaterialConfigRespVO;
import com.miyu.module.wms.controller.admin.materialconfig.vo.MaterialConfigSaveReqVO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.util.StringListUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.*;
import java.util.stream.Collectors;

@Mapper
public interface MaterialConfigConvert {

    MaterialConfigConvert INSTANCE = Mappers.getMapper(MaterialConfigConvert.class);

    default List<MaterialConfigRespVO> convertList(List<MaterialConfigDO> list, Map<String, MaterialConfigDO> map) {
        return CollectionUtils.convertList(list, materialConfigDO ->
                {
                    MaterialConfigRespVO materialConfigRespVO = BeanUtils.toBean(materialConfigDO, MaterialConfigRespVO.class);
                    List<String> containerConfigIds = StringListUtils.stringToArrayList(materialConfigDO.getContainerConfigIds());
                    materialConfigRespVO.setContainerConfigIds(containerConfigIds);
                    List<String> containerConfigNumbers = containerConfigIds.stream().map(id -> {
                        if (map.containsKey(id)) {
                            return map.get(id).getMaterialNumber();
                        }
                        return null;
                    }).collect(Collectors.toList());
                    materialConfigRespVO.setContainerConfigNumbers(containerConfigNumbers);
                    return materialConfigRespVO;
                });
    }

    default MaterialConfigSaveReqVO  convertSave(MCCMaterialConfigReqDTO reqDTO){
        MaterialConfigSaveReqVO saveReqVO = new MaterialConfigSaveReqVO();
        saveReqVO.setId(reqDTO.getId());
        saveReqVO.setMaterialNumber(reqDTO.getMaterialNumber());
        saveReqVO.setMaterialName(reqDTO.getMaterialName());
        saveReqVO.setMaterialCode(reqDTO.getMaterialTypeCode());
        saveReqVO.setMaterialType(reqDTO.getMaterialParentTypeCode());
        saveReqVO.setMaterialTypeName(reqDTO.getMaterialParentTypeName());
        saveReqVO.setMaterialSpecification(reqDTO.getMaterialSpecification());
        saveReqVO.setMaterialBrand(reqDTO.getMaterialBrand());
        saveReqVO.setMaterialUnit(reqDTO.getMaterialUnit());
        saveReqVO.setMaterialManage(reqDTO.getMaterialManage());
        saveReqVO.setMaterialSourceId(reqDTO.getMaterialSourceId());
        saveReqVO.setProcessInstanceId(reqDTO.getProcessInstanceId());
        saveReqVO.setStatus(reqDTO.getStatus());
        return saveReqVO;
    }


}
