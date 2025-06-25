package com.miyu.cloud.mcs.dal.mysql.batchdemandrecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.miyu.cloud.mcs.dal.dataobject.batchdemandrecord.BatchDemandRecordDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.mcs.controller.admin.batchdemandrecord.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 需求分拣详情 Mapper
 *
 * @author miyu
 */
@Mapper
public interface BatchDemandRecordMapper extends BaseMapperX<BatchDemandRecordDO> {

    default PageResult<BatchDemandRecordDO> selectPage(BatchDemandRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BatchDemandRecordDO>()
                .eqIfPresent(BatchDemandRecordDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(BatchDemandRecordDO::getDemandId, reqVO.getDemandId())
                .eqIfPresent(BatchDemandRecordDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(BatchDemandRecordDO::getMaterialNumber, reqVO.getMaterialNumber())
                .eqIfPresent(BatchDemandRecordDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(BatchDemandRecordDO::getBatchNumber, reqVO.getBatchNumber())
                .eqIfPresent(BatchDemandRecordDO::getTotality, reqVO.getTotality())
                .eqIfPresent(BatchDemandRecordDO::getBatch, reqVO.getBatch())
                .betweenIfPresent(BatchDemandRecordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BatchDemandRecordDO::getId));
    }

    @Select("DELETE FROM `mcs_batch_demand_record` bdr WHERE id = #{id} ")
    void deleteByIdPhy(@Param("id") String id);

    @Select("DELETE FROM `mcs_batch_demand_record` ${ew.customSqlSegment} ")
    void deleteBatchIdsPhy(@Param("ew") Wrapper<BatchDemandRecordDO> wrapper);
}
