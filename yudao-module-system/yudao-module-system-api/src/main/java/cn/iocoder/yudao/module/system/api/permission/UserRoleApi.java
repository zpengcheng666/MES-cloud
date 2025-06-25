package cn.iocoder.yudao.module.system.api.permission;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 用户角色")
public interface UserRoleApi {

    String PREFIX = ApiConstants.PREFIX + "/userRole";

    @GetMapping(PREFIX + "/getByUserId")
    @Operation(summary = "校验角色是否合法")
    @Parameter(name = "ids", description = "角色编号数组", example = "1,2", required = true)
    CommonResult<List<String>> getByUserId(@RequestParam("id") String userId);

    @GetMapping(PREFIX + "/getUserIdByRole")
    @Operation(summary = "根据角色获取用户Id")
    CommonResult<List<String>> getUserIdByRole(@RequestParam("roleId") String roleId);

}
