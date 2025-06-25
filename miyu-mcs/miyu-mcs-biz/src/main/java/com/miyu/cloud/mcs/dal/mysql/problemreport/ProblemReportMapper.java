package com.miyu.cloud.mcs.dal.mysql.problemreport;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.mcs.controller.admin.problemreport.vo.ProblemReportPageReqVO;
import com.miyu.cloud.mcs.dal.dataobject.problemreport.ProblemReportDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 问题上报 Mapper
 *
 * @author 王正浩
 */
@Mapper
public interface ProblemReportMapper extends BaseMapperX<ProblemReportDO> {

    default PageResult<ProblemReportDO> selectPage(ProblemReportPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProblemReportDO>()
                .eqIfPresent(ProblemReportDO::getStationId, reqVO.getStationId())
                .eqIfPresent(ProblemReportDO::getType, reqVO.getType())
                .eqIfPresent(ProblemReportDO::getReportId, reqVO.getReportId())
                .eqIfPresent(ProblemReportDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ProblemReportDO::getContent, reqVO.getContent())
                .betweenIfPresent(ProblemReportDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProblemReportDO::getId));
    }

}