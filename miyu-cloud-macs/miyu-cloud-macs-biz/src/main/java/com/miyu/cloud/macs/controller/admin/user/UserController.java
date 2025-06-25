package com.miyu.cloud.macs.controller.admin.user;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
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

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import com.miyu.cloud.macs.controller.admin.user.vo.*;
import com.miyu.cloud.macs.dal.dataobject.user.UserDO;
import com.miyu.cloud.macs.service.user.UserService;

@Tag(name = "管理后台 - 门禁用户")
@RestController
@RequestMapping("/macs/user")
@Validated
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/create")
    @Operation(summary = "创建门禁用户")
    @PreAuthorize("@ss.hasPermission('macs:user:create')")
    public CommonResult<String> createUser(@Valid @RequestBody UserSaveReqVO createReqVO) {
        return success(userService.createUser(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新门禁用户")
    @PreAuthorize("@ss.hasPermission('macs:user:update')")
    public CommonResult<Boolean> updateUser(@Valid @RequestBody UserSaveReqVO updateReqVO) {
        UserDO byUserId = userService.getByUserId(updateReqVO.getUserId());
        if (byUserId != null) {
            updateReqVO.setId(byUserId.getId());
            userService.updateUser(updateReqVO);
        } else {
            userService.createUser(updateReqVO);
        }
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除门禁用户")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('macs:user:delete')")
    public CommonResult<Boolean> deleteUser(@RequestParam("id") String id) {
        userService.deleteUser(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得门禁用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('macs:user:query')")
    public CommonResult<UserRespVO> getUser(@RequestParam("id") String id) {
        UserDO user = userService.getUser(id);
        return success(BeanUtils.toBean(user, UserRespVO.class));
    }

    @GetMapping("/getByUserId")
    @Operation(summary = "获得门禁用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('macs:user:query')")
    public CommonResult<UserRespVO> getByUserId(@RequestParam("id") String userId) {
        UserDO user = userService.getByUserId(userId);
        if (user == null) return success(new UserRespVO().setUserId(userId));
        return success(BeanUtils.toBean(user, UserRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得门禁用户分页")
    @PreAuthorize("@ss.hasPermission('macs:user:query')")
    public CommonResult<PageResult<UserRespVO>> getUserPage(@Valid UserPageReqVO pageReqVO) {
        PageResult<UserDO> pageResult = userService.getUserPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UserRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得门禁用户集合")
    @PreAuthorize("@ss.hasPermission('macs:user:query')")
    public CommonResult<List<UserRespVO>> getUserPage() {
        return success(BeanUtils.toBean(userService.list(), UserRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出门禁用户 Excel")
    @PreAuthorize("@ss.hasPermission('macs:user:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportUserExcel(@Valid UserPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<UserDO> list = userService.getUserPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "门禁用户.xls", "数据", UserRespVO.class,
                        BeanUtils.toBean(list, UserRespVO.class));
    }

}
