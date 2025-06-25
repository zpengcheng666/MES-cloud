package com.miyu.module.es.controller.admin.visit;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.annotation.security.PermitAll;
import javax.validation.*;
import javax.servlet.http.*;
import java.net.URL;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.module.es.controller.admin.visit.vo.*;
import com.miyu.module.es.dal.dataobject.visit.VisitDO;
import com.miyu.module.es.service.visit.VisitService;

@Tag(name = "管理后台 - 访客记录")
@RestController
@RequestMapping("/es/visit")
@Validated
public class VisitController {

    @Resource
    private VisitService visitService;

    @GetMapping("/get")
    @Operation(summary = "获得访客记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('es:visit:query')")
    public CommonResult<VisitRespVO> getVisit(@RequestParam("id") String id) {
        VisitRespVO visit = visitService.getVisit(id);
        return success(visit);
    }

    @GetMapping("/page")
    @Operation(summary = "获得访客记录分页")
    @PreAuthorize("@ss.hasPermission('es:visit:query')")
    public CommonResult<PageResult<VisitRespVO>> getVisitPage(@Valid VisitPageReqVO pageReqVO) {
        PageResult<VisitDO> pageResult = visitService.getVisitPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VisitRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出访客记录 Excel")
    @PreAuthorize("@ss.hasPermission('es:visit:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVisitExcel(@Valid VisitPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VisitDO> list = visitService.getVisitPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "访客记录.xls", "数据", VisitRespVO.class,
                        BeanUtils.toBean(list, VisitRespVO.class));
    }

}