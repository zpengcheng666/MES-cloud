package com.miyu.module.ppm.controller.admin.consignmentrefund;

import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.enums.consignmentrefund.ConsignmentRefundEnum;
import com.miyu.module.ppm.service.contract.ContractService;
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

import com.miyu.module.ppm.controller.admin.consignmentrefund.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentrefund.ConsignmentRefundDO;
import com.miyu.module.ppm.service.consignmentrefund.ConsignmentRefundService;

@Tag(name = "管理后台 - 采购退款单")
@RestController
@RequestMapping("/ppm/consignment-refund")
@Validated
public class ConsignmentRefundController {

    @Resource
    private ConsignmentRefundService consignmentRefundService;

    @Resource
    private ContractService contractService;

    @PostMapping("/create")
    @Operation(summary = "创建采购退款单")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-refund:create')")
    public CommonResult<String> createConsignmentRefund(@Valid @RequestBody ConsignmentRefundSaveReqVO createReqVO) {
        return success(consignmentRefundService.createConsignmentRefund(createReqVO));
    }

    @PostMapping("/createAndSubmit")
    @Operation(summary = "创建采购退款单")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-refund:create')")
    public CommonResult<String> createConsignmentRefundAndSubmit(@Valid @RequestBody ConsignmentRefundSaveReqVO createReqVO) {
        return success(consignmentRefundService.createConsignmentRefundAndSubmit(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新采购退款单")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-refund:update')")
    public CommonResult<Boolean> updateConsignmentRefund(@Valid @RequestBody ConsignmentRefundSaveReqVO updateReqVO) {
        consignmentRefundService.updateConsignmentRefund(updateReqVO);
        return success(true);
    }

    @PutMapping("/updateAndSubmit")
    @Operation(summary = "更新采购退款单")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-refund:update')")
    public CommonResult<Boolean> updateConsignmentRefundAndSubmit(@Valid @RequestBody ConsignmentRefundSaveReqVO updateReqVO) {
        consignmentRefundService.updateConsignmentRefundAndSubmit(updateReqVO);
        return success(true);
    }

    @PutMapping("/submit")
    @Operation(summary = "提交退款审批")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-refund:update')")
    public CommonResult<Boolean> submitConsignmentRefund(@RequestParam("id") String id) {
        consignmentRefundService.submitConsignmentRefund(id, getLoginUserId());
        return success(true);
    }

    @PutMapping("/confirm")
    @Operation(summary = "退款完成确认")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-refund:update')")
    public CommonResult<Boolean> confirmRefund(@RequestParam("id") String id) {

        consignmentRefundService.updateConsignmentRefundStatus(id, ConsignmentRefundEnum.FINISH.getStatus());
        return success(true);
    }


    @DeleteMapping("/delete")
    @Operation(summary = "删除采购退款单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:consignment-refund:delete')")
    public CommonResult<Boolean> deleteConsignmentRefund(@RequestParam("id") String id) {
        consignmentRefundService.deleteConsignmentRefund(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得采购退款单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-refund:query')")
    public CommonResult<ConsignmentRefundRespVO> getConsignmentRefund(@RequestParam("id") String id) {
        ConsignmentRefundDO consignmentRefund = consignmentRefundService.getConsignmentRefund(id);

        //获取合同名称信息
        ContractDO contractDO = contractService.getContractById(consignmentRefund.getContractId());
        ConsignmentRefundRespVO consignmentRefundRespVO = BeanUtils.toBean(consignmentRefund, ConsignmentRefundRespVO.class);
        consignmentRefundRespVO.setContractNo(contractDO.getNumber()).setContractName(contractDO.getName());

        return success(consignmentRefundRespVO);


    }

    @GetMapping("/page")
    @Operation(summary = "获得采购退款单分页")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-refund:query')")
    public CommonResult<PageResult<ConsignmentRefundRespVO>> getConsignmentRefundPage(@Valid ConsignmentRefundPageReqVO pageReqVO) {
        PageResult<ConsignmentRefundDO> pageResult = consignmentRefundService.getConsignmentRefundPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ConsignmentRefundRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出采购退款单 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-refund:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportConsignmentRefundExcel(@Valid ConsignmentRefundPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ConsignmentRefundDO> list = consignmentRefundService.getConsignmentRefundPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "采购退款单.xls", "数据", ConsignmentRefundRespVO.class,
                        BeanUtils.toBean(list, ConsignmentRefundRespVO.class));
    }

}