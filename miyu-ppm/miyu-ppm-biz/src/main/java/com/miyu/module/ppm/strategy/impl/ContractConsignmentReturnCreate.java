package com.miyu.module.ppm.strategy.impl;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentSaveReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailSaveReqVO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
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
 * 外协原材料退货
 */
@Service
@Transactional
public class ContractConsignmentReturnCreate implements IConsignmentFactory {


    @Resource
    private ShippingService shippingService;
    @Override
    public void validateCreate(PurchaseConsignmentSaveReqVO reqVO) {

        //查询外协发货单
        List<ShippingDetailDO>  shippingDetailDOS = shippingService.getShippingDetailListByProjectId(null,reqVO.getContractId(), ShippingTypeEnum.OUTSOURCING.getStatus());

        List<String> detailIds = shippingDetailDOS.stream().map(ShippingDetailDO::getId).collect(Collectors.toList());
        List<String> barCodes = shippingDetailDOS.stream().map(ShippingDetailDO::getBarCode).collect(Collectors.toList());

        PurchaseConsignmentDetailService purchaseConsignmentDetailService = SpringContextHolder.getBean(PurchaseConsignmentDetailService.class);
        //查询外协退货单
        List<ConsignmentDetailDO> returnDos = purchaseConsignmentDetailService.getDetailListByShippingDetailIds(detailIds);

        returnDos = returnDos.stream().filter(consignmentDetailDO -> !consignmentDetailDO.getConsignmentId().equals(reqVO.getId())).collect(Collectors.toList());
        List<String> useBarCode = returnDos.stream().map(ConsignmentDetailDO::getBarCode).collect(Collectors.toList());


        List<String> canBarcodes = barCodes.stream().filter(s -> !useBarCode.contains(s)).collect(Collectors.toList());


        for (PurchaseConsignmentDetailSaveReqVO reqVO1 :reqVO.getPurchaseConsignmentDetails()){
            if (canBarcodes.contains(reqVO1.getBarCode())){
                throw exception(new ErrorCode(1_010_000_012, reqVO1.getBarCode()+"条码已经存在发货单"));
            }
        }

    }
}
