package com.miyu.cloud.dms.dal.mysql.calendar;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.dms.controller.admin.calendar.specialvo.CalendarSpecialPageReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarSpecialDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 基础日历的工作日（特别版，调休节假日等特殊日期） Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface CalendarSpecialMapper extends BaseMapperX<CalendarSpecialDO> {

    default PageResult<CalendarSpecialDO> selectPage(CalendarSpecialPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CalendarSpecialDO>()
                .eqIfPresent(CalendarSpecialDO::getBcid, reqVO.getBcid())
                .eqIfPresent(CalendarSpecialDO::getCsname, reqVO.getCsname())
                .betweenIfPresent(CalendarSpecialDO::getCsdate, reqVO.getCsdate())
                .betweenIfPresent(CalendarSpecialDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CalendarSpecialDO::getId));
    }

    @Delete("delete from dms_calendar_special where bcid = #{id}")
    public void deleteByBasicId(@Param("id") String id);

//    @Delete(
//            "<script>" +
//            "delete from dms_calendar_special where bcid = #{id}" +
//            "and csdate in"+
//            "<foreach item='item' index='index' collection='dateList' open='(' separator=',' close=')'>"+
//                "#{item}"+
//            "</foreach>"+
//            "</script>"
//    )
    @Delete(
        "<script>" +
            "delete from dms_calendar_special where " +
            " csdate in "+
            "<foreach item='item' index='index' collection='dateList' open='(' separator=',' close=')'>"+
            "#{item}"+
            "</foreach>"+
        "</script>"
    )
    public void deleteByDates(@Param("dateList") List<String> dateList);

}
