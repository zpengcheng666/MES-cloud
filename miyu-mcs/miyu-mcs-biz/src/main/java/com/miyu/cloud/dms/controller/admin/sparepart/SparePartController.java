package com.miyu.cloud.dms.controller.admin.sparepart;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.cloud.dms.controller.admin.sparepart.vo.SparePartPageReqVO;
import com.miyu.cloud.dms.controller.admin.sparepart.vo.SparePartRespVO;
import com.miyu.cloud.dms.controller.admin.sparepart.vo.SparePartSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.sparepart.SparePartDO;
import com.miyu.cloud.dms.service.sparepart.SparePartService;
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

@Tag(name = "管理后台 - 备品/备件")
@RestController
@RequestMapping("/dms/spare-part")
@Validated
public class SparePartController {

    @Resource
    private SparePartService sparePartService;

    @PostMapping("/create")
    @Operation(summary = "创建备品/备件")
    @PreAuthorize("@ss.hasPermission('dms:spare-part:create')")
    public CommonResult<String> createSparePart(@Valid @RequestBody SparePartSaveReqVO createReqVO) {
        return success(sparePartService.createSparePart(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新备品/备件")
    @LogRecord(type = "DMS", subType = "spare-part", bizNo = "{{#updateReqVO.id}}", success = "备品/备件{{#updateReqVO.id}}成功更改内容")
    @PreAuthorize("@ss.hasPermission('dms:spare-part:update')")
    public CommonResult<Boolean> updateSparePart(@Valid @RequestBody SparePartSaveReqVO updateReqVO) {
        sparePartService.updateSparePart(updateReqVO);
        LogRecordContext.putVariable("newValue", updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除备品/备件")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:spare-part:delete')")
    public CommonResult<Boolean> deleteSparePart(@RequestParam("id") String id) {
        sparePartService.deleteSparePart(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得备品/备件")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:spare-part:query')")
    public CommonResult<SparePartRespVO> getSparePart(@RequestParam("id") String id) {
        SparePartDO sparePart = sparePartService.getSparePart(id);
        return success(BeanUtils.toBean(sparePart, SparePartRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得备品/备件分页")
    @PreAuthorize("@ss.hasPermission('dms:spare-part:query')")
    public CommonResult<PageResult<SparePartRespVO>> getSparePartPage(@Valid SparePartPageReqVO pageReqVO) {
        PageResult<SparePartDO> pageResult = sparePartService.getSparePartPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SparePartRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出备品/备件 Excel")
    @PreAuthorize("@ss.hasPermission('dms:spare-part:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSparePartExcel(@Valid SparePartPageReqVO pageReqVO,
                                     HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SparePartDO> list = sparePartService.getSparePartPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "备品/备件.xls", "数据", SparePartRespVO.class,
                BeanUtils.toBean(list, SparePartRespVO.class));
    }

    @GetMapping("/getList")
    @Operation(summary = "获得备品/备件列表")
    @PreAuthorize("@ss.hasPermission('dms:spare-part:query')")
    public CommonResult<List<SparePartRespVO>> getSparePartList() {
        List<SparePartDO> sparePartList = sparePartService.getSparePartList();
        return success(BeanUtils.toBean(sparePartList, SparePartRespVO.class));
    }

}
