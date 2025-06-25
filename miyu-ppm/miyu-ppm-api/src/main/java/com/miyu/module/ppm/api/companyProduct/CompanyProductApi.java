package com.miyu.module.ppm.api.companyProduct;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.companyProduct.dto.CompanyProductDTO;
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
@Tag(name = "RPC 服务 - 企业产品信息")
public interface CompanyProductApi {

    String PREFIX = ApiConstants.PREFIX + "/company-product";

    /**
     * 根据企业ID查询企业产品信息信息
     * @param id
     * @return
     */
    @GetMapping(PREFIX + "/list")
    @Operation(summary = "通过企业 ID 查询企业产品信息")
    @Parameter(name = "id", description = "企业ID", example = "1,2", required = true)
    CommonResult<List<CompanyProductDTO>> getCompanyProductList(@RequestParam("id")String id);


}
