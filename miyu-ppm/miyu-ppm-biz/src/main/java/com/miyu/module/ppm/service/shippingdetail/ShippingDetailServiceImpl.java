package com.miyu.module.ppm.service.shippingdetail;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDetailDO;
import com.miyu.module.ppm.dal.mysql.consignmentdetail.ConsignmentDetailMapper;
import com.miyu.module.ppm.dal.mysql.contract.ContractOrderMapper;
import com.miyu.module.ppm.dal.mysql.purchaserequirement.PurchaseRequirementDetailMapper;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.shippingdetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.shippingdetail.ShippingDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_DETAIL_NOT_EXISTS;

/**
 * 销售发货明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ShippingDetailServiceImpl implements ShippingDetailService {

    @Resource
    private ShippingDetailMapper shippingDetailMapper;

    @Resource
    private PurchaseRequirementDetailMapper requirementDetailMapper;

    @Resource
    private ContractOrderMapper contractOrderMapper;

    @Resource
    private ConsignmentDetailMapper consignmentDetailMapper;

    @Override
    public String createShippingDetail(ShippingDetailSaveReqVO createReqVO) {
        // 插入
        ShippingDetailDO shippingDetail = BeanUtils.toBean(createReqVO, ShippingDetailDO.class);
        shippingDetailMapper.insert(shippingDetail);
        // 返回
        return shippingDetail.getId();
    }

    @Override
    public void updateShippingDetail(ShippingDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateShippingDetailExists(updateReqVO.getId());
        // 更新
        ShippingDetailDO updateObj = BeanUtils.toBean(updateReqVO, ShippingDetailDO.class);
        shippingDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteShippingDetail(String id) {
        // 校验存在
        validateShippingDetailExists(id);
        // 删除
        shippingDetailMapper.deleteById(id);
    }

    private void validateShippingDetailExists(String id) {
        if (shippingDetailMapper.selectById(id) == null) {
            throw exception(SHIPPING_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public ShippingDetailDO getShippingDetail(String id) {
        return shippingDetailMapper.selectById(id);
    }

    @Override
    public PageResult<ShippingDetailDO> getShippingDetailPage(ShippingDetailPageReqVO pageReqVO) {
        return shippingDetailMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ShippingDetailDO> getOutboundOderByContractId(String contractId,List<Integer> status) {
        return shippingDetailMapper.getOutboundOderByContractId( contractId,null,status);
    }

    @Override
    public List<ShippingDetailDO> getDetailByProjectId(String projectId,String projectOrderId,Integer shippingType) {
        MPJLambdaWrapperX<ShippingDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eqIfPresent(ShippingDetailDO::getProjectId,projectId)
                .eqIfPresent(ShippingDetailDO::getProjectOrderId,projectOrderId)
            .eqIfPresent(ShippingDetailDO::getShippingType,shippingType)
        .ne(ShippingDetailDO::getShippingStatus,ShippingStatusEnum.CANCEL.getStatus());
        return shippingDetailMapper.selectList(wrapperX);
    }

    @Override
    public List<ShippingDetailDO> getDetailByProjectIds(Collection<String> projectId) {
        MPJLambdaWrapperX<ShippingDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.inIfPresent(ShippingDetailDO::getProjectId,projectId);
        return shippingDetailMapper.selectList(wrapperX);
    }

    @Override
    public List<ShippingDetailDO> getDetailsById(String shippingId, String infoId) {
        MPJLambdaWrapperX<ShippingDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eqIfPresent(ShippingDetailDO::getShippingId,shippingId)
                .eqIfPresent(ShippingDetailDO::getInfoId,infoId);
        return shippingDetailMapper.selectList(wrapperX);
    }

    @Override
    public List<ShippingDetailDO> getDetailByConsignmentDetailIds(Collection<String> consignmentDetailIds) {
        MPJLambdaWrapperX<ShippingDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(ShippingDetailDO::getConsignmentDetailId,consignmentDetailIds)
                .ne(ShippingDetailDO::getShippingStatus, ShippingStatusEnum.CANCEL.getStatus());
        return shippingDetailMapper.selectList(wrapperX);
    }

    @Override
    public List<ShippingDetailDO> getShippingDetailByProjectOrderId(String id) {
        LambdaQueryWrapperX<ShippingDetailDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eq(ShippingDetailDO::getProjectOrderId,id);
        List<ShippingDetailDO> shippingDetailDOS = shippingDetailMapper.selectList(wrapperX);
        return shippingDetailDOS;
    }

    @Override
    public List<ShippingDetailDO> getShippingDetailByPurchaseId(String id) {
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
        List<ConsignmentDetailDO> consignmentDetailDOS = consignmentDetailMapper.selectList(consignmentWrapperX);
        //上面查的收货,然后用收货的ConsignmentDetailIds去查退货
        List<String> consignmentDetailIds = consignmentDetailDOS.stream().filter((item) -> {
            return ObjectUtil.isNotNull(item.getConsignmentStatus()) && (item.getConsignmentStatus() == 6) && (item.getConsignmentType() == 1);
        }).map(ConsignmentDetailDO::getId).collect(Collectors.toList());
        if(consignmentDetailIds.size()==0){
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ShippingDetailDO> shippingDetailWrapper = new LambdaQueryWrapper<>();
        shippingDetailWrapper.in(ShippingDetailDO::getConsignmentDetailId, consignmentDetailIds);
        //这个也没过滤,pms自己过滤
        List<ShippingDetailDO> shippingDetailList = shippingDetailMapper.selectList(shippingDetailWrapper);
        return shippingDetailList;
    }

}
