package com.miyu.module.pdm.controller.admin.processRoute;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRoutePageReqVO;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRouteRespVO;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRouteSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.processRoute.ProcessRouteDO;
import com.miyu.module.pdm.service.processRoute.ProcessRouteService;
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

@Tag(name = "管理后台 - PDM 加工路线")
@RestController
@RequestMapping("/pdm/process-route")
@Validated
public class ProcessRouteController {
    @Resource
    private ProcessRouteService processRouteService;

    @PostMapping("/create")
    @Operation(summary = "创建加工路线")
    @PreAuthorize("@ss.hasPermission('pdm:processRoute:create')")
    public CommonResult<String> createProcessRoute(@Valid @RequestBody ProcessRouteSaveReqVO createReqVO) {
        return success(processRouteService.createProcessRoute(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改加工路线")
    @PreAuthorize("@ss.hasPermission('pdm:processRoute:update')")
    public CommonResult<Boolean> updateProcessRoute(@Valid @RequestBody ProcessRouteSaveReqVO updateReqVO) {
        processRouteService.updateProcessRoute(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除加工路线")
    @Parameter(name = "id", description = "产品Id", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:processRoute:delete')")
    public CommonResult<Boolean> deleteProcessRoute(@RequestParam("id") String id) {
        processRouteService.deleteProcessRoute(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品信息")
    @PreAuthorize("@ss.hasPermission('pdm:processRoute:query')")
    public CommonResult<ProcessRouteRespVO> getProcessRoute(@RequestParam("id") String id) {
        ProcessRouteDO product = processRouteService.getProcessRoute(id);
        return success(BeanUtils.toBean(product, ProcessRouteRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得加工路线分页")
    @PreAuthorize("@ss.hasPermission('pdm:processRoute:query')")
    public CommonResult<PageResult<ProcessRouteRespVO>> getProcessRoutePage(ProcessRoutePageReqVO pageReqVO) {
        PageResult<ProcessRouteDO> pageResult = processRouteService.getProcessRoutePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProcessRouteRespVO.class));
    }

}
