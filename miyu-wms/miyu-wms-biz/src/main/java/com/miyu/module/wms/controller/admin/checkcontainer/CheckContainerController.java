package com.miyu.module.wms.controller.admin.checkcontainer;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
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

import com.miyu.module.wms.controller.admin.checkcontainer.vo.*;
import com.miyu.module.wms.dal.dataobject.checkcontainer.CheckContainerDO;
import com.miyu.module.wms.service.checkcontainer.CheckContainerService;

@Tag(name = "管理后台 - 库存盘点容器")
@RestController
@RequestMapping("/wms/check-container")
@Validated
public class CheckContainerController {

    @Resource
    private CheckContainerService checkContainerService;

    @PostMapping("/create")
    @Operation(summary = "创建库存盘点容器")
    @PreAuthorize("@ss.hasPermission('wms:check-container:create')")
    public CommonResult<String> createCheckContainer(@Valid @RequestBody CheckContainerSaveReqVO createReqVO) {
        return success(checkContainerService.createCheckContainer(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新库存盘点容器")
    @PreAuthorize("@ss.hasPermission('wms:check-container:update')")
    public CommonResult<Boolean> updateCheckContainer(@Valid @RequestBody CheckContainerSaveReqVO updateReqVO) {
        checkContainerService.updateCheckContainer(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库存盘点容器")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:check-container:delete')")
    public CommonResult<Boolean> deleteCheckContainer(@RequestParam("id") String id) {
        checkContainerService.deleteCheckContainer(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得库存盘点容器")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:check-container:query')")
    public CommonResult<CheckContainerRespVO> getCheckContainer(@RequestParam("id") String id) {
        CheckContainerDO checkContainer = checkContainerService.getCheckContainer(id);
        return success(BeanUtils.toBean(checkContainer, CheckContainerRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库存盘点容器分页")
    @PreAuthorize("@ss.hasPermission('wms:check-container:query')")
    public CommonResult<PageResult<CheckContainerRespVO>> getCheckContainerPage(@Valid CheckContainerPageReqVO pageReqVO) {
        PageResult<CheckContainerDO> pageResult = checkContainerService.getCheckContainerPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CheckContainerRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库存盘点容器 Excel")
    @PreAuthorize("@ss.hasPermission('wms:check-container:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCheckContainerExcel(@Valid CheckContainerPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CheckContainerDO> list = checkContainerService.getCheckContainerPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "库存盘点容器.xls", "数据", CheckContainerRespVO.class,
                        BeanUtils.toBean(list, CheckContainerRespVO.class));
    }

}