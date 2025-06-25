package com.miyu.cloud.dms.dal.mysql.failurerecord;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.dms.controller.admin.failurerecord.vo.FailureRecordPageReqVO;
import com.miyu.cloud.dms.dal.dataobject.failurerecord.FailureRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 异常记录 Mapper
 *
 * @author miyu
 */
@Mapper
public interface FailureRecordMapper extends BaseMapperX<FailureRecordDO> {

    default PageResult<FailureRecordDO> selectPage(FailureRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FailureRecordDO>()
                .eqIfPresent(FailureRecordDO::getDevice, reqVO.getDevice())
                .eqIfPresent(FailureRecordDO::getCode, reqVO.getCode())
                .eqIfPresent(FailureRecordDO::getFaultState, reqVO.getFaultState())
                .betweenIfPresent(FailureRecordDO::getFaultTime, reqVO.getFaultTime())
                .betweenIfPresent(FailureRecordDO::getCreateTime, reqVO.getCreateTime()));

    }

}
