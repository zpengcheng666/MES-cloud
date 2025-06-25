package cn.iocoder.yudao.module.bpm.dal.mysql.oapurchase;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.bpm.controller.admin.oapurchase.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oapurchase.OaPurchaseDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * OA 采购申请 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OaPurchaseMapper extends BaseMapperX<OaPurchaseDO> {

    default PageResult<OaPurchaseDO> selectPage(OaPurchasePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaPurchaseDO>()
                .eqIfPresent(OaPurchaseDO::getDept, reqVO.getDept())
                .eqIfPresent(OaPurchaseDO::getApplicant, reqVO.getApplicant())
                .eqIfPresent(OaPurchaseDO::getPurchaseAgent, reqVO.getPurchaseAgent())
                .eqIfPresent(OaPurchaseDO::getSupplier, reqVO.getSupplier())
                .eqIfPresent(OaPurchaseDO::getSupplyCode, reqVO.getSupplyCode())
                .eqIfPresent(OaPurchaseDO::getContact, reqVO.getContact())
                .eqIfPresent(OaPurchaseDO::getTel, reqVO.getTel())
                .eqIfPresent(OaPurchaseDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OaPurchaseDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .betweenIfPresent(OaPurchaseDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OaPurchaseDO::getId));
    }

}
