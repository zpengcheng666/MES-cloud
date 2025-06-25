package cn.iocoder.yudao.module.pms.api.plan;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.pms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 项目计划")
public interface PlanApi {

    String PREFIX = ApiConstants.PREFIX + "/plan";

    /**
     * 关闭子订单
     * 传入的是子计划id集合，生产端是orderId
     * @param ids
     * @return
     */
    @PostMapping("close-plan-item")
    @Operation(summary = "关闭子订单")
    CommonResult<String> closePlanItem(@RequestParam("ids")Collection<String> ids);
}
