package com.miyu.module.es.dal.mysql.visit;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.miyu.module.es.dal.dataobject.visit.VisitDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.es.controller.admin.visit.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 访客记录 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface VisitMapper extends BaseMapperX<VisitDO> {

    @TenantIgnore
    @Select("select * FROM `es_visit` WHERE visit_record_id = #{id}")
    VisitDO selectByVisitRecordId(@Param("id") String id);


    @TenantIgnore
    default PageResult<VisitDO> selectPage(VisitPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VisitDO>()
                .likeIfPresent(VisitDO::getName, reqVO.getName())
                .betweenIfPresent(VisitDO::getVisitorCancelTime, reqVO.getVisitorCancelTime())
                .eqIfPresent(VisitDO::getStatus, reqVO.getStatus())
                .eqIfPresent(VisitDO::getCompany, reqVO.getCompany())
                .eqIfPresent(VisitDO::getCause, reqVO.getCause())
                .eqIfPresent(VisitDO::getFollowCount, reqVO.getFollowCount())
                .betweenIfPresent(VisitDO::getPlanBeginTime, reqVO.getPlanBeginTime())
                .betweenIfPresent(VisitDO::getPlanEndTime, reqVO.getPlanEndTime())
                .betweenIfPresent(VisitDO::getVisitorRecordTime, reqVO.getVisitorRecordTime())
                .eqIfPresent(VisitDO::getVisitorCheckCode, reqVO.getVisitorCheckCode())
                .eqIfPresent(VisitDO::getVisitTpId, reqVO.getVisitTpId())
                .eqIfPresent(VisitDO::getDeviceSn, reqVO.getDeviceSn()));
    }

    @TenantIgnore
    @Update(" UPDATE `es_visit` SET visitor_cancel_time = #{visitorCancelTime},status = #{status},visitor_record_time=#{visitorRecordTime},device_sn=#{deviceSn} WHERE visit_record_id = #{visitRecordId} AND deleted = 0  ")
    void updateByVisitRecordId(@Param("visitRecordId") String visitRecordId,
                               @Param("visitorCancelTime") LocalDateTime visitorCancelTime,
                               @Param("status") Integer status,
                               @Param("visitorRecordTime") LocalDateTime VisitorRecordTime,
                               @Param("deviceSn") String deviceSn);
}