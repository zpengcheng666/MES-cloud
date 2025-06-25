package com.miyu.module.qms.controller.admin.inspectionsheetscheme;

import com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo.InspectionSchemeUpdateReqVO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;
import com.miyu.module.qms.service.inspectionsheetscheme.InspectionSheetSchemeService;

import java.util.List;

@Tag(name = "管理后台 - 检验单方案任务计划")
@RestController
@RequestMapping("/qms/inspection-sheet-scheme")
@Validated
public class InspectionSheetSchemeController {

    @Resource
    private InspectionSheetSchemeService inspectionSheetSchemeService;

    @PostMapping("/create")
    @Operation(summary = "创建检验单方案任务计划")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-scheme:create')")
    public CommonResult<String> createInspectionSheetScheme(@Valid @RequestBody InspectionSheetSchemeSaveReqVO createReqVO) {
        return success(inspectionSheetSchemeService.createInspectionSheetScheme(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检验单方案任务计划")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-scheme:update')")
    public CommonResult<Boolean> updateInspectionSheetScheme(@Valid @RequestBody InspectionSheetSchemeSaveReqVO updateReqVO) {
        inspectionSheetSchemeService.updateInspectionSheetScheme(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除检验单方案任务计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-scheme:delete')")
    public CommonResult<Boolean> deleteInspectionSheetScheme(@RequestParam("id") String id) {
        inspectionSheetSchemeService.deleteInspectionSheetScheme(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检验单方案任务计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-scheme:query')")
    public CommonResult<InspectionSheetSchemeRespVO> getInspectionSheetScheme(@RequestParam("id") String id) {
        InspectionSheetSchemeDO inspectionSheetScheme = inspectionSheetSchemeService.getInspectionSheetScheme(id);
        return success(BeanUtils.toBean(inspectionSheetScheme, InspectionSheetSchemeRespVO.class));
    }


    @PutMapping("/updateAssign")
    @Operation(summary = "分配检验人员")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:update')")
    public CommonResult<Boolean> updateInspectionSheetSchemeAssign(@Valid @RequestBody InspectionSheetSchemeDO inspectionSheetScheme) {
        inspectionSheetSchemeService.updateInspectionSheetSchemeAssign(inspectionSheetScheme);
        return success(true);
    }

    @PutMapping("/updateSelfAssign")
    @Operation(summary = "自检分配检验人员")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:update')")
    public CommonResult<Boolean> updateInspectionSheetSchemeSelfAssign(@Valid @RequestBody InspectionSheetSchemeDO inspectionSheetScheme) {
        inspectionSheetSchemeService.updateInspectionSheetSchemeSelfAssign(inspectionSheetScheme);
        return success(true);
    }

    @PutMapping("/updateSelfSchemeClaim")
    @Operation(summary = "自检任务认领")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:update')")
    public CommonResult<Boolean> updateInspectionSheetSchemeClaim(@Valid @RequestBody InspectionSheetSchemeDO inspectionSheetScheme) {
        inspectionSheetSchemeService.updateInspectionSheetSchemeClaim(inspectionSheetScheme);
        return success(true);
    }


    @GetMapping("/get-sheet-scheme-info-by-id")
    @Operation(summary = "获得检测任务信息")
    @Parameter(name = "id", description = "检测任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<List<InspectionSheetSchemeRespVO>> getInspectionSheetSchemeInfoBySchemeId(@RequestParam("id") String id) {
        return success(BeanUtils.toBean(inspectionSheetSchemeService.getInspectionSheetSchemeInfoById(id), InspectionSheetSchemeRespVO.class));
    }

    @PostMapping("/update-inspection-scheme-result")
    @Operation(summary = "任务检验")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:update')")
    public CommonResult<Boolean> updateInspectionSchemeResult(@Valid @RequestBody InspectionSchemeUpdateReqVO updateReqVO) {
        inspectionSheetSchemeService.updateInspectionSchemeResult(updateReqVO);
        return success(true);
    }


    /**
     * 生产操作终端获取检验任务集合
     * @param reqVO
     * @return
     */
    @GetMapping("/terminal/list")
    @Operation(summary = "生产终端获得检验单方案任务计划")
    @Parameter(name = "inspectionSheetId", description = "检验单Id")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<List<InspectionSheetSchemeTerminalRespVO>> getInspectionSheetSchemeList4Terminal(@Valid InspectionSchemeTerminalReqVO reqVO) {

        List<InspectionSheetSchemeDO> list = inspectionSheetSchemeService.getInspectionSheetSchemeList4Terminal(reqVO);

        return success(BeanUtils.toBean(list, InspectionSheetSchemeTerminalRespVO.class));
    }

    /**
     * 生产操作终端获取检验任务集合
     * @param reqVO
     * @return
     */
    @GetMapping("/terminal/scanBarCode")
    @Operation(summary = "生产终端获得检验单方案任务")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<InspectionSheetSchemeTerminalRespVO> getInspectionSheetSchemeListByBarCode4Terminal(@Valid InspectionSchemeTerminalReqVO reqVO) {
        InspectionSheetSchemeDO scheme = inspectionSheetSchemeService.getInspectionSheetSchemeListByBarCode4Terminal(reqVO);
        return success(BeanUtils.toBean(scheme, InspectionSheetSchemeTerminalRespVO.class));
    }

    /**
     * 生产终端检验扫码验证barCode
     * @param reqVO
     * @return
     */
    @GetMapping("/terminal/validInspectionSchemeBarCode")
    @Operation(summary = "生产终端检验扫码验证barCode")
    @Parameter(name = "inspectionSheetId", description = "检验单任务Id")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<Boolean> validInspectionSchemeBarCode(@Valid InspectionSchemeTerminalValidReqVO reqVO) {
        inspectionSheetSchemeService.validInspectionSchemeBarCode(reqVO);
        return success(true);
    }
}
