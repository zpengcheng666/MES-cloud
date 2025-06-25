package com.miyu.cloud.macs.controller.admin.accessApplication;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
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

import com.miyu.cloud.macs.controller.admin.accessApplication.vo.*;
import com.miyu.cloud.macs.dal.dataobject.accessApplication.AccessApplicationDO;
import com.miyu.cloud.macs.service.accessApplication.AccessApplicationService;

@Tag(name = "管理后台 - 通行申请")
@RestController
@RequestMapping("/macs/access-application")
@Validated
public class AccessApplicationController {

    @Resource
    private AccessApplicationService accessApplicationService;

    @PostMapping("/create")
    @Operation(summary = "创建通行申请")
    @PreAuthorize("@ss.hasPermission('macs:access-application:create')")
    public CommonResult<String> createAccessApplication(@Valid @RequestBody AccessApplicationSaveReqVO createReqVO) {
        return success(accessApplicationService.createAccessApplication(createReqVO));
    }

    @PostMapping("/visitorApplication")
    @Operation(summary = "创建通行申请")
    @PreAuthorize("@ss.hasPermission('macs:access-application:create')")
    public CommonResult<String> visitorApplication(@Valid @RequestBody AccessApplicationSaveReqVO createReqVO) {
        return success(accessApplicationService.createVisitorApplication(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新通行申请")
    @PreAuthorize("@ss.hasPermission('macs:access-application:update')")
    public CommonResult<Boolean> updateAccessApplication(@Valid @RequestBody AccessApplicationSaveReqVO updateReqVO) {
        accessApplicationService.updateAccessApplication(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除通行申请")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('macs:access-application:delete')")
    public CommonResult<Boolean> deleteAccessApplication(@RequestParam("id") String id) {
        accessApplicationService.deleteAccessApplication(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得通行申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('macs:access-application:query')")
    public CommonResult<AccessApplicationRespVO> getAccessApplication(@RequestParam("id") String id) {
        AccessApplicationDO accessApplication = accessApplicationService.getAccessApplication(id);
        return success(BeanUtils.toBean(accessApplication, AccessApplicationRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得通行申请分页")
    @PreAuthorize("@ss.hasPermission('macs:access-application:query')")
    public CommonResult<PageResult<AccessApplicationRespVO>> getAccessApplicationPage(@Valid AccessApplicationPageReqVO pageReqVO) {
        PageResult<AccessApplicationDO> pageResult = accessApplicationService.getAccessApplicationPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AccessApplicationRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出通行申请 Excel")
    @PreAuthorize("@ss.hasPermission('macs:access-application:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAccessApplicationExcel(@Valid AccessApplicationPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AccessApplicationDO> list = accessApplicationService.getAccessApplicationPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "通行申请.xls", "数据", AccessApplicationRespVO.class,
                        BeanUtils.toBean(list, AccessApplicationRespVO.class));
    }

}
