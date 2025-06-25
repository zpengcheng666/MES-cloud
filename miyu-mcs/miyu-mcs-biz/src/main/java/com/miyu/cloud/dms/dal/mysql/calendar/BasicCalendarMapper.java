package com.miyu.cloud.dms.dal.mysql.calendar;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.dms.controller.admin.calendar.vo.BasicCalendarPageReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.BasicCalendarDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基础日历 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface BasicCalendarMapper extends BaseMapperX<BasicCalendarDO> {

    default PageResult<BasicCalendarDO> selectPage(BasicCalendarPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BasicCalendarDO>()
                .likeIfPresent(BasicCalendarDO::getName, reqVO.getName())
                .betweenIfPresent(BasicCalendarDO::getStartDate, reqVO.getStartDate())
                .betweenIfPresent(BasicCalendarDO::getEndDate, reqVO.getEndDate())
                .orderByDesc(BasicCalendarDO::getId));
    }

}
