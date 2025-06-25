package com.miyu.cloud.dms.controller.admin.maintainapplication;

import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
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

import com.miyu.cloud.dms.controller.admin.maintainapplication.vo.*;
import com.miyu.cloud.dms.dal.dataobject.maintainapplication.MaintainApplicationDO;
import com.miyu.cloud.dms.service.maintainapplication.MaintainApplicationService;

@Tag(name = "管理后台 - 设备维修申请")
@RestController
@RequestMapping("/dms/maintain-application")
@Validated
public class MaintainApplicationController {

    @Resource
    private MaintainApplicationService maintainApplicationService;

    @PostMapping("/create")
    @Operation(summary = "创建设备维修申请")
    @PreAuthorize("@ss.hasPermission('dms:maintain-application:create')")
    public CommonResult<String> createMaintainApplication(@Valid @RequestBody MaintainApplicationSaveReqVO createReqVO) {
        return success(maintainApplicationService.createMaintainApplication(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备维修申请")
    @LogRecord(type = "DMS", subType = "maintain-application", bizNo = "{{#updateReqVO.id}}", success = "设备维修申请{{#updateReqVO.id}}成功更改内容")
    @PreAuthorize("@ss.hasPermission('dms:maintain-application:update')")
    public CommonResult<Boolean> updateMaintainApplication(@Valid @RequestBody MaintainApplicationSaveReqVO updateReqVO) {
        maintainApplicationService.updateMaintainApplication(updateReqVO);
        LogRecordContext.putVariable("newValue", updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备维修申请")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:maintain-application:delete')")
    public CommonResult<Boolean> deleteMaintainApplication(@RequestParam("id") String id) {
        maintainApplicationService.deleteMaintainApplication(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备维修申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:maintain-application:query')")
    public CommonResult<MaintainApplicationRespVO> getMaintainApplication(@RequestParam("id") String id) {
        MaintainApplicationDO maintainApplication = maintainApplicationService.getMaintainApplication(id);
        return success(BeanUtils.toBean(maintainApplication, MaintainApplicationRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备维修申请分页")
    @PreAuthorize("@ss.hasPermission('dms:maintain-application:query')")
    public CommonResult<PageResult<MaintainApplicationRespVO>> getMaintainApplicationPage(@Valid MaintainApplicationPageReqVO pageReqVO) {
        PageResult<MaintainApplicationDO> pageResult = maintainApplicationService.getMaintainApplicationPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaintainApplicationRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备维修申请 Excel")
    @PreAuthorize("@ss.hasPermission('dms:maintain-application:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaintainApplicationExcel(@Valid MaintainApplicationPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MaintainApplicationDO> list = maintainApplicationService.getMaintainApplicationPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "设备维修申请.xls", "数据", MaintainApplicationRespVO.class,
                        BeanUtils.toBean(list, MaintainApplicationRespVO.class));
    }

}
