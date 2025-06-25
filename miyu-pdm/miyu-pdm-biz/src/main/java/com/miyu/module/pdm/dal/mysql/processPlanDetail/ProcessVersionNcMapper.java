package com.miyu.module.pdm.dal.mysql.processPlanDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessVersionNcReqVO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.ProcessVersionNcDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProcessVersionNcMapper extends BaseMapperX<ProcessVersionNcDO> {
    default int deleteNcByNcId( ProcessVersionNcReqVO req1VO) {
        return delete(Wrappers.lambdaUpdate(ProcessVersionNcDO.class)
                .eq(ProcessVersionNcDO::getNcId, req1VO.getNcId()));
    }
}
