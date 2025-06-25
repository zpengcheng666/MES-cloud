package com.miyu.module.pdm.controller.admin.toolingApply;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomRespVO;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRoutePageReqVO;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRouteRespVO;
import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingApplyReqVO;
import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingApplyRespVO;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategoryPageReqVO;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategoryRespVO;
import com.miyu.module.pdm.dal.dataobject.processRoute.ProcessRouteDO;
import com.miyu.module.pdm.dal.dataobject.toolingApply.ToolingApplyDO;
import com.miyu.module.pdm.dal.dataobject.toolingCategory.ToolingCategoryDO;
import com.miyu.module.pdm.service.toolingApply.ToolingApplyService;
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

@Tag(name = "管理后台 - PDM 工装申请")
@RestController
@RequestMapping("/pdm/tooling-apply")
@Validated
public class TollingApplyController {
    @Resource
    private ToolingApplyService toolingApplyService;

    @GetMapping("/list")
    @Operation(summary = "获得工装申请分页")
    @PreAuthorize("@ss.hasPermission('pdm:tooling-apply:query')")
    public CommonResult<List<ToolingApplyRespVO>> getToolingApplyList(@Valid ToolingApplyReqVO reqVO) {
        List<ToolingApplyDO> list =  toolingApplyService.getToolingApplyList(reqVO);
        return success(BeanUtils.toBean(list, ToolingApplyRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品分类信息分页")
    public CommonResult<PageResult<ToolingApplyRespVO>> getToolingApplyPage(@Valid ToolingApplyReqVO reqVO) {
        PageResult<ToolingApplyDO> pageResult = toolingApplyService.getToolingApplyPage(reqVO);
        return success(BeanUtils.toBean(pageResult, ToolingApplyRespVO.class));
    }
    @GetMapping("/get")
    @Operation(summary = "获得工装申请信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:tooling-apply:get')")
    public CommonResult<ToolingApplyRespVO> getToolingApply(@RequestParam("id") String id) {
        ToolingApplyReqVO toolingApply = toolingApplyService.getToolingApply(id);
        return success(BeanUtils.toBean(toolingApply, ToolingApplyRespVO.class));
    }

    @PostMapping("/create")
    @Operation(summary = "添加工装申请")
    @PreAuthorize("@ss.hasPermission('pdm:tooling-apply:create')")
    public CommonResult<String> createToolingApply(@Valid @RequestBody ToolingApplyReqVO createReqVO) {
        return success(toolingApplyService.createToolingApply(createReqVO));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工装申请信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pdm:tooling-apply:delete')")
    public CommonResult<Boolean> deleteToolingApply(@RequestParam("id") String id) {
        toolingApplyService.deleteToolingApply(id);
        return success(true);
    }

    @PutMapping("/update")
    @Operation(summary = "更新工装申请信息")
    @PreAuthorize("@ss.hasPermission('pdm:tooling-apply:update')")
    public CommonResult<Boolean> updateToolingApply(@Valid @RequestBody ToolingApplyReqVO updateReqVO) {
        toolingApplyService.updateToolingApply(updateReqVO);
        return success(true);
    }


    @PutMapping("/startApplyInstance")
    @Operation(summary = "工装申请发起流程")
    public CommonResult<Boolean> startApplyInstance(@Valid @RequestBody ToolingApplyReqVO updateReqVO) {
        toolingApplyService.startApplyInstance(updateReqVO);
        return success(true);
    }

}
