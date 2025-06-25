package com.miyu.module.ppm.service.shippinginstoragedetail;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.shippinginstoragedetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippinginstoragedetail.ShippingInstorageDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 销售订单入库明细 Service 接口
 *
 * @author 上海弥彧
 */
public interface ShippingInstorageDetailService {

    /**
     * 创建销售订单入库明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createShippingInstorageDetail(@Valid ShippingInstorageDetailSaveReqVO createReqVO);

    /**
     * 更新销售订单入库明细
     *
     * @param updateReqVO 更新信息
     */
    void updateShippingInstorageDetail(@Valid ShippingInstorageDetailSaveReqVO updateReqVO);

    /**
     * 删除销售订单入库明细
     *
     * @param id 编号
     */
    void deleteShippingInstorageDetail(String id);

    /**
     * 获得销售订单入库明细
     *
     * @param id 编号
     * @return 销售订单入库明细
     */
    ShippingInstorageDetailDO getShippingInstorageDetail(String id);

    /**
     * 获得销售订单入库明细分页
     *
     * @param pageReqVO 分页查询
     * @return 销售订单入库明细分页
     */
    PageResult<ShippingInstorageDetailDO> getShippingInstorageDetailPage(ShippingInstorageDetailPageReqVO pageReqVO);

}