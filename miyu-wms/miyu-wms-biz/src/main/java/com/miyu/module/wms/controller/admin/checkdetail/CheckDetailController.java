package com.miyu.module.wms.controller.admin.checkdetail;

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

import com.miyu.module.wms.controller.admin.checkdetail.vo.*;
import com.miyu.module.wms.dal.dataobject.checkdetail.CheckDetailDO;
import com.miyu.module.wms.service.checkdetail.CheckDetailService;

@Tag(name = "管理后台 - 库存盘点详情")
@RestController
@RequestMapping("/wms/check-detail")
@Validated
public class CheckDetailController {

    @Resource
    private CheckDetailService checkDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建库存盘点详情")
    @PreAuthorize("@ss.hasPermission('wms:check-detail:create')")
    public CommonResult<String> createCheckDetail(@Valid @RequestBody CheckDetailSaveReqVO createReqVO) {
        return success(checkDetailService.createCheckDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新库存盘点详情")
    @PreAuthorize("@ss.hasPermission('wms:check-detail:update')")
    public CommonResult<Boolean> updateCheckDetail(@Valid @RequestBody CheckDetailSaveReqVO updateReqVO) {
        checkDetailService.updateCheckDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库存盘点详情")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:check-detail:delete')")
    public CommonResult<Boolean> deleteCheckDetail(@RequestParam("id") String id) {
        checkDetailService.deleteCheckDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得库存盘点详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:check-detail:query')")
    public CommonResult<CheckDetailRespVO> getCheckDetail(@RequestParam("id") String id) {
        CheckDetailDO checkDetail = checkDetailService.getCheckDetail(id);
        return success(BeanUtils.toBean(checkDetail, CheckDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库存盘点详情分页")
    @PreAuthorize("@ss.hasPermission('wms:check-detail:query')")
    public CommonResult<PageResult<CheckDetailRespVO>> getCheckDetailPage(@Valid CheckDetailPageReqVO pageReqVO) {
        PageResult<CheckDetailDO> pageResult = checkDetailService.getCheckDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CheckDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库存盘点详情 Excel")
    @PreAuthorize("@ss.hasPermission('wms:check-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCheckDetailExcel(@Valid CheckDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CheckDetailDO> list = checkDetailService.getCheckDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "库存盘点详情.xls", "数据", CheckDetailRespVO.class,
                        BeanUtils.toBean(list, CheckDetailRespVO.class));
    }

}