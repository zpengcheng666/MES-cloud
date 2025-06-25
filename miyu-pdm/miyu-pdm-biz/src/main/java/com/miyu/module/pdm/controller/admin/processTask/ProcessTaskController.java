package com.miyu.module.pdm.controller.admin.processTask;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskRespVO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessTaskDO;
import com.miyu.module.pdm.service.processTask.ProcessTaskService;
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
@RequestMapping("pdm/process-task")
@Validated
public class ProcessTaskController {
    @Resource
    private ProcessTaskService processTaskService;
    @GetMapping("list")
    @Operation(summary = "获得项目编号列表", description = "项目编号倒序取所有")
    public CommonResult<List<ProcessTaskRespVO>> getProcessTaskDataList(@Valid ProcessTaskReqVO reqVO) {
        List<ProcessTaskDO> list = processTaskService.getProcessTaskList(reqVO);
        return success(BeanUtils.toBean(list, ProcessTaskRespVO.class));
    }

    @GetMapping("/getProcessTask")
    @Operation(summary = "获取任务详细信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ProcessTaskRespVO> getDataInfo(@RequestParam("id") String id) {
        ProcessTaskDO processTask = processTaskService.getProcessTask(id);
        return success((BeanUtils.toBean(processTask, ProcessTaskRespVO.class)));
    }


    @GetMapping("getPartListByProjectCode")
    @Operation(summary = "获得当前项目关联零件目录列表", description = "根据选中项目id")
    public CommonResult<List<ProcessTaskRespVO>> getPartListByProjectCode(@Valid ProcessTaskReqVO reqVO) {
        List<ProcessTaskRespVO> list = processTaskService.getPartListByProjectCode(reqVO);
        return success(list);
    }

    @PostMapping("/add")
    @Operation(summary = "添加负责人与截止日期")
//    @PreAuthorize("@ss.hasPermission('pdm:processTask:add')")
    public CommonResult<String> addProcessTask(@Valid @RequestBody ProcessTaskReqVO addReqVO) {
        return success(processTaskService.addProcessTask(addReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改负责人及截止日期")
//    @PreAuthorize("@ss.hasPermission('pdm:processTask:update')")
    public CommonResult<Boolean> updateProcessTask(@Valid @RequestBody ProcessTaskReqVO updateReqVO) {
        processTaskService.updateProcessTask(updateReqVO);
        return success(true);
    }
}
