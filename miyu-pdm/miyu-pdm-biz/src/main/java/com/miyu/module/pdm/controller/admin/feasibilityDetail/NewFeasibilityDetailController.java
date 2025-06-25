package com.miyu.module.pdm.controller.admin.feasibilityDetail;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.*;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.*;
import com.miyu.module.pdm.service.feasibilityDetail.FeasibilityDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "PDM - 技术评估")
@RestController
@RequestMapping("pdm/feasibility-detail-new")
@Validated
public class NewFeasibilityDetailController {

    @Resource
    private FeasibilityDetailService feasibilityDetailService;

    @GetMapping("getProjPartBomListByProjectCode")
    @Operation(summary = "获得当前选中项目的零件目录", description = "根据选中项目编号获取")
    public CommonResult<List<ProjPartBomRespVO>> getProjPartBomListByProjectCode(@Valid ProjPartBomReqVO reqVO) {
        List<ProjPartBomRespVO> list = feasibilityDetailService.getProjPartBomListByProjectCode(reqVO);
        return success(list);
    }

    @GetMapping("getPartListByProjectCode")
    @Operation(summary = "获得待评估的零件目录列表", description = "来源于任务分配")
    public CommonResult<List<ProjPartBomRespVO>> getPartListByProjectCode(@Valid ProjPartBomReqVO reqVO) {
        List<ProjPartBomRespVO> list = feasibilityDetailService.getPartListByProjectCodeNew(reqVO);
        return success(list);
    }

    @GetMapping("getPartDetailByTaskId")
    @Operation(summary = "审批详情页获取零件技术评估详细信息")
    public CommonResult<ProjPartBomRespVO> getPartDetailByTaskId(@RequestParam("id") String id) {
        ProjPartBomRespVO respVO = feasibilityDetailService.getPartDetailByTaskIdNew(id);
        return success(respVO);
    }
}
