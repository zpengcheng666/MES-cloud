package com.miyu.module.dc.controller.admin.offlinecollect;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.dc.controller.admin.offlinecollect.vo.OfflineCollectPageReqVO;
import com.miyu.module.dc.controller.admin.offlinecollect.vo.OfflineCollectResVO;
import com.miyu.module.dc.service.offlinecollect.OfflineCollectService;
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
@RequestMapping("/dc/offline-collect")
@Validated
public class OfflineCollectController {

    @Resource
    private OfflineCollectService offlineCollectService;

    @GetMapping("/page")
    @Operation(summary = "获得采集日志分页")
    @PreAuthorize("@ss.hasPermission('dc:device-offline:query')")
    public CommonResult<PageResult<OfflineCollectResVO>> getOfflineCollectPage(@Valid OfflineCollectPageReqVO reqVO) {
        List<OfflineCollectResVO> dataList = offlineCollectService.queryCollectList(reqVO);
        PageResult<OfflineCollectResVO> pageResult = new PageResult<>();
        pageResult.setTotal((long) dataList.size()).setList(dataList);
        return success(pageResult);
    }

    @GetMapping("/count")
    @Operation(summary = "获得采集日志总数")
    @PreAuthorize("@ss.hasPermission('dc:device-offline:query')")
    public CommonResult<Number> countOfflineCollect(@Valid OfflineCollectPageReqVO reqVO) {
        Number length = offlineCollectService.queryCollectCount(reqVO);
        return success(length);
    }


}
