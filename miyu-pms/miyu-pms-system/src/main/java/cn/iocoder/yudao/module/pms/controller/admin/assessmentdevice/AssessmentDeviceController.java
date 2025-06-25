package cn.iocoder.yudao.module.pms.controller.admin.assessmentdevice;

import cn.iocoder.yudao.module.pms.controller.admin.assessmentdevice.vo.AssessmentDevicePageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.assessmentdevice.vo.AssessmentDeviceRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.assessmentdevice.vo.AssessmentDeviceSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessmentdevice.AssessmentDeviceDO;
import cn.iocoder.yudao.module.pms.service.assessmentdevice.AssessmentDeviceService;
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

@Tag(name = "管理后台 - 评审子表，关联的设备")
@RestController
@RequestMapping("/pms/assessment-device")
@Validated
public class AssessmentDeviceController {

    @Resource
    private AssessmentDeviceService assessmentDeviceService;

    @PostMapping("/create")
    @Operation(summary = "创建评审子表，关联的设备")
    @PreAuthorize("@ss.hasPermission('pms:assessment-device:create')")
    public CommonResult<String> createAssessmentDevice(@Valid @RequestBody AssessmentDeviceSaveReqVO createReqVO) {
        return success(assessmentDeviceService.createAssessmentDevice(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新评审子表，关联的设备")
    @PreAuthorize("@ss.hasPermission('pms:assessment-device:update')")
    public CommonResult<Boolean> updateAssessmentDevice(@Valid @RequestBody AssessmentDeviceSaveReqVO updateReqVO) {
        assessmentDeviceService.updateAssessmentDevice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除评审子表，关联的设备")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pms:assessment-device:delete')")
    public CommonResult<Boolean> deleteAssessmentDevice(@RequestParam("id") String id) {
        assessmentDeviceService.deleteAssessmentDevice(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得评审子表，关联的设备")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pms:assessment-device:query')")
    public CommonResult<AssessmentDeviceRespVO> getAssessmentDevice(@RequestParam("id") String id) {
        AssessmentDeviceDO assessmentDevice = assessmentDeviceService.getAssessmentDevice(id);
        return success(BeanUtils.toBean(assessmentDevice, AssessmentDeviceRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得评审子表，关联的设备分页")
    @PreAuthorize("@ss.hasPermission('pms:assessment-device:query')")
    public CommonResult<PageResult<AssessmentDeviceRespVO>> getAssessmentDevicePage(@Valid AssessmentDevicePageReqVO pageReqVO) {
        PageResult<AssessmentDeviceDO> pageResult = assessmentDeviceService.getAssessmentDevicePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AssessmentDeviceRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出评审子表，关联的设备 Excel")
    @PreAuthorize("@ss.hasPermission('pms:assessment-device:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAssessmentDeviceExcel(@Valid AssessmentDevicePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AssessmentDeviceDO> list = assessmentDeviceService.getAssessmentDevicePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "评审子表，关联的设备.xls", "数据", AssessmentDeviceRespVO.class,
                        BeanUtils.toBean(list, AssessmentDeviceRespVO.class));
    }

}
