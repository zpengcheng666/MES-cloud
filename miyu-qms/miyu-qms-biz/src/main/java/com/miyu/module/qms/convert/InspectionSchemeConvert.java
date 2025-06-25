package com.miyu.module.qms.convert;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.pdm.api.processPlanDetail.dto.ProcedureRespDTO;
import com.miyu.module.qms.controller.admin.inspectionscheme.vo.InspectionSchemeRespVO;
import com.miyu.module.qms.dal.dataobject.inspectionscheme.InspectionSchemeDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface InspectionSchemeConvert {

    InspectionSchemeConvert INSTANCE = Mappers.getMapper(InspectionSchemeConvert.class);



    default List<InspectionSchemeRespVO> convertList(List<InspectionSchemeDO> list,Map<String, ProcedureRespDTO> map, Map<String, MaterialConfigRespDTO> materialConfigMap) {
        return CollectionUtils.convertList(list, schemeDO ->
        {
            InspectionSchemeRespVO respVO = BeanUtils.toBean(schemeDO, InspectionSchemeRespVO.class);
            MapUtils.findAndThen(map, schemeDO.getProcessId(), a -> respVO.setProcessName(a.getProcessName()).setProcedureName(a.getProcedureName())
            .setProcessCode(a.getProcessCode()).setProcedureNum(a.getProcedureNum()));

            MapUtils.findAndThen(materialConfigMap, schemeDO.getMaterialConfigId(), a -> respVO.setMaterialTypeName(a.getMaterialTypeName()).setMaterialSpecification(a.getMaterialSpecification()).setMaterialUnit(a.getMaterialUnit()));
            return respVO;
        });

    }
}
