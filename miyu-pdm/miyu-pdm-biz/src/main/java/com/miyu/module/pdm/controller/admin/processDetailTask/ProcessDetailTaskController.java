package com.miyu.module.pdm.controller.admin.processDetailTask;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskRespVO;
import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProcessDetailTaskReqVO;
import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProcessDetailTaskRespVO;
import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityTask.FeasibilityTaskDO;
import com.miyu.module.pdm.dal.dataobject.processDetailTask.ProcessDetailTaskDO;
import com.miyu.module.pdm.service.processDetailTask.ProcessDetailTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "PDM - 工艺详细设计任务")
@RestController
@RequestMapping("pdm/process-detail-task")
@Validated
public class ProcessDetailTaskController {

    @Resource
    private ProcessDetailTaskService processDetailTaskService;

    @GetMapping("/getProcessDetailTask")
    @Operation(summary = "获取评估任务详细信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ProcessDetailTaskRespVO> getDataInfo(@RequestParam("id") String id) {
        ProcessDetailTaskDO processDetailTask = processDetailTaskService.getProcessDetailTask(id);
        return success((BeanUtils.toBean(processDetailTask, ProcessDetailTaskRespVO.class)));
    }


    @GetMapping("getProjPartBomTreeList")
    @Operation(summary = "获取当前项目零件目录树表结构")
    public CommonResult<List<ProjPartBomTreeRespVO>> getProjPartBomTreeList(@Valid ProjPartBomReqVO reqVO) {
        List<ProjPartBomTreeRespVO> list = processDetailTaskService.getProjPartBomTreeList(reqVO);
        return success(list);
    }

    @GetMapping("getPartListByProjectCode")
    @Operation(summary = "获得当前项目关联零件目录列表", description = "根据选中项目id")
    public CommonResult<List<ProcessDetailTaskRespVO>> getPartListByProjectCode(@Valid ProcessDetailTaskReqVO reqVO) {
        List<ProcessDetailTaskRespVO> list = processDetailTaskService.getPartListByProjectCode(reqVO);
        return success(list);
    }


    /**指派工艺任务*/
    @PostMapping("/assign")
    @Operation(summary = "添加负责人与截止日期")
    public CommonResult<String> assignProcessTask(@Valid @RequestBody ProcessDetailTaskReqVO addReqVO) {
        return success(processDetailTaskService.assignProcessTask(addReqVO));
    }
    //@PreAuthorize("@ss.hasPermission('pdm:feasibilityTask:add')")


    @PutMapping("/update")
    @Operation(summary = "修改负责人及截止日期")
    public CommonResult<Boolean> updateProcessTask(@Valid @RequestBody ProcessDetailTaskReqVO updateReqVO) {
        processDetailTaskService.updateProcessTask(updateReqVO);
        return success(true);
    }
    //@PreAuthorize("@ss.hasPermission('pdm:feasibilityTask:update')")
}
