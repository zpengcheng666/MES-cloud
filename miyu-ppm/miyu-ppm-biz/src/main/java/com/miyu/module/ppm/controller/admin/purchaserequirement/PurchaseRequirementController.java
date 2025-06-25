package com.miyu.module.ppm.controller.admin.purchaserequirement;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.controller.admin.contract.util.StringListUtils;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDetailDO;
import lombok.extern.slf4j.Slf4j;
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
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

import com.miyu.module.ppm.controller.admin.purchaserequirement.vo.*;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDO;
import com.miyu.module.ppm.service.purchaserequirement.PurchaseRequirementService;

@Tag(name = "管理后台 - 采购申请主")
@RestController
@RequestMapping("/ppm/purchase-requirement")
@Validated
@Slf4j
public class PurchaseRequirementController {

    @Resource
    private PurchaseRequirementService purchaseRequirementService;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private DeptApi deptApi;

    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private PmsApi pmsApi;

    @PostMapping("/create")
    @Operation(summary = "创建采购申请")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-requirement:create')")
    public CommonResult<String> createPurchaseRequirement(@Valid @RequestBody PurchaseRequirementSaveReqVO createReqVO) {
        return success(purchaseRequirementService.createPurchaseRequirement(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新采购申请主")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-requirement:update')")
    public CommonResult<Boolean> updatePurchaseRequirement(@Valid @RequestBody PurchaseRequirementSaveReqVO updateReqVO) {
        purchaseRequirementService.updatePurchaseRequirement(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除采购申请主")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:purchase-requirement:delete')")
    public CommonResult<Boolean> deletePurchaseRequirement(@RequestParam("id") String id) {
        purchaseRequirementService.deletePurchaseRequirement(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得采购申请主")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-requirement:query')")
    public CommonResult<PurchaseRequirementRespVO> getPurchaseRequirement(@RequestParam("id") String id) {

        PurchaseRequirementDO purchaseRequirement = purchaseRequirementService.getPurchaseRequirement(id);
        // 获取采购申请详细
        List<PurchaseRequirementDetailDO> detailList = purchaseRequirementService.getPurchaseRequirementDetailListByRequirementId(id);

        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(
                convertSet(detailList, obj -> obj.getRequiredMaterial()));
        return success(BeanUtils.toBean(purchaseRequirement, PurchaseRequirementRespVO.class, vo -> {
            vo.setDetails(BeanUtils.toBean(detailList, PurchaseRequirementRespVO.Detail.class, o -> {
                o.setMaterialName(materialTypeMap.get(o.getRequiredMaterial()) == null ? "" : materialTypeMap.get(o.getRequiredMaterial()).getMaterialName());
                o.setMaterialUnit(materialTypeMap.get(o.getRequiredMaterial()) == null ? "" : materialTypeMap.get(o.getRequiredMaterial()).getMaterialUnit());
            }));
        }));
    }

    @GetMapping("/page")
    @Operation(summary = "获得采购申请主分页")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-requirement:query')")
    public CommonResult<PageResult<PurchaseRequirementRespVO>> getPurchaseRequirementPage(@Valid PurchaseRequirementPageReqVO pageReqVO) {
        PageResult<PurchaseRequirementDO> pageResult = purchaseRequirementService.getPurchaseRequirementPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }

        List<Long> userIdList = StringListUtils.stringListToLongList(convertList(pageResult.getList(), obj -> obj.getApplicant()));
        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);

        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(
                convertSet(pageResult.getList(), obj -> Long.parseLong(obj.getApplicationDepartment())));
        return success(BeanUtils.toBean(pageResult, PurchaseRequirementRespVO.class, vo -> {
            vo.setApplicant(userMap.get(NumberUtils.parseLong(vo.getApplicant())).getNickname());
            vo.setApplicationDepartment(deptMap.get(NumberUtils.parseLong(vo.getApplicationDepartment())).getName());
        }));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出采购申请主 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-requirement:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportPurchaseRequirementExcel(@Valid PurchaseRequirementPageReqVO pageReqVO,
                                               HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PurchaseRequirementDO> list = purchaseRequirementService.getPurchaseRequirementPage(pageReqVO).getList();
        List<Long> userIdList = StringListUtils.stringListToLongList(convertList(list, obj -> obj.getApplicant()));
        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);

        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(
                convertSet(list, obj -> Long.parseLong(obj.getApplicationDepartment())));

        // 导出 Excel
        ExcelUtils.write(response, "采购申请主.xls", "数据", PurchaseRequirementRespVO.class,
                BeanUtils.toBean(list, PurchaseRequirementRespVO.class, vo -> {
                    vo.setApplicant(userMap.get(NumberUtils.parseLong(vo.getApplicant())).getNickname());
                    vo.setApplicationDepartment(deptMap.get(NumberUtils.parseLong(vo.getApplicationDepartment())).getName());
                }));
    }

    @GetMapping("/purchase-requirement-detail/list-by-requirementId")
    @Operation(summary = "获得采购申请详细")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-requirement:query')")
    public CommonResult<List<PurchaseRequirementDetailRespVO>> getPurchaseRequirementDetailByRequirementId(PurchaseRequirementDetailReqVO reqVO) {
        // 获取采购申请详细
        List<PurchaseRequirementDetailDO> detailList = purchaseRequirementService.getPurchaseRequirementDetailList(reqVO);
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(
                convertSet(detailList, obj -> obj.getRequiredMaterial()));
        List<PurchaseRequirementDetailRespVO> respVOList = new ArrayList<>();
        Map<String, PmsApprovalDto> pmsApprovalDtoMap = pmsApi.getApprovalMap(convertSet(detailList, obj -> obj.getProjectId()));
        for (PurchaseRequirementDetailDO detailDO : detailList) {
            PurchaseRequirementDetailRespVO respVO = BeanUtils.toBean(detailDO, PurchaseRequirementDetailRespVO.class);
            MaterialConfigRespDTO a = materialTypeMap.get(detailDO.getRequiredMaterial());
            if (a != null) {
                respVO.setRequiredMaterialName(a.getMaterialName())
                        .setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                        .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                        .setMaterialSpecification(a.getMaterialSpecification())
                        .setMaterialBrand(a.getMaterialBrand())
                        .setMaterialTypeName(a.getMaterialTypeName())
                        .setMaterialParentTypeName(a.getMaterialParentTypeName());
            }
            PmsApprovalDto dto = pmsApprovalDtoMap.get(detailDO.getProjectId());
            if (dto != null) {
                respVO.setProjectName(dto.getProjectName());
                respVO.setProjectCode(dto.getProjectCode());
            }
            respVOList.add(respVO);
        }
        return success(respVOList);
    }

    @PutMapping("/submit")
    @Operation(summary = "提交采购申请审批")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-requirement:update')")
    public CommonResult<Boolean> submitRequirement(@RequestParam("id") String id) {
        purchaseRequirementService.submitRequirement(id, getLoginUserId());
        return success(true);
    }

    @PostMapping("/createAndSubmit")
    @Operation(summary = "创建并提交采购审批")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-requirement:create')")
    public CommonResult<Boolean> createAndsubmit(@Valid @RequestBody PurchaseRequirementSaveReqVO createReqVO) {
        purchaseRequirementService.createAndSubmitPurchaseRequirement(createReqVO);
        return success(true);
    }


    @GetMapping("/detailPage")
    @Operation(summary = "获得采购申请详情主分页")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-requirement:query')")
    public CommonResult<PageResult<PurchaseRequirementDetailRespVO>> getPurchaseRequirementDetailPage(@Valid PurchaseRequirementPageReqVO pageReqVO) {
        PageResult<PurchaseRequirementDetailDO> pageResult = purchaseRequirementService.getPurchaseRequirementDetailPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }

        List<Long> userIdList = StringListUtils.stringListToLongList(convertList(pageResult.getList(), obj -> obj.getApplicant()));
        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);

        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(
                convertSet(pageResult.getList(), obj -> Long.parseLong(obj.getApplicationDepartment())));


        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(
                convertSet(pageResult.getList(), obj -> obj.getRequiredMaterial()));
        List<PurchaseRequirementDetailRespVO> respVOList = new ArrayList<>();
        Map<String, PmsApprovalDto> pmsApprovalDtoMap = new HashMap<>();
        try {
            pmsApprovalDtoMap = pmsApi.getApprovalMap(convertSet(pageResult.getList(), obj -> obj.getProjectId()));
        } catch (Exception e) {
            log.error("PMS项目调用失败");
        }
        for (PurchaseRequirementDetailDO detailDO : pageResult.getList()) {
            PurchaseRequirementDetailRespVO respVO = BeanUtils.toBean(detailDO, PurchaseRequirementDetailRespVO.class);
            MaterialConfigRespDTO a = materialTypeMap.get(detailDO.getRequiredMaterial());
            if (a != null) {
                respVO.setRequiredMaterialName(a.getMaterialName())
                        .setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                        .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                        .setMaterialSpecification(a.getMaterialSpecification())
                        .setMaterialBrand(a.getMaterialBrand())
                        .setMaterialTypeName(a.getMaterialTypeName())
                        .setMaterialParentTypeName(a.getMaterialParentTypeName());
            }

            PmsApprovalDto dto = pmsApprovalDtoMap.get(detailDO.getProjectId());
            if (dto != null) {
                respVO.setProjectName(dto.getProjectName());
                respVO.setProjectCode(dto.getProjectCode());
            }
            respVO.setApplicant(userMap.get(NumberUtils.parseLong(respVO.getApplicant())).getNickname());
            respVO.setApplicationDepartment(deptMap.get(NumberUtils.parseLong(respVO.getApplicationDepartment())).getName());
            respVOList.add(respVO);
        }
        PageResult<PurchaseRequirementDetailRespVO> pageResults = new PageResult<PurchaseRequirementDetailRespVO>();

        pageResults.setList(respVOList);
        pageResults.setTotal(pageResult.getTotal());

        return success(pageResults);
    }

}