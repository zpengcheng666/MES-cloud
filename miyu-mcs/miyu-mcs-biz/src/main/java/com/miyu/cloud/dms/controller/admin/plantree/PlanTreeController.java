package com.miyu.cloud.dms.controller.admin.plantree;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.cloud.dms.controller.admin.plantree.vo.PlanTreePageReqVO;
import com.miyu.cloud.dms.controller.admin.plantree.vo.PlanTreeRespVO;
import com.miyu.cloud.dms.controller.admin.plantree.vo.PlanTreeSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.plantree.PlanTreeDO;
import com.miyu.cloud.dms.service.plantree.PlanTreeService;
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

@Tag(name = "管理后台 - 计划关联树")
@RestController
@RequestMapping("/dms/plan-tree")
@Validated
public class PlanTreeController {

    @Resource
    private PlanTreeService planTreeService;

    @PostMapping("/create")
    @Operation(summary = "创建计划关联树")
    @PreAuthorize("@ss.hasPermission('dms:plan-tree:create')")
    public CommonResult<String> createPlanTree(@Valid @RequestBody PlanTreeSaveReqVO createReqVO) {
        return success(planTreeService.createPlanTree(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新计划关联树")
    @PreAuthorize("@ss.hasPermission('dms:plan-tree:update')")
    public CommonResult<Boolean> updatePlanTree(@Valid @RequestBody PlanTreeSaveReqVO updateReqVO) {
        planTreeService.updatePlanTree(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除计划关联树")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:plan-tree:delete')")
    public CommonResult<Boolean> deletePlanTree(@RequestParam("id") String id) {
        planTreeService.deletePlanTree(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得计划关联树")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:plan-tree:query')")
    public CommonResult<PlanTreeRespVO> getPlanTree(@RequestParam("id") String id) {
        PlanTreeDO planTree = planTreeService.getPlanTree(id);
        return success(BeanUtils.toBean(planTree, PlanTreeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得计划关联树分页")
    @PreAuthorize("@ss.hasPermission('dms:plan-tree:query')")
    public CommonResult<PageResult<PlanTreeRespVO>> getPlanTreePage(@Valid PlanTreePageReqVO pageReqVO) {
        PageResult<PlanTreeDO> pageResult = planTreeService.getPlanTreePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PlanTreeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出计划关联树 Excel")
    @PreAuthorize("@ss.hasPermission('dms:plan-tree:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportPlanTreeExcel(@Valid PlanTreePageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PlanTreeDO> list = planTreeService.getPlanTreePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "计划关联树.xls", "数据", PlanTreeRespVO.class, BeanUtils.toBean(list, PlanTreeRespVO.class));
    }

    @GetMapping("/getList")
    @Operation(summary = "获得计划关联树")
    @PreAuthorize("@ss.hasPermission('dms:plan-tree:query')")
    public CommonResult<List<PlanTreeRespVO>> getPlanTree() {
        List<PlanTreeDO> planTreeList = planTreeService.getPlanTreeList();
        return success(BeanUtils.toBean(planTreeList, PlanTreeRespVO.class));
    }

}
