package com.miyu.module.ppm.strategy.impl;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentSaveReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailSaveReqVO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentTypeEnum;
import com.miyu.module.ppm.enums.shipping.ShippingTypeEnum;
import com.miyu.module.ppm.service.consignmentinfo.ConsignmentInfoService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.purchaseconsignmentdetail.PurchaseConsignmentDetailService;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.service.shippingdetail.ShippingDetailService;
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
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;


/***
 * 外协收货验证
 */
@Service
@Transactional
public class PurchaseConsignmentOutCreate implements IConsignmentFactory {

    @Resource
    private ContractService contractService;
    @Resource
    private ShippingService shippingService;
    @Resource
    private ShippingDetailService shippingDetailService;
    @Override
    public void validateCreate(PurchaseConsignmentSaveReqVO reqVO) {
        //查询外协发货单

        List<ShippingDetailDO> shippingDetailDOS =  shippingService.getShippingDetailListByProjectId(null,reqVO.getContractId(), ShippingTypeEnum.OUTSOURCING.getStatus());
        //排除掉 外协退货单
        List<String> detailIds = shippingDetailDOS.stream().map(ShippingDetailDO::getId).collect(Collectors.toList());

        PurchaseConsignmentDetailService purchaseConsignmentDetailService = SpringContextHolder.getBean(PurchaseConsignmentDetailService.class);
        List<ConsignmentDetailDO> consignmentDetailDOS = purchaseConsignmentDetailService.getDetailListByShippingDetailIds(detailIds);
        List<String> useDetailIds = consignmentDetailDOS.stream().map(ConsignmentDetailDO::getShippingDetailId).collect(Collectors.toList());
        //剩余可接收的条码信息
        List<ShippingDetailDO> canDetails = shippingDetailDOS.stream().filter(shippingDetailDO -> !useDetailIds.contains(shippingDetailDO.getId())).collect(Collectors.toList());

        List<String> barCodes = canDetails.stream().map(ShippingDetailDO::getBarCode).collect(Collectors.toList());
        //查看已有的外协收货单

        List<ConsignmentDetailDO> detailDOS =  purchaseConsignmentDetailService.getDetailListByProjectId(null,reqVO.getContractId(), ConsignmentTypeEnum.OUT.getStatus());
        if (!CollectionUtils.isEmpty(detailDOS)){
            detailDOS = detailDOS.stream().filter(consignmentDetailDO -> !consignmentDetailDO.getConsignmentId().equals(reqVO.getId())).collect(Collectors.toList());


            if (!CollectionUtils.isEmpty(detailDOS)){
                List<String> ids = detailDOS.stream().map(ConsignmentDetailDO::getId).collect(Collectors.toList());

                List<ShippingDetailDO>  returnDetailIds =  shippingDetailService.getDetailByConsignmentDetailIds(ids);
                List<String>  returnIds = returnDetailIds.stream().map(ShippingDetailDO::getId).collect(Collectors.toList());
                detailDOS = detailDOS.stream().filter(consignmentDetailDO -> !returnIds.contains(consignmentDetailDO.getId())).collect(Collectors.toList());

            }

        }

        List<String> useBarcodes = detailDOS.stream().map(ConsignmentDetailDO::getBarCode).collect(Collectors.toList());

        //校验条码
        List<String> canUseBarcodes = barCodes.stream().filter(s -> !useBarcodes.contains(s)).collect(Collectors.toList());


        for (PurchaseConsignmentDetailSaveReqVO reqVO1 :reqVO.getPurchaseConsignmentDetails()){

            if (!canUseBarcodes.contains(reqVO1.getBarCode())){
                throw exception(new ErrorCode(1_010_000_012, reqVO1.getBarCode()+"条码已经存在发货单"));
            }

        }


    }
}
