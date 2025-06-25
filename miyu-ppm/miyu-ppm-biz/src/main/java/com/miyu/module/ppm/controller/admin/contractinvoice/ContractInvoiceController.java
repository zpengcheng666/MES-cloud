package com.miyu.module.ppm.controller.admin.contractinvoice;

import com.miyu.module.ppm.dal.dataobject.contractinvoice.ContractInvoiceDetailDO;
import com.miyu.module.ppm.service.contractpayment.ContractPaymentService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

import com.miyu.module.ppm.controller.admin.contractinvoice.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractinvoice.ContractInvoiceDO;
import com.miyu.module.ppm.service.contractinvoice.ContractInvoiceService;

@Tag(name = "管理后台 - 购销合同发票")
@RestController
@RequestMapping("/ppm/contract-invoice")
@Validated
public class ContractInvoiceController {

    @Resource
    private ContractInvoiceService contractInvoiceService;

    @Resource
    private ContractPaymentService contractPaymentService;

    @PostMapping("/create")
    @Operation(summary = "创建购销合同发票")
    @PreAuthorize("@ss.hasPermission('ppm:contract-invoice:create')")
    public CommonResult<String> createContractInvoice(@Valid @RequestBody ContractInvoiceSaveReqVO createReqVO) {
        return success(contractInvoiceService.createContractInvoice(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新购销合同发票")
    @PreAuthorize("@ss.hasPermission('ppm:contract-invoice:update')")
    public CommonResult<Boolean> updateContractInvoice(@Valid @RequestBody ContractInvoiceSaveReqVO updateReqVO) {
        contractInvoiceService.updateContractInvoice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除购销合同发票")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:contract-invoice:delete')")
    public CommonResult<Boolean> deleteContractInvoice(@RequestParam("id") String id) {
        contractInvoiceService.deleteContractInvoice(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得购销合同发票")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract-invoice:query')")
    public CommonResult<ContractInvoiceRespVO> getContractInvoice(@RequestParam("id") String id) {
        ContractInvoiceDO contractInvoice = contractInvoiceService.getContractInvoice(id);
        // 获取合同发票集合
        List<ContractInvoiceDetailDO> invoiceList = contractInvoiceService.getContractInvoiceDetailListByInvoiceId(id);
        return success(BeanUtils.toBean(contractInvoice, ContractInvoiceRespVO.class, vo -> {
            vo.setInvoiceDetails(BeanUtils.toBean(invoiceList, ContractInvoiceRespVO.Invoice.class));
        }));
    }

    @GetMapping("/page")
    @Operation(summary = "获得购销合同发票分页")
    @PreAuthorize("@ss.hasPermission('ppm:contract-invoice:query')")
    public CommonResult<PageResult<ContractInvoiceRespVO>> getContractInvoicePage(@Valid ContractInvoicePageReqVO pageReqVO) {
        PageResult<ContractInvoiceDO> pageResult = contractInvoiceService.getContractInvoicePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ContractInvoiceRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出购销合同发票 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:contract-invoice:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportContractInvoiceExcel(@Valid ContractInvoicePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ContractInvoiceDO> list = contractInvoiceService.getContractInvoicePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "购销合同发票.xls", "数据", ContractInvoiceRespVO.class,
                        BeanUtils.toBean(list, ContractInvoiceRespVO.class));
    }

    @PutMapping("/submit")
    @Operation(summary = "提交合同审批")
    @PreAuthorize("@ss.hasPermission('ppm:contract-invoice:update')")
    public CommonResult<Boolean> submitContract(@RequestParam("id") String id,  @RequestParam("processKey") String processKey) {
        contractInvoiceService.submitContractInvoice(id, processKey, getLoginUserId());
        return success(true);
    }

    @PostMapping("/createAndSubmit")
    @Operation(summary = "创建并提交合同审批")
    @PreAuthorize("@ss.hasPermission('ppm:contract-invoice:create')")
    public CommonResult<Boolean> createAndsubmit(@Valid @RequestBody ContractInvoiceSaveReqVO createReqVO) {
        contractInvoiceService.createAndSubmitContractInvoice(createReqVO);
        return success(true);
    }

    // ==================== 子表（购销合同发票表详细） ====================

    @GetMapping("/contract-invoice-detail/list-by-invoice-id")
    @Operation(summary = "获得购销合同发票表详细列表")
    @Parameter(name = "invoiceId", description = "合同发票ID")
    @PreAuthorize("@ss.hasPermission('ppm:contract-invoice:query')")
    public CommonResult<List<ContractInvoiceDetailDO>> getContractInvoiceDetailListByInvoiceId(@RequestParam("invoiceId") String invoiceId) {
        return success(contractInvoiceService.getContractInvoiceDetailListByInvoiceId(invoiceId));
    }

}