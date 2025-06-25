package com.miyu.module.ppm.controller.admin.companyproduct;

import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderProductDO;
import com.miyu.module.ppm.service.contract.ContractService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.math.BigDecimal;
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
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

import com.miyu.module.ppm.controller.admin.companyproduct.vo.*;
import com.miyu.module.ppm.dal.dataobject.companyproduct.CompanyProductDO;
import com.miyu.module.ppm.service.companyproduct.CompanyProductService;

@Tag(name = "管理后台 - 企业产品表，用于销售和采购")
@RestController
@RequestMapping("/ppm/company-product")
@Validated
public class CompanyProductController {

    @Resource
    private CompanyProductService companyProductService;

    @Resource
    private ContractService contractService;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @PostMapping("/create")
    @Operation(summary = "创建企业产品表，用于销售和采购")
    @PreAuthorize("@ss.hasPermission('ppm:company-product:create')")
    public CommonResult<String> createCompanyProduct(@Valid @RequestBody CompanyProductSaveReqVO createReqVO) throws Exception {
        return success(companyProductService.createCompanyProduct(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新企业产品表，用于销售和采购")
    @PreAuthorize("@ss.hasPermission('ppm:company-product:update')")
    public CommonResult<Boolean> updateCompanyProduct(@Valid @RequestBody CompanyProductSaveReqVO updateReqVO) {
        companyProductService.updateCompanyProduct(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除企业产品表，用于销售和采购")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:company-product:delete')")
    public CommonResult<Boolean> deleteCompanyProduct(@RequestParam("id") String id) {
        companyProductService.deleteCompanyProduct(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得企业产品表，用于销售和采购")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:company-product:query')")
    public CommonResult<CompanyProductRespVO> getCompanyProduct(@RequestParam("id") String id) {
        CompanyProductDO companyProduct = companyProductService.getCompanyProduct(id);
        List<CompanyProductDO> list = new ArrayList();
        list.add(companyProduct);
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(
                convertSet(list, obj -> obj.getMaterialId()));
        return success(BeanUtils.toBean(companyProduct, CompanyProductRespVO.class, vo -> {
            vo.setProductName(materialTypeMap.get(vo.getMaterialId()).getMaterialName());
            vo.setMaterialNumber(materialTypeMap.get(vo.getMaterialId()).getMaterialNumber());
            vo.setMaterialCode(materialTypeMap.get(vo.getMaterialId()).getMaterialTypeCode());
            vo.setMaterialName(materialTypeMap.get(vo.getMaterialId()).getMaterialName());
            vo.setMaterialParentTypeId(materialTypeMap.get(vo.getMaterialId()).getMaterialParentTypeId());
            vo.setMaterialTypeId(materialTypeMap.get(vo.getMaterialId()).getMaterialTypeId());
            vo.setMaterialSpecification(materialTypeMap.get(vo.getMaterialId()).getMaterialSpecification());
            vo.setMaterialBrand(materialTypeMap.get(vo.getMaterialId()).getMaterialBrand());
            vo.setMaterialUnit(materialTypeMap.get(vo.getMaterialId()).getMaterialUnit());
            vo.setMaterialManage(materialTypeMap.get(vo.getMaterialId()).getMaterialManage());
        }));
    }

    @GetMapping("/page")
    @Operation(summary = "获得企业产品表，用于销售和采购分页")
    @PreAuthorize("@ss.hasPermission('ppm:company-product:query')")
    public CommonResult<PageResult<CompanyProductRespVO>> getCompanyProductPage(@Valid CompanyProductPageReqVO pageReqVO) {
        PageResult<CompanyProductDO> pageResult = companyProductService.getCompanyProductPage(pageReqVO);

        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(
                convertSet(pageResult.getList(), obj -> obj.getMaterialId()));



        return success(BeanUtils.toBean(pageResult, CompanyProductRespVO.class, vo -> {
            vo.setProductName(materialTypeMap.get(vo.getMaterialId())==null?"":materialTypeMap.get(vo.getMaterialId()).getMaterialName());
        }));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出企业产品表，用于销售和采购 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:company-product:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCompanyProductExcel(@Valid CompanyProductPageReqVO pageReqVO,
                                          HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CompanyProductDO> list = companyProductService.getCompanyProductPage(pageReqVO).getList();

        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(
                convertSet(list, obj -> obj.getMaterialId()));
        // 导出 Excel
        ExcelUtils.write(response, "企业产品表，用于销售和采购.xls", "数据", CompanyProductRespVO.class,
                BeanUtils.toBean(list, CompanyProductRespVO.class, vo -> {
                    vo.setProductName(materialTypeMap.get(vo.getMaterialId())==null?"":materialTypeMap.get(vo.getMaterialId()).getMaterialName());
                }));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得企业产品集合", description = "只包含被开启的产品，主要用于前端的下拉选项")
    @Parameter(name = "companyId", description = "企业ID", required = true)
    public CommonResult<List<CompanyProductRespVO>> getCompanyProductSimpleList(@RequestParam("companyId") String companyId) {
        List<CompanyProductDO> list = companyProductService.getProductListByCompanyId(companyId);

        Map<String, ContractOrderProductDO> orderMap = contractService.getContractProductPriceHis(convertSet(list, obj -> obj.getMaterialId()));

        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(
                convertSet(list, obj -> obj.getMaterialId()));

        return success(convertList(list, product -> new CompanyProductRespVO().setId(product.getMaterialId())
                .setProductName(materialTypeMap.get(product.getMaterialId()) ==null?"":materialTypeMap.get(product.getMaterialId()).getMaterialName())
                .setInitPrice(product.getInitPrice())
                .setLeadTime(product.getLeadTime())
                .setInitTax(product.getInitTax())
                .setMaterialUnit(materialTypeMap.get(product.getMaterialId())==null?"":materialTypeMap.get(product.getMaterialId()).getMaterialUnit())
                .setMaxPrice(orderMap.get(product.getMaterialId()) == null ? "" : orderMap.get(product.getMaterialId()).getMaxPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                .setMinPrice(orderMap.get(product.getMaterialId()) == null ? "" : orderMap.get(product.getMaterialId()).getMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                .setAvgPrice(orderMap.get(product.getMaterialId()) == null ? "" : orderMap.get(product.getMaterialId()).getAvgPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                .setLatestPrice(orderMap.get(product.getMaterialId()) == null ? "" : orderMap.get(product.getMaterialId()).getLatestPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString())));
    }
}