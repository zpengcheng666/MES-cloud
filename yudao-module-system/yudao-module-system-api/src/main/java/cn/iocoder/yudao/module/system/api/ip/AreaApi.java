package cn.iocoder.yudao.module.system.api.ip;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.system.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 区域")
public interface AreaApi {

    String PREFIX = ApiConstants.PREFIX + "/area";

    @GetMapping(PREFIX + "/get")
    @Operation(summary = "获得区域信息")
    @Parameter(name = "id", description = "获取格式化区域", example = "1024", required = true)
    CommonResult<String> format(@RequestParam("id") Integer id);

    default String getArea(Integer id) {
        if(id == null){
            return "";
        }
        return format(id).getCheckedData();
    }
}
