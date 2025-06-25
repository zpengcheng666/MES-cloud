package com.miyu.module.ppm.controller.admin.contractpayment;

import com.miyu.module.ppm.controller.admin.contractpaymentscheme.vo.ContractPaymentSchemeRespVO;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDetailDO;
import com.miyu.module.ppm.dal.dataobject.contractpaymentscheme.ContractPaymentSchemeDO;
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
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

import com.miyu.module.ppm.controller.admin.contractpayment.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDO;
import com.miyu.module.ppm.service.contractpayment.ContractPaymentService;

@Tag(name = "管理后台 - 合同付款")
@RestController
@RequestMapping("/ppm/contract-payment")
@Validated
public class ContractPaymentController {

    @Resource
    private ContractPaymentService contractPaymentService;

    @PostMapping("/create")
    @Operation(summary = "创建合同付款")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment:create')")
    public CommonResult<String> createContractPayment(@Valid @RequestBody ContractPaymentSaveReqVO createReqVO) {
        return success(contractPaymentService.createContractPayment(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新合同付款")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment:update')")
    public CommonResult<Boolean> updateContractPayment(@Valid @RequestBody ContractPaymentSaveReqVO updateReqVO) {
        contractPaymentService.updateContractPayment(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除合同付款")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment:delete')")
    public CommonResult<Boolean> deleteContractPayment(@RequestParam("id") String id) {
        contractPaymentService.deleteContractPayment(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得合同付款")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment:query')")
    public CommonResult<ContractPaymentRespVO> getContractPayment(@RequestParam("id") String id) {
        ContractPaymentDO contractPayment = contractPaymentService.getContractPaymentById(id);
        // 获取付款详情
        List<ContractPaymentDetailDO> paymentList = contractPaymentService.getPaymentListById(id);

        return success(BeanUtils.toBean(contractPayment, ContractPaymentRespVO.class, vo -> {
            vo.setPaymentDetails(BeanUtils.toBean(paymentList, ContractPaymentRespVO.PaymentDetail.class));
        }));
    }

    @GetMapping("/page")
    @Operation(summary = "获得合同付款分页")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment:query')")
    public CommonResult<PageResult<ContractPaymentRespVO>> getContractPaymentPage(@Valid ContractPaymentPageReqVO pageReqVO) {
        PageResult<ContractPaymentDO> pageResult = contractPaymentService.getContractPaymentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ContractPaymentRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出合同付款 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportContractPaymentExcel(@Valid ContractPaymentPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ContractPaymentDO> list = contractPaymentService.getContractPaymentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "合同付款.xls", "数据", ContractPaymentRespVO.class,
                        BeanUtils.toBean(list, ContractPaymentRespVO.class));
    }

    @GetMapping("/list-by-contract-id")
    @Operation(summary = "获得合同付款计划")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment:create')")
    public CommonResult<List<ContractPaymentSchemeRespVO>> getContractPaymentSchemeListByContractId(ContractPaymentReqVO reqVO) {
        List<ContractPaymentSchemeDO> schemeList = contractPaymentService.getContractPaymentSchemeListByContractId(reqVO);
        return success(BeanUtils.toBean(schemeList, ContractPaymentSchemeRespVO.class));
    }

    @PutMapping("/submit")
    @Operation(summary = "提交合同审批")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment:update')")
    public CommonResult<Boolean> submitContract(@RequestParam("id") String id,  @RequestParam("processKey") String processKey) {
        contractPaymentService.submitContractPayment(id, processKey, getLoginUserId());
        return success(true);
    }

    @PostMapping("/createAndSubmit")
    @Operation(summary = "创建并提交合同审批")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment:create')")
    public CommonResult<Boolean> createAndsubmit(@Valid @RequestBody ContractPaymentSaveReqVO createReqVO) {
        contractPaymentService.createAndSubmitContractPayment(createReqVO);
        return success(true);
    }

    @GetMapping("/list-invoice-by-payment-id")
    @Operation(summary = "获得合同发票详细列表")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment:query')")
    public CommonResult<List<ContractPaymentDO>> getList4InvoiceByPaymentId(ContractPaymentReqVO reqVO) {
        return success(contractPaymentService.getList4InvoiceByPaymentId(reqVO));
    }

    // ==================== 子表（合同付款详细） ====================

    @GetMapping("/contract-payment-detail/list-by-payment-id")
    @Operation(summary = "获得合同付款详细列表")
    @Parameter(name = "paymentId", description = "合同支付ID")
    @PreAuthorize("@ss.hasPermission('ppm:contract-payment:query')")
    public CommonResult<List<ContractPaymentDetailDO>> getList4PaymentByPaymentId(@RequestParam("paymentId") String paymentId) {
        return success(contractPaymentService.getPaymentListById(paymentId));
    }
}