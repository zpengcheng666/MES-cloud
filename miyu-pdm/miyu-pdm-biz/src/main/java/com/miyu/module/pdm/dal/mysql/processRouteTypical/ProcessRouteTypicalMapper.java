package com.miyu.module.pdm.dal.mysql.processRouteTypical;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.processRouteTypical.vo.ProcessRouteTypicalListReqVO;
import com.miyu.module.pdm.controller.admin.processRouteTypical.vo.ProcessRouteTypicalPageReqVO;
import org.apache.ibatis.annotations.Mapper;

import com.miyu.module.pdm.dal.dataobject.processRouteTypical.ProcessRouteTypicalDO;

import java.util.List;

@Mapper
public interface ProcessRouteTypicalMapper extends BaseMapperX<ProcessRouteTypicalDO> {
    default PageResult<ProcessRouteTypicalDO> selectPage(ProcessRouteTypicalPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProcessRouteTypicalDO>()
                .likeIfPresent(ProcessRouteTypicalDO::getName, reqVO.getName())
                .eqIfPresent(ProcessRouteTypicalDO::getId, reqVO.getId())
                .orderByDesc(ProcessRouteTypicalDO::getId));
    }

    default ProcessRouteTypicalDO selectByProcessRouteName(String name) {
        return selectOne(ProcessRouteTypicalDO::getName, name);
    }
    default List<ProcessRouteTypicalDO> selectList(ProcessRouteTypicalListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProcessRouteTypicalDO>()
                .likeIfPresent(ProcessRouteTypicalDO::getDescription, reqVO.getDescription())
                .likeIfPresent(ProcessRouteTypicalDO::getName, reqVO.getName())
                .orderByDesc(ProcessRouteTypicalDO::getId));
    }
}
