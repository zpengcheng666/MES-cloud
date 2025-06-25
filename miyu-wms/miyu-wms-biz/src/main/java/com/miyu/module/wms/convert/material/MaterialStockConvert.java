package com.miyu.module.wms.convert.material;

import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.enums.DictConstants;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface MaterialStockConvert {
    MaterialStockConvert INSTANCE = Mappers.getMapper(MaterialStockConvert.class);

}
