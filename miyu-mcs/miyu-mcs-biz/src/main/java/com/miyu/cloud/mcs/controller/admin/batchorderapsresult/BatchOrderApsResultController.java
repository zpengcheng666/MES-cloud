package com.miyu.cloud.mcs.controller.admin.batchorderapsresult;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.alibaba.fastjson.JSON;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.mcs.controller.admin.batchorderapsresult.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchorderapsresult.BatchOrderApsResultDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.dto.schedule.ScheduleConfig;
import com.miyu.cloud.mcs.dto.schedule.ScheduleJob;
import com.miyu.cloud.mcs.dto.schedule.SchedulePlan;
import com.miyu.cloud.mcs.service.batchorderapsresult.BatchOrderApsResultService;
import com.miyu.cloud.mcs.service.orderform.OrderFormService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.*;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 排产结果")
@RestController
@RequestMapping("/mcs/batch-order-aps-result")
@Validated
public class BatchOrderApsResultController {

    @Resource
    private BatchOrderApsResultService batchOrderApsResultService;
    @Resource
    private OrderFormService orderFormService;

    @PutMapping("/update")
    @Operation(summary = "更新排产结果")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order-aps-result:update')")
    public CommonResult<Boolean> updateBatchOrderApsResult(@Valid @RequestBody BatchOrderApsResultSaveReqVO updateReqVO) {
        batchOrderApsResultService.updateBatchOrderApsResult(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除排产结果")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:batch-order-aps-result:delete')")
    public CommonResult<Boolean> deleteBatchOrderApsResult(@RequestParam("id") String id) {
        batchOrderApsResultService.deleteBatchOrderApsResult(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得排产结果")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order-aps-result:query')")
    public CommonResult<BatchOrderApsResultRespVO> getBatchOrderApsResult(@RequestParam("id") String id) {
        BatchOrderApsResultDO batchOrderApsResult = batchOrderApsResultService.getBatchOrderApsResult(id);
        return success(BeanUtils.toBean(batchOrderApsResult, BatchOrderApsResultRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得排产结果分页")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order-aps-result:query')")
    public CommonResult<PageResult<BatchOrderApsResultRespVO>> getBatchOrderApsResultPage(@Valid BatchOrderApsResultPageReqVO pageReqVO) {
        PageResult<BatchOrderApsResultDO> pageResult = batchOrderApsResultService.getBatchOrderApsResultPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BatchOrderApsResultRespVO.class));
    }

    @PostMapping("/productionScheduling")
    @Operation(summary = "生产排产")
    public CommonResult<String> productionScheduling(@Valid @RequestBody OrderScheduleSaveVO createReqVO) {
        try {
            String apsId = batchOrderApsResultService.productionScheduling(createReqVO);
            return success(apsId);
        } catch (Exception e) {
            return CommonResult.error(5005, e.getMessage());
        }
    }

    @PostMapping("/schedulingAdopt")
    @Operation(summary = "排产通过")
    public CommonResult<String> schedulingAdopt(@RequestParam("id") String id) {
        try {
            BatchOrderApsResultDO apsResultDO = batchOrderApsResultService.getBatchOrderApsResult(id);
            ScheduleConfig scheduleConfig = JSON.parseObject(apsResultDO.getApsContent(), ScheduleConfig.class);
            List<SchedulePlan> planList = scheduleConfig.getPlanList();
            for (SchedulePlan schedulePlan : planList) {
                String orderId = schedulePlan.getId();
                OrderFormDO orderFormDO = orderFormService.getOrderForm(orderId);
                if (orderFormDO.getSchedulingStatus() == 0) {
                    orderFormService.createOrderFormDetail(orderFormDO);
                    orderFormService.updateById(orderFormDO.setSchedulingStatus(1));
                }
            }
            batchOrderApsResultService.schedulingAdopt(scheduleConfig);
            return success("操作成功");
        } catch (Exception e) {
            return CommonResult.error(5005, e.getMessage());
        }
    }

    @GetMapping("/getLedgerNameListByApsId")
    public CommonResult<List<String>> getLedgerNameListByApsId(@RequestParam("id") String id) {
        List<LedgerDO> deviceList = batchOrderApsResultService.getLedgerNameListByApsId(id);
        if (deviceList.size() == 0) return success(new ArrayList<>());
        return success(deviceList.stream().map(LedgerDO::getName).collect(Collectors.toList()));
    }

    @GetMapping("/getLedgerLoadByApsId")
    public CommonResult<?> getLedgerLoadByApsId(@RequestParam("id") String id) {
        Map<String,Object> deviceLoad = batchOrderApsResultService.getLedgerLoadByApsId(id);
        return success(deviceLoad);
    }

    @PostMapping("/saveAps")
    @Operation(summary = "生产排产")
    public CommonResult<String> saveAps(@RequestBody ScheduleConfig scheduleConfig) throws Exception {
        String apsId = batchOrderApsResultService.createApsResult(scheduleConfig);
        return success(apsId);
    }

}
