package cn.iocoder.yudao.module.pms.service.order;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.OrderListDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 项目订单 Service 接口
 *
 * @author 芋道源码
 */
public interface PmsOrderService {

    /**
     * 创建项目订单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createOrder(@Valid PmsOrderSaveReqVO createReqVO);

    /**
     * 更新项目订单
     *
     * @param updateReqVO 更新信息
     */
    void updateOrder(@Valid PmsOrderSaveReqVO updateReqVO);

    void updateOutOrder(@Valid PmsOrderSaveReqVO updateReqVO);

    /**
     * 删除项目订单
     *
     * @param id 编号
     */
    void deleteOrder(String id);

    /**
     * 获得项目订单
     *
     * @param id 编号
     * @return 项目订单
     */
    PmsOrderDO getOrder(String id);
    /**
     * 获得项目订单分页
     *
     * @param pageReqVO 分页查询
     * @return 项目订单分页
     */
    PageResult<PmsOrderDO> getOrderPage(PmsOrderPageReqVO pageReqVO);

    /**
     * 获得所有项目订单
     * @return
     */
    public List<PmsOrderDO> getListAll();

    /**
     * 通过项目id获取所有订单
     * @param projectId
     * @return
     */
    public List<PmsOrderDO> getListByProjectId(String projectId);

    /**
     * 获得项目订单,根据项目id
     *
     * @param projectId 编号
     * @return 项目订单
     */
    public List<PmsOrderDO> getOrderByProject(String projectId);

    public void unbind(String id);

    // ==================== 子表（项目订单表子） ====================

    /**
     * 获得项目订单表子分页
     *
     * @param pageReqVO 分页查询
     * @param projectOrderId 项目订单id
     * @return 项目订单表子分页
     */
    PageResult<OrderListDO> getOrderListPage(PageParam pageReqVO, String projectOrderId);

    /**
     * 创建项目订单表子
     *
     * @param orderList 创建信息
     * @return 编号
     */
    String createOrderList(@Valid OrderListDO orderList);

    /**
     * 更新项目订单表子
     *
     * @param orderList 更新信息
     */
    void updateOrderList(@Valid OrderListDO orderList);

    /**
     * 删除项目订单表子
     *
     * @param id 编号
     */
    void deleteOrderList(String id);

	/**
	 * 获得项目订单表子
	 *
	 * @param id 编号
     * @return 项目订单表子
	 */
    OrderListDO getOrderList(String id);

    /**
     * 获得项目订单表子列表
     *
     * @param projectOrderId 项目订单id
     * @return 项目订单表子列表
     */
    List<OrderListDO> getOrderListListByProjectOrderId(String projectOrderId);

}
