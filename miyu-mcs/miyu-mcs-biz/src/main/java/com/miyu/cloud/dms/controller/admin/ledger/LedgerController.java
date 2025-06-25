package com.miyu.cloud.dms.controller.admin.ledger;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import cn.iocoder.yudao.module.system.api.permission.PermissionApi;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerPageReqVO;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerRespListVO;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerRespVO;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerToLocationDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerToUserDO;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerToLocationMapper;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerToUserMapper;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 设备台账")
@RestController
@RequestMapping("/dms/ledger")
@Validated
public class LedgerController {

    @Resource
    private LedgerService ledgerService;

    @PostMapping("/create")
    @Operation(summary = "创建设备台账")
    @PreAuthorize("@ss.hasPermission('dms:ledger:create')")
    public CommonResult<String> createLedger(@Valid @RequestBody LedgerSaveReqVO createReqVO) {
        return success(ledgerService.createLedger(createReqVO));
    }

    @Autowired
    private FileApi fileApi;

    @PutMapping("/update")
    @Operation(summary = "更新设备台账")
    @LogRecord(type = "DMS", subType = "ledger", bizNo = "{{#updateReqVO.id}}", success = "设备台账{{#updateReqVO.id}}成功更改内容")
    @PreAuthorize("@ss.hasPermission('dms:ledger:update')")
    public CommonResult<Boolean> updateLedger(@Valid @RequestBody LedgerSaveReqVO updateReqVO) {
        ledgerService.updateLedger(updateReqVO);
        LogRecordContext.putVariable("newValue", updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备台账")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:ledger:delete')")
    public CommonResult<Boolean> deleteLedger(@RequestParam("id") String id) {
        deleteLedgerToUser(new LedgerToUserDO().setLedger(id));
        ledgerService.deleteLedger(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备台账")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:ledger:query')")
    public CommonResult<LedgerRespVO> getLedger(@RequestParam("id") String id) {
        LedgerDO ledger = ledgerService.getLedger(id);
        return success(BeanUtils.toBean(ledger, LedgerRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备台账分页")
    @PreAuthorize("@ss.hasPermission('dms:ledger:query')")
    public CommonResult<PageResult<LedgerRespVO>> getLedgerPage(@Valid LedgerPageReqVO pageReqVO) {
        PageResult<LedgerDO> pageResult = ledgerService.getLedgerPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, LedgerRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备台账 Excel")
    @PreAuthorize("@ss.hasPermission('dms:ledger:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportLedgerExcel(@Valid LedgerPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<LedgerDO> list = ledgerService.getLedgerPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "设备台账.xls", "数据", LedgerRespVO.class, BeanUtils.toBean(list, LedgerRespVO.class));
    }

    /************************************其他***************************/

    @GetMapping("/getList")
    @Operation(summary = "获得设备台账列表")
    @PreAuthorize("@ss.hasPermission('dms:ledger:query')")
    public CommonResult<List<LedgerRespListVO>> getLedgerList() {
        List<LedgerDO> ledgerList = ledgerService.getLedgerList();
        return success(BeanUtils.toBean(ledgerList, LedgerRespListVO.class));
    }

    @GetMapping("/getByIp")
    @Operation(summary = "通过ip获得设备台账")
    @PreAuthorize("@ss.hasPermission('dms:ledger:query')")
    public CommonResult<LedgerRespListVO> getLedgerByIp(@RequestHeader("x-forwarded-for") String forwardedIp, @RequestParam(value = "ip", required = false) String ip) {
        LedgerDO ledger = ledgerService.getLedgerByIp(ip == null ? forwardedIp : ip);
        return success(BeanUtils.toBean(ledger, LedgerRespListVO.class));
    }

    @Resource
    private PermissionApi permissionApi;
    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/ledgertouser/getUsers")
    @Operation(summary = "获得拥有指定角色的用户")
    public CommonResult<List<AdminUserRespDTO>> getUsersByRole(@RequestBody Long role) {
        List<Long> roles = new ArrayList<>();
        roles.add(role);
        CommonResult<Set<Long>> ids = permissionApi.getUserRoleIdListByRoleIds(roles);
        CommonResult<List<AdminUserRespDTO>> list = adminUserApi.getUserList(ids.getData());
        return success(list.getData());
    }

    /**************************LedgerToUser************************/

    @Resource
    private LedgerToUserMapper ledgerToUserMapper;

    @GetMapping("/ledgertouser/list")
    @Operation(summary = "获得台账-用户对应表")
    public CommonResult<List<LedgerToUserDO>> getLedgerToUser(@RequestParam(value = "ledger", required = false) String ledger, @RequestParam(value = "user", required = false) String user) {
        List<LedgerToUserDO> list = ledgerToUserMapper.selectList(new LambdaQueryWrapperX<LedgerToUserDO>().eqIfPresent(LedgerToUserDO::getLedger, ledger).eqIfPresent(LedgerToUserDO::getUser, user));
        return success(list);
    }

    @PostMapping("/ledgertouser/create")
    @Operation(summary = "创建台账-用户对应")
    @LogRecord(type = "DMS", subType = "ledgertouser", bizNo = "ledgertouser", success = "设备台账对应用户修改")
    public CommonResult<Boolean> createLedgerToUser(@Valid @RequestBody List<LedgerToUserDO> ledgerToUserDOS) {
        LogRecordContext.putVariable("newValue", ledgerToUserDOS);
        return success(ledgerToUserMapper.insertBatch(ledgerToUserDOS));
    }

    @DeleteMapping("/ledgertouser/delete")
    @Operation(summary = "删除台账-用户对应")
    public CommonResult<Boolean> deleteLedgerToUser(@RequestBody LedgerToUserDO ledgerToUserDO) {
        ledgerToUserMapper.delete(new LambdaQueryWrapperX<LedgerToUserDO>().eqIfPresent(LedgerToUserDO::getId, ledgerToUserDO.getId()).eqIfPresent(LedgerToUserDO::getLedger, ledgerToUserDO.getLedger()).eqIfPresent(LedgerToUserDO::getUser, ledgerToUserDO.getUser()));
        return success(true);
    }

    /****************************LedgerToLocation***********************/

    @Resource
    private LedgerToLocationMapper ledgerToLocationMapper;

    @GetMapping("/ledgertolocation/list")
    @Operation(summary = "获得指定台账的位置数据")
    public CommonResult<List<LedgerToLocationDO>> getLedgerToLocation(@RequestParam(value = "ledger") String ledger) {
        List<LedgerToLocationDO> list = ledgerToLocationMapper.selectList(new LambdaQueryWrapperX<LedgerToLocationDO>().eqIfPresent(LedgerToLocationDO::getLedger, ledger));
        return success(list);
    }

    @PostMapping("/ledgertolocation/create")
    @Operation(summary = "创建台账-位置数据")
    @LogRecord(type = "DMS", subType = "ledgertolocation", bizNo = "ledgertolocation", success = "设备台账对应库位修改")
    public CommonResult<Boolean> createLedgerToLocation(@Valid @RequestBody List<LedgerToLocationDO> ledgerToLocationDOS) {
        LogRecordContext.putVariable("newValue", ledgerToLocationDOS);
        return success(ledgerToLocationMapper.insertBatch(ledgerToLocationDOS));
    }

    @DeleteMapping("/ledgertolocation/delete")
    @Operation(summary = "删除台账-位置对应")
    public CommonResult<Boolean> deleteLedgerToLocation(@RequestBody LedgerToUserDO ledgerToUserDO) {
        ledgerToLocationMapper.delete(new LambdaQueryWrapperX<LedgerToLocationDO>().eqIfPresent(LedgerToLocationDO::getId, ledgerToUserDO.getId()));
        return success(true);
    }

}
