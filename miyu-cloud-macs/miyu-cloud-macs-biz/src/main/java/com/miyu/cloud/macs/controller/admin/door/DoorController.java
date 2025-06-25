package com.miyu.cloud.macs.controller.admin.door;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
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
import java.net.URISyntaxException;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import com.miyu.cloud.macs.controller.admin.door.vo.*;
import com.miyu.cloud.macs.dal.dataobject.door.DoorDO;
import com.miyu.cloud.macs.service.door.DoorService;

@Tag(name = "管理后台 - 门")
@RestController
@RequestMapping("/macs/door")
@Validated
public class DoorController {

    @Resource
    private DoorService doorService;

    @PostMapping("/create")
    @Operation(summary = "创建门")
    @PreAuthorize("@ss.hasPermission('macs:door:create')")
    public CommonResult<String> createDoor(@Valid @RequestBody DoorSaveReqVO createReqVO) {
        return success(doorService.createDoor(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新门")
    @PreAuthorize("@ss.hasPermission('macs:door:update')")
    public CommonResult<Boolean> updateDoor(@Valid @RequestBody DoorSaveReqVO updateReqVO) {
        doorService.updateDoor(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除门")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('macs:door:delete')")
    public CommonResult<Boolean> deleteDoor(@RequestParam("id") String id) {
        doorService.deleteDoor(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得门")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('macs:door:query')")
    public CommonResult<DoorRespVO> getDoor(@RequestParam("id") String id) {
        DoorDO door = doorService.getDoor(id);
        return success(BeanUtils.toBean(door, DoorRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得门分页")
    @PreAuthorize("@ss.hasPermission('macs:door:query')")
    public CommonResult<PageResult<DoorRespVO>> getDoorPage(@Valid DoorPageReqVO pageReqVO) {
        PageResult<DoorDO> pageResult = doorService.getDoorPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DoorRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出门 Excel")
    @PreAuthorize("@ss.hasPermission('macs:door:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDoorExcel(@Valid DoorPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DoorDO> list = doorService.getDoorPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "门.xls", "数据", DoorRespVO.class,
                        BeanUtils.toBean(list, DoorRespVO.class));
    }

    @Operation(summary="macs_door-开门")
    @PostMapping(value = "/openDoor")
    public CommonResult<String> openDoor(@RequestParam(name="id",required=true) String id) throws URISyntaxException {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        doorService.openDoor(loginUser,id);
        return success("正在执行!");
    }

    @Operation(summary="macs_door-关门")
    @PostMapping(value = "/closeDoor")
    public CommonResult<String> closeDoor(@RequestParam(name="id",required=true) String id) throws URISyntaxException {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        doorService.closeDoor(loginUser,id);
        return success("正在执行!");
    }

    @GetMapping("/list")
    @Operation(summary = "获得设备列表")
    @PreAuthorize("@ss.hasPermission('macs:door:query')")
    public CommonResult<List<DoorRespVO>> getDoorList(@Valid DoorPageReqVO listReqVO) {
        List<DoorDO> list = doorService.getDoorList(listReqVO);
        return success(BeanUtils.toBean(list, DoorRespVO.class));
    }

}
