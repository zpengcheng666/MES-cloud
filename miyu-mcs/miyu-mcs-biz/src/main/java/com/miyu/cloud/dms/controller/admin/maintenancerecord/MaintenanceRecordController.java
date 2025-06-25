package com.miyu.cloud.dms.controller.admin.maintenancerecord;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.cloud.dms.controller.admin.maintenancerecord.vo.MaintenanceRecordAddSaveReqVO;
import com.miyu.cloud.dms.controller.admin.maintenancerecord.vo.MaintenanceRecordPageReqVO;
import com.miyu.cloud.dms.controller.admin.maintenancerecord.vo.MaintenanceRecordRespVO;
import com.miyu.cloud.dms.controller.admin.maintenancerecord.vo.MaintenanceRecordSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.maintenancerecord.MaintenanceRecordDO;
import com.miyu.cloud.dms.dal.dataobject.maintenancerecord.MaintenanceRecordToSparePartDO;
import com.miyu.cloud.dms.service.maintenancerecord.MaintenanceRecordService;
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

@Tag(name = "管理后台 - 设备保养维护记录")
@RestController
@RequestMapping("/dms/maintenance-record")
@Validated
public class MaintenanceRecordController {

    @Resource
    private MaintenanceRecordService maintenanceRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建设备保养维护记录")
    @PreAuthorize("@ss.hasPermission('dms:maintenance-record:create')")
    public CommonResult<String> createMaintenanceRecord(@Valid @RequestBody MaintenanceRecordSaveReqVO createReqVO) {
        return success(maintenanceRecordService.createMaintenanceRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备保养维护记录")
    @PreAuthorize("@ss.hasPermission('dms:maintenance-record:update')")
    public CommonResult<Boolean> updateMaintenanceRecord(@Valid @RequestBody MaintenanceRecordSaveReqVO updateReqVO) {
        maintenanceRecordService.updateMaintenanceRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备保养维护记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:maintenance-record:delete')")
    public CommonResult<Boolean> deleteMaintenanceRecord(@RequestParam("id") String id) {
        maintenanceRecordService.deleteMaintenanceRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备保养维护记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:maintenance-record:query')")
    public CommonResult<MaintenanceRecordRespVO> getMaintenanceRecord(@RequestParam("id") String id) {
        MaintenanceRecordDO maintenanceRecord = maintenanceRecordService.getMaintenanceRecord(id);
        return success(BeanUtils.toBean(maintenanceRecord, MaintenanceRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备保养维护记录分页")
    @PreAuthorize("@ss.hasPermission('dms:maintenance-record:query')")
    public CommonResult<PageResult<MaintenanceRecordRespVO>> getMaintenanceRecordPage(@Valid MaintenanceRecordPageReqVO pageReqVO) {
        PageResult<MaintenanceRecordDO> pageResult = maintenanceRecordService.getMaintenanceRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaintenanceRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备保养维护记录 Excel")
    @PreAuthorize("@ss.hasPermission('dms:maintenance-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaintenanceRecordExcel(@Valid MaintenanceRecordPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MaintenanceRecordDO> list = maintenanceRecordService.getMaintenanceRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "设备保养维护记录.xls", "数据", MaintenanceRecordRespVO.class, BeanUtils.toBean(list, MaintenanceRecordRespVO.class));
    }

    @PostMapping("/add")
    @Operation(summary = "添加设备保养维护记录")
    @PreAuthorize("@ss.hasPermission('dms:maintenance-record:create')")
    public CommonResult<Boolean> addMaintenanceRecord(@Valid @RequestBody MaintenanceRecordAddSaveReqVO addReqVO) {
        maintenanceRecordService.addMaintenanceRecord(addReqVO);
        return success(true);
    }

    /*****************************************************************************************************/
    /********                                  使用备件                                           **********/
    /*****************************************************************************************************/

    @GetMapping("/SparePart/getByRecordId")
    @Operation(summary = "根据保养维护记录获得使用备件")
    @PreAuthorize("@ss.hasPermission('dms:maintenance-record:query')")
    public CommonResult<List<MaintenanceRecordToSparePartDO>> getSparePartByRecordId(@RequestParam("recordId") String recordId) {
        return success(maintenanceRecordService.getSparePartByRecordId(recordId));
    }

    @GetMapping("/SparePart/getByPartId")
    @Operation(summary = "根据备件获得使用记录")
    @PreAuthorize("@ss.hasPermission('dms:maintenance-record:query')")
    public CommonResult<List<MaintenanceRecordToSparePartDO>> getSparePartByPartId(@RequestParam("partId") String partId) {
        return success(maintenanceRecordService.getSparePartByPartId(partId));
    }

}
