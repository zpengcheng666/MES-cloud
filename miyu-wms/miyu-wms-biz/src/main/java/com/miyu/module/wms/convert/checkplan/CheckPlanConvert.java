package com.miyu.module.wms.convert.checkplan;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.wms.controller.admin.checkplan.vo.CheckPlanRespVO;
import com.miyu.module.wms.dal.dataobject.checkplan.CheckPlanDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.util.StringListUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface CheckPlanConvert {

    CheckPlanConvert INSTANCE = Mappers.getMapper(CheckPlanConvert.class);

    default List<CheckPlanRespVO> convertList(List<CheckPlanDO> list, Map<String, MaterialConfigDO> map) {
        return CollectionUtils.convertList(list, checkPlanDO ->
                {
                    CheckPlanRespVO checkPlanRespVO = BeanUtils.toBean(checkPlanDO, CheckPlanRespVO.class);
                    List<String> containerConfigIds = StringListUtils.stringToArrayList(checkPlanDO.getMaterialConfigIds());
                    checkPlanRespVO.setMaterialConfigIds(containerConfigIds);
                    List<String> materialConfigNumbers = containerConfigIds.stream().map(id -> {
                        if (map.containsKey(id)) {
                            return map.get(id).getMaterialNumber();
                        }
                        return null;
                    }).collect(Collectors.toList());
                    checkPlanRespVO.setMaterialConfigNumbers(materialConfigNumbers);
                    return checkPlanRespVO;
                });
    }


}
