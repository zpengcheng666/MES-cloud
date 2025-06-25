package com.miyu.cloud.mcs.restServer.service.order;

import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;

import java.util.Collection;
import java.util.List;

public interface OrderRestService {

    //首任务 外协通知项目
    void outsourcingBegin(BatchOrderDO batchOrderDO);

    //工步任务完成后 通知项目物料变更(物码,进度)
    void orderMaterialUpdate(String barCode, OrderFormDO orderFormDO, String procedureNum, Integer status);

    //一生多加工 编码添加
    void createMaterialInOrder(OrderFormDO orderFormDO, String barCode, String procedureNum);

    //校验物料是否可使用
    boolean checkMaterialUsageInfo(Collection<String> barCodeList);

    void updateOrderMaterial(String orderNumber, List<String> barCodeList);
}
