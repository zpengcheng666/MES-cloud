package com.miyu.module.ppm.controller.admin.companyqualitycontrol;

import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
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

import com.miyu.module.ppm.controller.admin.companyqualitycontrol.vo.*;
import com.miyu.module.ppm.dal.dataobject.companyqualitycontrol.CompanyQualityControlDO;
import com.miyu.module.ppm.service.companyqualitycontrol.CompanyQualityControlService;

@Tag(name = "管理后台 - 企业质量控制信息")
@RestController
@RequestMapping("/ppm/company-quality-control")
@Validated
public class CompanyQualityControlController {

    @Resource
    private CompanyQualityControlService companyQualityControlService;

    @PostMapping("/create")
    @Operation(summary = "创建企业质量控制信息")
    @PreAuthorize("@ss.hasPermission('ppm:company:create')")
    public CommonResult<String> createCompanyQualityControl(@Valid @RequestBody CompanyQualityControlSaveReqVO createReqVO) {
        return success(companyQualityControlService.createCompanyQualityControl(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新企业质量控制信息")
    @PreAuthorize("@ss.hasPermission('ppm:company:update')")
    public CommonResult<Boolean> updateCompanyQualityControl(@Valid @RequestBody CompanyQualityControlSaveReqVO updateReqVO) {
        companyQualityControlService.updateCompanyQualityControl(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除企业质量控制信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:company:delete')")
    public CommonResult<Boolean> deleteCompanyQualityControl(@RequestParam("id") String id) {
        companyQualityControlService.deleteCompanyQualityControl(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得企业质量控制信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:company:query')")
    public CommonResult<CompanyQualityControlRespVO> getCompanyQualityControl(@RequestParam("id") String id) {
        CompanyQualityControlDO companyQualityControl = companyQualityControlService.getCompanyQualityControl(id);
        return success(BeanUtils.toBean(companyQualityControl, CompanyQualityControlRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得企业质量控制信息分页")
    @PreAuthorize("@ss.hasPermission('ppm:company:query')")
    public CommonResult<PageResult<CompanyQualityControlRespVO>> getCompanyQualityControlPage(@Valid CompanyQualityControlPageReqVO pageReqVO) {
        PageResult<CompanyQualityControlDO> pageResult = companyQualityControlService.getCompanyQualityControlPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CompanyQualityControlRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出企业质量控制信息 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:company:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCompanyQualityControlExcel(@Valid CompanyQualityControlPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CompanyQualityControlDO> list = companyQualityControlService.getCompanyQualityControlPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "企业质量控制信息.xls", "数据", CompanyQualityControlRespVO.class,
                        BeanUtils.toBean(list, CompanyQualityControlRespVO.class));
    }


    @GetMapping("/get-by-company-id")
    @Operation(summary = "获得企业质量控制信息")
    @Parameter(name = "companyId", description = "公司ID")
    @PreAuthorize("@ss.hasPermission('ppm:company:query')")
    public CommonResult<CompanyQualityControlDO> getCompanyQualityControlByCompanyId(@RequestParam("companyId") String companyId) {
        return success(companyQualityControlService.getCompanyQualityControlByCompanyId(companyId));
    }
}