package com.miyu.module.mcc.api.encodingrule;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.miyu.module.mcc.api.encodingrule.dto.DemoDTO;
import com.miyu.module.mcc.api.encodingrule.dto.GeneratorCodeReqDTO;
import com.miyu.module.mcc.api.encodingrule.dto.UpdateCodeReqDTO;
import com.miyu.module.mcc.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 编码规则")
public interface EncodingRuleApi {


    String PREFIX = ApiConstants.PREFIX + "/encoding-generator";



    @PostMapping(PREFIX + "/getAndGeneratorMaterialConfig")
    @Operation(summary = "获取半成品类码")
    CommonResult<DemoDTO> getAndGeneratorMaterialConfig(@RequestBody DemoDTO dto);


    @PostMapping(PREFIX + "/generatorCode")
    @Operation(summary = "生码")
    CommonResult<String> generatorCode(@RequestBody GeneratorCodeReqDTO dto) throws InterruptedException;


    @PostMapping(PREFIX + "/generatorNewCodeByOldCode")
    @Operation(summary = "根据旧码生成新码")
    CommonResult<String> generatorNewCodeByOldCode(@RequestBody GeneratorCodeReqDTO dto) throws InterruptedException, JsonProcessingException;


    @PostMapping(PREFIX + "/updateCodeStatus")
    @Operation(summary = "更新码状态")
    CommonResult<String> updateCodeStatus(@RequestBody UpdateCodeReqDTO dto) throws InterruptedException;

}
