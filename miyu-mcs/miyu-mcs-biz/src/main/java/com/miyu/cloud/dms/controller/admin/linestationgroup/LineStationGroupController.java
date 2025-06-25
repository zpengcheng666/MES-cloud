package com.miyu.cloud.dms.controller.admin.linestationgroup;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.dms.api.devicetype.dto.CommonDevice;
import com.miyu.cloud.dms.controller.admin.linestationgroup.vo.LineStationGroupPageReqVO;
import com.miyu.cloud.dms.controller.admin.linestationgroup.vo.LineStationGroupRespVO;
import com.miyu.cloud.dms.controller.admin.linestationgroup.vo.LineStationGroupSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.dms.service.linestationgroup.LineStationGroupService;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 产线/工位组")
@RestController
@RequestMapping("/dms/line-station-group")
@Validated
public class LineStationGroupController {

    @Resource
    private LineStationGroupService lineStationGroupService;
    @Resource
    private LedgerService ledgerService;

    @PostMapping("/create")
    @Operation(summary = "创建产线/工位组")
    @PreAuthorize("@ss.hasPermission('dms:line-station-group:create')")
    public CommonResult<String> createLineStationGroup(@Valid @RequestBody LineStationGroupSaveReqVO createReqVO) {
        return success(lineStationGroupService.createLineStationGroup(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产线/工位组")
    @LogRecord(type = "DMS", subType = "line-station-group", bizNo = "{{#updateReqVO.id}}", success = "产线/工位组{{#updateReqVO.id}}成功更改内容")
    @PreAuthorize("@ss.hasPermission('dms:line-station-group:update')")
    public CommonResult<Boolean> updateLineStationGroup(@Valid @RequestBody LineStationGroupSaveReqVO updateReqVO) {
        lineStationGroupService.updateLineStationGroup(updateReqVO);
        LogRecordContext.putVariable("newValue", updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产线/工位组")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:line-station-group:delete')")
    public CommonResult<Boolean> deleteLineStationGroup(@RequestParam("id") String id) {
        lineStationGroupService.deleteLineStationGroup(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产线/工位组")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:line-station-group:query')")
    public CommonResult<LineStationGroupRespVO> getLineStationGroup(@RequestParam("id") String id) {
        LineStationGroupDO lineStationGroup = lineStationGroupService.getLineStationGroup(id);
        return success(BeanUtils.toBean(lineStationGroup, LineStationGroupRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产线/工位组分页")
    @PreAuthorize("@ss.hasPermission('dms:line-station-group:query')")
    public CommonResult<PageResult<LineStationGroupRespVO>> getLineStationGroupPage(@Valid LineStationGroupPageReqVO pageReqVO) {
        PageResult<LineStationGroupDO> pageResult = lineStationGroupService.getLineStationGroupPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, LineStationGroupRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产线/工位组 Excel")
    @PreAuthorize("@ss.hasPermission('dms:line-station-group:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportLineStationGroupExcel(@Valid LineStationGroupPageReqVO pageReqVO,
                                            HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<LineStationGroupDO> list = lineStationGroupService.getLineStationGroupPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "产线/工位组.xls", "数据", LineStationGroupRespVO.class,
                BeanUtils.toBean(list, LineStationGroupRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得产线/工位组列表")
    @PreAuthorize("@ss.hasPermission('dms:line-station-group:query')")
    public CommonResult<List<LineStationGroupRespVO>> getLineStationGroupList() {
        List<LineStationGroupDO> list = lineStationGroupService.getLineStationGroupList();
        return success(BeanUtils.toBean(list, LineStationGroupRespVO.class));
    }

    @GetMapping("/getDeviceUnitList")
    @Operation(summary = "获取所有终端集合")
    public CommonResult<List<CommonDevice>> getDeviceUnitList() {
        List<LineStationGroupDO> lineStationGroupList = lineStationGroupService.getLineStationGroupList();
        List<LedgerDO> ledgerList = ledgerService.list(new QueryWrapper<LedgerDO>().isNull("lint_station_group"));
        List<CommonDevice> commonDevices = BeanUtils.toBean(lineStationGroupList, CommonDevice.class);
        commonDevices.addAll(BeanUtils.toBean(ledgerList, CommonDevice.class));
        return success(commonDevices);
    }

}
