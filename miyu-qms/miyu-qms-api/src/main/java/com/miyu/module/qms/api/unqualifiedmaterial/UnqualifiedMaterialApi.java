package com.miyu.module.qms.api.unqualifiedmaterial;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.qms.api.dto.UnqualifiedMaterialRespDTO;
import com.miyu.module.qms.enums.ApiConstants;
import com.miyu.module.qms.api.dto.UnqualifiedMaterialReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 不合格品处理信息")
public interface UnqualifiedMaterialApi {

    String PREFIX = ApiConstants.PREFIX + "/unqualifiedmaterial";

    @PostMapping(PREFIX + "/update")
    @Operation(summary = "更新不合格品处理方式")
    CommonResult<String>  updateUnqualifiedMaterial(@RequestBody List<UnqualifiedMaterialReqDTO> reqDTOList);


    /***
     * 根据barcode查询不合格品处理结果集合
     * @param barCodes
     * @return
     */
    @GetMapping(PREFIX + "/listUnqualifiedMaterialByBarCodes")
    @Operation(summary = "物料条码集合查询不合格品处理结果集合")
    @Parameter(name = "barCodes", description = "物料条码数组", example = "1,2", required = true)
    CommonResult<List<UnqualifiedMaterialRespDTO>> getUnqualifiedMaterialListByBarCodes(@RequestParam("barCodes") Collection<String> barCodes);


    @PostMapping(PREFIX + "/updateAudit")
    @Operation(summary = "更新资料库审批状态")
    @Parameters({
            @Parameter(name = "businessKey", description = "流程编号", required = true, example = "1024"),
            @Parameter(name = "status", description = "流程定义标识", required = true, example = "xxx")
    })
    CommonResult<String>  updateUnqualifiedAuditStatus(@RequestParam("businessKey") String businessKey, @RequestParam("status") Integer status);
}
