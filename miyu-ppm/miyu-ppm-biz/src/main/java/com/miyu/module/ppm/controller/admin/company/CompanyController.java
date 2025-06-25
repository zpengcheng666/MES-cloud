package com.miyu.module.ppm.controller.admin.company;

import cn.iocoder.yudao.module.system.api.ip.AreaApi;
import com.miyu.module.ppm.dal.dataobject.companydatabasefile.CompanyDatabaseFileDO;
import com.miyu.module.ppm.service.companyproduct.CompanyProductService;
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
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

import com.miyu.module.ppm.controller.admin.company.vo.*;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.service.company.CompanyService;

@Tag(name = "管理后台 - 企业基本信息")
@RestController
@RequestMapping("/ppm/company")
@Validated
public class CompanyController {

    @Resource
    private CompanyService companyService;

    @Resource
    private CompanyProductService companyProductService;


    @Resource
    private AreaApi areaApi;

    @PostMapping("/create")
    @Operation(summary = "创建企业基本信息")
    @PreAuthorize("@ss.hasPermission('ppm:company:create')")
    public CommonResult<String> createCompany(@Valid @RequestBody CompanySaveReqVO createReqVO) {
        return success(companyService.createCompany(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新企业基本信息")
    @PreAuthorize("@ss.hasPermission('ppm:company:update')")
    public CommonResult<Boolean> updateCompany(@Valid @RequestBody CompanySaveReqVO updateReqVO) {
        companyService.updateCompany(updateReqVO);
        return success(true);
    }
    @PutMapping("/updateAndSubmit")
    @Operation(summary = "更新并提交供应商审批")
    @PreAuthorize("@ss.hasPermission('ppm:company:create')")
    public CommonResult<Boolean> updateAndSubmit(@Valid @RequestBody CompanySaveReqVO updateReqVO) {
        companyService.updateCompanyAndSubmit(updateReqVO);
        return success(true);
    }
    @DeleteMapping("/delete")
    @Operation(summary = "删除企业基本信息")
    @Parameter(name = "ids", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:company:delete')")
    public CommonResult<Boolean> deleteCompany(@RequestParam("ids") List<String> ids) {
        companyService.deleteCompany(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得企业基本信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:company:query')")
    public CommonResult<CompanyRespVO> getCompany(@RequestParam("id") String id) {
        CompanyDO company = companyService.getCompany(id);
        return success(BeanUtils.toBean(company, CompanyRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得企业基本信息分页")
    @PreAuthorize("@ss.hasPermission('ppm:company:query')")
    public CommonResult<PageResult<CompanyRespVO>> getCompanyPage(@Valid CompanyPageReqVO pageReqVO) {
        PageResult<CompanyDO> pageResult = companyService.getCompanyPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CompanyRespVO.class, vo -> {
            vo.setAreaName(areaApi.getArea(vo.getArea()));
        }));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出企业基本信息 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:company:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCompanyExcel(@Valid CompanyPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CompanyDO> list = companyService.getCompanyPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "企业基本信息.xls", "数据", CompanyRespVO.class,
                        BeanUtils.toBean(list, CompanyRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得供应商")
    @PreAuthorize("@ss.hasPermission('ppm:company:query')")
    public CommonResult<List<Map<String, Object>>> getCompanySimpleList() {
        return success(companyService.getCompanySimpleList());
    }

    @GetMapping("/list-by-type")
    @Operation(summary = "获得企业基本信息")
    @Parameter(name = "type", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:company:query')")
    public CommonResult<List<CompanyRespVO>> getContractListByType(@RequestParam("type")  Collection<String> types) {
        List<CompanyDO> conmpanyList = companyService.getCompanyListByType(types);
        return success(BeanUtils.toBean(conmpanyList, CompanyRespVO.class, vo -> {
            vo.setAreaName(areaApi.getArea(vo.getArea()));
        }));
    }

    @PutMapping("/submit")
    @Operation(summary = "提交供应商审批")
    @PreAuthorize("@ss.hasPermission('ppm:company:update')")
    public CommonResult<Boolean> submit(@RequestParam("id") String id,  @RequestParam("processKey") String processKey) {
        companyService.submitCompany(id, processKey, getLoginUserId());
        return success(true);
    }

    @PostMapping("/createAndSubmit")
    @Operation(summary = "创建并提交供应商审批")
    @PreAuthorize("@ss.hasPermission('ppm:company:create')")
    public CommonResult<Boolean> createAndsubmit(@Valid @RequestBody CompanySaveReqVO createReqVO) {
        companyService.createAndSubmitCompany(createReqVO);
        return success(true);
    }


    @GetMapping("/coord/get")
    @Operation(summary = "获得外协企业基本信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:company:query')")
    public CommonResult<CompanyRespVO> getCompanyCoord(@RequestParam("id") String id) {
        CompanyDO company = companyService.getCompany(id);
        List<CompanyDatabaseFileDO> fileList = companyService.getDatabaseFileListByCompanyId(id);
        return success(BeanUtils.toBean(company, CompanyRespVO.class, o -> {
            o.setFileUrl(fileList.stream().map(CompanyDatabaseFileDO::getFileUrl).collect(Collectors.toList()));
        }));
    }

    @PostMapping("/coord/create")
    @Operation(summary = "创建外协企业基本信息")
    @PreAuthorize("@ss.hasPermission('ppm:company:create')")
    public CommonResult<String> createCompanyCoord(@Valid @RequestBody CompanyCoordSaveReqVO createReqVO) {
        return success(companyService.createCompanyCoord(createReqVO));
    }

    @PutMapping("/coord/update")
    @Operation(summary = "更新外协企业基本信息")
    @PreAuthorize("@ss.hasPermission('ppm:company:update')")
    public CommonResult<Boolean> updateCompanyCoord(@Valid @RequestBody CompanyCoordSaveReqVO updateReqVO) {
        companyService.updateCompanyCoord(updateReqVO);
        return success(true);
    }


    @DeleteMapping("/coord/delete")
    @Operation(summary = "删除外协企业基本信息")
    @Parameter(name = "ids", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:company:delete')")
    public CommonResult<Boolean> deleteCompanyCoord(@RequestParam("ids") List<String> ids) {
        companyService.deleteCompanyCoord(ids);
        return success(true);
    }
}