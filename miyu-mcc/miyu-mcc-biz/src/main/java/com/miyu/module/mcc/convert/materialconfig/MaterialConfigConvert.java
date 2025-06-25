package com.miyu.module.mcc.convert.materialconfig;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.mcc.controller.admin.materialconfig.vo.MaterialConfigRespVO;
import com.miyu.module.mcc.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.mcc.utils.StringListUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface MaterialConfigConvert {

    MaterialConfigConvert INSTANCE = Mappers.getMapper(MaterialConfigConvert.class);


}
