package com.miyu.module.wms.controller.admin.order;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


@Tag(name = "管理后台 - 物流出入库订单管理")
@RestController
@RequestMapping("/wms/order")
@Validated
public class OrderController {

    @Resource
    private OrderApi orderApi;
    /**
     * 订单批量接收
     * 订单 分为 ：入库，出库，移库
     * 以立体库为主体 入立体库为入库，出立体库为出库
     * 在非产线库中转移为 移库
     * @param orderReqDTOList
     * @return
     */
    @PostMapping("/batch/distribute")
    @Operation(summary = "订单批量接收")
    CommonResult<List<String>> orderDistribute(@RequestBody List<OrderReqDTO> orderReqDTOList){
        return orderApi.orderDistribute(orderReqDTOList);
    }

    /**
     * 订单批量查询 目前用于返回 通过订单号 和 订单类型 查询订单状态详情
     * 必填字段：订单类型 订单号
     * 示例：
     * [
     *   {
     *     "orderNumber": "我是零件",
     *     "orderType": 1,
     *   }
     * ]
     * @param orderReqDTOList
     * @return
     */
    @PostMapping("/batch/list")
    @Operation(summary = "订单批量查询")
    CommonResult<List<OrderReqDTO>> orderList(@RequestBody List<OrderReqDTO> orderReqDTOList){
        return orderApi.orderList(orderReqDTOList);
    }

    /**
     * 根据订单类型和批次号查询订单详情
     * 必填字段：订单类型 物料批次号
     * 示例：
     * [
     *   {
     *     "orderType": "1",
     *     "batchNumber": 1,
     *   }
     * ]
     * @param orderReqDTO
     * @return
     */
    @PostMapping("/getOrderListByOrderTypeAndBatchNumber")
    @Operation(summary = "根据订单类型和批次号查询订单详情")
    CommonResult<List<OrderReqDTO>> getOrderListByOrderTypeAndBatchNumber(@RequestBody OrderReqDTO orderReqDTO){
        return orderApi.getOrderListByOrderTypeAndBatchNumber(orderReqDTO);
    }



    /**
     * 采购入库单批量接收  采购入库专用接口
     * 测试数据：
     * [
     *   {
     *     "orderNumber": "123",
     *     "orderStatus": 1, //非必填
     *     "materialId": "1803264792352952321",
     *     "quantity": 1
     *   }
     * ]
     * @param orderReqDTOList
     * @return
     */
    @PostMapping("/batch/purchaseindistribute")
    @Operation(summary = "采购入库单批量接收")
    CommonResult<List<String>> orderPurchaseInDistribute(@RequestBody List<OrderReqDTO> orderReqDTOList){
        return orderApi.orderPurchaseInDistribute(orderReqDTOList);
    }
    /**
     * 销售退货入库单批量接收  销售退货入库专用接口
     *  测试数据：
     * [
     *   {
     *     "orderNumber": "123",
     *     "targetWarehouseId": "1802877800038940674",
     *     "materialStockId": "1807957569231138817",
     *     "quantity": 1
     *   }
     * ]
     * @param orderReqDTOList
     * @return
     */
    @PostMapping("/batch/purchasereturnindistribute")
    @Operation(summary = "销售退货入库批量接收")
    CommonResult<List<String>> orderPurchaseReturnInDistribute(@RequestBody List<OrderReqDTO> orderReqDTOList){
        return orderApi.orderPurchaseReturnInDistribute(orderReqDTOList);
    }



    /**
     * 出入库单状态更新  仅限更新状态为 待入库、待出库、待移库、已关闭
     * 只需要传递 订单类型 订单状态 物料id
     * @param orderReqDTOList
     * @return
     */
    @PostMapping("/batch/updateStatus")
    @Operation(summary = "订单状态更新")
    CommonResult<List<String>> orderUpdateStatus(@RequestBody List<OrderReqDTO> orderReqDTOList){
        return orderApi.orderUpdateStatus(orderReqDTOList);
    }



}
