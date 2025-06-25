package com.miyu.module.ppm.service.shippinginstorage;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.shippinginstorage.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippinginstorage.ShippingInstorageDO;
import com.miyu.module.ppm.dal.dataobject.shippinginstoragedetail.ShippingInstorageDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 销售订单入库 Service 接口
 *
 * @author 上海弥彧
 */
public interface ShippingInstorageService {



    void checkInBoundInfo();

    void checkSchemeSheetResult();

    /****
     * 委托加工入库
     * @param id
     * @param status
     */
    void updateShippingInstorgerProcessInstanceStatus(String id,Integer status);

    /***
     * 委托加工退货
     * @param id
     * @param status
     */
    void updateShippingProcessInstanceStatus(String id,Integer status);

}