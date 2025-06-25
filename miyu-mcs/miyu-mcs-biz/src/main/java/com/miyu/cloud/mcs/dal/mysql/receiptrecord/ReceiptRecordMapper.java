package com.miyu.cloud.mcs.dal.mysql.receiptrecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.miyu.cloud.mcs.dal.dataobject.receiptrecord.ReceiptRecordDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.mcs.controller.admin.receiptrecord.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 生产单元签收记录 Mapper
 *
 * @author miyu
 */
@Mapper
public interface ReceiptRecordMapper extends BaseMapperX<ReceiptRecordDO> {

    default PageResult<ReceiptRecordDO> selectPage(ReceiptRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ReceiptRecordDO>()
                .eqIfPresent(ReceiptRecordDO::getApplicationId, reqVO.getApplicationId())
                .eqIfPresent(ReceiptRecordDO::getProcessingUnitId, reqVO.getProcessingUnitId())
                .eqIfPresent(ReceiptRecordDO::getResourceType, reqVO.getResourceCode())
                .likeIfPresent(ReceiptRecordDO::getResourceTypeCode, reqVO.getResourceTypeCode())
                .orderByDesc(ReceiptRecordDO::getId));
    }

}
