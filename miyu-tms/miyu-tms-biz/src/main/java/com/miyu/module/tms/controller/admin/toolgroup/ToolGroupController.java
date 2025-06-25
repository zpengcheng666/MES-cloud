package com.miyu.module.tms.controller.admin.toolgroup;

import com.miyu.module.tms.controller.admin.toolgroupdetail.vo.ToolGroupDetailPageReqVO;
import com.miyu.module.tms.controller.admin.toolgroupdetail.vo.ToolGroupDetailRespVO;
import com.miyu.module.tms.dal.dataobject.toolgroupdetail.ToolGroupDetailDO;
import com.miyu.module.tms.service.toolgroupdetail.ToolGroupDetailService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.module.tms.controller.admin.toolgroup.vo.*;
import com.miyu.module.tms.dal.dataobject.toolgroup.ToolGroupDO;
import com.miyu.module.tms.service.toolgroup.ToolGroupService;

@Tag(name = "管理后台 - 刀具组装")
@RestController
@RequestMapping("/tms/tool-group")
@Validated
public class ToolGroupController {

    @Resource
    private ToolGroupService toolGroupService;

    @Resource
    private ToolGroupDetailService toolGroupDetailService;


    @PostMapping("/create")
    @Operation(summary = "创建刀具组装")
    @PreAuthorize("@ss.hasPermission('tms:group:create')")
    public CommonResult<String> createToolGroup(@Valid @RequestBody ToolGroupSaveReqVO createReqVO) {
        return success(toolGroupService.createToolGroup(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新刀具组装")
    @PreAuthorize("@ss.hasPermission('tms:group:update')")
    public CommonResult<Boolean> updateToolGroup(@Valid @RequestBody ToolGroupSaveReqVO updateReqVO) {
        toolGroupService.updateToolGroup(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除刀具组装")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tms:group:delete')")
    public CommonResult<Boolean> deleteToolGroup(@RequestParam("id") String id) {
        toolGroupService.deleteToolGroup(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得刀具组装")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:group:query')")
    public CommonResult<ToolGroupRespVO> getToolGroup(@RequestParam("id") String id) {
        ToolGroupDO toolGroup = toolGroupService.getToolGroupById(id);
        // 刀具组装详情
        List<ToolGroupDetailDO> detailList = toolGroupDetailService.getToolGroupDetailListByGroupId(id);
        // 根据类别分类
        // 刀柄
        List<ToolGroupDetailDO> dbList = detailList.stream().filter(item -> "DB".equals(item.getMaterialCode())).collect(Collectors.toList());
        // 刀具
        List<ToolGroupDetailDO> djList = detailList.stream().filter(item -> "DP".equals(item.getMaterialCode()) || "DT".equals(item.getMaterialCode())).collect(Collectors.toList());
        // 配件
        List<ToolGroupDetailDO> pjList = detailList.stream().filter(item -> "PJ".equals(item.getMaterialCode())).collect(Collectors.toList());
        return success(BeanUtils.toBean(toolGroup, ToolGroupRespVO.class, vo -> {
            vo.setHandle(dbList);
            vo.setTool(djList);
            vo.setAccessories(pjList);
        }));
    }

    @GetMapping("/page")
    @Operation(summary = "获得刀具组装分页")
    @PreAuthorize("@ss.hasPermission('tms:group:query')")
    public CommonResult<PageResult<ToolGroupRespVO>> getToolGroupPage(@Valid ToolGroupPageReqVO pageReqVO) {
        PageResult<ToolGroupDO> pageResult = toolGroupService.getToolGroupPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ToolGroupRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出刀具组装 Excel")
    @PreAuthorize("@ss.hasPermission('tms:group:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportToolGroupExcel(@Valid ToolGroupPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ToolGroupDO> list = toolGroupService.getToolGroupPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "刀具组装.xls", "数据", ToolGroupRespVO.class,
                        BeanUtils.toBean(list, ToolGroupRespVO.class));
    }

    @GetMapping("/detail/list")
    @Operation(summary = "获得刀具组装列表")
    @PreAuthorize("@ss.hasPermission('tms:group:query')")
    public CommonResult<List<ToolGroupDetailRespVO>> getGroupList(@Valid ToolGroupDetailPageReqVO pageReqVO) {
        List<ToolGroupDetailDO> pageResult = toolGroupService.getToolGroupDetailList(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ToolGroupDetailRespVO.class));
    }



    @GetMapping("/getGroupByMainConfigId")
    @Operation(summary = "成品类型ID获得刀具组装")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:group:query')")
    public CommonResult<List<ToolGroupRespVO>> getGroupByMainConfigId(@RequestParam("id") String mainConfigId) {
        List<ToolGroupDO> toolGroup = toolGroupService.getGroupByMainConfigId(mainConfigId);
        return success(BeanUtils.toBean(toolGroup, ToolGroupRespVO.class));
    }
}
