package com.miyu.module.pdm.convert.dataStatistics;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.DataStatisticsRespVO;
import com.miyu.module.pdm.dal.dataobject.dataStatistics.DataStatisticsDO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface DataStatisticsConvert {
    DataStatisticsConvert INSTANCE = Mappers.getMapper(DataStatisticsConvert.class);

    default List<DataStatisticsRespVO> convertList(List<DataStatisticsDO> list, Map<Long, AdminUserRespDTO> userMap) {
        return CollectionUtils.convertList(list, DataStatisticsDO ->
        {
            DataStatisticsRespVO dataStatisticsRespVO = BeanUtils.toBean(DataStatisticsDO, DataStatisticsRespVO.class);
            if (StringUtils.isNotBlank(DataStatisticsDO.getCreator()))
                MapUtils.findAndThen(userMap, Long.valueOf(DataStatisticsDO.getCreator()), a -> dataStatisticsRespVO.setCreator(a.getNickname()));
            return dataStatisticsRespVO;
        });

    }
}
