package com.miyu.module.pdm.controller.admin.feasibilityTask;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.BomReqVO;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.BomRespVO;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskRespVO;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRouteSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityTask.FeasibilityTaskDO;
import com.miyu.module.pdm.service.feasibilityTask.FeasibilityTaskService;
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

@Tag(name = "PDM - 技术评估任务")
@RestController
@RequestMapping("pdm/feasibility-task")
@Validated
public class FeasibilityTaskController {
    @Resource
    private FeasibilityTaskService feasibilityTaskService;
    @GetMapping("list")
    @Operation(summary = "获得项目编号列表", description = "项目编号倒序取所有")
    public CommonResult<List<FeasibilityTaskRespVO>> getFeasibilityTaskDataList(@Valid FeasibilityTaskReqVO reqVO) {
        List<FeasibilityTaskDO> list = feasibilityTaskService.getFeasibilityTaskList(reqVO);
        return success(BeanUtils.toBean(list, FeasibilityTaskRespVO.class));
    }

    @GetMapping("/getFeasibilityTask")
    @Operation(summary = "获取评估任务详细信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<FeasibilityTaskRespVO> getDataInfo(@RequestParam("id") String id) {
        FeasibilityTaskDO feasibilityTask = feasibilityTaskService.getFeasibilityTask(id);
        return success((BeanUtils.toBean(feasibilityTask, FeasibilityTaskRespVO.class)));
    }


    @GetMapping("getPartListByProjectCode")
    @Operation(summary = "获得当前项目关联零件目录列表", description = "根据选中项目id")
    public CommonResult<List<FeasibilityTaskRespVO>> getPartListByProjectCode(@Valid FeasibilityTaskReqVO reqVO) {
        List<FeasibilityTaskRespVO> list = feasibilityTaskService.getPartListByProjectCode(reqVO);
        return success(list);
    }

    @PostMapping("/add")
    @Operation(summary = "添加负责人与截止日期")
    @PreAuthorize("@ss.hasPermission('pdm:feasibilityTask:add')")
    public CommonResult<String> addFeasibilityTask(@Valid @RequestBody FeasibilityTaskReqVO addReqVO) {
        return success(feasibilityTaskService.addFeasibilityTask(addReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改负责人及截止日期")
    @PreAuthorize("@ss.hasPermission('pdm:feasibilityTask:update')")
    public CommonResult<Boolean> updateFeasibilityTask(@Valid @RequestBody FeasibilityTaskReqVO updateReqVO) {
        feasibilityTaskService.updateFeasibilityTask(updateReqVO);
        return success(true);
    }
}

