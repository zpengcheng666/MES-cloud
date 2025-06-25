package com.miyu.module.ppm.strategy.impl;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentSaveReqVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingSaveReqVO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.purchaseconsignmentdetail.PurchaseConsignmentDetailService;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.strategy.IConsignmentFactory;
import com.miyu.module.ppm.strategy.IShippingFactory;
import com.miyu.module.ppm.utils.SpringContextHolder;
import groovy.lang.Lazy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/***
 * 销售退货校验
 */
@Service
@Transactional
public class ShippingCreate implements IShippingFactory {


    @Override
    public void validateCreate(ShippingSaveReqVO reqVO) {

        ShippingService shippingService = SpringContextHolder.getBean(ShippingService.class);
        //根据项目查询已经发货的发货单
        List<ShippingDetailDO> shippingDetailDOS =  shippingService.getShippingDetailListByProjectId(reqVO.getProjectId(),null,reqVO.getShippingType());


        if (!CollectionUtils.isEmpty(shippingDetailDOS)){

            List<String> detailIds = shippingDetailDOS.stream().map(ShippingDetailDO::getId).collect(Collectors.toList());
            PurchaseConsignmentDetailService purchaseConsignmentDetailService = SpringContextHolder.getBean(PurchaseConsignmentDetailService.class);
            List<ConsignmentDetailDO> detailDOS = purchaseConsignmentDetailService.getDetailListByShippingDetailIds(detailIds);


            List<String> useDetailIds = new ArrayList<>();
            if (!org.springframework.util.CollectionUtils.isEmpty(detailDOS)){
                useDetailIds.addAll(detailDOS.stream().map(ConsignmentDetailDO::getShippingDetailId).collect(Collectors.toList()));
            }
            List<ShippingDetailDO>  shippingDetailDOs = shippingDetailDOS.stream().filter(shippingDetailDO -> !useDetailIds.contains(shippingDetailDO.getId())).collect(Collectors.toList());

            if (StringUtils.isNotBlank(reqVO.getId())){
                shippingDetailDOs = shippingDetailDOS.stream().filter(shippingDetailDO -> !shippingDetailDO.getShippingId().equals(reqVO.getId())).collect(Collectors.toList());
            }


            List<String> barCodes = shippingDetailDOs.stream().map(ShippingDetailDO::getBarCode).collect(Collectors.toList());

            List<ShippingDetailDO> list = reqVO.getShippingDetails().stream().filter(shippingDetailDO -> barCodes.contains(shippingDetailDO.getBarCode())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(list)){
                throw exception(new ErrorCode(1_010_000_012, list.get(0).getBarCode()+"条码已经存在发货单"));
            }


        }




    }
}
