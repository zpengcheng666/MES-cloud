package com.miyu.module.qms.api.inspectionsheet;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.qms.api.dto.InspectionSheetReqDTO;
import com.miyu.module.qms.api.dto.InspectionSheetRespDTO;
import com.miyu.module.qms.api.dto.InspectionSheetSchemeMaterialRespDTO;
import com.miyu.module.qms.api.dto.InspectionSheetSelfReqDTO;
import com.miyu.module.qms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 检验单")
public interface InspectionSheetApi {

    String PREFIX = ApiConstants.PREFIX + "/inspectionsheet";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建检验单")
    CommonResult<String> createInspectionSheet(@RequestBody InspectionSheetReqDTO reqDTO);

    @PostMapping(PREFIX + "/createSelf")
    @Operation(summary = "自检创建检验单")
    CommonResult<String> createInspectionSheetSelfInspection(@RequestBody InspectionSheetSelfReqDTO reqDTO);

    /***
     * 根据来源单号查询检验单集合
     * @param numbers
     * @return
     */
    @GetMapping(PREFIX + "/listByRecordNumber")
    @Operation(summary = "来源单号查询检验单集合")
    @Parameter(name = "numbers", description = "来源单号数组", example = "1,2", required = true)
    CommonResult<List<InspectionSheetRespDTO>> getInspectionSheetListByRecordNumber(@RequestParam("ids") Collection<String> numbers);

    /**
     * 检验单号查询检验单产品检验结果（生产）
     * @param number
     * @return
     */
    @GetMapping(PREFIX + "/listMaterialByRecordNumber")
    @Operation(summary = "生产单号查询检验单")
    @Parameter(name = "number", description = "生产单号", example = "1", required = true)
    CommonResult<List<InspectionSheetSchemeMaterialRespDTO>> getInspectionMaterialListByRecordNumber(@RequestParam("number") String number, @RequestHeader("Tenant-Id") String tenantId);

    /**
     * 检验单号查询检验单产品检验结果（采购、销售）
     * @param numbers
     * @return
     */
    @GetMapping(PREFIX + "/listMaterialByRecordNumberBatch")
    @Operation(summary = "生产单号查询检验单")
    @Parameter(name = "numbers", description = "生产单号", example = "1", required = true)
    CommonResult<List<InspectionSheetSchemeMaterialRespDTO>> getInspectionMaterialListByRecordNumber(@RequestParam("numbers") Collection<String> numbers);
}
