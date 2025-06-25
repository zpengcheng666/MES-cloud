package com.miyu.cloud.dms.dal.mysql.calendarshift;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.miyu.cloud.dms.controller.admin.calendarshift.vo.ShiftTypePageReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.BasicCalendarDO;
import com.miyu.cloud.dms.dal.dataobject.calendarshift.ShiftTypeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 班次类型 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface ShiftTypeMapper extends BaseMapperX<ShiftTypeDO> {

    default PageResult<ShiftTypeDO> selectPage(ShiftTypePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShiftTypeDO>()
                .likeIfPresent(ShiftTypeDO::getName, reqVO.getName())
                .betweenIfPresent(ShiftTypeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ShiftTypeDO::getId));
    }

    default PageResult<ShiftTypeDO> selectPageWithBasic(ShiftTypePageReqVO reqVO) {
        MPJLambdaWrapper<ShiftTypeDO> wrapper = JoinWrappers.lambda(ShiftTypeDO.class)
                .selectAll(ShiftTypeDO.class)
                .leftJoin(BasicCalendarDO.class,BasicCalendarDO::getId,ShiftTypeDO::getBcid)
                .selectAs(BasicCalendarDO::getStartDate,ShiftTypeDO::getStartDate)
                .selectAs(BasicCalendarDO::getEndDate,ShiftTypeDO::getEndDate)
                .orderByDesc(ShiftTypeDO::getId);
        return selectJoinPage(reqVO,ShiftTypeDO.class,wrapper);
    }

}
