package com.miyu.cloud.macs.controller.admin.visitor;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import com.miyu.cloud.macs.controller.admin.visitorRegion.vo.VisitorRegionPageReqVO;
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

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import com.miyu.cloud.macs.controller.admin.visitor.vo.*;
import com.miyu.cloud.macs.dal.dataobject.visitor.VisitorDO;
import com.miyu.cloud.macs.service.visitor.VisitorService;

@Tag(name = "管理后台 - 访客")
@RestController
@RequestMapping("/macs/visitor")
@Validated
public class VisitorController {

    @Resource
    private VisitorService visitorService;

    @PostMapping("/create")
    @Operation(summary = "创建访客")
    @PreAuthorize("@ss.hasPermission('macs:visitor:create')")
    public CommonResult<String> createVisitor(@Valid @RequestBody VisitorSaveReqVO createReqVO) {
        return success(visitorService.createVisitor(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新访客")
    @PreAuthorize("@ss.hasPermission('macs:visitor:update')")
    public CommonResult<Boolean> updateVisitor(@Valid @RequestBody VisitorSaveReqVO updateReqVO) {
        visitorService.updateVisitor(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除访客")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('macs:visitor:delete')")
    public CommonResult<Boolean> deleteVisitor(@RequestParam("id") String id) {
        visitorService.deleteVisitor(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得访客")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('macs:visitor:query')")
    public CommonResult<VisitorRespVO> getVisitor(@RequestParam("id") String id) {
        VisitorDO visitor = visitorService.getVisitor(id);
        return success(BeanUtils.toBean(visitor, VisitorRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得访客分页")
    @PreAuthorize("@ss.hasPermission('macs:visitor:query')")
    public CommonResult<PageResult<VisitorRespVO>> getVisitorPage(@Valid VisitorPageReqVO pageReqVO) {
        PageResult<VisitorDO> pageResult = visitorService.getVisitorPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VisitorRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得所有访客")
    @PreAuthorize("@ss.hasPermission('macs:visitor:query')")
    public CommonResult<?> getVisitorList() {
        return success(visitorService.list());
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出访客 Excel")
    @PreAuthorize("@ss.hasPermission('macs:visitor:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVisitorExcel(@Valid VisitorPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VisitorDO> list = visitorService.getVisitorPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "访客.xls", "数据", VisitorRespVO.class,
                        BeanUtils.toBean(list, VisitorRespVO.class));
    }

    @GetMapping("/getPageByApplicationId")
    @Operation(summary = "获得访客分页")
    @PreAuthorize("@ss.hasPermission('macs:visitor:query')")
    public CommonResult<PageResult<VisitorRespVO>> getPageByApplicationId(@Valid VisitorRegionPageReqVO pageReqVO) {
        PageResult<VisitorDO> pageResult = visitorService.getPageByApplicationId(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VisitorRespVO.class));
    }

    @PostMapping("/visitorDeparture")
    @Operation(summary = "离厂")
    @PreAuthorize("@ss.hasPermission('macs:visitor:update')")
    public CommonResult<Boolean> visitorDeparture(@RequestBody VisitorSaveReqVO updateReqVO) {
        visitorService.visitorDeparture(updateReqVO);
        return success(true);
    }

}
