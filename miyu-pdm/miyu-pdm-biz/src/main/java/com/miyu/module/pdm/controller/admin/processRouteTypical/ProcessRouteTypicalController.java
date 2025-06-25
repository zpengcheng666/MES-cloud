package com.miyu.module.pdm.controller.admin.processRouteTypical;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.processRouteTypical.vo.ProcessRouteTypicalPageReqVO;
import com.miyu.module.pdm.controller.admin.processRouteTypical.vo.ProcessRouteTypicalRespVO;
import com.miyu.module.pdm.controller.admin.processRouteTypical.vo.ProcessRouteTypicalSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.processRouteTypical.ProcessRouteTypicalDO;
import com.miyu.module.pdm.service.processRouteTypical.ProcessRouteTypicalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - PDM 典型工艺路线")
@RestController
@RequestMapping("/pdm/process-route-typical")
@Validated
public class ProcessRouteTypicalController {
    @Resource
    private ProcessRouteTypicalService processRouteTypicalService;

    @PostMapping("/create")
    @Operation(summary = "创建典型工艺路线")
    @PreAuthorize("@ss.hasPermission('pdm:processRouteTypical:create')")
    public CommonResult<String> createProcessRouteTypical(@Valid @RequestBody ProcessRouteTypicalSaveReqVO createReqVO) {
        return success(processRouteTypicalService.createProcessRouteTypical(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改典型工艺路线")
    @PreAuthorize("@ss.hasPermission('pdm:processRouteTypical:update')")
    public CommonResult<Boolean> updateProcessRouteTypical(@Valid @RequestBody ProcessRouteTypicalSaveReqVO updateReqVO) {
        processRouteTypicalService.updateProcessRouteTypical(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除典型工艺路线")
    @Parameter(name = "id", description = "工艺路线Id", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:processRouteTypical:delete')")
    public CommonResult<Boolean> deleteProcessRouteTypical(@RequestParam("id") String id) {
        processRouteTypicalService.deleteProcessRouteTypical(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得典型工艺路线信息")
    @PreAuthorize("@ss.hasPermission('pdm:processRouteTypical:query')")
    public CommonResult<ProcessRouteTypicalRespVO> getProcessRouteTypical(@RequestParam("id") String id) {
        ProcessRouteTypicalDO product = processRouteTypicalService.getProcessRouteTypical(id);
        return success(BeanUtils.toBean(product, ProcessRouteTypicalRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得典型工艺路线分页")
    @PreAuthorize("@ss.hasPermission('pdm:processRouteTypical:query')")
    public CommonResult<PageResult<ProcessRouteTypicalRespVO>> getProcessRouteTypicalPage(ProcessRouteTypicalPageReqVO pageReqVO) {
        PageResult<ProcessRouteTypicalDO> pageResult = processRouteTypicalService.getProcessRouteTypicalPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProcessRouteTypicalRespVO.class));
    }

}
