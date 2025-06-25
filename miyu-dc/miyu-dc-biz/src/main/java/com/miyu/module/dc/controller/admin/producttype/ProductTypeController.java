package com.miyu.module.dc.controller.admin.producttype;

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

import com.miyu.module.dc.controller.admin.producttype.vo.*;
import com.miyu.module.dc.dal.dataobject.producttype.ProductTypeDO;
import com.miyu.module.dc.service.producttype.ProductTypeService;

@Tag(name = "管理后台 - 产品类型")
@RestController
@RequestMapping("/dc/product-type")
@Validated
public class ProductTypeController {

    @Resource
    private ProductTypeService productTypeService;

    @PostMapping("/create")
    @Operation(summary = "创建产品类型")
    @PreAuthorize("@ss.hasPermission('dc:product-type:create')")
    public CommonResult<String> createProductType(@Valid @RequestBody ProductTypeSaveReqVO createReqVO) {
        return success(productTypeService.createProductType(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品类型")
    @PreAuthorize("@ss.hasPermission('dc:product-type:update')")
    public CommonResult<Boolean> updateProductType(@Valid @RequestBody ProductTypeSaveReqVO updateReqVO) {
        productTypeService.updateProductType(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品类型")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dc:product-type:delete')")
    public CommonResult<Boolean> deleteProductType(@RequestParam("id") String id) {
        productTypeService.deleteProductType(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品类型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dc:product-type:query')")
    public CommonResult<ProductTypeRespVO> getProductType(@RequestParam("id") String id) {
        ProductTypeDO productType = productTypeService.getProductType(id);
        return success(BeanUtils.toBean(productType, ProductTypeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品类型分页")
    @PreAuthorize("@ss.hasPermission('dc:product-type:query')")
    public CommonResult<PageResult<ProductTypeRespVO>> getProductTypePage(@Valid ProductTypePageReqVO pageReqVO) {
        PageResult<ProductTypeDO> pageResult = productTypeService.getProductTypePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductTypeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品类型 Excel")
    @PreAuthorize("@ss.hasPermission('dc:product-type:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductTypeExcel(@Valid ProductTypePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductTypeDO> list = productTypeService.getProductTypePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "产品类型.xls", "数据", ProductTypeRespVO.class,
                        BeanUtils.toBean(list, ProductTypeRespVO.class));
    }

    @GetMapping("/getList")
    @Operation(summary = "获取产品类型列表")
    @PreAuthorize("@ss.hasPermission('dc:product-type:query')")
    public CommonResult<List<ProductTypeRespVO>> getProductTypeList() {
        List<ProductTypeDO> productTypeDOList = productTypeService.getProductTypeList();
        return success(BeanUtils.toBean(productTypeDOList,ProductTypeRespVO.class));
    }

    @GetMapping("/queryProductByDeviceId")
    @Operation(summary = "获取产品类型详细信息")
    @PreAuthorize("@ss.hasPermission('dc:product-type:query')")
    public CommonResult<List<ProductTypeRespVO>> queryProductByDeviceId(@RequestParam("id") String id) {
        List<ProductTypeDO> productTypeDOList = productTypeService.queryProductByDeviceId(id);
        return success(BeanUtils.toBean(productTypeDOList,ProductTypeRespVO.class));
    }

    @GetMapping("/getProductListByDeviceId")
    @Operation(summary = "根据设备获取产品信息")
    @PreAuthorize("@ss.hasPermission('dc:product-type:query')")
    public CommonResult<List<ProductTypeRespVO>> getProductListByDeviceId(@RequestParam("id") String id) {
        List<ProductTypeDO> productTypeDOList = productTypeService.getProductListByDeviceId(id);
        return success(BeanUtils.toBean(productTypeDOList,ProductTypeRespVO.class));
    }

}