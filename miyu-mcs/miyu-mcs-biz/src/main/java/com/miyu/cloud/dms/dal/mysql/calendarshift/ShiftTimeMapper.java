package com.miyu.cloud.dms.dal.mysql.calendarshift;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.dms.dal.dataobject.calendarshift.ShiftTimeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 班次时间 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface ShiftTimeMapper extends BaseMapperX<ShiftTimeDO> {

    default List<ShiftTimeDO> selectListByTypeId(String typeId) {
        return selectList(ShiftTimeDO::getTypeId, typeId);
    }

    default int deleteByTypeId(String typeId) {
        return delete(ShiftTimeDO::getTypeId, typeId);
    }

}
