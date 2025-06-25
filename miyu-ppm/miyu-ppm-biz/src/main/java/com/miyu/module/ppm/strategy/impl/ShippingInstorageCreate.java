package com.miyu.module.ppm.strategy.impl;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentSaveReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailSaveReqVO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import com.miyu.module.ppm.service.consignmentinfo.ConsignmentInfoService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.shippinginfo.ShippingInfoService;
import com.miyu.module.ppm.strategy.IConsignmentFactory;
import com.miyu.module.ppm.utils.SpringContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;


/***
 * 外协收货验证
 */
@Service
@Transactional
public class ShippingInstorageCreate implements IConsignmentFactory {

    @Resource
    private ContractService contractService;
    @Resource
    private ShippingInfoService shippingInfoService;
    @Override
    public void validateCreate(PurchaseConsignmentSaveReqVO reqVO) {
        //
        ConsignmentInfoService consignmentInfoService = SpringContextHolder.getBean(ConsignmentInfoService.class);
        List<ConsignmentInfoDO> consignmentDetailDOS = consignmentInfoService.getConsignmentInfoByContractId(reqVO.getContractId(),reqVO.getId());
        List<ContractOrderDO> contractOrderDOS = contractService.getContractOrderListByContractId(reqVO.getContractId());
        //查询订单数量
        Map<String,ContractOrderDO> map =  cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(contractOrderDOS, ContractOrderDO::getMaterialId);
        Map<String,ShippingInfoDO> returnMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(consignmentDetailDOS)){
            List<ShippingInfoDO> shippingInfoServices = shippingInfoService.getShippingInfoByContractId(reqVO.getContractId());
            returnMap= cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(shippingInfoServices, ShippingInfoDO::getMaterialConfigId);
        }

        //查询已发货的订单
        Map<String,ConsignmentInfoDO> consignmentInfoDOMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(consignmentDetailDOS, ConsignmentInfoDO::getMaterialConfigId);

        for (PurchaseConsignmentDetailSaveReqVO detailSaveReqVO: reqVO.getPurchaseConsignmentDetails()){
            BigDecimal totalNumber = map.get(detailSaveReqVO.getMaterialConfigId()).getQuantity();
            BigDecimal number = detailSaveReqVO.getConsignedAmount();
            BigDecimal useNumber = new BigDecimal(0);
            BigDecimal returnNumber = new BigDecimal(0);
            if (consignmentInfoDOMap.get(detailSaveReqVO.getMaterialConfigId()) != null){
                ConsignmentInfoDO consignmentInfoDO = consignmentInfoDOMap.get(detailSaveReqVO.getMaterialConfigId());
                if (consignmentInfoDO.getSignedAmount() != null){
                    useNumber = useNumber.add(consignmentInfoDO.getSignedAmount());
                }else {
                    useNumber = useNumber.add(consignmentInfoDO.getConsignedAmount());
                }
            }
            if (returnMap.get(detailSaveReqVO.getMaterialConfigId()) !=null){
                returnNumber.add(returnMap.get(detailSaveReqVO.getMaterialConfigId()).getConsignedAmount());
            }

            if (number.add(useNumber).subtract(returnNumber).compareTo(totalNumber)>0){
                throw exception(new ErrorCode(1_010_000_012, detailSaveReqVO.getMaterialName()+"数量超过限制"));
            }

        }

    }
}
