package com.miyu.cloud.macs.dal.mysql.collector;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.miyu.cloud.macs.dal.dataobject.collector.CollectorDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.macs.controller.admin.collector.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * (通行卡,人脸,指纹)采集器 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CollectorMapper extends BaseMapperX<CollectorDO> {

    @Select("SELECT mc.*,mde.`name` deviceName,mdo.`name` doorName,mr.`name` regionName " +
            "FROM (SELECT*FROM `macs_collector` ${ew.customSqlSegment}) mc " +
            "LEFT JOIN macs_device mde ON mc.device_id=mde.id " +
            "LEFT JOIN macs_door mdo ON mc.door_id=mdo.id " +
            "LEFT JOIN macs_region mr ON mdo.region_id=mr.id")
    IPage<CollectorDO> selectPageList(IPage<CollectorDO> page, @Param("ew") Wrapper<CollectorDO> queryWrapper);
}