package com.miyu.cloud.mcs.controller.admin.receiptrecord;

import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.cloud.mcs.service.distributionapplication.DistributionApplicationService;
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

import com.miyu.cloud.mcs.controller.admin.receiptrecord.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.receiptrecord.ReceiptRecordDO;
import com.miyu.cloud.mcs.service.receiptrecord.ReceiptRecordService;

@Tag(name = "管理后台 - 生产单元签收记录")
@RestController
@RequestMapping("/mcs/receipt-record")
@Validated
public class ReceiptRecordController {

    @Resource
    private ReceiptRecordService receiptRecordService;
    @Resource
    private DistributionApplicationService distributionApplicationService;
    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建生产单元签收记录")
    @PreAuthorize("@ss.hasPermission('mcs:receipt-record:create')")
    public CommonResult<String> createReceiptRecord(@Valid @RequestBody ReceiptRecordSaveReqVO createReqVO) {
        return success(receiptRecordService.createReceiptRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新生产单元签收记录")
    @PreAuthorize("@ss.hasPermission('mcs:receipt-record:update')")
    public CommonResult<Boolean> updateReceiptRecord(@Valid @RequestBody ReceiptRecordSaveReqVO updateReqVO) {
        receiptRecordService.updateReceiptRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除生产单元签收记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:receipt-record:delete')")
    public CommonResult<Boolean> deleteReceiptRecord(@RequestParam("id") String id) {
        receiptRecordService.deleteReceiptRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得生产单元签收记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcs:receipt-record:query')")
    public CommonResult<ReceiptRecordRespVO> getReceiptRecord(@RequestParam("id") String id) {
        ReceiptRecordDO receiptRecord = receiptRecordService.getReceiptRecord(id);
        return success(BeanUtils.toBean(receiptRecord, ReceiptRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得生产单元签收记录分页")
    @PreAuthorize("@ss.hasPermission('mcs:receipt-record:query')")
    public CommonResult<PageResult<ReceiptRecordRespVO>> getReceiptRecordPage(@Valid ReceiptRecordPageReqVO pageReqVO) {
        PageResult<ReceiptRecordRespVO> pageResult = receiptRecordService.getReceiptRecordPage(pageReqVO);
        Set<Long> userIds = pageResult.getList().stream().filter(item -> item.getCreator() != null).map(item -> Long.parseLong(item.getCreator())).collect(Collectors.toSet());
        List<AdminUserRespDTO> userList = adminUserApi.getUserList(userIds).getData();
        Map<String, String> userMap = userList.stream().collect(Collectors.toMap(item -> item.getId() + "", AdminUserRespDTO::getNickname, (a, b) -> b));
        for (ReceiptRecordRespVO receiptRecordRespVO : pageResult.getList()) {
            receiptRecordRespVO.setCreatorName(userMap.get(receiptRecordRespVO.getCreator()));
        }
        return success(pageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出生产单元签收记录 Excel")
    @PreAuthorize("@ss.hasPermission('mcs:receipt-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportReceiptRecordExcel(@Valid ReceiptRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ReceiptRecordRespVO> list = receiptRecordService.getReceiptRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "生产单元签收记录.xls", "数据", ReceiptRecordRespVO.class, list);
    }

}
