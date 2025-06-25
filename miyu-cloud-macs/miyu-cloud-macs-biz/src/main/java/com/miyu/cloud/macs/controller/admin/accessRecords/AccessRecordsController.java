package com.miyu.cloud.macs.controller.admin.accessRecords;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import com.miyu.cloud.macs.controller.admin.accessRecords.vo.*;
import com.miyu.cloud.macs.dal.dataobject.accessRecords.AccessRecordsDO;
import com.miyu.cloud.macs.service.accessRecords.AccessRecordsService;

@Tag(name = "管理后台 - 通行记录")
@RestController
@RequestMapping("/macs/access-records")
@Validated
public class AccessRecordsController {

    @Resource
    private AccessRecordsService accessRecordsService;

    @PostMapping("/create")
    @Operation(summary = "创建通行记录")
    @PreAuthorize("@ss.hasPermission('macs:access-records:create')")
    public CommonResult<String> createAccessRecords(@Valid @RequestBody AccessRecordsSaveReqVO createReqVO) {
        return success(accessRecordsService.createAccessRecords(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新通行记录")
    @PreAuthorize("@ss.hasPermission('macs:access-records:update')")
    public CommonResult<Boolean> updateAccessRecords(@Valid @RequestBody AccessRecordsSaveReqVO updateReqVO) {
        accessRecordsService.updateAccessRecords(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除通行记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('macs:access-records:delete')")
    public CommonResult<Boolean> deleteAccessRecords(@RequestParam("id") String id) {
        accessRecordsService.deleteAccessRecords(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得通行记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('macs:access-records:query')")
    public CommonResult<AccessRecordsRespVO> getAccessRecords(@RequestParam("id") String id) {
        AccessRecordsDO accessRecords = accessRecordsService.getAccessRecords(id);
        return success(BeanUtils.toBean(accessRecords, AccessRecordsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得通行记录分页")
    @PreAuthorize("@ss.hasPermission('macs:access-records:query')")
    public CommonResult<PageResult<AccessRecordsRespVO>> getAccessRecordsPage(@Valid AccessRecordsPageReqVO pageReqVO) {
        PageResult<AccessRecordsDO> pageResult = accessRecordsService.getAccessRecordsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AccessRecordsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出通行记录 Excel")
    @PreAuthorize("@ss.hasPermission('macs:access-records:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAccessRecordsExcel(@Valid AccessRecordsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AccessRecordsDO> list = accessRecordsService.getAccessRecordsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "通行记录.xls", "数据", AccessRecordsRespVO.class,
                        BeanUtils.toBean(list, AccessRecordsRespVO.class));
    }

    @GetMapping("/getAccessRecords")
    @Operation(summary = "访客行程")
    public CommonResult<List<AccessRecordsDO>> queryAccessRecords(
            @RequestParam("visitorId")String id,
            @RequestParam(name = "actions", required = false)Object[] actions){
        QueryWrapper<AccessRecordsDO> wrapper = new QueryWrapper<>();
        wrapper.eq("visitor_id",id)
                .orderByAsc("create_time");
        if (actions != null) {
            wrapper.in("action", actions);
        }
        List<AccessRecordsDO> list = accessRecordsService.list(wrapper);
        return CommonResult.success(list);
    }

}
