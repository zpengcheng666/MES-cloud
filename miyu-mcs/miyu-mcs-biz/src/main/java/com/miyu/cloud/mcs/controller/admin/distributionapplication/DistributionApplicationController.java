package com.miyu.cloud.mcs.controller.admin.distributionapplication;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.cloud.dms.api.devicetype.dto.CommonDevice;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.service.devicetype.DeviceTypeService;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.dms.service.linestationgroup.LineStationGroupService;
import com.miyu.cloud.mcs.controller.admin.batchrecord.vo.BatchRecordRespVO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.service.batchrecord.BatchRecordService;
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

import com.miyu.cloud.mcs.controller.admin.distributionapplication.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.distributionapplication.DistributionApplicationDO;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import com.miyu.cloud.mcs.service.distributionapplication.DistributionApplicationService;

@Tag(name = "管理后台 - 物料配送申请")
@RestController
@RequestMapping("/mcs/distribution-application")
@Validated
public class DistributionApplicationController {

    @Resource
    private DistributionApplicationService distributionApplicationService;
    @Resource
    private DeviceTypeService deviceTypeService;
    @Resource
    private LedgerService ledgerService;
    @Resource
    private LineStationGroupService lineStationGroupService;
    @Resource
    private BatchRecordService batchRecordService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建物料配送申请")
    @PreAuthorize("@ss.hasPermission('mcs:distribution-application:create')")
    public CommonResult<String> createDistributionApplication(@Valid @RequestBody DistributionApplicationSaveReqVO createReqVO) {
        return success(distributionApplicationService.createDistributionApplication(createReqVO));
    }

    @PostMapping("/createApplication")
    @Operation(summary = "创建物料配送申请")
    @PreAuthorize("@ss.hasPermission('mcs:distribution-application:create')")
    public CommonResult<?> createApplication(@RequestBody DistributionApplicationEditVO createReqVO) {
        try {
            distributionApplicationService.createApplication(createReqVO);
            return success("操作成功");
        } catch (ServiceException e) {
            return CommonResult.error(e);
        }
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料配送申请")
    @PreAuthorize("@ss.hasPermission('mcs:distribution-application:update')")
    public CommonResult<Boolean> updateDistributionApplication(@Valid @RequestBody DistributionApplicationSaveReqVO updateReqVO) {
        distributionApplicationService.updateDistributionApplication(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料配送申请")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:distribution-application:delete')")
    public CommonResult<Boolean> deleteDistributionApplication(@RequestParam("id") String id) {
        distributionApplicationService.deleteDistributionApplication(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料配送申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcs:distribution-application:query')")
    public CommonResult<DistributionApplicationRespVO> getDistributionApplication(@RequestParam("id") String id) {
        DistributionApplicationDO distributionApplication = distributionApplicationService.getDistributionApplication(id);
        return success(BeanUtils.toBean(distributionApplication, DistributionApplicationRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料配送申请分页")
    @PreAuthorize("@ss.hasPermission('mcs:distribution-application:query')")
    public CommonResult<PageResult<DistributionApplicationRespVO>> getDistributionApplicationPage(@Valid DistributionApplicationPageReqVO pageReqVO) {
        PageResult<DistributionApplicationDO> pageResult = distributionApplicationService.getDistributionApplicationPage(pageReqVO);

        Set<Long> userIds = pageResult.getList().stream().map(item -> Long.parseLong(item.getCreator())).collect(Collectors.toSet());
        List<CommonDevice> list = BeanUtils.toBean(lineStationGroupService.getLineStationGroupList(), CommonDevice.class);
        list.addAll(BeanUtils.toBean(ledgerService.getLedgerList(), CommonDevice.class));
        List<AdminUserRespDTO> userList = adminUserApi.getUserList(userIds).getData();
        Map<String, String> map = list.stream().collect(Collectors.toMap(CommonDevice::getId, CommonDevice::getName, (a, b) -> b));
        Map<String, String> userMap = userList.stream().collect(Collectors.toMap(item -> item.getId() + "", AdminUserRespDTO::getNickname, (a, b) -> b));

        PageResult<DistributionApplicationRespVO> data = BeanUtils.toBean(pageResult, DistributionApplicationRespVO.class);
        for (DistributionApplicationRespVO applicationRespVO : data.getList()) {
            applicationRespVO.setUnitName(map.get(applicationRespVO.getProcessingUnitId()));
            applicationRespVO.setCreatorName(userMap.get(applicationRespVO.getCreator()));
        }
        return success(data);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料配送申请 Excel")
    @PreAuthorize("@ss.hasPermission('mcs:distribution-application:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDistributionApplicationExcel(@Valid DistributionApplicationPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DistributionApplicationDO> list = distributionApplicationService.getDistributionApplicationPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料配送申请.xls", "数据", DistributionApplicationRespVO.class,
                        BeanUtils.toBean(list, DistributionApplicationRespVO.class));
    }

    @GetMapping("/getApplicationUnit")
    public CommonResult<?> getApplicationUnit() {
        try {
            List<CommonDevice> unitList = deviceTypeService.getAllUnitList();
            return success(unitList);
        } catch (ServiceException e) {
            return CommonResult.error(e);
        }
    }

    @GetMapping("/getBatchRecordByUnitForDelivery")
    public CommonResult<?> getBatchRecordByUnitForDelivery(@RequestParam("deviceUnitId") String unitId) {
        List<BatchRecordRespVO> batchRecordDOList = batchRecordService.getBatchRecordByUnitForDelivery(unitId);
        return success(batchRecordDOList);
    }

    @GetMapping("/getRecordListByBatchAndType")
    @Operation(summary = "获得物料配送申请分页")
    @PreAuthorize("@ss.hasPermission('mcs:distribution-application:update')")
    public CommonResult<DistributionApplicationEditVO> getRecordListByBatchAndType(@Valid DistributionApplicationEditVO editVO) {
        return success(distributionApplicationService.getRecordListByBatchAndType(editVO));
    }

    @PostMapping("/submitApplication")
    @Operation(summary = "配送申请提交")
    @PreAuthorize("@ss.hasPermission('mcs:distribution-application:update')")
    public CommonResult<String> submitApplication(@RequestParam("id") String id) {
        try {
            distributionApplicationService.submitApplication(id);
            return success("提交成功");
        } catch (ServiceException e) {
            return CommonResult.error(e);
        }
    }

}
