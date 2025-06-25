package com.miyu.module.qms.controller.admin.inspectionsheet;

import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.pdm.api.processPlanDetail.ProcessPlanDetailApi;
import com.miyu.module.pdm.api.processPlanDetail.dto.ProcedureRespDTO;
import com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo.InspectionSheetSchemePageReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo.InspectionSheetSchemeRespVO;
import com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo.InspectionMaterialOutBoundReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo.InspectionSheetSchemeMaterialRespVO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import com.miyu.module.qms.utils.StringListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
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
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

import com.miyu.module.qms.controller.admin.inspectionsheet.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheet.InspectionSheetDO;
import com.miyu.module.qms.service.inspectionsheet.InspectionSheetService;

@Tag(name = "管理后台 - 检验单")
@RestController
@RequestMapping("/qms/inspection-sheet")
@Validated
public class InspectionSheetController {

    @Resource
    private InspectionSheetService inspectionSheetService;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private DeptApi deptApi;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Resource
    private ProcessPlanDetailApi processPlanDetailApi;

    @PostMapping("/create")
    @Operation(summary = "创建检验单")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:create')")
    public CommonResult<String> createInspectionSheet(@Valid @RequestBody InspectionSheetSaveReqVO createReqVO) {
        return success(inspectionSheetService.createInspectionSheet(createReqVO));
    }

    @PostMapping("/self-inspection/create")
    @Operation(summary = "创建检验单(自检)")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:create')")
    public CommonResult<String> createInspectionSheetSelfInspection(@Valid @RequestBody InspectionSheetSelfCheckSaveReqVO createReqVO) {
        return success(inspectionSheetService.createInspectionSheetSelfInspection(createReqVO));
    }

