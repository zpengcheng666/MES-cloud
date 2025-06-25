package cn.iocoder.yudao.module.bpm.controller.admin.oasupply;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.bpm.controller.admin.oasupply.vo.OaSupplyPageReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.oasupply.vo.OaSupplyRespVO;
import cn.iocoder.yudao.module.bpm.controller.admin.oasupply.vo.OaSupplySaveReqVO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oasupply.OaSupplyDO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oasupply.OaSupplyListDO;
import cn.iocoder.yudao.module.bpm.service.oasupply.OaSupplyService;
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

@Tag(name = "管理后台 - OA 物品领用")
@RestController
@RequestMapping("/bpm/oa-supply")
@Validated
public class OaSupplyController {

    @Resource
    private OaSupplyService oaSupplyService;

    @PostMapping("/create")
    @Operation(summary = "创建OA 物品领用")
    @PreAuthorize("@ss.hasPermission('bpm:oa-supply:create')")
    public CommonResult<Long> createOaSupply(@Valid @RequestBody OaSupplySaveReqVO createReqVO) {
        return success(oaSupplyService.createOaSupply(getLoginUserId(),createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新OA 物品领用")
    @PreAuthorize("@ss.hasPermission('bpm:oa-supply:update')")
    public CommonResult<Boolean> updateOaSupply(@Valid @RequestBody OaSupplySaveReqVO updateReqVO) {
        oaSupplyService.updateOaSupply(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除OA 物品领用")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('bpm:oa-supply:delete')")
    public CommonResult<Boolean> deleteOaSupply(@RequestParam("id") Long id) {
        oaSupplyService.deleteOaSupply(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得OA 物品领用")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('bpm:oa-supply:query')")
    public CommonResult<OaSupplyRespVO> getOaSupply(@RequestParam("id") Long id) {
        OaSupplyDO oaSupply = oaSupplyService.getOaSupply(id);
        return success(BeanUtils.toBean(oaSupply, OaSupplyRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得OA 物品领用分页")
    @PreAuthorize("@ss.hasPermission('bpm:oa-supply:query')")
    public CommonResult<PageResult<OaSupplyRespVO>> getOaSupplyPage(@Valid OaSupplyPageReqVO pageReqVO) {
        PageResult<OaSupplyDO> pageResult = oaSupplyService.getOaSupplyPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OaSupplyRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出OA 物品领用 Excel")
    @PreAuthorize("@ss.hasPermission('bpm:oa-supply:export')")
    public void exportOaSupplyExcel(@Valid OaSupplyPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OaSupplyDO> list = oaSupplyService.getOaSupplyPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "OA 物品领用.xls", "数据", OaSupplyRespVO.class,
                        BeanUtils.toBean(list, OaSupplyRespVO.class));
    }

    // ==================== 子表（OA 物品领用表-物品清单） ====================

    @GetMapping("/oa-supply-list/list-by-supply-id")
    @Operation(summary = "获得OA 物品领用表-物品清单列表")
    @Parameter(name = "supplyId", description = "采购父表id")
    @PreAuthorize("@ss.hasPermission('bpm:oa-supply:query')")
    public CommonResult<List<OaSupplyListDO>> getOaSupplyListListBySupplyId(@RequestParam("supplyId") Long supplyId) {
        return success(oaSupplyService.getOaSupplyListListBySupplyId(supplyId));
    }

}
