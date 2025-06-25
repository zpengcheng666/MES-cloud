package com.miyu.module.ppm.dal.mysql.purchaserequirement;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.controller.admin.purchaserequirement.vo.PurchaseRequirementDetailPageReqVO;
import com.miyu.module.ppm.controller.admin.purchaserequirement.vo.PurchaseRequirementDetailReqVO;
import com.miyu.module.ppm.controller.admin.purchaserequirement.vo.PurchaseRequirementPageReqVO;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDO;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购需求明细，可以来源于采购申请或MRP Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface PurchaseRequirementDetailMapper extends BaseMapperX<PurchaseRequirementDetailDO> {

    default PageResult<PurchaseRequirementDetailDO> selectPage(PurchaseRequirementPageReqVO reqVO) {

        MPJLambdaWrapperX<PurchaseRequirementDetailDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(PurchaseRequirementDO.class,PurchaseRequirementDO::getId,PurchaseRequirementDetailDO::getRequirementId)
                .selectAs(PurchaseRequirementDO::getNumber,PurchaseRequirementDetailDO::getRequirementNumber)
                .selectAs(PurchaseRequirementDO::getType,PurchaseRequirementDetailDO::getType)
                .selectAs(PurchaseRequirementDO::getApplicant,PurchaseRequirementDetailDO::getApplicant)
                .selectAs(PurchaseRequirementDO::getApplicationDate,PurchaseRequirementDetailDO::getApplicationDate)
                .selectAs(PurchaseRequirementDO::getApplicationDepartment,PurchaseRequirementDetailDO::getApplicationDepartment)
                .selectAs(PurchaseRequirementDO::getApplicationReason,PurchaseRequirementDetailDO::getApplicationReason)
                .selectAs("(select sum(a.quantity) from pd_contract_order a LEFT JOIN pd_contract b ON a.contract_id = b.id and b.status not in (2, 3) where a.requirement_detail_id = t.id)", PurchaseRequirementDetailDO::getPurchasedQuantity)
                .selectAll(PurchaseRequirementDetailDO.class);
        wrapper.eqIfExists(PurchaseRequirementDO::getStatus,reqVO.getStatus())
                .eqIfExists(PurchaseRequirementDO::getIsValid,reqVO.getIsValid())
                .eqIfExists(PurchaseRequirementDO::getApplicationDepartment,reqVO.getApplicationDepartment())
                .eqIfExists(PurchaseRequirementDO::getApplicant,reqVO.getApplicant())
                .eqIfExists(PurchaseRequirementDO::getType,reqVO.getType());


        return selectPage(reqVO, wrapper);
    }

    default List<PurchaseRequirementDetailDO> getPurchaseRequirementDetailRequirementId(String id) {
        return selectList(PurchaseRequirementDetailDO::getRequirementId, id);
    }

    default List<PurchaseRequirementDetailDO> getPurchaseRequirementDetailList(PurchaseRequirementDetailReqVO reqVO) {
        MPJLambdaWrapperX<PurchaseRequirementDetailDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.selectAs("(select sum(a.quantity) from pd_contract_order a LEFT JOIN pd_contract b ON a.contract_id = b.id and b.status not in (2, 3) where a.requirement_detail_id = t.id)", PurchaseRequirementDetailDO::getPurchasedQuantity)
                .selectAll(PurchaseRequirementDetailDO.class);
        return selectList(wrapper.eq(PurchaseRequirementDetailDO::getRequirementId, reqVO.getRequirementId()).eqIfPresent(PurchaseRequirementDetailDO::getIsValid, reqVO.getIsValid()));
    }
}
