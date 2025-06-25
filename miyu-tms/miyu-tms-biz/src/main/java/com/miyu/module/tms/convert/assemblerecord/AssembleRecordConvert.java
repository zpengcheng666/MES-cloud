package com.miyu.module.tms.convert.assemblerecord;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.tms.controller.admin.assembletask.vo.AssembleRecordRespVO;
import com.miyu.module.tms.controller.admin.toolinfo.vo.AssembleRecordVO;
import com.miyu.module.tms.controller.admin.toolinfo.vo.ToolInfoRespVO;
import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigRespDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Mapper
public interface AssembleRecordConvert {

    AssembleRecordConvert INSTANCE = Mappers.getMapper(AssembleRecordConvert.class);

    default List<AssembleRecordRespVO> toAssembleRecordRespVO(List<AssembleRecordDO> assembleRecordS, Map<String, MaterialStockRespDTO> materialMap, Map<String, MaterialConfigRespDTO> materialConfigMap) {
        if (assembleRecordS == null || assembleRecordS.isEmpty()) {
            return Collections.emptyList();
        }

        List<AssembleRecordRespVO> result = new ArrayList<>();
        for (AssembleRecordDO assembleRecordDO : assembleRecordS) {
            AssembleRecordRespVO assembleRecordRespVO = BeanUtils.toBean(assembleRecordDO, AssembleRecordRespVO.class);

            if (materialMap != null && materialMap.containsKey(assembleRecordDO.getMaterialStockId())) {
                MaterialStockRespDTO materialStockRespDTO = materialMap.get(assembleRecordDO.getMaterialStockId());
                assembleRecordRespVO.setAppendageBarCode(materialStockRespDTO.getBarCode());

                if (materialConfigMap != null && materialConfigMap.containsKey(materialStockRespDTO.getMaterialConfigId())) {
                    MaterialConfigRespDTO materialConfigRespDTO = materialConfigMap.get(materialStockRespDTO.getMaterialConfigId());
                    assembleRecordRespVO.setAppendageMaterialNumber(materialConfigRespDTO.getMaterialNumber());
                    assembleRecordRespVO.setAppendageMaterialCode(materialConfigRespDTO.getMaterialNumber());
                    assembleRecordRespVO.setAppendageMaterialName(materialConfigRespDTO.getMaterialName());
                }
            }

            result.add(assembleRecordRespVO);
        }

        return result;
    }

    default List<AssembleRecordVO> toAssembleRecordVO(List<AssembleRecordDO> assembleRecordS, Map<String, MaterialStockRespDTO> materialMap, Map<String, MaterialConfigRespDTO> materialConfigMap) {
        if (assembleRecordS == null || assembleRecordS.isEmpty()) {
            return Collections.emptyList();
        }

        List<AssembleRecordVO> result = new ArrayList<>();
        for (AssembleRecordDO assembleRecordDO : assembleRecordS) {
            AssembleRecordVO assembleRecordRespVO = BeanUtils.toBean(assembleRecordDO, AssembleRecordVO.class);

            if (materialMap != null && materialMap.containsKey(assembleRecordDO.getMaterialStockId())) {
                MaterialStockRespDTO materialStockRespDTO = materialMap.get(assembleRecordDO.getMaterialStockId());
                assembleRecordRespVO.setAppendageBarCode(materialStockRespDTO.getBarCode());

                if (materialConfigMap != null && materialConfigMap.containsKey(materialStockRespDTO.getMaterialConfigId())) {
                    MaterialConfigRespDTO materialConfigRespDTO = materialConfigMap.get(materialStockRespDTO.getMaterialConfigId());
                    assembleRecordRespVO.setAppendageMaterialNumber(materialConfigRespDTO.getMaterialNumber());
                    assembleRecordRespVO.setAppendageMaterialCode(materialConfigRespDTO.getMaterialNumber());
                    assembleRecordRespVO.setAppendageMaterialName(materialConfigRespDTO.getMaterialName());
                }
            }

            result.add(assembleRecordRespVO);
        }

        return result;
    }
    default ToolInfoRespVO toToolInfoAndRecord(ToolInfoDO toolInfoDO, Map<String, MaterialStockRespDTO> stockRespDTOMap, Map<String, MaterialConfigRespDTO> materialConfigMap, List<AssembleRecordDO> assembleRecordDOS) {
        ToolInfoRespVO respVO = BeanUtils.toBean(toolInfoDO, ToolInfoRespVO.class);
        List<AssembleRecordVO> toolHandleList = new ArrayList<>();
        List<AssembleRecordVO> toolHeadList = new ArrayList<>();
        List<AssembleRecordVO> toolAccessoryList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(assembleRecordDOS)){
            for (AssembleRecordDO recordDO :assembleRecordDOS){
                AssembleRecordVO assembleRecordVO = BeanUtils.toBean(recordDO,AssembleRecordVO.class);
                MaterialStockRespDTO stockRespDTO = stockRespDTOMap.get(recordDO.getMaterialStockId());
                if (stockRespDTO == null){
                    continue;
                }
                MaterialConfigRespDTO materialConfigRespDTO = materialConfigMap.get(stockRespDTO.getMaterialConfigId());
                if (materialConfigRespDTO == null){
                    continue;
                }
                assembleRecordVO.setId(assembleRecordVO.getId());
                assembleRecordVO.setAppendageBarCode(stockRespDTO.getBarCode());
                assembleRecordVO.setAppendageMaterialCode(materialConfigRespDTO.getMaterialCode());
                assembleRecordVO.setAppendageMaterialNumber(materialConfigRespDTO.getMaterialNumber());
                assembleRecordVO.setAppendageMaterialName(materialConfigRespDTO.getMaterialName());
                if (materialConfigRespDTO.getMaterialCode().equals("PJ")){
                    toolAccessoryList.add(assembleRecordVO);
                }else if (materialConfigRespDTO.getMaterialCode().equals("DT") || materialConfigRespDTO.getMaterialCode().equals("DP")){
                    toolHeadList.add(assembleRecordVO);
                }else {
                    toolHandleList.add(assembleRecordVO);
                }

            }
        }
        respVO.setToolHandleList(toolHandleList);
        respVO.setToolHeadList(toolHeadList);
        respVO.setToolAccessoryList(toolAccessoryList);

        return respVO;
    }
}
