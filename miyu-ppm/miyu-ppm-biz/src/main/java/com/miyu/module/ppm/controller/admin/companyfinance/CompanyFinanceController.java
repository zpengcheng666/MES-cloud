package com.miyu.module.ppm.controller.admin.companyfinance;

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

import com.miyu.module.ppm.controller.admin.companyfinance.vo.*;
import com.miyu.module.ppm.dal.dataobject.companyfinance.CompanyFinanceDO;
import com.miyu.module.ppm.service.companyfinance.CompanyFinanceService;

@Tag(name = "管理后台 - 企业税务信息")
@RestController
@RequestMapping("/ppm/company-finance")
@Validated
public class CompanyFinanceController {

    @Resource
    private CompanyFinanceService companyFinanceService;

    @PostMapping("/create")
    @Operation(summary = "创建企业税务信息")
    @PreAuthorize("@ss.hasPermission('ppm:company:create')")
    public CommonResult<String> createCompanyFinance(@Valid @RequestBody CompanyFinanceSaveReqVO createReqVO) {
        return success(companyFinanceService.createCompanyFinance(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新企业税务信息")
    @PreAuthorize("@ss.hasPermission('ppm:company:update')")
    public CommonResult<Boolean> updateCompanyFinance(@Valid @RequestBody CompanyFinanceSaveReqVO updateReqVO) {
        companyFinanceService.updateCompanyFinance(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除企业税务信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:company:delete')")
    public CommonResult<Boolean> deleteCompanyFinance(@RequestParam("id") String id) {
        companyFinanceService.deleteCompanyFinance(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得企业税务信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:company:query')")
    public CommonResult<CompanyFinanceRespVO> getCompanyFinance(@RequestParam("id") String id) {
        CompanyFinanceDO companyFinance = companyFinanceService.getCompanyFinance(id);
        return success(BeanUtils.toBean(companyFinance, CompanyFinanceRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得企业税务信息分页")
    @PreAuthorize("@ss.hasPermission('ppm:company:query')")
    public CommonResult<PageResult<CompanyFinanceRespVO>> getCompanyFinancePage(@Valid CompanyFinancePageReqVO pageReqVO) {
        PageResult<CompanyFinanceDO> pageResult = companyFinanceService.getCompanyFinancePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CompanyFinanceRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出企业税务信息 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:company:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCompanyFinanceExcel(@Valid CompanyFinancePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CompanyFinanceDO> list = companyFinanceService.getCompanyFinancePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "企业税务信息.xls", "数据", CompanyFinanceRespVO.class,
                        BeanUtils.toBean(list, CompanyFinanceRespVO.class));
    }

    @GetMapping("/get-by-company-id")
    @Operation(summary = "获得企业税务信息")
    @Parameter(name = "companyId", description = "公司ID")
    @PreAuthorize("@ss.hasPermission('ppm:company:query')")
    public CommonResult<List<CompanyFinanceRespVO>> getCompanyFinanceByCompanyId(@RequestParam("companyId") String companyId) {
        List<CompanyFinanceDO> list = companyFinanceService.getCompanyFinanceByCompanyId(companyId);
        return success(BeanUtils.toBean(list, CompanyFinanceRespVO.class));
    }
}