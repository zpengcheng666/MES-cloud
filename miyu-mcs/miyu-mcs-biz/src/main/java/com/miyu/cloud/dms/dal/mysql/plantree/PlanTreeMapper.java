package com.miyu.cloud.dms.dal.mysql.plantree;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.dms.controller.admin.plantree.vo.PlanTreePageReqVO;
import com.miyu.cloud.dms.dal.dataobject.plantree.PlanTreeDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.dms.controller.admin.plantree.vo.*;

/**
 * 计划关联树 Mapper
 *
 * @author 王正浩
 */
@Mapper
public interface PlanTreeMapper extends BaseMapperX<PlanTreeDO> {

    default PageResult<PlanTreeDO> selectPage(PlanTreePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PlanTreeDO>()
                .eqIfPresent(PlanTreeDO::getParent, reqVO.getParent())
                .likeIfPresent(PlanTreeDO::getName, reqVO.getName())
                .likeIfPresent(PlanTreeDO::getRemark, reqVO.getRemark())
                .eqIfPresent(PlanTreeDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(PlanTreeDO::getDeviceTypeId, reqVO.getDeviceTypeId())
                .orderByDesc(PlanTreeDO::getId));
    }

}