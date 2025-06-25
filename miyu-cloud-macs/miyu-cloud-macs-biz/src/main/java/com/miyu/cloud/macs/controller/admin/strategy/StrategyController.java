package com.miyu.cloud.macs.controller.admin.strategy;

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

import com.miyu.cloud.macs.controller.admin.strategy.vo.*;
import com.miyu.cloud.macs.dal.dataobject.strategy.StrategyDO;
import com.miyu.cloud.macs.service.strategy.StrategyService;

@Tag(name = "管理后台 - 策略")
@RestController
@RequestMapping("/macs/strategy")
@Validated
public class StrategyController {

    @Resource
    private StrategyService strategyService;

    @PostMapping("/create")
    @Operation(summary = "创建策略")
    @PreAuthorize("@ss.hasPermission('macs:strategy:create')")
    public CommonResult<String> createStrategy(@Valid @RequestBody StrategySaveReqVO createReqVO) {
        return success(strategyService.createStrategy(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新策略")
    @PreAuthorize("@ss.hasPermission('macs:strategy:update')")
    public CommonResult<Boolean> updateStrategy(@Valid @RequestBody StrategySaveReqVO updateReqVO) {
        strategyService.updateStrategy(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除策略")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('macs:strategy:delete')")
    public CommonResult<Boolean> deleteStrategy(@RequestParam("id") String id) {
        strategyService.deleteStrategy(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得策略")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('macs:strategy:query')")
    public CommonResult<StrategyRespVO> getStrategy(@RequestParam("id") String id) {
        StrategyDO strategy = strategyService.getStrategy(id);
        return success(BeanUtils.toBean(strategy, StrategyRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得策略分页")
    @PreAuthorize("@ss.hasPermission('macs:strategy:query')")
    public CommonResult<PageResult<StrategyRespVO>> getStrategyPage(@Valid StrategyPageReqVO pageReqVO) {
        PageResult<StrategyDO> pageResult = strategyService.getStrategyPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, StrategyRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出策略 Excel")
    @PreAuthorize("@ss.hasPermission('macs:strategy:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportStrategyExcel(@Valid StrategyPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<StrategyDO> list = strategyService.getStrategyPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "策略.xls", "数据", StrategyRespVO.class,
                        BeanUtils.toBean(list, StrategyRespVO.class));
    }

}
