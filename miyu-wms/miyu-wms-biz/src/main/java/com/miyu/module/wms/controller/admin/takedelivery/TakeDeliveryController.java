package com.miyu.module.wms.controller.admin.takedelivery;

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

import com.miyu.module.wms.controller.admin.takedelivery.vo.*;
import com.miyu.module.wms.dal.dataobject.takedelivery.TakeDeliveryDO;
import com.miyu.module.wms.service.takedelivery.TakeDeliveryService;

@Tag(name = "管理后台 - 物料收货")
@RestController
@RequestMapping("/wms/take-delivery")
@Validated
public class TakeDeliveryController {

    @Resource
    private TakeDeliveryService takeDeliveryService;

    @PostMapping("/create")
    @Operation(summary = "创建物料收货")
    @PreAuthorize("@ss.hasPermission('wms:take-delivery:create')")
    public CommonResult<String> createTakeDelivery(@Valid @RequestBody TakeDeliverySaveReqVO createReqVO) {
        return success(takeDeliveryService.createTakeDelivery(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料收货")
    @PreAuthorize("@ss.hasPermission('wms:take-delivery:update')")
    public CommonResult<Boolean> updateTakeDelivery(@Valid @RequestBody TakeDeliverySaveReqVO updateReqVO) {
        takeDeliveryService.updateTakeDelivery(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料收货")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:take-delivery:delete')")
    public CommonResult<Boolean> deleteTakeDelivery(@RequestParam("id") String id) {
        takeDeliveryService.deleteTakeDelivery(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料收货")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:take-delivery:query')")
    public CommonResult<TakeDeliveryRespVO> getTakeDelivery(@RequestParam("id") String id) {
        TakeDeliveryDO takeDelivery = takeDeliveryService.getTakeDelivery(id);
        return success(BeanUtils.toBean(takeDelivery, TakeDeliveryRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料收货分页")
    @PreAuthorize("@ss.hasPermission('wms:take-delivery:query')")
    public CommonResult<PageResult<TakeDeliveryRespVO>> getTakeDeliveryPage(@Valid TakeDeliveryPageReqVO pageReqVO) {
        PageResult<TakeDeliveryDO> pageResult = takeDeliveryService.getTakeDeliveryPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TakeDeliveryRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料收货 Excel")
    @PreAuthorize("@ss.hasPermission('wms:take-delivery:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportTakeDeliveryExcel(@Valid TakeDeliveryPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TakeDeliveryDO> list = takeDeliveryService.getTakeDeliveryPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料收货.xls", "数据", TakeDeliveryRespVO.class,
                        BeanUtils.toBean(list, TakeDeliveryRespVO.class));
    }

}