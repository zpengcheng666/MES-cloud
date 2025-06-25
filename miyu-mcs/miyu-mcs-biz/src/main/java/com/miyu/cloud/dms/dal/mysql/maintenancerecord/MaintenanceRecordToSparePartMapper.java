package com.miyu.cloud.dms.dal.mysql.maintenancerecord;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.dms.dal.dataobject.maintenancerecord.MaintenanceRecordToSparePartDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MaintenanceRecordToSparePartMapper extends BaseMapperX<MaintenanceRecordToSparePartDO> {
}
