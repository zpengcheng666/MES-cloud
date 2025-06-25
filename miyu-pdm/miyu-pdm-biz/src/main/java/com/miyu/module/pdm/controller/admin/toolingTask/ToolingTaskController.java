package com.miyu.module.pdm.controller.admin.toolingTask;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessPlanDetailRespVO;
import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingApplyReqVO;
import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingApplyRespVO;
import com.miyu.module.pdm.controller.admin.toolingTask.vo.ToolingTaskReqVO;
import com.miyu.module.pdm.controller.admin.toolingTask.vo.ToolingTaskRespVO;
import com.miyu.module.pdm.dal.dataobject.toolingTask.ToolingTaskDO;
import com.miyu.module.pdm.service.toolingTask.ToolingTaskService;
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

@Tag(name = "PDM - 工艺任务")
@RestController
@RequestMapping("pdm/tooling-task")
@Validated
public class ToolingTaskController {
    @Resource
    private ToolingTaskService toolingTaskService;

    @GetMapping("list")
    @Operation(summary = "获得项目编号列表", description = "项目编号倒序取所有")
    public CommonResult<List<ToolingTaskRespVO>> getProcessTaskDataList(@Valid ToolingTaskReqVO reqVO) {
        List<ToolingTaskDO> list = toolingTaskService.getToolingTaskList(reqVO);
        return success(BeanUtils.toBean(list, ToolingTaskRespVO.class));
    }

    @GetMapping("/getToolingTask")
    @Operation(summary = "获取工装详细信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ToolingTaskRespVO> getDataInfo(@RequestParam("id") String id) {
        ToolingTaskDO toolingTask = toolingTaskService.getToolingTask(id);
        return success((BeanUtils.toBean(toolingTask, ToolingTaskRespVO.class)));
    }


    @GetMapping("getToolingTaskDataList")
    @Operation(description = "工装设计任务列表")
    public CommonResult<List<ToolingTaskRespVO>> getToolingTaskDataList(@Valid ToolingTaskReqVO reqVO) {
        List<ToolingTaskRespVO> list = toolingTaskService.getToolingTaskDataList(reqVO);
        return success(list);
    }

    @GetMapping("getToolingDetailList")
    @Operation(description = "工装详细设计列表")
    public CommonResult<List<ToolingTaskRespVO>> getToolingDetailList(@Valid ToolingTaskReqVO reqVO) {
        List<ToolingTaskRespVO> list = toolingTaskService.getToolingDetailList(reqVO);
        return success(list);
    }

    @GetMapping("getToolingDetailById")
    @Operation(summary = "获取工装详细设计细信息")
    public CommonResult<ToolingTaskRespVO> getToolingDetailById(@RequestParam("id") String id) {
        ToolingTaskRespVO respVO = toolingTaskService.getToolingDetailById(id);
        return success(respVO);
    }

    @PostMapping("/add")
    @Operation(summary = "添加负责人与截止日期")
    public CommonResult<String> addToolingTask(@Valid @RequestBody ToolingTaskReqVO addReqVO) {
        return success(toolingTaskService.addToolingTask(addReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改负责人及截止日期")
    public CommonResult<Boolean> ToolingTaskReqVO(@Valid @RequestBody ToolingTaskReqVO updateReqVO) {
        toolingTaskService.updateToolingTask(updateReqVO);
        return success(true);
    }

    @PutMapping("/startDetailInstance")
    @Operation(summary = "工装申请发起流程")
    public CommonResult<Boolean> startDetailInstance(@Valid @RequestBody ToolingTaskReqVO updateReqVO) {
        toolingTaskService.startDetailInstance(updateReqVO);
        return success(true);
    }
    @GetMapping("/get")
    @Operation(summary = "获得工装申请信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:tooling-apply:get')")
    public CommonResult<ToolingTaskRespVO> getToolingApply(@RequestParam("id") String id) {
        ToolingTaskReqVO toolingApply = toolingTaskService.getToolingDetail(id);
        return success(BeanUtils.toBean(toolingApply, ToolingTaskRespVO.class));
    }

    @GetMapping("getFileByCustomizedIndex")
    @Operation(summary = "根据客户化标识获取对应文件目录")
    public CommonResult<List<ToolingTaskRespVO>> getFileByCustomizedIndex(@RequestParam("customizedIndex") String customizedIndex) {
        List<ToolingTaskRespVO>  respVO = toolingTaskService.getFileByCustomizedIndex(customizedIndex);
        return success(respVO);
    }
}
