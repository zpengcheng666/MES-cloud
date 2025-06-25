package com.miyu.module.ppm.controller.admin.contractconsignmentdetail;

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

import com.miyu.module.ppm.controller.admin.contractconsignmentdetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractconsignmentdetail.ContractConsignmentDetailDO;
import com.miyu.module.ppm.service.contractconsignmentdetail.ContractConsignmentDetailService;

@Tag(name = "管理后台 - 外协发货单详情")
@RestController
@RequestMapping("/ppm/contract-consignment-detail")
@Validated
public class ContractConsignmentDetailController {

    @Resource
    private ContractConsignmentDetailService contractConsignmentDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建外协发货单详情")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-detail:create')")
    public CommonResult<String> createContractConsignmentDetail(@Valid @RequestBody ContractConsignmentDetailSaveReqVO createReqVO) {
        return success(contractConsignmentDetailService.createContractConsignmentDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新外协发货单详情")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-detail:update')")
    public CommonResult<Boolean> updateContractConsignmentDetail(@Valid @RequestBody ContractConsignmentDetailSaveReqVO updateReqVO) {
        contractConsignmentDetailService.updateContractConsignmentDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除外协发货单详情")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-detail:delete')")
    public CommonResult<Boolean> deleteContractConsignmentDetail(@RequestParam("id") String id) {
        contractConsignmentDetailService.deleteContractConsignmentDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得外协发货单详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-detail:query')")
    public CommonResult<ContractConsignmentDetailRespVO> getContractConsignmentDetail(@RequestParam("id") String id) {
        ContractConsignmentDetailDO contractConsignmentDetail = contractConsignmentDetailService.getContractConsignmentDetail(id);
        return success(BeanUtils.toBean(contractConsignmentDetail, ContractConsignmentDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得外协发货单详情分页")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-detail:query')")
    public CommonResult<PageResult<ContractConsignmentDetailRespVO>> getContractConsignmentDetailPage(@Valid ContractConsignmentDetailPageReqVO pageReqVO) {
        PageResult<ContractConsignmentDetailDO> pageResult = contractConsignmentDetailService.getContractConsignmentDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ContractConsignmentDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出外协发货单详情 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportContractConsignmentDetailExcel(@Valid ContractConsignmentDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ContractConsignmentDetailDO> list = contractConsignmentDetailService.getContractConsignmentDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "外协发货单详情.xls", "数据", ContractConsignmentDetailRespVO.class,
                        BeanUtils.toBean(list, ContractConsignmentDetailRespVO.class));
    }

}