    @PostMapping("/task/create")
    @Operation(summary = "创建检验单(检验任务)")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:create')")
    public CommonResult<String> createInspectionSheetTask(@Valid @RequestBody InspectionSheetSaveReqVO createReqVO) {
        return success(inspectionSheetService.createInspectionSheetTask(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检验单")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:update')")
    public CommonResult<Boolean> updateInspectionSheet(@Valid @RequestBody InspectionSheetSaveReqVO updateReqVO) {
        inspectionSheetService.updateInspectionSheet(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除检验单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:delete')")
    public CommonResult<Boolean> deleteInspectionSheet(@RequestParam("id") String id) {
        inspectionSheetService.deleteInspectionSheet(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检验单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<InspectionSheetRespVO> getInspectionSheet(@RequestParam("id") String id) {
        InspectionSheetDO inspectionSheet = inspectionSheetService.getInspectionSheet(id);
        return success(BeanUtils.toBean(inspectionSheet, InspectionSheetRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得检验单分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<PageResult<InspectionSheetRespVO>> getInspectionSheetPage(@Valid InspectionSheetPageReqVO pageReqVO) {
        PageResult<InspectionSheetDO> pageResult = inspectionSheetService.getInspectionSheetPage(pageReqVO);
        List<Long> userIdList = StringListUtils.stringListToLongList(convertList(pageResult.getList(), obj -> obj.getHeader()));

        Map<Long, AdminUserRespDTO> userMap;
        // 负责人
        if(!CollectionUtils.isEmpty(userIdList)) {
            userIdList = userIdList.stream().distinct().collect(Collectors.toList());
            userMap = userApi.getUserMap(userIdList);
        } else {
            userMap = new HashMap<>();
        }
        return success(BeanUtils.toBean(pageResult, InspectionSheetRespVO.class, vo -> {
            if(StringUtils.isNotBlank(vo.getHeader())) {
                MapUtils.findAndThen(userMap, Long.valueOf(vo.getHeader()), a -> vo.setHeader(a.getNickname()));
            }
        }));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出检验单 Excel")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionSheetExcel(@Valid InspectionSheetPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionSheetDO> list = inspectionSheetService.getInspectionSheetPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "检验单.xls", "数据", InspectionSheetRespVO.class,
                        BeanUtils.toBean(list, InspectionSheetRespVO.class));
    }

    @PutMapping("/get-inspection-sheet-info")
    @Operation(summary = "获得检验单信息")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<InspectionSheetRespVO> getInspectionSheetInfo(@Valid @RequestBody InspectionSheetReqVO reqVO) {
        InspectionSheetDO inspectionSheetDO = inspectionSheetService.getInspectionSheetInfo(reqVO);
        //用户
        List<Long> assignIdList = new ArrayList<>();
        if(StringUtils.isNotBlank(inspectionSheetDO.getAssignmentId())){
            assignIdList = Arrays.asList(Long.valueOf(inspectionSheetDO.getAssignmentId()));
        }
        Map<Long, AdminUserRespDTO> userMap;
        Map<Long, DeptRespDTO> deptMap;
        // 存在分配检验人员
        if(!CollectionUtils.isEmpty(assignIdList)) {
            assignIdList = assignIdList.stream().distinct().collect(Collectors.toList());
            userMap = userApi.getUserMap(assignIdList);
//            deptMap = deptApi.getDeptMap(assignIdList);
        } else {
            userMap = new HashMap<>();
//            deptMap = new HashMap<>();
        }

        List<String> produceIds = new ArrayList<>(Arrays.asList(inspectionSheetDO.getProcessId()));
        Map<String, ProcedureRespDTO> map;
        if (!CollectionUtils.isEmpty(produceIds)){
            map = processPlanDetailApi.getProcedureMapByIds(produceIds);
        }
        else {
            map = new HashMap<>();
        }

        // 物料类型
        List<String> materialConfigIds = new ArrayList<>(Arrays.asList(inspectionSheetDO.getMaterialConfigId()));
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);

        return success(BeanUtils.toBean(inspectionSheetDO, InspectionSheetRespVO.class, vo -> {
            if(StringUtils.isNotBlank(vo.getAssignmentId())) {
                // 分配人员
//                if(vo.getAssignmentType() == 1) {
                    MapUtils.findAndThen(userMap, Long.valueOf(vo.getAssignmentId()), a -> vo.setAssignmentName(a.getNickname()));
//                }
//                // 分配班组
//                else {
//                    MapUtils.findAndThen(deptMap, Long.valueOf(vo.getAssignmentId()), a -> vo.setAssignmentName(a.getName()));
//                }

                // 物料类型信息
                if(materialTypeMap.containsKey(vo.getMaterialConfigId())){
                    vo.setMaterialName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialName());
                    vo.setMaterialTypeName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialTypeName());
                    vo.setMaterialSpecification(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialSpecification());
                    vo.setMaterialUnit(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialUnit());
                }

                // 工艺 工序名称
                if(StringUtils.isNotBlank(vo.getProcessId())){
                    MapUtils.findAndThen(map, vo.getProcessId(), a -> vo.setTechnologyName(a.getProcessName()).setProcessName(a.getProcedureName()));
                }
            }
        }));
    }

    @GetMapping("/get-inspection-sheet-info-by-scheme-id")
    @Operation(summary = "获得检验单信息（检验任务查）")
    @Parameter(name = "id", description = "检测任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<InspectionSheetSchemeRespVO> getInspectionSheetInfoBySchemeId(@RequestParam("id") String id) {
        InspectionSheetSchemeDO schemeDO = inspectionSheetService.getInspectionSheetInfoBySchemeId(id);
        //用户
        List<Long> userIdList = new ArrayList<>();
        if(StringUtils.isNotBlank(schemeDO.getAssignmentId())){
            userIdList.add(Long.valueOf(schemeDO.getAssignmentId()));
        }

        // 班组
        List<Long> assignTeamIdList = new ArrayList<>();
        if(StringUtils.isNotBlank(schemeDO.getAssignmentTeamId())){
            assignTeamIdList.add(Long.valueOf(schemeDO.getAssignmentTeamId()));
        }

        // 负责人
//        if(StringUtils.isNotBlank(inspectionSheetDO.getHeader())){
//            userIdList.add(Long.valueOf(inspectionSheetDO.getHeader()));
//        }

        userIdList.add(Long.valueOf(schemeDO.getCreator()));
        Map<Long, AdminUserRespDTO> userMap;
        Map<Long, DeptRespDTO> deptMap;
        // 存在分配检验人员
        if(!CollectionUtils.isEmpty(userIdList)) {
            userIdList = userIdList.stream().distinct().collect(Collectors.toList());
            userMap = userApi.getUserMap(userIdList);
//            deptMap = deptApi.getDeptMap(userIdList);
        } else {
            userMap = new HashMap<>();
//            deptMap = new HashMap<>();
        }

        if(!CollectionUtils.isEmpty(assignTeamIdList)) {
            assignTeamIdList = assignTeamIdList.stream().distinct().collect(Collectors.toList());
            deptMap = deptApi.getDeptMap(assignTeamIdList);
        } else {
            deptMap = new HashMap<>();
        }

        List<String> produceIds = new ArrayList<>(Arrays.asList(schemeDO.getProcessId()));
        Map<String, ProcedureRespDTO> map;
        if (!CollectionUtils.isEmpty(produceIds)){
            map = processPlanDetailApi.getProcedureMapByIds(produceIds);
        }
        else {
            map = new HashMap<>();
        }

        // 物料类型
        List<String> materialConfigIds = new ArrayList<>(Arrays.asList(schemeDO.getMaterialConfigId()));
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);

        return success(BeanUtils.toBean(schemeDO, InspectionSheetSchemeRespVO.class, vo -> {
            // 分配人员
            if(StringUtils.isNotBlank(vo.getAssignmentId())) {
                MapUtils.findAndThen(userMap, Long.valueOf(vo.getAssignmentId()), a -> vo.setAssignmentName(a.getNickname()));
            }
            // 分配班组
            if(StringUtils.isNotBlank(vo.getAssignmentTeamId())){
                MapUtils.findAndThen(deptMap, Long.valueOf(vo.getAssignmentTeamId()), a -> vo.setAssignmentTeamName(a.getName()));
            }

            // 工艺 工序名称
            if(StringUtils.isNotBlank(vo.getProcessId())){
                MapUtils.findAndThen(map, vo.getProcessId(), a -> vo.setProcessName(a.getProcessName()).setProcedureName(a.getProcedureName()));
            }

            // 物料类型信息
            if(materialTypeMap.containsKey(vo.getMaterialConfigId())){
                vo.setMaterialName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialName());
                vo.setMaterialTypeName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialTypeName());
                vo.setMaterialSpecification(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialSpecification());
                vo.setMaterialUnit(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialUnit());
            }

        }));
    }


    @GetMapping("/get-inspection-sheet-by-scheme-id")
    @Operation(summary = "获得检验单信息（检验任务查）")
    @Parameter(name = "id", description = "检测任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<InspectionSheetRespVO> getInspectionSheetBySchemeId(@RequestParam("id") String id) {
        InspectionSheetDO inspectionSheetDO = inspectionSheetService.getInspectionSheetBySchemeId(id);
        return success(BeanUtils.toBean(inspectionSheetDO, InspectionSheetRespVO.class));

    }


    @GetMapping("/task/page")
    @Operation(summary = "专检领取检验任务获得检验单分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<PageResult<InspectionSheetSchemeRespVO>> getInspectionSheetTaskPage(@Valid InspectionSheetTaskPageReqVO pageReqVO) {
        PageResult<InspectionSheetSchemeDO> pageResult = inspectionSheetService.getInspectionSheetTaskPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InspectionSheetSchemeRespVO.class));
    }

