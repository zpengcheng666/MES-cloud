package com.miyu.module.pdm.controller.admin.processSupplement;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessPlanDetailReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessPlanDetailRespVO;
import com.miyu.module.pdm.controller.admin.processSupplement.vo.ProcessSupplementPageReqVO;
import com.miyu.module.pdm.controller.admin.processSupplement.vo.ProcessSupplementRespVO;
import com.miyu.module.pdm.controller.admin.processSupplement.vo.ProcessSupplementSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.processSupplement.ProcessSupplementDO;
import com.miyu.module.pdm.service.processSupplement.ProcessSupplementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - PDM 补加工工艺规程")
@RestController
@RequestMapping("/pdm/process-supplement")
@Validated
public class ProcessSupplementController {
    @Resource
    private ProcessSupplementService processSupplementService;

    @PostMapping("/create")
    @Operation(summary = "创建补加工工艺规程")
    public CommonResult<String> createProcessSupplement(@Valid @RequestBody ProcessSupplementSaveReqVO createReqVO) {
        return success(processSupplementService.createProcessSupplement(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改补加工工艺规程")
    public CommonResult<Boolean> updateProcessSupplement(@Valid @RequestBody ProcessSupplementSaveReqVO updateReqVO) {
        processSupplementService.updateProcessSupplement(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Parameter(name = "id", description = "id", required = true, example = "1024")
    public CommonResult<Boolean> deleteProcessSupplement(@RequestParam("id") String id) {
        processSupplementService.deleteProcessSupplement(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得补加工工艺规程信息")
    public CommonResult<ProcessSupplementRespVO> getProcessSupplement(@RequestParam("id") String id) {
        ProcessSupplementDO product = processSupplementService.getProcessSupplement(id);
        return success(BeanUtils.toBean(product, ProcessSupplementRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得补加工工艺规程分页")
    public CommonResult<PageResult<ProcessSupplementRespVO>> getProcessSupplementPage(ProcessSupplementPageReqVO pageReqVO) {
        PageResult<ProcessSupplementDO> pageResult = processSupplementService.getProcessSupplementPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProcessSupplementRespVO.class));
    }

    @GetMapping("/getProcessSupplementTreeList")
    @Operation(summary = "获得补加工工艺规程结构树")
    public CommonResult<List<ProcessPlanDetailRespVO>> getProcessSupplementTreeList(@Valid ProcessPlanDetailReqVO reqVO) {
        List<ProcessPlanDetailRespVO> list = processSupplementService.getProcessSupplementTreeList(reqVO);
        return success(list);
    }

    @PutMapping("/startProcessInstance")
    @Operation(summary = "补加工工艺规程发起流程")
    public CommonResult<Boolean> startProcessInstance(@Valid @RequestBody ProcessSupplementSaveReqVO reqVO) {
        processSupplementService.startProcessInstance(reqVO);
        return success(true);
    }

}
