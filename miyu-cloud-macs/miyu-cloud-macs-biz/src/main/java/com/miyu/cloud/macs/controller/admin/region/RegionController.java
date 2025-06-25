package com.miyu.cloud.macs.controller.admin.region;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
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

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import com.miyu.cloud.macs.controller.admin.region.vo.*;
import com.miyu.cloud.macs.dal.dataobject.region.RegionDO;
import com.miyu.cloud.macs.service.region.RegionService;

@Tag(name = "管理后台 - 区域")
@RestController
@RequestMapping("/macs/region")
@Validated
public class RegionController {

    @Resource
    private RegionService regionService;

    @PostMapping("/create")
    @Operation(summary = "创建区域")
    @PreAuthorize("@ss.hasPermission('macs:region:create')")
    public CommonResult<String> createRegion(@Valid @RequestBody RegionSaveReqVO createReqVO) {
        return success(regionService.createRegion(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新区域")
    @PreAuthorize("@ss.hasPermission('macs:region:update')")
    public CommonResult<Boolean> updateRegion(@Valid @RequestBody RegionSaveReqVO updateReqVO) {
        regionService.updateRegion(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除区域")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('macs:region:delete')")
    public CommonResult<Boolean> deleteRegion(@RequestParam("id") String id) {
        regionService.deleteRegion(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得区域")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('macs:region:query')")
    public CommonResult<RegionRespVO> getRegion(@RequestParam("id") String id) {
        RegionDO region = regionService.getRegion(id);
        return success(BeanUtils.toBean(region, RegionRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得区域列表")
    @PreAuthorize("@ss.hasPermission('macs:region:query')")
    public CommonResult<List<RegionRespVO>> getRegionList(@Valid RegionListReqVO listReqVO) {
        List<RegionDO> list = regionService.getRegionList(listReqVO);
        return success(BeanUtils.toBean(list, RegionRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出区域 Excel")
    @PreAuthorize("@ss.hasPermission('macs:region:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportRegionExcel(@Valid RegionListReqVO listReqVO,
              HttpServletResponse response) throws IOException {
        List<RegionDO> list = regionService.getRegionList(listReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "区域.xls", "数据", RegionRespVO.class,
                        BeanUtils.toBean(list, RegionRespVO.class));
    }

}
