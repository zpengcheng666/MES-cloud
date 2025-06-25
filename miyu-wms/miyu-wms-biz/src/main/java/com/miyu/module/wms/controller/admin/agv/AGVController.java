package com.miyu.module.wms.controller.admin.agv;

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

import com.miyu.module.wms.controller.admin.agv.vo.*;
import com.miyu.module.wms.dal.dataobject.agv.AGVDO;
import com.miyu.module.wms.service.agv.AGVService;

@Tag(name = "管理后台 - AGV 信息")
@RestController
@RequestMapping("/wms/AGV")
@Validated
public class AGVController {

    @Resource
    private AGVService aGVService;

    @PostMapping("/create")
    @Operation(summary = "创建AGV 信息")
    @PreAuthorize("@ss.hasPermission('wms:AGV:create')")
    public CommonResult<String> createAGV(@Valid @RequestBody AGVSaveReqVO createReqVO) {
        return success(aGVService.createAGV(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新AGV 信息")
    @PreAuthorize("@ss.hasPermission('wms:AGV:update')")
    public CommonResult<Boolean> updateAGV(@Valid @RequestBody AGVSaveReqVO updateReqVO) {
        aGVService.updateAGV(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除AGV 信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:AGV:delete')")
    public CommonResult<Boolean> deleteAGV(@RequestParam("id") String id) {
        aGVService.deleteAGV(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得AGV 信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:AGV:query')")
    public CommonResult<AGVRespVO> getAGV(@RequestParam("id") String id) {
        AGVDO aGV = aGVService.getAGV(id);
        return success(BeanUtils.toBean(aGV, AGVRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得AGV 信息分页")
    @PreAuthorize("@ss.hasPermission('wms:AGV:query')")
    public CommonResult<PageResult<AGVRespVO>> getAGVPage(@Valid AGVPageReqVO pageReqVO) {
        PageResult<AGVDO> pageResult = aGVService.getAGVPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AGVRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出AGV 信息 Excel")
    @PreAuthorize("@ss.hasPermission('wms:AGV:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAGVExcel(@Valid AGVPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AGVDO> list = aGVService.getAGVPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "AGV 信息.xls", "数据", AGVRespVO.class,
                        BeanUtils.toBean(list, AGVRespVO.class));
    }

}