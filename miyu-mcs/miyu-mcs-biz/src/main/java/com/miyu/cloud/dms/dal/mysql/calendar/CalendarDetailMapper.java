package com.miyu.cloud.dms.dal.mysql.calendar;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.dms.controller.admin.calendar.detailvo.CalendarDetailPageReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarDetailDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 基础日历的工作日 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface CalendarDetailMapper extends BaseMapperX<CalendarDetailDO> {

    default PageResult<CalendarDetailDO> selectPage(CalendarDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CalendarDetailDO>()
                .eqIfPresent(CalendarDetailDO::getBcid, reqVO.getBcid())
                .eqIfPresent(CalendarDetailDO::getCdname, reqVO.getCdname())
                .betweenIfPresent(CalendarDetailDO::getCddate, reqVO.getCddate())
                .betweenIfPresent(CalendarDetailDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CalendarDetailDO::getId));
    }

    @Delete("delete from dms_calendar_detail where bcid = #{id}")
    public void deleteByBasicId(@Param("id") String id);

}
