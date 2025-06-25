package com.miyu.cloud.mcs.dal.mysql.batchorderdemand;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import com.miyu.cloud.mcs.dal.dataobject.batchorderdemand.BatchOrderDemandDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.mcs.controller.admin.batchorderdemand.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 批次订单需求 Mapper
 *
 * @author miyu
 */
@Mapper
public interface BatchOrderDemandMapper extends BaseMapperX<BatchOrderDemandDO> {

    default PageResult<BatchOrderDemandDO> selectPage(BatchOrderDemandPageReqVO reqVO) {
        return selectPage(reqVO, new MPJLambdaWrapperX<BatchOrderDemandDO>()
                .eqIfPresent(BatchOrderDemandDO::getOrderId, reqVO.getOrderId())
                .likeIfPresent(BatchOrderDemandDO::getOrderNumber, reqVO.getOrderNumber())
                .eqIfPresent(BatchOrderDemandDO::getResourceType, reqVO.getResourceType())
                .likeIfPresent(BatchOrderDemandDO::getResourceTypeCode, reqVO.getResourceTypeCode())
                .eqIfPresent(BatchOrderDemandDO::getStatus, reqVO.getStatus())
                .orderByAsc(BatchOrderDemandDO::getStatus)
                .orderByDesc(BatchOrderDemandDO::getOrderId)
                .orderByAsc(BatchOrderDemandDO::getId)
                .leftJoin(OrderFormDO.class, OrderFormDO::getId, BatchOrderDemandDO::getOrderId)
                .selectAll(BatchOrderDemandDO.class)
                .selectAs(OrderFormDO::getOrderNumber,BatchOrderDemandDO::getOrderNumber));
    }

    @Select("SELECT bod.*,`or`.order_number orderNumber,bo.batch_number batchNumber, pu.unit_name unitName " +
            "FROM (SELECT*FROM mcs_batch_order_demand ${ew.customSqlSegment} ) bod " +
            "LEFT JOIN mcs_order_form `or` ON bod.order_id=`or`.id " +
            "LEFT JOIN mcs_batch_order bo ON bod.batch_id=bo.id " +
            "LEFT JOIN dms_processing_unit pu ON bod.processing_unit_id=pu.id " +
            "WHERE bod.deleted=0 ")
    IPage<BatchOrderDemandDO> selectDemandPage(IPage<BatchOrderDemandDO> mpPage, @Param("ew") Wrapper<BatchOrderDemandDO> wrapper);

    @Delete("DELETE FROM `mcs_batch_order_demand` WHERE id = #{id} ")
    void deleteCompletelyById(@Param("id") String id);

    @Select("SELECT bod.*,`or`.order_number orderNumber,bo.batch_number batchNumber, pu.unit_name unitName " +
            "FROM (SELECT*FROM mcs_batch_order_demand ${ew.customSqlSegment} ) bod " +
            "LEFT JOIN mcs_order_form `or` ON bod.order_id=`or`.id " +
            "LEFT JOIN mcs_batch_order bo ON bod.batch_id=bo.id " +
            "LEFT JOIN dms_processing_unit pu ON bod.processing_unit_id=pu.id " +
            "WHERE bod.deleted=0 ")
    List<BatchOrderDemandDO> selectDemandListByBatch(@Param("ew") QueryWrapper<BatchOrderDemandDO> queryWrapper);
}
