package com.miyu.cloud.mcs.dal.mysql.batchorderapsresult;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.mcs.controller.admin.batchorderapsresult.vo.BatchOrderApsResultPageReqVO;
import com.miyu.cloud.mcs.dal.dataobject.batchorderapsresult.BatchOrderApsResultDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 排产结果 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface BatchOrderApsResultMapper extends BaseMapperX<BatchOrderApsResultDO> {

    default PageResult<BatchOrderApsResultDO> selectPage(BatchOrderApsResultPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BatchOrderApsResultDO>()
                .betweenIfPresent(BatchOrderApsResultDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(BatchOrderApsResultDO::getApsContent, reqVO.getApsContent())
                .eqIfPresent(BatchOrderApsResultDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BatchOrderApsResultDO::getSubmitedTime, reqVO.getSubmitedTime())
                .betweenIfPresent(BatchOrderApsResultDO::getAcceptedTime, reqVO.getAcceptedTime())
                .eqIfPresent(BatchOrderApsResultDO::getAcceptedBy, reqVO.getAcceptedBy())
                .betweenIfPresent(BatchOrderApsResultDO::getNullifiedTime, reqVO.getNullifiedTime())
                .eqIfPresent(BatchOrderApsResultDO::getNullifiedBy, reqVO.getNullifiedBy())
                .eqIfPresent(BatchOrderApsResultDO::getPlanPriority, reqVO.getPlanPriority())
                .eqIfPresent(BatchOrderApsResultDO::getApsConfig, reqVO.getApsConfig())
                .eqIfPresent(BatchOrderApsResultDO::getSysOrgCode, reqVO.getSysOrgCode())
                .betweenIfPresent(BatchOrderApsResultDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BatchOrderApsResultDO::getId));
    }

}
