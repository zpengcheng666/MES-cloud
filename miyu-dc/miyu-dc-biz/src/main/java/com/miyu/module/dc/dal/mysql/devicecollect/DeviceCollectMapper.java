package com.miyu.module.dc.dal.mysql.devicecollect;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.miyu.module.dc.dal.dataobject.devicecollect.DeviceCollectDO;
import com.miyu.module.dc.dal.dataobject.producttype.ProductTypeDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface DeviceCollectMapper extends BaseMapperX<DeviceCollectDO> {

    default List<DeviceCollectDO> getProductTypeByDeviceId(String deviceId){
        MPJLambdaWrapperX<DeviceCollectDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(DeviceCollectDO::getDeviceId,deviceId)
                .selectAll(DeviceCollectDO.class);
        return selectList(wrapperX);
    };

    /**
     * 物理删除设备采集类型数据
     */
    @Delete("delete from dc_device_collect where device_id = #{id} ")
    Integer deleteAll(@Param("id") String id);

    /**
     * 根据topic查询数据
     */
    @TenantIgnore
    default List<DeviceCollectDO> queryDeviceCollectByTopic(String id ,String deviceId){
        MPJLambdaWrapperX<DeviceCollectDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(DeviceCollectDO::getTopicId,id).eq(DeviceCollectDO::getDeviceId,deviceId)
                .selectAll(DeviceCollectDO.class);
        return selectList(wrapperX);
    }

    @TenantIgnore
    default DeviceCollectDO queryDeviceByTopicId(String id){
        MPJLambdaWrapper<DeviceCollectDO> wrapperX = new MPJLambdaWrapper<>();
        wrapperX.eq(DeviceCollectDO::getTopicId,id);
        return selectOne(wrapperX);
    }

    /**
     * 根据主键查询类型Id
     * @param id
     * @return
     */
    default List<String> queryById(String id){
        MPJLambdaWrapper<DeviceCollectDO> wrapperX = new MPJLambdaWrapper<>();
        wrapperX.eq(DeviceCollectDO::getDeviceId,id);
        return selectList(wrapperX).stream().map(DeviceCollectDO::getProductTypeId).collect(Collectors.toList());
    }

    /**
     * 根据设备+产品查询topicId
     */
    default String queryTopic(String DeviceId , String productTypeId){
        MPJLambdaWrapper<DeviceCollectDO> wrapperX = new MPJLambdaWrapper<>();
        wrapperX.eq(DeviceCollectDO::getDeviceId,DeviceId).eq(DeviceCollectDO::getProductTypeId,productTypeId);
        return selectOne(wrapperX).getTopicId();
    }

    default List<DeviceCollectDO> getProductById(String id){
        return selectList(new LambdaQueryWrapper<DeviceCollectDO>()
                .eq(DeviceCollectDO::getProductTypeId, id)
        );
    }



}
