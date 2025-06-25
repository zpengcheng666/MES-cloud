package com.miyu.module.qms.dal.mysql.retraceconfig;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.qms.dal.dataobject.retraceconfig.RetraceConfigDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.retraceconfig.vo.*;

/**
 * 追溯字段配置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface RetraceConfigMapper extends BaseMapperX<RetraceConfigDO> {

    default PageResult<RetraceConfigDO> selectPage(RetraceConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RetraceConfigDO>()
                .betweenIfPresent(RetraceConfigDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(RetraceConfigDO::getName, reqVO.getName())
                .eqIfPresent(RetraceConfigDO::getNo, reqVO.getNo())
                .orderByDesc(RetraceConfigDO::getId));
    }

}