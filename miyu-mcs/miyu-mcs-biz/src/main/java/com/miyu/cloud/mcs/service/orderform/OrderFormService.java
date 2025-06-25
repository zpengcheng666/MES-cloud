package com.miyu.cloud.mcs.service.orderform;

import java.util.*;
import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.miyu.cloud.mcs.controller.admin.orderform.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.mcs.dto.schedule.ScheduleResourceType;

/**
 * 生产订单 Service 接口
 *
 * @author miyu
 */
public interface OrderFormService {

    /**
     * 创建生产订单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createOrderFormIntegral(@Valid OrderFormSaveReqVO createReqVO);

    String createOrderForm(@Valid OrderFormSaveReqVO createReqVO);

    void createOrderFormDetail(OrderFormDO orderFormDO);

    /**
     * 更新生产订单
     *
     * @param updateReqVO 更新信息
     */
    void updateOrderForm(@Valid OrderFormSaveReqVO updateReqVO);

    /**
     * 删除生产订单
     *
     * @param id 编号
     */
    void deleteOrderForm(String id);

    /**
     * 获得生产订单
     *
     * @param id 编号
     * @return 生产订单
     */
    OrderFormDO getOrderForm(String id);

    /**
     * 获得生产订单分页
     *
     * @param pageReqVO 分页查询
     * @return 生产订单分页
     */
    PageResult<OrderFormDO> getOrderFormPage(OrderFormPageReqVO pageReqVO);

    List<OrderFormDO> list(Wrapper<OrderFormDO> wrapper);

    void updateById(OrderFormDO orderForm);

    /**
     * 订单提交
     * @param id 订单id
     */
    void orderSubmit(String id);

    List<OrderFormDO> getOrderFormSelectList(OrderFormSelectListRespVO listRespVO);

    void orderCancel(String id);

    void orderDelete(String id);

    void generateDemandByOrderIds(List<String> orderIdList);

    void orderIssued(String id);

    List<ScheduleResourceType> getResourceDemandByOrderId(Collection<String> orderIdList);
}