    @GetMapping("/task/claim/page")
    @Operation(summary = "待领取检验任务列表")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<PageResult<InspectionSheetSchemeRespVO>> getInspectionClaimTaskListPage(InspectionSheetTaskPageReqVO pageReqVO) {
        PageResult<InspectionSheetSchemeDO> list = inspectionSheetService.getInspectionClaimTaskPage(pageReqVO);
        return success(BeanUtils.toBean(list, InspectionSheetSchemeRespVO.class));
    }

    @GetMapping("/task/inspection/page")
    @Operation(summary = "待领取检验任务列表")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<PageResult<InspectionSheetSchemeRespVO>> getInspectionTaskListPage(InspectionSheetTaskPageReqVO pageReqVO) {
        PageResult<InspectionSheetSchemeDO> list = inspectionSheetService.getInspectionTaskPage(pageReqVO);
        return success(BeanUtils.toBean(list, InspectionSheetSchemeRespVO.class));
    }

    @GetMapping("/task/unqualified/page")
    @Operation(summary = "待领取检验任务列表")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<PageResult<InspectionSheetSchemeRespVO>> getUnqualifiedTaskPage(InspectionSheetTaskPageReqVO pageReqVO) {
        PageResult<InspectionSheetSchemeDO> list = inspectionSheetService.getUnqualifiedTaskPage(pageReqVO);
        return success(BeanUtils.toBean(list, InspectionSheetSchemeRespVO.class));
    }

    // ==================== 子表（检验单方案任务计划） ====================

