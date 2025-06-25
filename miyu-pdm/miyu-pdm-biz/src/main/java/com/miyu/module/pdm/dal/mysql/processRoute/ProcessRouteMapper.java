package com.miyu.module.pdm.dal.mysql.processRoute;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRouteListReqVO;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRoutePageReqVO;
import com.miyu.module.pdm.dal.dataobject.processRoute.ProcessRouteDO;
import com.miyu.module.pdm.dal.dataobject.product.ProductCategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProcessRouteMapper extends BaseMapperX<ProcessRouteDO> {
    default PageResult<ProcessRouteDO> selectPage(ProcessRoutePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProcessRouteDO>()
                .likeIfPresent(ProcessRouteDO::getName, reqVO.getName())
                .eqIfPresent(ProcessRouteDO::getId, reqVO.getId())
                .orderByDesc(ProcessRouteDO::getId));
    }

    default ProcessRouteDO selectByProcessRouteName(String name) {
        return selectOne(ProcessRouteDO::getName, name);
    }
    default List<ProcessRouteDO> selectList(ProcessRouteListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProcessRouteDO>()
                .likeIfPresent(ProcessRouteDO::getDescription, reqVO.getDescription())
                .likeIfPresent(ProcessRouteDO::getName, reqVO.getName())
                .orderByDesc(ProcessRouteDO::getId));
    }
}
