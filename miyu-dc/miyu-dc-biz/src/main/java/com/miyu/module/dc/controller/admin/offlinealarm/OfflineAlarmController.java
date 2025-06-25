package com.miyu.module.dc.controller.admin.offlinealarm;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.dc.controller.admin.offlinealarm.vo.OfflineAlarmPageReqVO;
import com.miyu.module.dc.controller.admin.offlinealarm.vo.OfflineAlarmResVO;
import com.miyu.module.dc.controller.admin.offlinecollect.vo.OfflineCollectPageReqVO;
import com.miyu.module.dc.controller.admin.offlinecollect.vo.OfflineCollectResVO;
import com.miyu.module.dc.service.offlinealarm.OfflineAlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "采集日志")
@RestController
@RequestMapping("/dc/offline-alarm")
@Validated
public class OfflineAlarmController {

    @Resource
    OfflineAlarmService offlineAlarmService;

    @GetMapping("OlinePage")
    @Operation(summary = "查询在线异常日志")
    @PreAuthorize("@ss.hasPermission('dc:device-offline:query')")
    public CommonResult<PageResult<OfflineAlarmResVO>> queryOlineStatusDetailPage(@Valid OfflineAlarmPageReqVO reqVO) {
        List<OfflineAlarmResVO> dataList = offlineAlarmService.queryOlineStatusDetailPage(reqVO);
        PageResult<OfflineAlarmResVO> pageResult = new PageResult<>();
        if(dataList != null) {pageResult.setTotal((long) dataList.size()).setList(dataList);}
        return success(pageResult);
    }

    @GetMapping("OlineCount")
    @Operation(summary = "查询在线异常日志总数")
    @PreAuthorize("@ss.hasPermission('dc:device-offline:query')")
    public CommonResult<Number> queryOlineStatusDetailCount(@Valid OfflineAlarmPageReqVO reqVO) {
        Number length = offlineAlarmService.queryOlineStatusDetailCount(reqVO);
        return success(length);
    }

    @GetMapping("NormPage")
    @Operation(summary = "查询标准值异常日志")
    @PreAuthorize("@ss.hasPermission('dc:device-offline:query')")
    public CommonResult<PageResult<OfflineAlarmResVO>> queryNormStatusDetailPage(@Valid OfflineAlarmPageReqVO reqVO) {
        List<OfflineAlarmResVO> dataList = offlineAlarmService.queryNormStatusDetailPage(reqVO);
        PageResult<OfflineAlarmResVO> pageResult = new PageResult<>();
        if(dataList != null) {pageResult.setTotal((long) dataList.size()).setList(dataList);}
        return success(pageResult);
    }

    @GetMapping("NormCount")
    @Operation(summary = "查询标准值异常日志总数")
    @PreAuthorize("@ss.hasPermission('dc:device-offline:query')")
    public CommonResult<Number> queryNormStatusDetailCount(@Valid OfflineAlarmPageReqVO reqVO) {
        Number length = offlineAlarmService.queryNormStatusDetailCount(reqVO);
        return success(length);
    }

}
