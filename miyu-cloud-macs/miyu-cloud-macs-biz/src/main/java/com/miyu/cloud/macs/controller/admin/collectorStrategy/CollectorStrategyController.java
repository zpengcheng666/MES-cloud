package com.miyu.cloud.macs.controller.admin.collectorStrategy;

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

import com.miyu.cloud.macs.controller.admin.collectorStrategy.vo.*;
import com.miyu.cloud.macs.dal.dataobject.collectorStrategy.CollectorStrategyDO;
import com.miyu.cloud.macs.service.collectorStrategy.CollectorStrategyService;

@Tag(name = "管理后台 - 设备策略")
@RestController
@RequestMapping("/macs/collector-strategy")
@Validated
public class CollectorStrategyController {

    @Resource
    private CollectorStrategyService collectorStrategyService;

    @PostMapping("/create")
    @Operation(summary = "创建设备策略")
    @PreAuthorize("@ss.hasPermission('macs:collector-strategy:create')")
    public CommonResult<String> createCollectorStrategy(@Valid @RequestBody CollectorStrategySaveReqVO createReqVO) {
        return success(collectorStrategyService.createCollectorStrategy(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备策略")
    @PreAuthorize("@ss.hasPermission('macs:collector-strategy:update')")
    public CommonResult<Boolean> updateCollectorStrategy(@Valid @RequestBody CollectorStrategySaveReqVO updateReqVO) {
        collectorStrategyService.updateCollectorStrategy(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备策略")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('macs:collector-strategy:delete')")
    public CommonResult<Boolean> deleteCollectorStrategy(@RequestParam("id") String id) {
        collectorStrategyService.deleteCollectorStrategy(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备策略")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('macs:collector-strategy:query')")
    public CommonResult<CollectorStrategyRespVO> getCollectorStrategy(@RequestParam("id") String id) {
        CollectorStrategyDO collectorStrategy = collectorStrategyService.getCollectorStrategy(id);
        return success(BeanUtils.toBean(collectorStrategy, CollectorStrategyRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备策略分页")
    @PreAuthorize("@ss.hasPermission('macs:collector-strategy:query')")
    public CommonResult<PageResult<CollectorStrategyRespVO>> getCollectorStrategyPage(@Valid CollectorStrategyPageReqVO pageReqVO) {
        PageResult<CollectorStrategyDO> pageResult = collectorStrategyService.getCollectorStrategyPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CollectorStrategyRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备策略 Excel")
    @PreAuthorize("@ss.hasPermission('macs:collector-strategy:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCollectorStrategyExcel(@Valid CollectorStrategyPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CollectorStrategyDO> list = collectorStrategyService.getCollectorStrategyPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "设备策略.xls", "数据", CollectorStrategyRespVO.class,
                        BeanUtils.toBean(list, CollectorStrategyRespVO.class));
    }

}
