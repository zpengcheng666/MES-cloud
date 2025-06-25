package com.miyu.cloud.dms.dal.mysql.devicetype;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.dms.controller.admin.devicetype.vo.DeviceTypePageReqVO;
import com.miyu.cloud.dms.dal.dataobject.devicetype.DeviceTypeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备类型 Mapper
 *
 * @author 王正浩
 */
@Mapper
public interface DeviceTypeMapper extends BaseMapperX<DeviceTypeDO> {

    default PageResult<DeviceTypeDO> selectPage(DeviceTypePageReqVO reqVO) {
        return selectPage(reqVO,
                new LambdaQueryWrapperX<DeviceTypeDO>()
                        .likeIfPresent(DeviceTypeDO::getCode, reqVO.getCode())
                        .likeIfPresent(DeviceTypeDO::getName, reqVO.getName())
                        .eqIfPresent(DeviceTypeDO::getType, reqVO.getType())
                        .eqIfPresent(DeviceTypeDO::getEnable, reqVO.getEnable())
                        .likeIfPresent(DeviceTypeDO::getSpecification, reqVO.getSpecification())
                        .likeIfPresent(DeviceTypeDO::getManufacturer, reqVO.getManufacturer())
                        .likeIfPresent(DeviceTypeDO::getCountryRegion, reqVO.getCountryRegion())
                        .likeIfPresent(DeviceTypeDO::getContacts, reqVO.getContacts())
                        .likeIfPresent(DeviceTypeDO::getContactPhone, reqVO.getContactPhone())
                        .likeIfPresent(DeviceTypeDO::getRemark, reqVO.getRemark())
                        .likeIfPresent(DeviceTypeDO::getCreator, reqVO.getCreator())
                        .betweenIfPresent(DeviceTypeDO::getCreateTime, reqVO.getCreateTime())
                        .likeIfPresent(DeviceTypeDO::getUpdater, reqVO.getUpdater())
                        .betweenIfPresent(DeviceTypeDO::getUpdateTime, reqVO.getUpdateTime())
                        .orderByDesc(DeviceTypeDO::getId)
        );
    }

    default DeviceTypeDO getEgId() {
        return selectOne(DeviceTypeDO::getCode, "EG");
    }

}