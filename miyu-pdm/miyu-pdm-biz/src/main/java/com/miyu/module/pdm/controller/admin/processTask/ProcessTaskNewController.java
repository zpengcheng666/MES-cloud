package com.miyu.module.pdm.controller.admin.processTask;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskRespVO;
import com.miyu.module.pdm.service.processTask.ProcessTaskService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("pdm/process-task-new")
@Validated
public class ProcessTaskNewController {
    @Resource
    private ProcessTaskService processTaskService;

    @GetMapping("getPartListByProjectCode")
    @Operation(summary = "获得待分配任务的零件目录列表", description = "来源于项目立项")
    public CommonResult<List<ProcessTaskRespVO>> getPartListByProjectCode(@Valid ProcessTaskRespVO reqVO) {
        List<ProcessTaskRespVO> list = processTaskService.getPartListByProjectCodeNew(reqVO);
        return success(list);
    }

    @PostMapping("/addNewProcessTask")
    @Operation(summary = "添加负责人与截止日期")
    @PreAuthorize("@ss.hasPermission('pdm:feasibilityTask:add')")
    public CommonResult<String> addFeasibilityTask(@Valid @RequestBody ProcessTaskReqVO addReqVO) {
        return success(processTaskService.addProcessTaskNew(addReqVO));
    }
}
