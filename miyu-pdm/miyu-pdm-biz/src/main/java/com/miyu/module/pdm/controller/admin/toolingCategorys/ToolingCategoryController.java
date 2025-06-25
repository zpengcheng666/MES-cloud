package com.miyu.module.pdm.controller.admin.toolingCategorys;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.module.pdm.controller.admin.productCategory.vo.ProductCategoryListReqVO;
import com.miyu.module.pdm.controller.admin.productCategory.vo.ProductCategoryRespVO;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategoryListReqVO;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategoryPageReqVO;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategoryRespVO;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategorySaveReqVO;
import com.miyu.module.pdm.dal.dataobject.product.ProductCategoryDO;
import com.miyu.module.pdm.dal.dataobject.toolingCategory.ToolingCategoryDO;
import com.miyu.module.pdm.service.toolingCategory.ToolingCategoryService;
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

@Tag(name = "管理后台 - 产品分类信息")
@RestController
@RequestMapping("/pdm/tooling-category")
@Validated
public class ToolingCategoryController {

    @Resource
    private ToolingCategoryService toolingCategoryService;

    @PostMapping("/create")
    @Operation(summary = "创建产品分类信息")
    @PreAuthorize("@ss.hasPermission('pdm:tooling-category:create')")
    public CommonResult<Long> createToolingCategory(@Valid @RequestBody ToolingCategorySaveReqVO createReqVO) {
        return success(toolingCategoryService.createToolingCategory(createReqVO));
    }
    @GetMapping("/list")
    @Operation(summary = "获得产品分类列表")
    @PreAuthorize("@ss.hasPermission('pdm:product-category:query')")
    public CommonResult<List<ToolingCategoryRespVO>> getProductCategoryList(@Valid ToolingCategoryListReqVO listReqVO) {
        List<ToolingCategoryDO> list = toolingCategoryService.getProductCategoryList(listReqVO);
        return success(BeanUtils.toBean(list, ToolingCategoryRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得产品分类精简列表", description = "只包含被开启的分类，主要用于前端的下拉选项")
    public CommonResult<List<ToolingCategoryRespVO>> getProductCategorySimpleList() {
        List<ToolingCategoryDO> list = toolingCategoryService.getProductCategoryList(
                new ToolingCategoryListReqVO().setStatus(CommonStatusEnum.ENABLE.getStatus()));
        return success(convertList(list, category -> new ToolingCategoryRespVO()
                .setId(category.getId()).setName(category.getName()).setParentId(category.getParentId())));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品分类信息")
    @PreAuthorize("@ss.hasPermission('pdm:tooling-category:update')")
    public CommonResult<Boolean> updateToolingCategory(@Valid @RequestBody ToolingCategorySaveReqVO updateReqVO) {
        toolingCategoryService.updateToolingCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品分类信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pdm:tooling-category:delete')")
    public CommonResult<Boolean> deleteToolingCategory(@RequestParam("id") Long id) {
        toolingCategoryService.deleteToolingCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品分类信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:tooling-category:query')")
    public CommonResult<ToolingCategoryRespVO> getToolingCategory(@RequestParam("id") Long id) {
        ToolingCategoryDO toolingCategory = toolingCategoryService.getToolingCategory(id);
        return success(BeanUtils.toBean(toolingCategory, ToolingCategoryRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品分类信息分页")
    @PreAuthorize("@ss.hasPermission('pdm:tooling-category:query')")
    public CommonResult<PageResult<ToolingCategoryRespVO>> getToolingCategoryPage(@Valid ToolingCategoryPageReqVO pageReqVO) {
        PageResult<ToolingCategoryDO> pageResult = toolingCategoryService.getToolingCategoryPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ToolingCategoryRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品分类信息 Excel")
    @PreAuthorize("@ss.hasPermission('pdm:tooling-category:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportToolingCategoryExcel(@Valid ToolingCategoryPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ToolingCategoryDO> list = toolingCategoryService.getToolingCategoryPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "产品分类信息.xls", "数据", ToolingCategoryRespVO.class,
                        BeanUtils.toBean(list, ToolingCategoryRespVO.class));
    }

}