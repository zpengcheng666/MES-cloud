package com.miyu.cloud.macs.dal.mysql.device;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.miyu.cloud.macs.dal.dataobject.device.DeviceDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.macs.controller.admin.device.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 设备 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceMapper extends BaseMapperX<DeviceDO> {

    default PageResult<DeviceDO> selectPage(DevicePageReqVO reqVO) {
        return selectDevicePage(reqVO, new LambdaQueryWrapperX<DeviceDO>()
                .eqIfPresent(DeviceDO::getCode, reqVO.getCode())
                .likeIfPresent(DeviceDO::getName, reqVO.getName())
                .eqIfPresent(DeviceDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DeviceDO::getRegionId, reqVO.getRegionId())
                .eqIfPresent(DeviceDO::getIp, reqVO.getIp())
                .eqIfPresent(DeviceDO::getPort, reqVO.getPort())
                .eqIfPresent(DeviceDO::getAccountNumber, reqVO.getAccountNumber())
                .eqIfPresent(DeviceDO::getPassword, reqVO.getPassword())
                .eqIfPresent(DeviceDO::getEnableStatus, reqVO.getEnableStatus())
                .eqIfPresent(DeviceDO::getCreateBy, reqVO.getCreateBy())
                .betweenIfPresent(DeviceDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(DeviceDO::getUpdateBy, reqVO.getUpdateBy())
                .orderByDesc(DeviceDO::getId));
    }

    default PageResult<DeviceDO> selectDevicePage(PageParam pageParam, Wrapper<DeviceDO> queryWrapper) {
        if (PageParam.PAGE_SIZE_NONE.equals(pageParam.getPageSize())) {
            List<DeviceDO> list = mySelectList(queryWrapper);
            return new PageResult<>(list, (long) list.size());
        }

        // MyBatis Plus 查询
        IPage<DeviceDO> mpPage = MyBatisUtils.buildPage(pageParam, null);
        IPage<DeviceDO> res = mySelectPage(mpPage, queryWrapper);
        // 转换返回
        return new PageResult<>(res.getRecords(), res.getTotal());
    }

    @Select("SELECT d.*,r.`name` region_name FROM (SELECT*FROM `macs_device` ${ ew.customSqlSegment }) d LEFT JOIN macs_region r ON d.region_id=r.id")
    IPage<DeviceDO> mySelectPage(IPage<DeviceDO> mpPage, @Param("ew") Wrapper<DeviceDO> queryWrapper);

    @Select("SELECT d.*,r.`name` region_name FROM (SELECT*FROM `macs_device` ${ ew.customSqlSegment }) d LEFT JOIN macs_region r ON d.region_id=r.id")
    List<DeviceDO> mySelectList(@Param("ew") Wrapper<DeviceDO> queryWrapper);
}
