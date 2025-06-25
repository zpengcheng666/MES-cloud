package com.miyu.cloud.mcs.dal.mysql.orderform;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.mcs.controller.admin.orderform.vo.*;

/**
 * 生产订单 Mapper
 *
 * @author miyu
 */
@Mapper
public interface OrderFormMapper extends BaseMapperX<OrderFormDO> {

    default PageResult<OrderFormDO> selectPage(OrderFormPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrderFormDO>()
                .likeIfPresent(OrderFormDO::getProjectNumber, reqVO.getProjectNumber())
                .likeIfPresent(OrderFormDO::getOrderNumber, reqVO.getOrderNumber())
                .likeIfPresent(OrderFormDO::getPartNumber, reqVO.getPartNumber())
                .eqIfPresent(OrderFormDO::getOrderType, reqVO.getOrderType())
                .eqIfPresent(OrderFormDO::getPriority, reqVO.getPriority())
                .eqIfPresent(OrderFormDO::getIssued, reqVO.getIssued())
                .orderByDesc(OrderFormDO::getId));
    }

    default List<OrderFormDO> mySelectList(OrderFormSelectListRespVO listRespVO) {
        return selectList(new LambdaQueryWrapperX<OrderFormDO>()
                .likeIfPresent(OrderFormDO::getOrderNumber, listRespVO.getOrderNumber())
                .inIfPresent(OrderFormDO::getStatus, listRespVO.getStatus())
                .orderByDesc(OrderFormDO::getId));
    }
}
