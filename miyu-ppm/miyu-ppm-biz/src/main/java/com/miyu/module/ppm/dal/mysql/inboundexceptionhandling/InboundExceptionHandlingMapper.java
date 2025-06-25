package com.miyu.module.ppm.dal.mysql.inboundexceptionhandling;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.ppm.dal.dataobject.inboundexceptionhandling.InboundExceptionHandlingDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.inboundexceptionhandling.vo.*;

/**
 * 入库异常处理 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface InboundExceptionHandlingMapper extends BaseMapperX<InboundExceptionHandlingDO> {

    default PageResult<InboundExceptionHandlingDO> selectPage(InboundExceptionHandlingPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InboundExceptionHandlingDO>()
                .eqIfPresent(InboundExceptionHandlingDO::getNo, reqVO.getNo())
                .betweenIfPresent(InboundExceptionHandlingDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(InboundExceptionHandlingDO::getStatus, reqVO.getStatus())
                .eqIfPresent(InboundExceptionHandlingDO::getConsignmentType, reqVO.getConsignmentType())
                .eqIfPresent(InboundExceptionHandlingDO::getRusultType, reqVO.getRusultType())
                .eqIfPresent(InboundExceptionHandlingDO::getExceptionType, reqVO.getExceptionType())
                .eqIfPresent(InboundExceptionHandlingDO::getContractId, reqVO.getContractId())
                .eqIfPresent(InboundExceptionHandlingDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(InboundExceptionHandlingDO::getCompanyId, reqVO.getCompanyId())
                .orderByDesc(InboundExceptionHandlingDO::getId));
    }

}