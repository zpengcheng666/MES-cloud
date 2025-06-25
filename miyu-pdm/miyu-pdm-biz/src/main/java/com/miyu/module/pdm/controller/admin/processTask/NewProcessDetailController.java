package com.miyu.module.pdm.controller.admin.processTask;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomRespVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcedureFileSaveReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.service.processPlanDetail.ProcessPlanDetailService;
import com.miyu.module.pdm.service.processTask.ProcessDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "PDM - 工艺方案编制")
@RestController
@RequestMapping("pdm/process-detail-new")
@Validated
public class NewProcessDetailController {

    @Resource
    private ProcessDetailService processDetailService;

    @Resource
    private ProcessPlanDetailService processPlanDetailService;

    @GetMapping("getProjPartBomTreeListNew")
    @Operation(summary = "获得未关闭项目的零件目录结构树")
    public CommonResult<List<ProjPartBomTreeRespVO>> getProjPartBomTreeListNew(@Valid ProjPartBomReqVO reqVO) {
        List<ProjPartBomTreeRespVO> list = processDetailService.getProjPartBomTreeListNew(reqVO);
        return success(list);
    }

    @GetMapping("getPartDetailByTaskIdNew")
    @Operation(summary = "审批详情页获取零件工艺方案详细信息")
    public CommonResult<ProjPartBomRespVO> getPartDetailByTaskIdNew(@RequestParam("id") String id) {
        ProjPartBomRespVO respVO = processDetailService.getPartDetailByTaskIdNew(id);
        return success(respVO);
    }
    @GetMapping("getPartDetailNewHome")
    @Operation(summary = "主页工艺方案编制")
    public CommonResult<List<ProjPartBomRespVO>> getPartDetailNewHome(ProjPartBomReqVO reqVO) {
        List<ProjPartBomRespVO> respVO = processDetailService.getPartDetailNewHome(reqVO);
        return success(respVO);
    }
    @PostMapping("/saveProcedureFile")
    @PermitAll
    @Operation(summary = "保存工艺草图(免登录)")
    public CommonResult<String> saveProcedureFile(@Valid @RequestBody ProcedureFileSaveReqVO reqVO) {
        processPlanDetailService.saveSelectedResource(reqVO);
        return success("保存成功");
    }
}
