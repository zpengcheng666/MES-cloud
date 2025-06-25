package com.miyu.module.pdm.dal.mysql.device;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.device.vo.DeviceListReqVO;
import com.miyu.module.pdm.dal.dataobject.device.DeviceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * PDM 设备-临时 Mapper
 *
 * @author liuy
 */
@Mapper
public interface DeviceMapper extends BaseMapperX<DeviceDO> {

    default List<DeviceDO> selectList(DeviceListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeviceDO>()
                .likeIfPresent(DeviceDO::getCode, reqVO.getCode())
                .likeIfPresent(DeviceDO::getName, reqVO.getName())
                .eqIfPresent(DeviceDO::getType, reqVO.getType())
                .eq(DeviceDO::getEnable, 1)
                .inIfPresent(DeviceDO::getId, reqVO.getDeviceIds())
                .orderByDesc(DeviceDO::getId));
    }

    default List<DeviceDO> selectListByDeviceIds(Collection<String> deviceIds) {
        return selectList(DeviceDO::getId, deviceIds);
    }


}
