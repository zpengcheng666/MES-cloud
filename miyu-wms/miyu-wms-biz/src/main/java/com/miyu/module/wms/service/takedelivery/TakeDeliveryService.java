package com.miyu.module.wms.service.takedelivery;

import java.util.*;
import javax.validation.*;
import com.miyu.module.wms.controller.admin.takedelivery.vo.*;
import com.miyu.module.wms.dal.dataobject.takedelivery.TakeDeliveryDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 物料收货 Service 接口
 *
 * @author QianJy
 */
public interface TakeDeliveryService {

    /**
     * 创建物料收货
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createTakeDelivery(@Valid TakeDeliverySaveReqVO createReqVO);

    /**
     * 更新物料收货
     *
     * @param updateReqVO 更新信息
     */
    void updateTakeDelivery(@Valid TakeDeliverySaveReqVO updateReqVO);

    /**
     * 删除物料收货
     *
     * @param id 编号
     */
    void deleteTakeDelivery(String id);

    /**
     * 获得物料收货
     *
     * @param id 编号
     * @return 物料收货
     */
    TakeDeliveryDO getTakeDelivery(String id);

    /**
     * 获得物料收货分页
     *
     * @param pageReqVO 分页查询
     * @return 物料收货分页
     */
    PageResult<TakeDeliveryDO> getTakeDeliveryPage(TakeDeliveryPageReqVO pageReqVO);

}