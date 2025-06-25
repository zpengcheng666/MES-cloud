package com.miyu.cloud.dms.controller.admin.failurerecord;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.cloud.dms.controller.admin.failurerecord.vo.FailureRecordPageReqVO;
import com.miyu.cloud.dms.controller.admin.failurerecord.vo.FailureRecordRespVO;
import com.miyu.cloud.dms.controller.admin.failurerecord.vo.FailureRecordSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.failurerecord.FailureRecordDO;
import com.miyu.cloud.dms.service.failurerecord.FailureRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 异常记录")
@RestController
@RequestMapping("/dms/failure-record")
@Validated
public class FailureRecordController {

    @Resource
    private FailureRecordService failureRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建异常记录")
    @PreAuthorize("@ss.hasPermission('dms:failure-record:create')")
    public CommonResult<String> createFailureRecord(@Valid @RequestBody FailureRecordSaveReqVO createReqVO) {
        return success(failureRecordService.createFailureRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新异常记录")
    @PreAuthorize("@ss.hasPermission('dms:failure-record:update')")
    public CommonResult<Boolean> updateFailureRecord(@Valid @RequestBody FailureRecordSaveReqVO updateReqVO) {
        failureRecordService.updateFailureRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除异常记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:failure-record:delete')")
    public CommonResult<Boolean> deleteFailureRecord(@RequestParam("id") String id) {
        failureRecordService.deleteFailureRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得异常记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:failure-record:query')")
    public CommonResult<FailureRecordRespVO> getFailureRecord(@RequestParam("id") String id) {
        FailureRecordDO failureRecord = failureRecordService.getFailureRecord(id);
        return success(BeanUtils.toBean(failureRecord, FailureRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得异常记录分页")
    @PreAuthorize("@ss.hasPermission('dms:failure-record:query')")
    public CommonResult<PageResult<FailureRecordRespVO>> getFailureRecordPage(@Valid FailureRecordPageReqVO pageReqVO) {
        PageResult<FailureRecordDO> pageResult = failureRecordService.getFailureRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, FailureRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出异常记录 Excel")
    @PreAuthorize("@ss.hasPermission('dms:failure-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFailureRecordExcel(@Valid FailureRecordPageReqVO pageReqVO,
                                         HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<FailureRecordDO> list = failureRecordService.getFailureRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "异常记录.xls", "数据", FailureRecordRespVO.class,
                BeanUtils.toBean(list, FailureRecordRespVO.class));
    }

    @GetMapping("/getList")
    @Operation(summary = "根据设备获得异常记录")
    @Parameter(name = "id", description = "设备id", required = true, example = "1024")
    public CommonResult<List<FailureRecordRespVO>> getFailureRecordList(@RequestParam("id") String id) {
        List<FailureRecordDO> list = failureRecordService.getFailureRecordList(id);
        return success(BeanUtils.toBean(list, FailureRecordRespVO.class));
    }

}
