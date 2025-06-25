package com.miyu.module.ppm.controller.admin.consignmentreturndetail;

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

import com.miyu.module.ppm.controller.admin.consignmentreturndetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import com.miyu.module.ppm.service.consignmentreturndetail.ConsignmentReturnDetailService;

@Tag(name = "管理后台 - 销售退货单详情")
@RestController
@RequestMapping("/ppm/consignment-return-detail")
@Validated
public class ConsignmentReturnDetailController {

    @Resource
    private ConsignmentReturnDetailService consignmentReturnDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建销售退货单详情")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return-detail:create')")
    public CommonResult<String> createConsignmentReturnDetail(@Valid @RequestBody ConsignmentReturnDetailSaveReqVO createReqVO) {
        return success(consignmentReturnDetailService.createConsignmentReturnDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新销售退货单详情")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return-detail:update')")
    public CommonResult<Boolean> updateConsignmentReturnDetail(@Valid @RequestBody ConsignmentReturnDetailSaveReqVO updateReqVO) {
        consignmentReturnDetailService.updateConsignmentReturnDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除销售退货单详情")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return-detail:delete')")
    public CommonResult<Boolean> deleteConsignmentReturnDetail(@RequestParam("id") String id) {
        consignmentReturnDetailService.deleteConsignmentReturnDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得销售退货单详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return-detail:query')")
    public CommonResult<ConsignmentReturnDetailRespVO> getConsignmentReturnDetail(@RequestParam("id") String id) {
        ConsignmentReturnDetailDO consignmentReturnDetail = consignmentReturnDetailService.getConsignmentReturnDetail(id);
        return success(BeanUtils.toBean(consignmentReturnDetail, ConsignmentReturnDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得销售退货单详情分页")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return-detail:query')")
    public CommonResult<PageResult<ConsignmentReturnDetailRespVO>> getConsignmentReturnDetailPage(@Valid ConsignmentReturnDetailPageReqVO pageReqVO) {
        PageResult<ConsignmentReturnDetailDO> pageResult = consignmentReturnDetailService.getConsignmentReturnDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ConsignmentReturnDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出销售退货单详情 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportConsignmentReturnDetailExcel(@Valid ConsignmentReturnDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ConsignmentReturnDetailDO> list = consignmentReturnDetailService.getConsignmentReturnDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "销售退货单详情.xls", "数据", ConsignmentReturnDetailRespVO.class,
                        BeanUtils.toBean(list, ConsignmentReturnDetailRespVO.class));
    }

}