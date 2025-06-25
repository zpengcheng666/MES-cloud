package com.miyu.cloud.macs.controller.admin.visitorRegion;

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

import com.miyu.cloud.macs.controller.admin.visitorRegion.vo.*;
import com.miyu.cloud.macs.dal.dataobject.visitorRegion.VisitorRegionDO;
import com.miyu.cloud.macs.service.visitorRegion.VisitorRegionService;

@Tag(name = "管理后台 - 访客区域权限")
@RestController
@RequestMapping("/macs/visitor-region")
@Validated
public class VisitorRegionController {

    @Resource
    private VisitorRegionService visitorRegionService;

    @GetMapping("/getPageByVisitor")
    @Operation(summary = "获得访客区域权限分页")
    public CommonResult<PageResult<VisitorRegionRespVO>> getVisitorRegionPageByVisitor(@Valid VisitorRegionPageReqVO pageReqVO) {
        PageResult<VisitorRegionRespVO> pageResult = visitorRegionService.getVisitorRegionPageByVisitor(pageReqVO);
        return success(pageResult);
    }
    @GetMapping("/getByVisitorAndApplication")
    @Operation(summary = "获得访客区域权限分页")
    public CommonResult<PageResult<VisitorRegionRespVO>> getByVisitorAndApplication(@Valid VisitorRegionPageReqVO pageReqVO) {
        PageResult<VisitorRegionRespVO> pageResult = visitorRegionService.getByVisitorAndApplication(pageReqVO);
        return success(pageResult);
    }

    @PostMapping("/create")
    @Operation(summary = "创建访客区域权限")
    public CommonResult<String> createVisitorRegion(@Valid @RequestBody VisitorRegionSaveReqVO createReqVO) {
        return success(visitorRegionService.createVisitorRegion(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新访客区域权限")
    public CommonResult<Boolean> updateVisitorRegion(@Valid @RequestBody VisitorRegionSaveReqVO updateReqVO) {
        visitorRegionService.updateVisitorRegion(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除访客区域权限")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteVisitorRegion(@RequestParam("id") String id) {
        visitorRegionService.deleteVisitorRegion(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得访客区域权限")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<VisitorRegionRespVO> getVisitorRegion(@RequestParam("id") String id) {
        VisitorRegionDO visitorRegion = visitorRegionService.getVisitorRegion(id);
        return success(BeanUtils.toBean(visitorRegion, VisitorRegionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得访客区域权限分页")
    public CommonResult<PageResult<VisitorRegionRespVO>> getVisitorRegionPage(@Valid VisitorRegionPageReqVO pageReqVO) {
        PageResult<VisitorRegionDO> pageResult = visitorRegionService.getVisitorRegionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VisitorRegionRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出访客区域权限 Excel")
    public void exportVisitorRegionExcel(@Valid VisitorRegionPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VisitorRegionDO> list = visitorRegionService.getVisitorRegionPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "访客区域权限.xls", "数据", VisitorRegionRespVO.class,
                        BeanUtils.toBean(list, VisitorRegionRespVO.class));
    }

}
