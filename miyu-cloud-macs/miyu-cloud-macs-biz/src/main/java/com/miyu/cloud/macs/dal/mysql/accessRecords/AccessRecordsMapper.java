package com.miyu.cloud.macs.dal.mysql.accessRecords;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.macs.dal.dataobject.accessRecords.AccessRecordsDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.macs.controller.admin.accessRecords.vo.*;

/**
 * 通行记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AccessRecordsMapper extends BaseMapperX<AccessRecordsDO> {

    default PageResult<AccessRecordsDO> selectPage(AccessRecordsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AccessRecordsDO>()
                .eqIfPresent(AccessRecordsDO::getUserId, reqVO.getUserId())
                .eqIfPresent(AccessRecordsDO::getVisitorId, reqVO.getVisitorId())
                .eqIfPresent(AccessRecordsDO::getUserCode, reqVO.getUserCode())
                .likeIfPresent(AccessRecordsDO::getUserName, reqVO.getUserName())
                .eqIfPresent(AccessRecordsDO::getOperatorId, reqVO.getOperatorId())
                .eqIfPresent(AccessRecordsDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(AccessRecordsDO::getDoorId, reqVO.getDoorId())
                .likeIfPresent(AccessRecordsDO::getDoorName, reqVO.getDoorName())
                .eqIfPresent(AccessRecordsDO::getCollectorId, reqVO.getCollectorId())
                .likeIfPresent(AccessRecordsDO::getCollectorName, reqVO.getCollectorName())
                .eqIfPresent(AccessRecordsDO::getCollectorCode, reqVO.getCollectorCode())
                .eqIfPresent(AccessRecordsDO::getRegionId, reqVO.getRegionId())
                .likeIfPresent(AccessRecordsDO::getRegionName, reqVO.getRegionName())
                .eqIfPresent(AccessRecordsDO::getAction, reqVO.getAction())
                .eqIfPresent(AccessRecordsDO::getMessage, reqVO.getMessage())
                .betweenIfPresent(AccessRecordsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AccessRecordsDO::getId));
    }

}