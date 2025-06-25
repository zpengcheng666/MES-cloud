package com.miyu.module.pdm.api.projectAssessment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.api.projectAssessment.dto.TechnologyAssessmentRespDTO;
import com.miyu.module.pdm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 技术评估-相关数据获取")
public interface TechnologyAssessmentApi {

    String PREFIX = ApiConstants.PREFIX + "/technology";

    @GetMapping(PREFIX + "/getTechnologyAssement")
    @Operation(summary = "获取技术评估相关数据")
    CommonResult<TechnologyAssessmentRespDTO> getTechnologyAssement(@RequestParam("projectCode") String projectCode);

}
