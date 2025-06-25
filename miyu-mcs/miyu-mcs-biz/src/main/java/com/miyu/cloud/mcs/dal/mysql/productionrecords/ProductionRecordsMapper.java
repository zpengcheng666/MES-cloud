package com.miyu.cloud.mcs.dal.mysql.productionrecords;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.miyu.cloud.mcs.dal.dataobject.productionrecords.ProductionRecordsDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.mcs.controller.admin.productionrecords.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 现场作业记录 Mapper
 *
 * @author miyu
 */
@Mapper
public interface ProductionRecordsMapper extends BaseMapperX<ProductionRecordsDO> {

    default PageResult<ProductionRecordsDO> selectPage(ProductionRecordsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductionRecordsDO>()
                .eqIfPresent(ProductionRecordsDO::getProcessingUnitId, reqVO.getProcessingUnitId())
                .eqIfPresent(ProductionRecordsDO::getBarCode, reqVO.getBatchCode())
                .eqIfPresent(ProductionRecordsDO::getOperationType, reqVO.getOperationType())
                .orderByDesc(ProductionRecordsDO::getOrderId)
                .orderByAsc(ProductionRecordsDO::getId));
    }

    @Select("SELECT pr.*,`or`.order_number orderNumber,br.batch_number batchNumber,pu.unit_name unitName,l.`name` deviceName " +
            "FROM (SELECT*FROM `mcs_production_records` ${ew.customSqlSegment} ) pr " +
            "LEFT JOIN `mcs_order_form` `or` ON pr.order_id=`or`.id " +
            "LEFT JOIN `mcs_batch_order` br ON pr.batch_id=br.id " +
            "LEFT JOIN `dms_processing_unit` pu ON pr.processing_unit_id=pu.id " +
            "LEFT JOIN dms_ledger l ON pr.equipment_id=l.id " +
            "WHERE pr.deleted=0 ")
    IPage<ProductionRecordsDO> getProductionRecordsPage(IPage page, @Param("ew") Wrapper<ProductionRecordsDO> queryWrapper);
}
