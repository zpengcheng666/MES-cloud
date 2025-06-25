package com.miyu.module.pdm.controller.admin.product;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.module.pdm.controller.admin.product.vo.ProductPageReqVO;
import com.miyu.module.pdm.controller.admin.product.vo.ProductRespVO;
import com.miyu.module.pdm.controller.admin.product.vo.ProductSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.product.ProductDO;
import com.miyu.module.pdm.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

@Tag(name = "PDM - 产品")
@RestController
@RequestMapping("/pdm/product")
@Validated
public class ProductController {

    @Resource
    private ProductService productService;

    @PostMapping("/create")
    @Operation(summary = "创建产品")
    @PreAuthorize("@ss.hasPermission('pdm:product:create')")
    public CommonResult<String> createProduct(@Valid @RequestBody ProductSaveReqVO createReqVO) {
        return success(productService.createProduct(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改产品")
    @PreAuthorize("@ss.hasPermission('pdm:product:update')")
    public CommonResult<Boolean> updateProduct(@Valid @RequestBody ProductSaveReqVO updateReqVO) {
        productService.updateProduct(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品")
    @Parameter(name = "id", description = "产品Id", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:product:delete')")
    public CommonResult<Boolean> deleteProduct(@RequestParam("id") String id) {
        productService.deleteProduct(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品信息")
    @PreAuthorize("@ss.hasPermission('pdm:product:query')")
    public CommonResult<ProductRespVO> getProduct(@RequestParam("id") String id) {
        ProductDO product = productService.getProduct(id);
        return success(BeanUtils.toBean(product, ProductRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品分页")
    @PreAuthorize("@ss.hasPermission('pdm:product:query')")
    public CommonResult<PageResult<ProductRespVO>> getProductPage(ProductPageReqVO pageReqVO) {
        return success(productService.getProductVOPage(pageReqVO));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得产品精简列表", description = "只包含被开启的产品，主要用于前端的下拉选项")
    public CommonResult<List<ProductRespVO>> getProductSimpleList(ProductPageReqVO pageReqVO) {
        List<ProductRespVO> list = productService.getProductVOListByStatus(pageReqVO);
        return success(BeanUtils.toBean(list, ProductRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品 Excel")
    @ApiAccessLog(operateType = EXPORT)
    @PreAuthorize("@ss.hasPermission('pdm:product:export')")
    public void export(HttpServletResponse response, @Validated ProductPageReqVO pageReqVO) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<ProductRespVO> pageResult = productService.getProductVOPage(pageReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "产品.xls", "数据", ProductRespVO.class,
                pageResult.getList());
    }

}
