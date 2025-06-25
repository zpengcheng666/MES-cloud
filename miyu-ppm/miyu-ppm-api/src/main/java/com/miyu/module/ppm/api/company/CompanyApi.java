package com.miyu.module.ppm.api.company;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 企业信息")
public interface CompanyApi {

    String PREFIX = ApiConstants.PREFIX + "/company";

    /**
     * 根据企业ID查询企业信息
     * @param ids
     * @return
     */
    @GetMapping(PREFIX + "/list")
    @Operation(summary = "通过企业 ID 查询企业信息")
    @Parameter(name = "ids", description = "企业ID数组", example = "1,2", required = true)
    CommonResult<List<CompanyRespDTO>> getCompanyList(@RequestParam("ids") Collection<String> ids);


    /**
     * 获得企业 Map
     *
     * @param ids 企业ID数组
     * @return 企业 Map
     */
    default Map<String, CompanyRespDTO> getCompanyMap(Collection<String> ids) {
        List<CompanyRespDTO> companyRespDTOS = getCompanyList(ids).getCheckedData();
        return CollectionUtils.convertMap(companyRespDTOS, CompanyRespDTO::getId);
    }


    @PostMapping(PREFIX + "/update")
    @Operation(summary = "更新供应商审批状态")
    @Parameters({
            @Parameter(name = "businessKey", description = "流程编号", required = true, example = "1024"),
            @Parameter(name = "status", description = "流程定义标识", required = true, example = "xxx")
    })
    CommonResult<String>  updateCompanyAuditStatus(@RequestParam("businessKey") String businessKey, @RequestParam("status") Integer status);
}
