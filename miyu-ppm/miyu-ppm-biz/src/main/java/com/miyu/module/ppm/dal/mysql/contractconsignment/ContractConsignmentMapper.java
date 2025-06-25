package com.miyu.module.ppm.dal.mysql.contractconsignment;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.ppm.dal.dataobject.contractconsignment.ContractConsignmentDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.contractconsignment.vo.*;

/**
 * 外协发货 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface ContractConsignmentMapper extends BaseMapperX<ContractConsignmentDO> {

    default PageResult<ContractConsignmentDO> selectPage(ContractConsignmentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ContractConsignmentDO>()
                .eqIfPresent(ContractConsignmentDO::getConsignmentNo, reqVO.getConsignmentNo())
                .likeIfPresent(ContractConsignmentDO::getConsignmentName, reqVO.getConsignmentName())
                .eqIfPresent(ContractConsignmentDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ContractConsignmentDO::getCompanyId, reqVO.getCompanyId())
                .eqIfPresent(ContractConsignmentDO::getConsigner, reqVO.getConsigner())
                .betweenIfPresent(ContractConsignmentDO::getConsignerDate, reqVO.getConsignerDate())
                .eqIfPresent(ContractConsignmentDO::getDeliveryMethod, reqVO.getDeliveryMethod())
                .eqIfPresent(ContractConsignmentDO::getDeliveryBy, reqVO.getDeliveryBy())
                .eqIfPresent(ContractConsignmentDO::getDeliveryNumber, reqVO.getDeliveryNumber())
                .eqIfPresent(ContractConsignmentDO::getDeliveryContact, reqVO.getDeliveryContact())
                .eqIfPresent(ContractConsignmentDO::getConsignedBy, reqVO.getConsignedBy())
                .betweenIfPresent(ContractConsignmentDO::getConsignedDate, reqVO.getConsignedDate())
                .eqIfPresent(ContractConsignmentDO::getConsignedContact, reqVO.getConsignedContact())
                .betweenIfPresent(ContractConsignmentDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ContractConsignmentDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ContractConsignmentDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .eqIfPresent(ContractConsignmentDO::getFileUrl, reqVO.getFileUrl())
                .eqIfPresent(ContractConsignmentDO::getConsignmentStatus, reqVO.getConsignmentStatus())
                .orderByDesc(ContractConsignmentDO::getId));
    }




}