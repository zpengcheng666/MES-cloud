package com.miyu.module.qms.controller.admin.unqualifiedregistration;

import com.miyu.module.qms.controller.admin.unqualifiedmaterial.vo.UnqualifiedMaterialRespVO;
import com.miyu.module.qms.dal.dataobject.unqualifiedmaterial.UnqualifiedMaterialDO;
import org.apache.commons.lang3.StringUtils;
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
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.module.qms.controller.admin.unqualifiedregistration.vo.*;
import com.miyu.module.qms.dal.dataobject.unqualifiedregistration.UnqualifiedRegistrationDO;
import com.miyu.module.qms.service.unqualifiedregistration.UnqualifiedRegistrationService;

@Tag(name = "管理后台 - 不合格品登记")
@RestController
@RequestMapping("/qms/unqualified-registration")
@Validated
public class UnqualifiedRegistrationController {

    @Resource
    private UnqualifiedRegistrationService unqualifiedRegistrationService;

    @PostMapping("/create")
    @Operation(summary = "创建不合格品登记")
    @PreAuthorize("@ss.hasPermission('qms:unqualified-registration:create')")
    public CommonResult<String> createUnqualifiedRegistration(@Valid @RequestBody UnqualifiedRegistrationSaveReqVO createReqVO) {
        return success(unqualifiedRegistrationService.createUnqualifiedRegistration(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新不合格品登记")
    @PreAuthorize("@ss.hasPermission('qms:unqualified-registration:update')")
    public CommonResult<Boolean> updateUnqualifiedRegistration(@Valid @RequestBody UnqualifiedRegistrationSaveReqVO updateReqVO) {
        unqualifiedRegistrationService.updateUnqualifiedRegistration(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除不合格品登记")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:unqualified-registration:delete')")
    public CommonResult<Boolean> deleteUnqualifiedRegistration(@RequestParam("id") String id) {
        unqualifiedRegistrationService.deleteUnqualifiedRegistration(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得不合格品登记")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:unqualified-registration:query')")
    public CommonResult<UnqualifiedRegistrationRespVO> getUnqualifiedRegistration(@RequestParam("id") String id) {
        UnqualifiedRegistrationDO unqualifiedRegistration = unqualifiedRegistrationService.getUnqualifiedRegistration(id);
        return success(BeanUtils.toBean(unqualifiedRegistration, UnqualifiedRegistrationRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得不合格品登记分页")
    @PreAuthorize("@ss.hasPermission('qms:unqualified-registration:query')")
    public CommonResult<PageResult<UnqualifiedRegistrationRespVO>> getUnqualifiedRegistrationPage(@Valid UnqualifiedRegistrationPageReqVO pageReqVO) {
        PageResult<UnqualifiedRegistrationDO> pageResult = unqualifiedRegistrationService.getUnqualifiedRegistrationPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UnqualifiedRegistrationRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出不合格品登记 Excel")
    @PreAuthorize("@ss.hasPermission('qms:unqualified-registration:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportUnqualifiedRegistrationExcel(@Valid UnqualifiedRegistrationPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<UnqualifiedRegistrationDO> list = unqualifiedRegistrationService.getUnqualifiedRegistrationPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "不合格品登记.xls", "数据", UnqualifiedRegistrationRespVO.class,
                        BeanUtils.toBean(list, UnqualifiedRegistrationRespVO.class));
    }

    @PostMapping("/saveBatch")
    @Operation(summary = "创建不合格品登记")
    @PreAuthorize("@ss.hasPermission('qms:unqualified-registration:create')")
    public CommonResult<Boolean> saveUnqualifiedRegistrationBatch(@Valid @RequestBody UnqualifiedRegistrationSaveReqVO createReqVO) {
        unqualifiedRegistrationService.saveUnqualifiedRegistrationBatch(createReqVO);
        return success(true);
    }


    @GetMapping("/list-by-sheet-scheme-id")
    @Operation(summary = "检验任务ID获得不合格品登记")
    @Parameter(name = "id", description = "检验单任务ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:unqualified-registration:query')")
    public CommonResult<List<UnqualifiedMaterialRespVO>> getUnqualifiedRegistrationListBySheetSchemeId(@RequestParam("id") String id) {
        List<UnqualifiedMaterialDO> list = unqualifiedRegistrationService.getUnqualifiedRegistrationListBySheetSchemeId(id);
        return success(BeanUtils.toBean(list, UnqualifiedMaterialRespVO.class, vo ->{
            if(StringUtils.isNotBlank(vo.getDefectiveCodesStr())){
                vo.setDefectiveCode( Arrays.asList(vo.getDefectiveCodesStr().split(",")));
            }
        }));
    }

    @PostMapping("/saveAndAuditBatch")
    @Operation(summary = "创建不合格品登记并提交审核")
    @PreAuthorize("@ss.hasPermission('qms:unqualified-registration:create')")
    public CommonResult<Boolean> saveAndAuditUnqualifiedRegistrationBatch(@Valid @RequestBody UnqualifiedRegistrationSaveReqVO createReqVO) {
        unqualifiedRegistrationService.saveAndAuditUnqualifiedRegistrationBatch(createReqVO);
        return success(true);
    }
}
