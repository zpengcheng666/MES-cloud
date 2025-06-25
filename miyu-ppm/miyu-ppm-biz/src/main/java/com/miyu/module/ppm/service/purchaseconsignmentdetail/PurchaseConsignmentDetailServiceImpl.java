package com.miyu.module.ppm.service.purchaseconsignmentdetail;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDetailDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.mysql.consignment.ConsignmentMapper;
import com.miyu.module.ppm.dal.mysql.contract.ContractOrderMapper;
import com.miyu.module.ppm.dal.mysql.purchaserequirement.PurchaseRequirementDetailMapper;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentStatusEnum;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.consignmentdetail.ConsignmentDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 收货明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class PurchaseConsignmentDetailServiceImpl implements PurchaseConsignmentDetailService {

    @Resource
    private ConsignmentDetailMapper consignmentDetailMapper;

    @Resource
    private ConsignmentMapper consignmentMapper;

    @Resource
    private PurchaseRequirementDetailMapper requirementDetailMapper;

    @Resource
    private ContractOrderMapper contractOrderMapper;

    @Override
    public String createPurchaseConsignmentDetail(PurchaseConsignmentDetailSaveReqVO createReqVO) {
        // 插入
        ConsignmentDetailDO purchaseConsignmentDetail = BeanUtils.toBean(createReqVO, ConsignmentDetailDO.class);
        consignmentDetailMapper.insert(purchaseConsignmentDetail);
        // 返回
        return purchaseConsignmentDetail.getId();
    }

    @Override
    public void updatePurchaseConsignmentDetail(PurchaseConsignmentDetailSaveReqVO updateReqVO) {
        // 校验存在
        validatePurchaseConsignmentDetailExists(updateReqVO.getId());
        // 更新
        ConsignmentDetailDO updateObj = BeanUtils.toBean(updateReqVO, ConsignmentDetailDO.class);
        consignmentDetailMapper.updateById(updateObj);
    }

    @Override
    public void deletePurchaseConsignmentDetail(String id) {
        // 校验存在
        validatePurchaseConsignmentDetailExists(id);
        // 删除
        consignmentDetailMapper.deleteById(id);
    }

    private void validatePurchaseConsignmentDetailExists(String id) {
        if (consignmentDetailMapper.selectById(id) == null) {
            throw exception(PURCHASE_CONSIGNMENT_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public ConsignmentDetailDO getPurchaseConsignmentDetail(String id) {
        return consignmentDetailMapper.selectById(id);
    }

    @Override
    public PageResult<ConsignmentDetailDO> getPurchaseConsignmentDetailPage(PurchaseConsignmentDetailPageReqVO pageReqVO) {
        return consignmentDetailMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ConsignmentDetailDO> getInboundOderByContractId(String contractId, List<Integer> status) {
        return consignmentDetailMapper.getInboundOderByContractId( contractId,null,status);
    }

    /**
     * 通过产品编号查询收货单明细Id
     */
    @Override
    public ConsignmentDetailDO queryConsignmentDetailIdByConfigId(String materialConfigId, String consignmentId) {
        return consignmentDetailMapper.queryConsignmentDetailIdByConfigId(materialConfigId,consignmentId);
    }

    /**
     * 通过产品编号查询收货单明细数据
     */
    @Override
    public List<ConsignmentDetailDO> queryConsignmentDetailIdById(String id) {
        return consignmentDetailMapper.queryConsignmentDetailIdById(id);
    }

    @Override
    public List<ConsignmentDetailDO> getDetailListByProjectId(String projectId, String contract, Integer consignmentType) {
        MPJLambdaWrapperX<ConsignmentDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ConsignmentDO.class, ConsignmentDO::getId, ConsignmentDetailDO::getConsignmentId)
                .selectAs(ConsignmentDO::getNo, ConsignmentDetailDO::getNo)
                .selectAs(ConsignmentDO::getName, ConsignmentDetailDO::getName)
                .selectAll(ConsignmentDetailDO.class);
        wrapperX.eqIfPresent(ConsignmentDetailDO::getProjectId, projectId)
                .eqIfPresent(ConsignmentDetailDO::getConsignmentType, consignmentType)
                .neIfPresent(ConsignmentDetailDO::getConsignmentStatus,ConsignmentStatusEnum.REJECT.getStatus())
                .eqIfPresent(ConsignmentDetailDO::getContractId, contract);

        return consignmentDetailMapper.selectList(wrapperX);
    }

    @Override
    public List<ConsignmentDetailDO> queryConsignmentDetailIdByConsignmentId(String consignmentId) {
        return consignmentDetailMapper.queryConsignmentDetailIdByConsignmentId(consignmentId);
    }

    @Override
    public List<ConsignmentDetailDO> getDetailListByShippingDetailIds(Collection<String> ids) {
        MPJLambdaWrapperX<ConsignmentDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(ConsignmentDetailDO::getShippingDetailId,ids)
        .ne(ConsignmentDetailDO::getConsignmentStatus, ConsignmentStatusEnum.REJECT.getStatus());
        return consignmentDetailMapper.selectList(wrapperX);
    }

    @Override
    public List<ConsignmentDetailDO> getDetailListByIds(Collection<String> consignmentIds) {

        MPJLambdaWrapperX<ConsignmentDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(ConsignmentDetailDO::getConsignmentId,consignmentIds);

        return consignmentDetailMapper.selectList(wrapperX);
    }

    @Override
    public List<ConsignmentDetailDO> getDetailListByInfoId(String consignmentInfoId) {
        return consignmentDetailMapper.selectList(ConsignmentDetailDO::getInfoId,consignmentInfoId);
    }

    @Override
    public List<ConsignmentDetailDO> getPurchaseDetailByProjectOrderId(String id) {
        LambdaQueryWrapperX<ConsignmentDetailDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eq(ConsignmentDetailDO::getProjectOrderId,id);
        List<ConsignmentDetailDO> consignmentDetailDOS = consignmentDetailMapper.selectList(wrapperX);
        return consignmentDetailDOS;
    }

    /**
     * 通过采购计划id查询收货明细
     * @param id
     * @return
     */
    @Override
    public List<ConsignmentDetailDO> getPurchaseDetailByPurchaseId(String id) {
        LambdaQueryWrapperX<PurchaseRequirementDetailDO> requireWrapperX = new LambdaQueryWrapperX<>();
        requireWrapperX.eq(PurchaseRequirementDetailDO::getSourceId,id);
        //其实应该只有一条
        List<PurchaseRequirementDetailDO> purchaseRequirementDetailList = requirementDetailMapper.selectList(requireWrapperX);
        if(purchaseRequirementDetailList.size()==0){
            return Collections.emptyList();
        }
        List<String> requireIds = purchaseRequirementDetailList.stream().map(PurchaseRequirementDetailDO::getId).collect(Collectors.toList());
        LambdaQueryWrapperX<ContractOrderDO> orderWrapper = new LambdaQueryWrapperX<>();
        orderWrapper.in(ContractOrderDO::getRequirementDetailId,requireIds);
        List<ContractOrderDO> contractOrderDOList = contractOrderMapper.selectList(orderWrapper);
        if(contractOrderDOList.size()==0){
            return Collections.emptyList();
        }
        List<String> orderIds = contractOrderDOList.stream().map(ContractOrderDO::getId).collect(Collectors.toList());
        LambdaQueryWrapperX<ConsignmentDetailDO> consignmentWrapperX = new LambdaQueryWrapperX<>();
        consignmentWrapperX.in(ConsignmentDetailDO::getOrderId,orderIds);
        //没过滤，pms自己过滤
        List<ConsignmentDetailDO> consignmentDetailDOS = consignmentDetailMapper.selectList(consignmentWrapperX);
        return consignmentDetailDOS;
    }


}
