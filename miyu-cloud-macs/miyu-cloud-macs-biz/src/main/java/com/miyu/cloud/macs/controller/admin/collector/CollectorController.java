package com.miyu.cloud.macs.controller.admin.collector;

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

import com.miyu.cloud.macs.controller.admin.collector.vo.*;
import com.miyu.cloud.macs.dal.dataobject.collector.CollectorDO;
import com.miyu.cloud.macs.service.collector.CollectorService;

@Tag(name = "管理后台 - (通行卡,人脸,指纹)采集器")
@RestController
@RequestMapping("/macs/collector")
@Validated
public class CollectorController {

    @Resource
    private CollectorService collectorService;

    @PostMapping("/create")
    @Operation(summary = "创建采集器")
    @PreAuthorize("@ss.hasPermission('macs:collector:create')")
    public CommonResult<String> createCollector(@Valid @RequestBody CollectorSaveReqVO createReqVO) {
        return success(collectorService.createCollector(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新采集器")
    @PreAuthorize("@ss.hasPermission('macs:collector:update')")
    public CommonResult<Boolean> updateCollector(@Valid @RequestBody CollectorSaveReqVO updateReqVO) {
        collectorService.updateCollector(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除采集器")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('macs:collector:delete')")
    public CommonResult<Boolean> deleteCollector(@RequestParam("id") String id) {
        collectorService.deleteCollector(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得采集器")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('macs:collector:query')")
    public CommonResult<CollectorRespVO> getCollector(@RequestParam("id") String id) {
        CollectorDO collector = collectorService.getCollector(id);
        return success(BeanUtils.toBean(collector, CollectorRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得采集器分页")
    @PreAuthorize("@ss.hasPermission('macs:collector:query')")
    public CommonResult<PageResult<CollectorRespVO>> getCollectorPage(@Valid CollectorPageReqVO pageReqVO) {
        PageResult<CollectorDO> pageResult = collectorService.getCollectorPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CollectorRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得采集器集合")
    @PreAuthorize("@ss.hasPermission('macs:collector:query')")
    public CommonResult<List<CollectorRespVO>> list(@Valid CollectorSaveReqVO ReqVO) {
        List<CollectorDO> result = collectorService.getCollectorList(ReqVO);
        return success(BeanUtils.toBean(result, CollectorRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出采集器 Excel")
    @PreAuthorize("@ss.hasPermission('macs:collector:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCollectorExcel(@Valid CollectorPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CollectorDO> list = collectorService.getCollectorPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "采集器.xls", "数据", CollectorRespVO.class,
                        BeanUtils.toBean(list, CollectorRespVO.class));
    }

}
