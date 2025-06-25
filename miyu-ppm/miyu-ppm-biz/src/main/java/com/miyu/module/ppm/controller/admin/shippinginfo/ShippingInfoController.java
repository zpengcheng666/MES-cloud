package com.miyu.module.ppm.controller.admin.shippinginfo;

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

import com.miyu.module.ppm.controller.admin.shippinginfo.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import com.miyu.module.ppm.service.shippinginfo.ShippingInfoService;

@Tag(name = "管理后台 - 销售发货产品")
@RestController
@RequestMapping("/ppm/shipping-info")
@Validated
public class ShippingInfoController {

    @Resource
    private ShippingInfoService shippingInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建销售发货产品")
    @PreAuthorize("@ss.hasPermission('dm:shipping-info:create')")
    public CommonResult<String> createShippingInfo(@Valid @RequestBody ShippingInfoSaveReqVO createReqVO) {
        return success(shippingInfoService.createShippingInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新销售发货产品")
    @PreAuthorize("@ss.hasPermission('dm:shipping-info:update')")
    public CommonResult<Boolean> updateShippingInfo(@Valid @RequestBody ShippingInfoSaveReqVO updateReqVO) {
        shippingInfoService.updateShippingInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除销售发货产品")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping-info:delete')")
    public CommonResult<Boolean> deleteShippingInfo(@RequestParam("id") String id) {
        shippingInfoService.deleteShippingInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得销售发货产品")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dm:shipping-info:query')")
    public CommonResult<ShippingInfoRespVO> getShippingInfo(@RequestParam("id") String id) {
        ShippingInfoDO shippingInfo = shippingInfoService.getShippingInfo(id);
        return success(BeanUtils.toBean(shippingInfo, ShippingInfoRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得销售发货产品分页")
    @PreAuthorize("@ss.hasPermission('dm:shipping-info:query')")
    public CommonResult<PageResult<ShippingInfoRespVO>> getShippingInfoPage(@Valid ShippingInfoPageReqVO pageReqVO) {
        PageResult<ShippingInfoDO> pageResult = shippingInfoService.getShippingInfoPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ShippingInfoRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出销售发货产品 Excel")
    @PreAuthorize("@ss.hasPermission('dm:shipping-info:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShippingInfoExcel(@Valid ShippingInfoPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ShippingInfoDO> list = shippingInfoService.getShippingInfoPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "销售发货产品.xls", "数据", ShippingInfoRespVO.class,
                        BeanUtils.toBean(list, ShippingInfoRespVO.class));
    }

}