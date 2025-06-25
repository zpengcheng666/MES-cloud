package com.miyu.cloud.dms.controller.admin.inspectionrecord;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.cloud.dms.controller.admin.inspectionrecord.vo.InspectionRecordAddReqVO;
import com.miyu.cloud.dms.controller.admin.inspectionrecord.vo.InspectionRecordPageReqVO;
import com.miyu.cloud.dms.controller.admin.inspectionrecord.vo.InspectionRecordRespVO;
import com.miyu.cloud.dms.controller.admin.inspectionrecord.vo.InspectionRecordSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.inspectionrecord.InspectionRecordDO;
import com.miyu.cloud.dms.service.inspectionrecord.InspectionRecordService;
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

@Tag(name = "管理后台 - 设备检查记录")
@RestController
@RequestMapping("/dms/inspection-record")
@Validated
public class InspectionRecordController {

    @Resource
    private InspectionRecordService inspectionRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建设备检查记录")
    @PreAuthorize("@ss.hasPermission('dms:inspection-record:create')")
    public CommonResult<String> createInspectionRecord(@Valid @RequestBody InspectionRecordSaveReqVO createReqVO) {
        return success(inspectionRecordService.createInspectionRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备检查记录")
    @PreAuthorize("@ss.hasPermission('dms:inspection-record:update')")
    public CommonResult<Boolean> updateInspectionRecord(@Valid @RequestBody InspectionRecordSaveReqVO updateReqVO) {
        inspectionRecordService.updateInspectionRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备检查记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:inspection-record:delete')")
    public CommonResult<Boolean> deleteInspectionRecord(@RequestParam("id") String id) {
        inspectionRecordService.deleteInspectionRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备检查记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:inspection-record:query')")
    public CommonResult<InspectionRecordRespVO> getInspectionRecord(@RequestParam("id") String id) {
        InspectionRecordDO inspectionRecord = inspectionRecordService.getInspectionRecord(id);
        return success(BeanUtils.toBean(inspectionRecord, InspectionRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备检查记录分页")
    @PreAuthorize("@ss.hasPermission('dms:inspection-record:query')")
    public CommonResult<PageResult<InspectionRecordRespVO>> getInspectionRecordPage(@Valid InspectionRecordPageReqVO pageReqVO) {
        PageResult<InspectionRecordDO> pageResult = inspectionRecordService.getInspectionRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InspectionRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备检查记录 Excel")
    @PreAuthorize("@ss.hasPermission('dms:inspection-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionRecordExcel(@Valid InspectionRecordPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionRecordDO> list = inspectionRecordService.getInspectionRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "设备检查记录.xls", "数据", InspectionRecordRespVO.class, BeanUtils.toBean(list, InspectionRecordRespVO.class));
    }

    @PostMapping("/add")
    @Operation(summary = "新增设备检查记录")
    public CommonResult<Boolean> addInspectionRecord(@Valid @RequestBody InspectionRecordAddReqVO addReqVO) {
        inspectionRecordService.addInspectionRecord(addReqVO);
        return success(true);
    }

}
