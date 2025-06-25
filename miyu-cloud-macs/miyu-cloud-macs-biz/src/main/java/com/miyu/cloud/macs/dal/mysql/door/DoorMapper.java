package com.miyu.cloud.macs.dal.mysql.door;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.miyu.cloud.macs.dal.dataobject.door.DoorDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.macs.controller.admin.door.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 门 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DoorMapper extends BaseMapperX<DoorDO> {

    @Select("SELECT * FROM (SELECT d.*,r.`name` regionName,device.`name` deviceName FROM `macs_door` d LEFT JOIN macs_region r ON d.region_id=r.id LEFT JOIN macs_device device ON d.device_id=device.id) c ${ew.customSqlSegment} ")
    IPage<DoorDO> selectPageList(IPage<DoorDO> page, @Param("ew") Wrapper<DoorDO> queryWrapper);

}