package com.miyu.cloud.macs.dal.mysql.visitor;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miyu.cloud.macs.dal.dataobject.visitor.VisitorDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.macs.controller.admin.visitor.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 申请角色 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface VisitorMapper extends BaseMapperX<VisitorDO> {

    default PageResult<VisitorDO> selectPage(VisitorPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VisitorDO>()
                .eqIfPresent(VisitorDO::getIdCard, reqVO.getIdCard())
                .likeIfPresent(VisitorDO::getName, reqVO.getName())
                .eqIfPresent(VisitorDO::getAvatar, reqVO.getAvatar())
                .eqIfPresent(VisitorDO::getSex, reqVO.getSex())
                .eqIfPresent(VisitorDO::getPhone, reqVO.getPhone())
                .eqIfPresent(VisitorDO::getStatus, reqVO.getStatus())
                .eqIfPresent(VisitorDO::getOrganization, reqVO.getOrganization())
                .eqIfPresent(VisitorDO::getDepartment, reqVO.getDepartment())
                .eqIfPresent(VisitorDO::getRegionId, reqVO.getRegionId())
                .eqIfPresent(VisitorDO::getCreateBy, reqVO.getCreateBy())
                .betweenIfPresent(VisitorDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(VisitorDO::getUpdateBy, reqVO.getUpdateBy())
                .orderByDesc(VisitorDO::getId));
    }

    @Select("SELECT*FROM macs_visitor WHERE id IN " +
            "(SELECT visitor_id FROM macs_visitor_region WHERE application_id= #{applicationId} GROUP BY visitor_id)")
    List<VisitorDO> getPageByApplicationId(Page<VisitorDO> page, @Param("applicationId") String applicationId);
}
