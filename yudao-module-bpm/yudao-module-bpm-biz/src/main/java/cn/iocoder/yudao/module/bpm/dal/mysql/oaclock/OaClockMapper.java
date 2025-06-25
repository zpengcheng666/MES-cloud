package cn.iocoder.yudao.module.bpm.dal.mysql.oaclock;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.bpm.controller.admin.oaclock.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oaclock.OaClockDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * OA 打卡 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OaClockMapper extends BaseMapperX<OaClockDO> {

    default PageResult<OaClockDO> selectPage(OaClockPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaClockDO>()
                .likeIfPresent(OaClockDO::getName, reqVO.getName())
                .eqIfPresent(OaClockDO::getType, reqVO.getType())
                .eqIfPresent(OaClockDO::getDept, reqVO.getDept())
                .eqIfPresent(OaClockDO::getJob, reqVO.getJob())
                .betweenIfPresent(OaClockDO::getClockTime, reqVO.getClockTime())
                .eqIfPresent(OaClockDO::getReason, reqVO.getReason())
                .eqIfPresent(OaClockDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OaClockDO::getClockStatus, reqVO.getClockStatus())
                .eqIfPresent(OaClockDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .betweenIfPresent(OaClockDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OaClockDO::getId));
    }

}
