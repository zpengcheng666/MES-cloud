package com.miyu.module.es.controller.admin.brakeSync;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.es.controller.admin.brake.vo.BrakeSaveReqVO;
import com.miyu.module.es.controller.admin.brakeSync.vo.BrakeSyncRespVO;
import com.miyu.module.es.controller.admin.brakeSync.vo.BrakeSyncSaveReqVO;
import com.miyu.module.es.service.brakeSync.BrakeSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 配置管理")
@RestController
@RequestMapping("/es/brakeSync")
@Validated
public class BrakeSyncController {

    @Resource
    private BrakeSyncService brakeSyncService;

    @PutMapping("/update")
    @Operation(summary = "更新配置管理")
    @PreAuthorize("@ss.hasPermission('es:brakeSync:update')")
    public CommonResult<Boolean> updateBrakeSync(@Valid @RequestBody BrakeSyncSaveReqVO updateReqVO) {
        brakeSyncService.updateBrakeSync(updateReqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得访客记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('es:brakeSync:get')")
    public CommonResult<BrakeSyncRespVO> getBrakeSync(@RequestParam("id") String id) {
        BrakeSyncRespVO brakeSyncRespVO = brakeSyncService.getBrakeSync(id);
        return success(brakeSyncRespVO);
    }



}
