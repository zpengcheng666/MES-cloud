package com.miyu.cloud.mcs.controller.admin.productionrecords;

import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.cloud.mcs.controller.admin.productionrecords.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.productionrecords.ProductionRecordsDO;
import com.miyu.cloud.mcs.service.productionrecords.ProductionRecordsService;

@Tag(name = "管理后台 - 现场作业记录")
@RestController
@RequestMapping("/mcs/production-records")
@Validated
public class ProductionRecordsController {

    @Resource
    private ProductionRecordsService productionRecordsService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建现场作业记录")
    @PreAuthorize("@ss.hasPermission('mcs:production-records:create')")
    public CommonResult<String> createProductionRecords(@Valid @RequestBody ProductionRecordsSaveReqVO createReqVO) {
        return success(productionRecordsService.createProductionRecords(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新现场作业记录")
    @PreAuthorize("@ss.hasPermission('mcs:production-records:update')")
    public CommonResult<Boolean> updateProductionRecords(@Valid @RequestBody ProductionRecordsSaveReqVO updateReqVO) {
        productionRecordsService.updateProductionRecords(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除现场作业记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:production-records:delete')")
    public CommonResult<Boolean> deleteProductionRecords(@RequestParam("id") String id) {
        productionRecordsService.deleteProductionRecords(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得现场作业记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcs:production-records:query')")
    public CommonResult<ProductionRecordsRespVO> getProductionRecords(@RequestParam("id") String id) {
        ProductionRecordsDO productionRecords = productionRecordsService.getProductionRecords(id);
        return success(BeanUtils.toBean(productionRecords, ProductionRecordsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得现场作业记录分页")
    @PreAuthorize("@ss.hasPermission('mcs:production-records:query')")
    public CommonResult<PageResult<ProductionRecordsRespVO>> getProductionRecordsPage(@Valid ProductionRecordsPageReqVO pageReqVO) {
        PageResult<ProductionRecordsDO> pageResult = productionRecordsService.getProductionRecordsPage(pageReqVO);
        PageResult<ProductionRecordsRespVO> data = BeanUtils.toBean(pageResult, ProductionRecordsRespVO.class);
        Set<Long> userIds = pageResult.getList().stream().filter(item -> item.getOperationBy() != null).map(item -> Long.parseLong(item.getOperationBy())).collect(Collectors.toSet());
        List<AdminUserRespDTO> userList = adminUserApi.getUserList(userIds).getData();
        Map<String, String> userMap = userList.stream().collect(Collectors.toMap(item -> item.getId() + "", AdminUserRespDTO::getNickname, (a, b) -> b));
        for (ProductionRecordsRespVO recordsRespVO : data.getList()) {
            if (StringUtils.isNotBlank(recordsRespVO.getOperationBy())) {
                recordsRespVO.setOperationName(userMap.get(recordsRespVO.getOperationBy()));
            }
        }
        return success(data);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出现场作业记录 Excel")
    @PreAuthorize("@ss.hasPermission('mcs:production-records:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductionRecordsExcel(@Valid ProductionRecordsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductionRecordsDO> list = productionRecordsService.getProductionRecordsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "现场作业记录.xls", "数据", ProductionRecordsRespVO.class,
                        BeanUtils.toBean(list, ProductionRecordsRespVO.class));
    }

}
