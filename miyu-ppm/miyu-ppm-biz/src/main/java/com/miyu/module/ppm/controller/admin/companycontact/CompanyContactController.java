package com.miyu.module.ppm.controller.admin.companycontact;

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

import com.miyu.module.ppm.controller.admin.companycontact.vo.*;
import com.miyu.module.ppm.dal.dataobject.companycontact.CompanyContactDO;
import com.miyu.module.ppm.service.companycontact.CompanyContactService;

@Tag(name = "管理后台 - 企业联系人")
@RestController
@RequestMapping("/ppm/company-contact")
@Validated
public class CompanyContactController {

    @Resource
    private CompanyContactService companyContactService;

    @PostMapping("/create")
    @Operation(summary = "创建企业联系人")
    @PreAuthorize("@ss.hasPermission('ppm:company-contact:create')")
    public CommonResult<String> createCompanyContact(@Valid @RequestBody CompanyContactSaveReqVO createReqVO) {
        return success(companyContactService.createCompanyContact(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新企业联系人")
    @PreAuthorize("@ss.hasPermission('ppm:company-contact:update')")
    public CommonResult<Boolean> updateCompanyContact(@Valid @RequestBody CompanyContactSaveReqVO updateReqVO) {
        companyContactService.updateCompanyContact(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除企业联系人")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:company-contact:delete')")
    public CommonResult<Boolean> deleteCompanyContact(@RequestParam("id") String id) {
        companyContactService.deleteCompanyContact(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得企业联系人")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:company-contact:query')")
    public CommonResult<CompanyContactRespVO> getCompanyContact(@RequestParam("id") String id) {
        CompanyContactDO companyContact = companyContactService.getCompanyContact(id);
        return success(BeanUtils.toBean(companyContact, CompanyContactRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得企业联系人分页")
    @PreAuthorize("@ss.hasPermission('ppm:company-contact:query')")
    public CommonResult<PageResult<CompanyContactRespVO>> getCompanyContactPage(@Valid CompanyContactPageReqVO pageReqVO) {
        PageResult<CompanyContactDO> pageResult = companyContactService.getCompanyContactPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CompanyContactRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出企业联系人 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:company-contact:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCompanyContactExcel(@Valid CompanyContactPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CompanyContactDO> list = companyContactService.getCompanyContactPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "企业联系人.xls", "数据", CompanyContactRespVO.class,
                        BeanUtils.toBean(list, CompanyContactRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得企业联系人")
    @Parameter(name = "companyId", description = "企业ID", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:company:query')")
    public CommonResult<Map<String, Object>> getCompanyContactSimpleList(@RequestParam("companyId") String companyId) {
        return success(companyContactService.getCompanyContactSimpleList(companyId));
    }
}