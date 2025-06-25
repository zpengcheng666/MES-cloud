package com.miyu.module.mcc.api.materialtype;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigReqDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.mcc.api.materialtype.dto.MaterialTypeReqDTO;
import com.miyu.module.mcc.api.materialtype.dto.MaterialTypeRespDTO;
import com.miyu.module.mcc.enums.ApiConstants;
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


@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 编码类别属性")
public interface MaterialTypeApi {

    String PREFIX = ApiConstants.PREFIX + "/material_type";

    @GetMapping(PREFIX + "/getByCode")
    @Operation(summary = "根据类别码查询类别")
    CommonResult<MaterialTypeRespDTO> getByCode(@RequestParam("code")String code);

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建类别")
    CommonResult<String> createMaterialType(@Valid @RequestBody MaterialTypeReqDTO reqDTO);

    @GetMapping(PREFIX + "/list")
    @Operation(summary = "通过 ID 查询类别")
    @Parameter(name = "ids", description = "类别IDs", example = "1,2", required = true)
    CommonResult<List<MaterialTypeRespDTO>> getMaterialTypeList(@RequestParam("ids") Collection<String> ids);



    @GetMapping(PREFIX + "/listByProperty")
    @Operation(summary = "通过 类别属性 查询类别")
    @Parameter(name = "encodingProperty", description = "类别属性", example = "1,2", required = true)
    CommonResult<List<MaterialTypeRespDTO>> getMaterialTypeListByProperty(@RequestParam("encodingProperty") Integer encodingProperty);
}