    @GetMapping("/inspection-sheet-scheme/page")
    @Operation(summary = "获得检验单方案任务计划分页")
    @Parameter(name = "inspectionSheetId", description = "检验单Id")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<PageResult<InspectionSheetSchemeRespVO>> getInspectionSheetSchemePage(@Valid InspectionSheetSchemePageReqVO pageReqVO) {

        PageResult<InspectionSheetSchemeDO> pageResult = inspectionSheetService.getInspectionSheetSchemePage(pageReqVO);
        // 用户
        List<Long> assignIdList = StringListUtils.stringListToLongList(convertList(pageResult.getList(), obj -> obj.getAssignmentId()));
        // 班组
        List<Long> assignTeamIdList = StringListUtils.stringListToLongList(convertList(pageResult.getList(), obj -> obj.getAssignmentTeamId()));

        Map<Long, AdminUserRespDTO> userMap;
        Map<Long, DeptRespDTO> deptMap;
        // 存在分配检验人员
        if(!CollectionUtils.isEmpty(assignIdList)) {
            assignIdList = assignIdList.stream().distinct().collect(Collectors.toList());
            userMap = userApi.getUserMap(assignIdList);
        } else {
            userMap = new HashMap<>();
        }

        // 存在分配检验班组
        if(!CollectionUtils.isEmpty(assignTeamIdList)) {
            assignTeamIdList = assignTeamIdList.stream().distinct().collect(Collectors.toList());
            deptMap = deptApi.getDeptMap(assignTeamIdList);
        } else {
            deptMap = new HashMap<>();
        }

        List<String> produceIds = convertList(pageResult.getList(), InspectionSheetSchemeDO::getProcessId);
        Map<String, ProcedureRespDTO> map;
        if (!CollectionUtils.isEmpty(produceIds)){
            map = processPlanDetailApi.getProcedureMapByIds(produceIds);
        }
        else {
            map = new HashMap<>();
        }

        return success(BeanUtils.toBean(pageResult, InspectionSheetSchemeRespVO.class, vo -> {
            // 分配人员
            if(StringUtils.isNotBlank(vo.getAssignmentId())) {
                MapUtils.findAndThen(userMap, Long.valueOf(vo.getAssignmentId()), a -> vo.setAssignmentName(a.getNickname()));
            }
            // 分配班组
            if(StringUtils.isNotBlank(vo.getAssignmentTeamId())){
                MapUtils.findAndThen(deptMap, Long.valueOf(vo.getAssignmentTeamId()), a -> vo.setAssignmentTeamName(a.getName()));
            }
            // 工序ID
            if(StringUtils.isNotBlank(vo.getProcessId())){
                MapUtils.findAndThen(map, vo.getProcessId(), a -> vo.setProcessName(a.getProcessName()).setProcedureName(a.getProcedureName()));
            }
        }));
    }

    @PostMapping("/inspection-sheet-scheme/create")
    @Operation(summary = "创建检验单方案任务计划")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:create')")
    public CommonResult<String> createInspectionSheetScheme(@Valid @RequestBody InspectionSheetSchemeDO inspectionSheetScheme) {
        return success(inspectionSheetService.createInspectionSheetScheme(inspectionSheetScheme));
    }

    @PutMapping("/inspection-sheet-scheme/update")
    @Operation(summary = "更新检验单方案任务计划")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:update')")
    public CommonResult<Boolean> updateInspectionSheetScheme(@Valid @RequestBody InspectionSheetSchemeDO inspectionSheetScheme) {
        inspectionSheetService.updateInspectionSheetScheme(inspectionSheetScheme);
        return success(true);
    }

    @DeleteMapping("/inspection-sheet-scheme/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除检验单方案任务计划")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:delete')")
    public CommonResult<Boolean> deleteInspectionSheetScheme(@RequestParam("id") String id) {
        inspectionSheetService.deleteInspectionSheetScheme(id);
        return success(true);
    }

	@GetMapping("/inspection-sheet-scheme/get")
	@Operation(summary = "获得检验单方案任务计划")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
	public CommonResult<InspectionSheetSchemeDO> getInspectionSheetScheme(@RequestParam("id") String id) {
	    return success(inspectionSheetService.getInspectionSheetScheme(id));
	}


    @GetMapping("/get-inspection-sheet-info-material")
    @Operation(summary = "物料条码和批次号获得检验单信息")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<List<InspectionSheetSchemeMaterialRespVO>> getInspectionSheetInfoMaterial(@Valid InspectionSheetMaterialReqVO reqVO) {
        List<InspectionSheetSchemeMaterialDO> inspectionSheetMaterialList = inspectionSheetService.getInspectionSheetInfoMaterial(reqVO);
        //用户
        List<Long> userIdList = StringListUtils.stringListToLongList(convertList(inspectionSheetMaterialList, InspectionSheetSchemeMaterialDO::getAssignmentId));
        Map<Long, AdminUserRespDTO> userMap;
        // 存在分配检验人员
        if(!CollectionUtils.isEmpty(userIdList)) {
            userIdList = userIdList.stream().distinct().collect(Collectors.toList());
            userMap = userApi.getUserMap(userIdList);
        } else {
            userMap = new HashMap<>();
        }

        return success(BeanUtils.toBean(inspectionSheetMaterialList, InspectionSheetSchemeMaterialRespVO.class, vo -> {
            if(StringUtils.isNotBlank(vo.getAssignmentId())) {
                MapUtils.findAndThen(userMap, Long.valueOf(vo.getAssignmentId()), a -> vo.setAssignmentName(a.getNickname()));
            }
        }));
    }


    @PostMapping("/outBound")
    @Operation(summary = "通知WMS出库")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:update')")
    public CommonResult<Boolean> outBoundShipping(@Valid @RequestBody InspectionMaterialOutBoundReqVO req) {
        inspectionSheetService.outBoundInspection(req);
        return success(true);
    }
}
