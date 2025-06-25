package com.miyu.module.dc.dal.mysql.device;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.dc.dal.dataobject.device.DeviceDO;
import com.miyu.module.dc.dal.dataobject.devicecollect.DeviceCollectDO;
import com.miyu.module.dc.dal.dataobject.mqtt.ReceiveDO;
import com.miyu.module.dc.dal.dataobject.producttype.ProductTypeDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.dc.controller.admin.device.vo.*;

/**
 * 设备 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceMapper extends BaseMapperX<DeviceDO> {

    @Delete("delete from dc_device where id = #{id}")
    void deleteDeviceById(String id);

    default PageResult<DeviceDO> selectPage(DevicePageReqVO reqVO) {

        MPJLambdaWrapperX<DeviceDO> wrapperX = new MPJLambdaWrapperX<>();

        wrapperX.selectAll(DeviceDO.class);
        return selectPage(reqVO, wrapperX
                .likeIfPresent(DeviceDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(DeviceDO::getCommType, reqVO.getCommType())
                .betweenIfPresent(DeviceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceDO::getId));

    }


    default DeviceDO getUserName(String id){
        return selectOne(DeviceDO::getId, id);
    }

    @TenantIgnore
    default String getIdByDeviceId(String deviceId){ return selectOne(DeviceDO::getDeviceId, deviceId).getId(); }

    default List<DeviceDO> selectByDeviceTypeID(String deviceTypeId){
        return selectList(DeviceDO::getDeviceTypeId, deviceTypeId);
    }

    @TenantIgnore
    default DeviceDO selectDeviceById(String id){return selectOne(DeviceDO::getId, id);}

}