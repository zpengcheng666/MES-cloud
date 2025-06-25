package com.miyu.cloud.macs.dal.mysql.accessApplication;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.macs.dal.dataobject.accessApplication.AccessApplicationDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.macs.controller.admin.accessApplication.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 通行申请 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AccessApplicationMapper extends BaseMapperX<AccessApplicationDO> {

    default PageResult<AccessApplicationDO> selectPage(AccessApplicationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AccessApplicationDO>()
                .eqIfPresent(AccessApplicationDO::getApplicationNumber, reqVO.getApplicationNumber())
                .eqIfPresent(AccessApplicationDO::getAgent, reqVO.getAgent())
                .eqIfPresent(AccessApplicationDO::getOrganization, reqVO.getOrganization())
                .eqIfPresent(AccessApplicationDO::getDepartment, reqVO.getDepartment())
                .eqIfPresent(AccessApplicationDO::getReason, reqVO.getReason())
                .eqIfPresent(AccessApplicationDO::getStatus, reqVO.getStatus())
                .eqIfPresent(AccessApplicationDO::getCreateBy, reqVO.getCreateBy())
                .betweenIfPresent(AccessApplicationDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AccessApplicationDO::getUpdateBy, reqVO.getUpdateBy())
                .orderByDesc(AccessApplicationDO::getId));
    }

    @Select("SELECT aa.*FROM macs_access_application aa LEFT JOIN ( " +
            "SELECT*FROM macs_visitor_region WHERE visitor_id= #{visitorId} ) vr ON vr.application_id=aa.id WHERE vr.id IS NOT NULL GROUP BY aa.id")
    List<AccessApplicationDO> selectListByVisitor(@Param("visitorId") String visitorId);

    @Select("SELECT*FROM `macs_access_application` aa LEFT JOIN (" +
            "SELECT*FROM macs_visitor_region WHERE invalid_date> NOW() AND visitor_id= #{visitorId} ) vr ON aa.id=vr.application_id " +
            "WHERE aa.`status`=2 AND deleted=0 AND vr.id IS NOT NULL GROUP BY aa.id")
    List<AccessApplicationDO> effectiveListByVisitor(@Param("visitorId") String visitorId);
}
