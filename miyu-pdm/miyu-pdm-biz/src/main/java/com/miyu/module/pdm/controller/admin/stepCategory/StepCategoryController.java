package com.miyu.module.pdm.controller.admin.stepCategory;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.StepCategoryListReqVO;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.StepCategoryRespVO;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.StepCategorySaveReqVO;
import com.miyu.module.pdm.dal.dataobject.stepCategory.StepCategoryDO;
import com.miyu.module.pdm.service.stepCategory.StepCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

@Tag(name = "管理后台 - PDM 工步分类")
@RestController
@RequestMapping("/pdm/step-category")
@Validated
public class StepCategoryController {

    @Resource
    private StepCategoryService stepCategoryService;

    @PostMapping("/create")
    @Operation(summary = "创建工步分类")
    @PreAuthorize("@ss.hasPermission('pdm:step-category:create')")
    public CommonResult<Long> createStepCategory(@Valid @RequestBody StepCategorySaveReqVO createReqVO) {
        return success(stepCategoryService.createStepCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工步分类")
    @PreAuthorize("@ss.hasPermission('pdm:step-category:update')")
    public CommonResult<Boolean> updateStepCategory(@Valid @RequestBody StepCategorySaveReqVO updateReqVO) {
        stepCategoryService.updateStepCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工步分类")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pdm:step-category:delete')")
    public CommonResult<Boolean> deleteStepCategory(@RequestParam("id") Long id) {
        stepCategoryService.deleteStepCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工步分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:step-category:query')")
    public CommonResult<StepCategoryRespVO> getStepCategory(@RequestParam("id") Long id) {
        StepCategoryDO category = stepCategoryService.getStepCategory(id);
        return success(BeanUtils.toBean(category, StepCategoryRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得工步分类列表")
    @PreAuthorize("@ss.hasPermission('pdm:step-category:query')")
    public CommonResult<List<StepCategoryRespVO>> getStepCategoryList(@Valid StepCategoryListReqVO listReqVO) {
        List<StepCategoryDO> list = stepCategoryService.getStepCategoryList(listReqVO);
        return success(BeanUtils.toBean(list, StepCategoryRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得工步分类精简列表", description = "只包含被开启的分类，主要用于前端的下拉选项")
    public CommonResult<List<StepCategoryRespVO>> getStepCategorySimpleList() {
        List<StepCategoryDO> list = stepCategoryService.getStepCategoryList(
                new StepCategoryListReqVO().setStatus(CommonStatusEnum.ENABLE.getStatus()));
        return success(convertList(list, category -> new StepCategoryRespVO()
                .setId(category.getId()).setName(category.getName()).setParentId(category.getParentId())));
    }

}