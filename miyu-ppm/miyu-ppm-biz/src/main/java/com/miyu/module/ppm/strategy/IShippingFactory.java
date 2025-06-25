package com.miyu.module.ppm.strategy;



import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentSaveReqVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingSaveReqVO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;

public interface IShippingFactory {



    void  validateCreate(ShippingSaveReqVO reqVO);

}
