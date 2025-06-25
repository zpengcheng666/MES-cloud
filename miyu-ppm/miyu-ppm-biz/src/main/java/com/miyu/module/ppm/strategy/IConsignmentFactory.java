package com.miyu.module.ppm.strategy;



import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentSaveReqVO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;

import java.util.List;
import java.util.Map;

public interface IConsignmentFactory {


    void validateCreate(PurchaseConsignmentSaveReqVO reqVO);

}
