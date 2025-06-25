package cn.iocoder.yudao.module.pms.controller.admin.notifymessage;

import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessagePageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessageRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessageSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.notifymessage.NotifyMessageDO;
import cn.iocoder.yudao.module.pms.service.notifymessage.NotifyMessageService;
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
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

@Tag(name = "管理后台 - 简易版,项目计划提醒")
@RestController
@RequestMapping("/pms/notify-message")
@Validated
public class NotifyMessageController {

    @Resource
    private NotifyMessageService notifyMessageService;

    @PostMapping("/create")
    @Operation(summary = "创建简易版,项目计划提醒")
    @PreAuthorize("@ss.hasPermission('pms:notify-message:create')")
    public CommonResult<Long> createNotifyMessage(@Valid @RequestBody NotifyMessageSaveReqVO createReqVO) {
        return success(notifyMessageService.createNotifyMessage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新简易版,项目计划提醒")
    @PreAuthorize("@ss.hasPermission('pms:notify-message:update')")
    public CommonResult<Boolean> updateNotifyMessage(@Valid @RequestBody NotifyMessageSaveReqVO updateReqVO) {
        notifyMessageService.updateNotifyMessage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除简易版,项目计划提醒")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pms:notify-message:delete')")
    public CommonResult<Boolean> deleteNotifyMessage(@RequestParam("id") Long id) {
        notifyMessageService.deleteNotifyMessage(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得简易版,项目计划提醒")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pms:notify-message:query')")
    public CommonResult<NotifyMessageRespVO> getNotifyMessage(@RequestParam("id") Long id) {
        NotifyMessageDO notifyMessage = notifyMessageService.getNotifyMessage(id);
        return success(BeanUtils.toBean(notifyMessage, NotifyMessageRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得简易版,项目计划提醒分页")
    @PreAuthorize("@ss.hasPermission('pms:notify-message:query')")
    public CommonResult<PageResult<NotifyMessageRespVO>> getNotifyMessagePage(@Valid NotifyMessagePageReqVO pageReqVO) {
        PageResult<NotifyMessageDO> pageResult = notifyMessageService.getNotifyMessagePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, NotifyMessageRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出简易版,项目计划提醒 Excel")
    @PreAuthorize("@ss.hasPermission('pms:notify-message:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportNotifyMessageExcel(@Valid NotifyMessagePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<NotifyMessageDO> list = notifyMessageService.getNotifyMessagePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "简易版,项目计划提醒.xls", "数据", NotifyMessageRespVO.class,
                        BeanUtils.toBean(list, NotifyMessageRespVO.class));
    }

    /**
     * 条件查询
     * @param req
     * @return
     */
    @GetMapping("/selectListByEntity")
    @Operation(summary = "获得简易版,项目计划提醒")
    @PreAuthorize("@ss.hasPermission('pms:notify-message:query')")
    public CommonResult<List<NotifyMessageRespVO>> selectListByEntity(NotifyMessageReqVO req) {
        List<NotifyMessageDO> notifyMessageDOS = notifyMessageService.selectListByEntity(req);
        return success(BeanUtils.toBean(notifyMessageDOS, NotifyMessageRespVO.class));
    }

}
