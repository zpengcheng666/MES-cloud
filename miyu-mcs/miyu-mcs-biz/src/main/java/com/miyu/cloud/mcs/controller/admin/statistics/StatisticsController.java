package com.miyu.cloud.mcs.controller.admin.statistics;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.miyu.cloud.mcs.controller.admin.statistics.vo.StatisticQueryReqVO;
import com.miyu.cloud.mcs.service.statistics.StatisticsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Tag(name = "数据统计-图表")
@RestController
@RequestMapping("/mcs/statistics")
public class StatisticsController {

    @Resource
    private StatisticsService StatisticsService;

    //工人实际工时
    @GetMapping("/actualHours")
    public CommonResult<?> actualHours(StatisticQueryReqVO params) {
        return CommonResult.success(StatisticsService.getWorkerActualHoursList(params));
    }

    //工人有效工时
    @GetMapping("/effectiveHours")
    public CommonResult<?> effectiveHours(StatisticQueryReqVO params) {
        return CommonResult.success(StatisticsService.getWorkerEffectiveHoursList(params));
    }

    //工人加工记录
    @GetMapping("/workerProcessingRecords")
    public CommonResult<?> workerProcessingRecords(StatisticQueryReqVO params) {
        return CommonResult.success(StatisticsService.getWorkerProcessingRecords(params));
    }

    //订单进度
    @GetMapping("/orderProgress")
    public CommonResult<?> orderProgress() {
        return null;
    }

    //设备加工记录
    @GetMapping("/deviceProcessingRecords")
    public CommonResult<?> deviceProcessingRecords() {
        return null;
    }

    //质量统计
    @GetMapping("/qualityStatistics")
    public CommonResult<?> qualityStatistics() {
        return null;
    }
}
