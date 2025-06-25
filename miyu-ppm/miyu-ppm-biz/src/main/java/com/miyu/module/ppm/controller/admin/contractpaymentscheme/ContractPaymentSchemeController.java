package com.miyu.module.ppm.controller.admin.contractpaymentscheme;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

import com.miyu.module.ppm.controller.admin.contractpaymentscheme.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractpaymentscheme.ContractPaymentSchemeDO;
import com.miyu.module.ppm.service.contractpaymentscheme.ContractPaymentSchemeService;

@Tag(name = "管理后台 - 合同付款计划")
@RestController
@RequestMapping("/ppm/contract-payment-scheme")
@Validated
public class ContractPaymentSchemeController {

    @Resource
    private ContractPaymentSchemeService contractPaymentSchemeService;

    @PostMapping("/create")
    @Operation(summary = "创建合同付款计划")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment-scheme:create')")
    public CommonResult<String> createContractPaymentScheme(@Valid @RequestBody ContractPaymentSchemeSaveReqVO createReqVO) {
        return success(contractPaymentSchemeService.createContractPaymentScheme(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新合同付款计划")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment-scheme:update')")
    public CommonResult<Boolean> updateContractPaymentScheme(@Valid @RequestBody ContractPaymentSchemeSaveReqVO updateReqVO) {
        contractPaymentSchemeService.updateContractPaymentScheme(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除合同付款计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment-scheme:delete')")
    public CommonResult<Boolean> deleteContractPaymentScheme(@RequestParam("id") String id) {
        contractPaymentSchemeService.deleteContractPaymentScheme(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得合同付款计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment-scheme:query')")
    public CommonResult<ContractPaymentSchemeRespVO> getContractPaymentScheme(@RequestParam("id") String id) {
        ContractPaymentSchemeDO contractPaymentScheme = contractPaymentSchemeService.getContractPaymentScheme(id);
        return success(BeanUtils.toBean(contractPaymentScheme, ContractPaymentSchemeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得合同付款计划分页")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment-scheme:query')")
    public CommonResult<PageResult<ContractPaymentSchemeRespVO>> getContractPaymentSchemePage(@Valid ContractPaymentSchemePageReqVO pageReqVO) {
        PageResult<ContractPaymentSchemeDO> pageResult = contractPaymentSchemeService.getContractPaymentSchemePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ContractPaymentSchemeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出合同付款计划 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment-scheme:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportContractPaymentSchemeExcel(@Valid ContractPaymentSchemePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ContractPaymentSchemeDO> list = contractPaymentSchemeService.getContractPaymentSchemePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "合同付款计划.xls", "数据", ContractPaymentSchemeRespVO.class,
                        BeanUtils.toBean(list, ContractPaymentSchemeRespVO.class));
    }
}