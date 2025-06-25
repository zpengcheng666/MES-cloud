package com.miyu.module.pdm.controller.admin.feasibilityTask;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskRespVO;
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
@RequestMapping("pdm/feasibility-task-new")
@Validated
public class NewFeasibilityTaskController {
    @Resource
    private FeasibilityTaskService feasibilityTaskService;

    @GetMapping("getPartListByProjectCode")
    @Operation(summary = "获得待分配任务的零件目录列表", description = "来源于项目立项")
    public CommonResult<List<FeasibilityTaskRespVO>> getPartListByProjectCode(@Valid FeasibilityTaskReqVO reqVO) {
        List<FeasibilityTaskRespVO> list = feasibilityTaskService.getPartListByProjectCodeNew(reqVO);
        return success(list);
    }

    @PostMapping("/add")
    @Operation(summary = "添加负责人与截止日期")
    @PreAuthorize("@ss.hasPermission('pdm:feasibilityTask:add')")
    public CommonResult<String> addFeasibilityTask(@Valid @RequestBody FeasibilityTaskReqVO addReqVO) {
        return success(feasibilityTaskService.addFeasibilityTaskNew(addReqVO));
    }
}

