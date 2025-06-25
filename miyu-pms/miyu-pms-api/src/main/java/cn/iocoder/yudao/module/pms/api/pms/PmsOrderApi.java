package cn.iocoder.yudao.module.pms.api.pms;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.pms.api.pms.dto.OrderFillContractDTO;
import cn.iocoder.yudao.module.pms.api.pms.dto.OrderListDTO;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsPlanDTO;
import cn.iocoder.yudao.module.pms.api.pms.orderDto.PmsOrderRespDTO;
import cn.iocoder.yudao.module.pms.api.pms.orderDto.PmsOrderSaveReqDTO;
import cn.iocoder.yudao.module.pms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "RPC 服务 - 项目订单信息")
public interface PmsOrderApi {

    String PREFIX = ApiConstants.PREFIX+ "/company";
    /**
     * 通过项目id直接查询所有子订单(跳过订单)
     * @param ProjectId
     * @return
     */
    @GetMapping(PREFIX + "/selectOrderItemListByPid")
    @Operation(summary = "补充合同id")
    CommonResult<List<OrderListDTO>>  getOrderItemList(@RequestParam("ProjectId") String ProjectId);

    /**
     * 供外部创建订单使用
     * @param req
     * @return
     */
    @PostMapping(PREFIX + "/createOrder")
    @Operation(summary = "补充合同id")
    CommonResult<String>  createOrder(@Valid @RequestBody PmsOrderSaveReqDTO req);

    @GetMapping(PREFIX + "/listByOrderStatus")
    @Operation(summary = "通过订单状态查询订单(0,未开始,1,待审核，2待评审，3,准备；4，生产；5,出库；6，关闭)")
    @Parameter(name = "orderStatusList", description = "部门编号数组", example = "1,2", required = true)
    CommonResult<List<PmsOrderRespDTO>> listByOrderStatus(@RequestParam("orderStatusList") Collection<Integer> orderStatusList);
}
