package cn.iocoder.yudao.module.bpm.controller.admin.oapurchase;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.bpm.controller.admin.oapurchase.vo.OaPurchasePageReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.oapurchase.vo.OaPurchaseRespVO;
import cn.iocoder.yudao.module.bpm.controller.admin.oapurchase.vo.OaPurchaseSaveReqVO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oapurchase.OaPurchaseDO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oapurchase.OaPurchaseListDO;
import cn.iocoder.yudao.module.bpm.service.oapurchase.OaPurchaseService;
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

@Tag(name = "管理后台 - OA 采购申请")
@RestController
@RequestMapping("/bpm/oa-purchase")
@Validated
public class OaPurchaseController {

    @Resource
    private OaPurchaseService oaPurchaseService;

    @PostMapping("/create")
    @Operation(summary = "创建OA 采购申请")
    @PreAuthorize("@ss.hasPermission('bpm:oa-purchase:create')")
    public CommonResult<Long> createOaPurchase(@Valid @RequestBody OaPurchaseSaveReqVO createReqVO) {
        System.out.println("123");
        return success(oaPurchaseService.createOaPurchase(getLoginUserId(),createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新OA 采购申请")
    @PreAuthorize("@ss.hasPermission('bpm:oa-purchase:update')")
    public CommonResult<Boolean> updateOaPurchase(@Valid @RequestBody OaPurchaseSaveReqVO updateReqVO) {
        oaPurchaseService.updateOaPurchase(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除OA 采购申请")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('bpm:oa-purchase:delete')")
    public CommonResult<Boolean> deleteOaPurchase(@RequestParam("id") Long id) {
        oaPurchaseService.deleteOaPurchase(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得OA 采购申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('bpm:oa-purchase:query')")
    public CommonResult<OaPurchaseRespVO> getOaPurchase(@RequestParam("id") Long id) {
        OaPurchaseDO oaPurchase = oaPurchaseService.getOaPurchase(id);
        return success(BeanUtils.toBean(oaPurchase, OaPurchaseRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得OA 采购申请分页")
    @PreAuthorize("@ss.hasPermission('bpm:oa-purchase:query')")
    public CommonResult<PageResult<OaPurchaseRespVO>> getOaPurchasePage(@Valid OaPurchasePageReqVO pageReqVO) {
        PageResult<OaPurchaseDO> pageResult = oaPurchaseService.getOaPurchasePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OaPurchaseRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出OA 采购申请 Excel")
    @PreAuthorize("@ss.hasPermission('bpm:oa-purchase:export')")
    public void exportOaPurchaseExcel(@Valid OaPurchasePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OaPurchaseDO> list = oaPurchaseService.getOaPurchasePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "OA 采购申请.xls", "数据", OaPurchaseRespVO.class,
                        BeanUtils.toBean(list, OaPurchaseRespVO.class));
    }

    // ==================== 子表（OA 采购申请） ====================

    @GetMapping("/oa-purchase-list/list-by-purchase-id")
    @Operation(summary = "获得OA 采购申请列表")
    @Parameter(name = "purchaseId", description = "采购父表id")
    @PreAuthorize("@ss.hasPermission('bpm:oa-purchase:query')")
    public CommonResult<List<OaPurchaseListDO>> getOaPurchaseListListByPurchaseId(@RequestParam("purchaseId") Long purchaseId) {
        return success(oaPurchaseService.getOaPurchaseListListByPurchaseId(purchaseId));
    }

}
