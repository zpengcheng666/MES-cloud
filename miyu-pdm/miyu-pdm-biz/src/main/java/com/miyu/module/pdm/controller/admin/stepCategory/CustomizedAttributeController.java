package com.miyu.module.pdm.controller.admin.stepCategory;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.dataobject.vo.DataObjectSaveReqVO;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.*;
import com.miyu.module.pdm.dal.dataobject.stepCategory.CustomizedAttributeDO;
import com.miyu.module.pdm.dal.dataobject.stepCategory.StepCategoryDO;
import com.miyu.module.pdm.service.stepCategory.CustomizedAttributeService;
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

@Tag(name = "管理后台 - PDM 工步分类")
@RestController
@RequestMapping("/pdm/customizedAttribute")
@Validated
public class CustomizedAttributeController {

    @Resource
    private CustomizedAttributeService customizedAttributeService;

    @GetMapping("/get")
    @Operation(summary = "获得自定义属性列表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<List<CustomizedAttributeRespVO>> getCustomizedAttribute(@RequestParam("id") String id) {
        List<CustomizedAttributeDO> customizedAttribute = customizedAttributeService.getCustomizedAttributesByCategoryId(id);
        return success(BeanUtils.toBean(customizedAttribute, CustomizedAttributeRespVO.class));
    }

    @GetMapping("getById")
    @Operation(summary = "获得自定义属性")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<CustomizedAttributeRespVO> getCustomizedAttributeById(@RequestParam("id") String id) {
        CustomizedAttributeDO customizedAttribute = customizedAttributeService.getCustomizedAttribute(id);
        return success(BeanUtils.toBean(customizedAttribute, CustomizedAttributeRespVO.class));
    }
    @RequestMapping("/updateIndex")
    @Operation(summary = "更新产品数据对象")
    public CommonResult<Boolean>  updateIndex(@Valid @RequestBody CustomizedAttributeReqVO updateReqVO) {
        customizedAttributeService.updateIndex(updateReqVO);
        return success(true);
    }

    @PostMapping("/create")
    @Operation(summary = "新增自定义属性")
    public CommonResult<String> createCustomizedAttribute(@Valid @RequestBody CustomizedAttributeSaveReqVO createReqVO) {
        return success(customizedAttributeService.customizedAttribute(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改自定义属性")
    public CommonResult<Boolean> updateCustomizedAttribute(@Valid @RequestBody CustomizedAttributeSaveReqVO updateReqVO) {
        customizedAttributeService.updatecustomizedAttribute(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除自定义属性")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCustomizedAttribute(@RequestParam("id") String id) {
        customizedAttributeService.deletecustomizedAttribute(id);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "获得列表")
    public CommonResult<List<CustomizedAttributeRespVO>> getStepCategoryList(@Valid CustomizedAttributeSaveReqVO listReqVO) {
        List<CustomizedAttributeDO> list = customizedAttributeService.getCustomizedAttributeList(listReqVO);
        return success(BeanUtils.toBean(list, CustomizedAttributeRespVO.class));
    }
}
