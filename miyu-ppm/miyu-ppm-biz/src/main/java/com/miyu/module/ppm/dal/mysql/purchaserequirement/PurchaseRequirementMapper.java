package com.miyu.module.ppm.dal.mysql.purchaserequirement;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDO;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDetailDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.purchaserequirement.vo.*;

/**
 * 采购申请主 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface PurchaseRequirementMapper extends BaseMapperX<PurchaseRequirementDO> {

    default PageResult<PurchaseRequirementDO> selectPage(PurchaseRequirementPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PurchaseRequirementDO>()
                .eqIfPresent(PurchaseRequirementDO::getType, reqVO.getType())
                .eqIfPresent(PurchaseRequirementDO::getApplicant, reqVO.getApplicant())
                .eqIfPresent(PurchaseRequirementDO::getApplicationDepartment, reqVO.getApplicationDepartment())
                .betweenIfPresent(PurchaseRequirementDO::getApplicationDate, reqVO.getApplicationDate())
                .eqIfPresent(PurchaseRequirementDO::getApplicationReason, reqVO.getApplicationReason())
                .betweenIfPresent(PurchaseRequirementDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(PurchaseRequirementDO::getIsValid, reqVO.getIsValid())
                .eqIfPresent(PurchaseRequirementDO::getStatus, reqVO.getStatus())
                .eqIfPresent(PurchaseRequirementDO::getCreationIp, reqVO.getCreationIp())
                .eqIfPresent(PurchaseRequirementDO::getUpdatedIp, reqVO.getUpdatedIp())
                .orderByDesc(PurchaseRequirementDO::getId));
    }




}