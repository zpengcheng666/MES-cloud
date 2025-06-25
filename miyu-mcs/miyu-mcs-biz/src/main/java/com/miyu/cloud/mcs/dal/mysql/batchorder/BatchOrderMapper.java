package com.miyu.cloud.mcs.dal.mysql.batchorder;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.mcs.controller.admin.batchorder.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 批次级订单 Mapper
 *
 * @author miyu
 */
@Mapper
public interface BatchOrderMapper extends BaseMapperX<BatchOrderDO> {

    default PageResult<BatchOrderDO> selectPage(BatchOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BatchOrderDO>()
                .likeIfPresent(BatchOrderDO::getOrderId, reqVO.getOrderId())
                .orderByDesc(BatchOrderDO::getId));
    }

    @Select("SELECT bo.*,bo1.batch_number preBatchNumber " +
            "FROM `mcs_batch_order` bo " +
            "LEFT JOIN mcs_batch_order bo1 ON bo.pre_batch_id=bo1.id " +
            "WHERE bo.order_id = #{orderId} ORDER BY id ASC")
    List<BatchOrderDO> selectBatchList(@Param("orderId") String orderId);

    @Select("<script>"+
            "SELECT bo.*,bo1.batch_number preBatchNumber " +
            "FROM `mcs_batch_order` bo " +
            "LEFT JOIN mcs_batch_order bo1 ON bo.pre_batch_id=bo1.id " +
            "WHERE bo.order_id in "+
                "<foreach item='item' index='index' collection='orderIds' open='(' separator=',' close=')'>"+
                    "#{item}"+
                "</foreach>"+
            "ORDER BY id ASC"+
            "</script>")
    List<BatchOrderDO> selectBatchListByOrderIds(@Param("orderIds") List<String> orderIds);

    @Select("delete FROM `mcs_batch_order` WHERE id = #{id} ")
    void deleteByIdPhy(@Param("id") String id);

    @Select("delete FROM `mcs_batch_order` ${ew.customSqlSegment} ")
    void deleteBatchIdsPhy(@Param("ew") Wrapper<BatchOrderDO> wrapper);
}
