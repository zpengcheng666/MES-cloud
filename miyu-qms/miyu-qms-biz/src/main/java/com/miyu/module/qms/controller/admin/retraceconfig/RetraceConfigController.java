package com.miyu.module.qms.controller.admin.retraceconfig;

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

import com.miyu.module.qms.controller.admin.retraceconfig.vo.*;
import com.miyu.module.qms.dal.dataobject.retraceconfig.RetraceConfigDO;
import com.miyu.module.qms.service.retraceconfig.RetraceConfigService;

@Tag(name = "管理后台 - 追溯字段配置")
@RestController
@RequestMapping("/qms/retrace-config")
@Validated
public class RetraceConfigController {

    @Resource
    private RetraceConfigService retraceConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建追溯字段配置")
    @PreAuthorize("@ss.hasPermission('qms:retrace-config:create')")
    public CommonResult<String> createRetraceConfig(@Valid @RequestBody RetraceConfigSaveReqVO createReqVO) {
        return success(retraceConfigService.createRetraceConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新追溯字段配置")
    @PreAuthorize("@ss.hasPermission('qms:retrace-config:update')")
    public CommonResult<Boolean> updateRetraceConfig(@Valid @RequestBody RetraceConfigSaveReqVO updateReqVO) {
        retraceConfigService.updateRetraceConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除追溯字段配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:retrace-config:delete')")
    public CommonResult<Boolean> deleteRetraceConfig(@RequestParam("id") String id) {
        retraceConfigService.deleteRetraceConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得追溯字段配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:retrace-config:query')")
    public CommonResult<RetraceConfigRespVO> getRetraceConfig(@RequestParam("id") String id) {
        RetraceConfigDO retraceConfig = retraceConfigService.getRetraceConfig(id);
        return success(BeanUtils.toBean(retraceConfig, RetraceConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得追溯字段配置分页")
    @PreAuthorize("@ss.hasPermission('qms:retrace-config:query')")
    public CommonResult<PageResult<RetraceConfigRespVO>> getRetraceConfigPage(@Valid RetraceConfigPageReqVO pageReqVO) {
        PageResult<RetraceConfigDO> pageResult = retraceConfigService.getRetraceConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, RetraceConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出追溯字段配置 Excel")
    @PreAuthorize("@ss.hasPermission('qms:retrace-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportRetraceConfigExcel(@Valid RetraceConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<RetraceConfigDO> list = retraceConfigService.getRetraceConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "追溯字段配置.xls", "数据", RetraceConfigRespVO.class,
                        BeanUtils.toBean(list, RetraceConfigRespVO.class));
    }


    @GetMapping("/list")
    @Operation(summary = "获得追溯字段配置")
    @PreAuthorize("@ss.hasPermission('qms:retrace-config:query')")
    public CommonResult<List<RetraceConfigRespVO>> getRetraceConfigList(@Valid RetraceConfigPageReqVO pageReqVO) {
        List<RetraceConfigDO> list = retraceConfigService.getRetraceConfigList();
        return success(BeanUtils.toBean(list, RetraceConfigRespVO.class));
    }

}