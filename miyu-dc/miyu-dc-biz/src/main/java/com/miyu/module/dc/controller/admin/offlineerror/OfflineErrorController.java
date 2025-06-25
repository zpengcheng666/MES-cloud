package com.miyu.module.dc.controller.admin.offlineerror;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.dc.controller.admin.offlinecollect.vo.OfflineCollectPageReqVO;
import com.miyu.module.dc.controller.admin.offlinecollect.vo.OfflineCollectResVO;
import com.miyu.module.dc.controller.admin.offlineerror.vo.OfflineErrorPageReqVO;
import com.miyu.module.dc.controller.admin.offlineerror.vo.OfflineErrorResVO;
import com.miyu.module.dc.service.offlineerror.OfflineErrorService;
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

@Tag(name = "错误日志")
@RestController
@RequestMapping("/dc/offline-error")
@Validated
public class OfflineErrorController {

    @Resource
    private OfflineErrorService offlineErrorService;

    @GetMapping("page")
    @Operation(summary = "获得错误日志分页")
    @PreAuthorize("@ss.hasPermission('dc:device-offline:query')")
    public CommonResult<PageResult<OfflineErrorResVO>> getOfflineErrorPage(@Validated OfflineErrorPageReqVO reqVO) {
        List<OfflineErrorResVO> dataList = offlineErrorService.queryOfflineError(reqVO);
        PageResult<OfflineErrorResVO> pageResult = new PageResult<>();
        pageResult.setTotal((long) dataList.size()).setList(dataList);
        return success(pageResult);
    }

    @GetMapping("/count")
    @Operation(summary = "获得错误日志总数")
    @PreAuthorize("@ss.hasPermission('dc:device-offline:query')")
    public CommonResult<Number> countOfflineError(@Valid OfflineErrorPageReqVO reqVO) {
        Number length = offlineErrorService.queryOfflineErrorCount(reqVO);
        return success(length);
    }

}
