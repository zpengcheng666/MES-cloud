package com.miyu.module.wms.api.order;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.wms.api.mateiral.dto.CarryTrayStatusDTO;
import com.miyu.module.wms.api.order.dto.*;
import com.miyu.module.wms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 订单模块")
public interface OrderApi {

    String PREFIX = ApiConstants.PREFIX + "/order";

    /**
     * 订单批量接收
     * 订单 分为 ：入库，出库，移库
     * 以立体库为主体 入立体库为入库，出立体库为出库
     * 在非产线库中转移为 移库
     * @param orderReqDTOList
     * @return
     */
    @PostMapping(PREFIX + "/batch/distribute")
    @Operation(summary = "订单批量接收")
    CommonResult<List<String>> orderDistribute(@RequestBody List<OrderReqDTO> orderReqDTOList);


    /**
     * 订单批量查询 目前用于返回 通过订单号 和 订单类型 查询订单状态详情
     * 必填字段：订单类型 订单号
     *
     * @param orderReqDTOList
     * @return
     */
    @PostMapping(PREFIX + "/batch/list")
    @Operation(summary = "订单批量查询")
    CommonResult<List<OrderReqDTO>> orderList(@RequestBody List<OrderReqDTO> orderReqDTOList);


    /**
     * 根据物料条码查询出入库单
     *
     * 非必填：orderType: 1-入库单 2-出库单 3-移库单 //不传默认全查
     *
     * @param barCode
     * @return
     */
    @GetMapping(PREFIX + "/getOrderListByChooseBarCode")
    @Operation(summary = "根据物料条码查询出入库单")
    CommonResult<List<OrderReqDTO>> getOrderListByChooseBarCode(@RequestParam("barCode") String barCode, @RequestParam(value = "type", required = false) Integer type);


    /**
     * 根据订单类型和批次号查询订单详情
     * 必填字段：订单类型 物料批次号
     *
     * @param orderReqDTO
     * @return
     */
    @PostMapping(PREFIX + "/getOrderListByOrderTypeAndBatchNumber")
    @Operation(summary = "根据订单类型和批次号查询订单详情")
    CommonResult<List<OrderReqDTO>> getOrderListByOrderTypeAndBatchNumber(@RequestBody OrderReqDTO orderReqDTO);

    /**
     * 采购入库单批量接收  采购入库、原材料入库专用接口
     * @param orderReqDTOList
     * @return
     */
    @PostMapping(PREFIX + "/batch/purchaseindistribute")
    @Operation(summary = "采购入库单、原材料入库单批量接收")
    CommonResult<List<String>> orderPurchaseInDistribute(@RequestBody List<OrderReqDTO> orderReqDTOList);

    /**
     * 销售退货入库单批量接收  销售退货入库专用接口
     * @param orderReqDTOList
     * @return
     */
    @PostMapping(PREFIX + "/batch/purchasereturnindistribute")
    @Operation(summary = "销售退货入库批量接收")
    CommonResult<List<String>> orderPurchaseReturnInDistribute(@RequestBody List<OrderReqDTO> orderReqDTOList);


    /**
     * 出入库单状态更新  仅限更新状态为 待审批（6） 待质检（0） 待出/入库（1） 已关闭（5）
     * 必填字段： 订单类型 订单状态 选择的物料id
     * @param orderReqDTOList
     * @return
     */
    @PostMapping(PREFIX + "/batch/updateStatus")
    @Operation(summary = "订单状态更新")
    CommonResult<List<String>> orderUpdateStatus(@RequestBody List<OrderReqDTO> orderReqDTOList);

    /**
     * 更新 目标仓库
     * 目前仅用于 更新出库单目标仓库（必填） 和 订单状态（选填）
     *
     * @param orderUpdateDTOList
     * @return
     */
    @PostMapping(PREFIX + "/batch/updateTargetWarehouse")
    @Operation(summary = "订单更新目标仓库")
    CommonResult<List<String>> updateTargetWarehouse(@RequestBody List<OrderUpdateDTO> orderUpdateDTOList);


    /**
     * 自动生产调度
     * @param productionOrderDTOList
     * @return
     */
    @PostMapping(PREFIX + "/batch/autoProductionDispatch")
    @Operation(summary = "自动生产调度")
    CommonResult<List<ProductionOrderRespDTO>> autoProductionDispatch(@Valid @RequestBody List<ProductionOrderReqDTO> productionOrderDTOList);

    /**
     * 指定库位间搬运
     *  从接驳位到接驳位之间的移库
     *  目前仅用于 三坐标 静置位到检测位的搬运
     * @param specifiedTransportationReqDTOList
     * @return
     */
    @PostMapping(PREFIX + "/batch/specifiedStorageSpaceTransportation")
    @Operation(summary = "指定库位间搬运")
    CommonResult<List<SpecifiedTransportationRespDTO>> specifiedStorageSpaceTransportation(@Valid @RequestBody List<SpecifiedTransportationReqDTO> specifiedTransportationReqDTOList);


    /**
     * 获取呼叫的托盘 运输状态
     * @return
     */
    @GetMapping(PREFIX + "/list/getCallTrayStatusByBarCodes")
    @Operation(summary = "获取呼叫的托盘的运输状态")
    CommonResult<Map<String, CarryTrayStatusDTO>> getCallTrayStatus();


    /**
     * 查询所有未完成的 出库单、移库单 -- 用于追踪物料的运输状态
     * @return
     */
    @GetMapping(PREFIX + "/list/getNotCompleteOrder")
    @Operation(summary = "查询所有未完成的 出库单、移库单")
    CommonResult<List<OrderReqDTO>> getNotCompleteOrder();
}
