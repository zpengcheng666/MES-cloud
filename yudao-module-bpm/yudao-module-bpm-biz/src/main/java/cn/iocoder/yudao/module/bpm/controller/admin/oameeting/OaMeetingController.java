package cn.iocoder.yudao.module.bpm.controller.admin.oameeting;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.bpm.controller.admin.oameeting.vo.OaMeetingPageReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.oameeting.vo.OaMeetingRespVO;
import cn.iocoder.yudao.module.bpm.controller.admin.oameeting.vo.OaMeetingSaveReqVO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oameeting.OaMeetingDO;
import cn.iocoder.yudao.module.bpm.service.oameeting.OaMeetingService;
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

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - OA 会议申请")
@RestController
@RequestMapping("/bpm/oa-meeting")
@Validated
public class OaMeetingController {

    @Resource
    private OaMeetingService oaMeetingService;

    @PostMapping("/create")
    @Operation(summary = "创建OA 会议申请")
    @PreAuthorize("@ss.hasPermission('bpm:oa-meeting:create')")
    public CommonResult<Long> createOaMeeting(@Valid @RequestBody OaMeetingSaveReqVO createReqVO) {
        return success(oaMeetingService.createOaMeeting(getLoginUserId(),createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新OA 会议申请")
    @PreAuthorize("@ss.hasPermission('bpm:oa-meeting:update')")
    public CommonResult<Boolean> updateOaMeeting(@Valid @RequestBody OaMeetingSaveReqVO updateReqVO) {
        oaMeetingService.updateOaMeeting(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除OA 会议申请")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('bpm:oa-meeting:delete')")
    public CommonResult<Boolean> deleteOaMeeting(@RequestParam("id") Long id) {
        oaMeetingService.deleteOaMeeting(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得OA 会议申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('bpm:oa-meeting:query')")
    public CommonResult<OaMeetingRespVO> getOaMeeting(@RequestParam("id") Long id) {
        OaMeetingDO oaMeeting = oaMeetingService.getOaMeeting(id);
        return success(BeanUtils.toBean(oaMeeting, OaMeetingRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得OA 会议申请分页")
    @PreAuthorize("@ss.hasPermission('bpm:oa-meeting:query')")
    public CommonResult<PageResult<OaMeetingRespVO>> getOaMeetingPage(@Valid OaMeetingPageReqVO pageReqVO) {
        PageResult<OaMeetingDO> pageResult = oaMeetingService.getOaMeetingPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OaMeetingRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出OA 会议申请 Excel")
    @PreAuthorize("@ss.hasPermission('bpm:oa-meeting:export')")
    public void exportOaMeetingExcel(@Valid OaMeetingPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OaMeetingDO> list = oaMeetingService.getOaMeetingPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "OA 会议申请.xls", "数据", OaMeetingRespVO.class,
                        BeanUtils.toBean(list, OaMeetingRespVO.class));
    }

}
