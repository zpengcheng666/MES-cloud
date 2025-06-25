package com.miyu.cloud.dms.dal.mysql.linestationgroup;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.dms.controller.admin.linestationgroup.vo.LineStationGroupPageReqVO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 产线/工位组 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface LineStationGroupMapper extends BaseMapperX<LineStationGroupDO> {

    default PageResult<LineStationGroupDO> selectPage(LineStationGroupPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<LineStationGroupDO>()
                .likeIfPresent(LineStationGroupDO::getCode, reqVO.getCode())
                .likeIfPresent(LineStationGroupDO::getName, reqVO.getName())
                .eqIfPresent(LineStationGroupDO::getType, reqVO.getType())
                .eqIfPresent(LineStationGroupDO::getEnable, reqVO.getEnable())
                .eqIfPresent(LineStationGroupDO::getAffiliationDeviceType, reqVO.getAffiliationDeviceType())
                .eqIfPresent(LineStationGroupDO::getIp, reqVO.getIp())
                .eqIfPresent(LineStationGroupDO::getLocation, reqVO.getLocation())
                .likeIfPresent(LineStationGroupDO::getRemark, reqVO.getRemark())
                .eqIfPresent(LineStationGroupDO::getCreator, reqVO.getCreator())
                .betweenIfPresent(LineStationGroupDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(LineStationGroupDO::getUpdater, reqVO.getUpdater())
                .orderByDesc(LineStationGroupDO::getId));
    }

}