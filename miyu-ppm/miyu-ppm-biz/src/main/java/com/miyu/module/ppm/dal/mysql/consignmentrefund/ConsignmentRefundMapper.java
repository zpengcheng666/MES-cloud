package com.miyu.module.ppm.dal.mysql.consignmentrefund;

import java.math.BigDecimal;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.github.yulichang.method.mp.SelectList;
import com.github.yulichang.wrapper.segments.SelectLabel;
import com.miyu.module.ppm.dal.dataobject.companyfinance.CompanyFinanceDO;
import com.miyu.module.ppm.dal.dataobject.consignmentrefund.ConsignmentRefundDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.consignmentrefund.vo.*;

/**
 * 采购退款单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ConsignmentRefundMapper extends BaseMapperX<ConsignmentRefundDO> {

    default PageResult<ConsignmentRefundDO> selectPage(ConsignmentRefundPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ConsignmentRefundDO>()
                .eqIfPresent(ConsignmentRefundDO::getNo, reqVO.getNo())
                .eqIfPresent(ConsignmentRefundDO::getConsignmentReturnId, reqVO.getConsignmentReturnId())
                .eqIfPresent(ConsignmentRefundDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ConsignmentRefundDO::getRefundType, reqVO.getRefundType())
                .betweenIfPresent(ConsignmentRefundDO::getRefundTime, reqVO.getRefundTime())
                .eqIfPresent(ConsignmentRefundDO::getRefundPrice, reqVO.getRefundPrice())
                .betweenIfPresent(ConsignmentRefundDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ConsignmentRefundDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ConsignmentRefundDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .eqIfPresent(ConsignmentRefundDO::getRefundStatus, reqVO.getRefundStatus())
                .orderByDesc(ConsignmentRefundDO::getId));
    }

    default BigDecimal queryRefundPriceByReturnId(String consignmentReturnId){
        MPJLambdaWrapperX<ConsignmentRefundDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.eq(ConsignmentRefundDO::getConsignmentReturnId, consignmentReturnId)
               .selectAll(ConsignmentRefundDO.class);
        ;
        return selectList(wrapper).stream().map(ConsignmentRefundDO::getRefundPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    default BigDecimal queryRefundPriceByReturnId(String consignmentReturnId , String consignmentRefundId){
        MPJLambdaWrapperX<ConsignmentRefundDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.eq(ConsignmentRefundDO::getConsignmentReturnId, consignmentReturnId)
                .ne(ConsignmentRefundDO::getId, consignmentRefundId)
                .selectAll(ConsignmentRefundDO.class);
        ;
        return selectList(wrapper).stream().map(ConsignmentRefundDO::getRefundPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}