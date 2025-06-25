package cn.iocoder.yudao.module.bpm.controller.admin.oaclock;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.bpm.controller.admin.oaclock.vo.OaClockPageReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.oaclock.vo.OaClockRespVO;
import cn.iocoder.yudao.module.bpm.controller.admin.oaclock.vo.OaClockSaveReqVO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oaclock.OaClockDO;
import cn.iocoder.yudao.module.bpm.service.oaclock.OaClockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
@Tag(name = "管理后台 - OA 打卡")
@RestController
@RequestMapping("/bpm/oa-clock")
@Validated
public class OaClockController {

    @Resource
    private OaClockService oaClockService;

    @PostMapping("/create")
    @Operation(summary = "创建OA 打卡")
    @PreAuthorize("@ss.hasPermission('bpm:oa-clock:create')")
    public CommonResult<Long> createOaClock(@Valid @RequestBody OaClockSaveReqVO createReqVO) {
        return success(oaClockService.createOaClock(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新OA 打卡")
    @PreAuthorize("@ss.hasPermission('bpm:oa-clock:update')")
    public CommonResult<Boolean> updateOaClock(@Valid @RequestBody OaClockSaveReqVO updateReqVO) {
        oaClockService.updateOaClock(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除OA 打卡")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('bpm:oa-clock:delete')")
    public CommonResult<Boolean> deleteOaClock(@RequestParam("id") Long id) {
        oaClockService.deleteOaClock(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得OA 打卡")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('bpm:oa-clock:query')")
    public CommonResult<OaClockRespVO> getOaClock(@RequestParam("id") Long id) {
        OaClockDO oaClock = oaClockService.getOaClock(id);
        return success(BeanUtils.toBean(oaClock, OaClockRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得OA 打卡分页")
    @PreAuthorize("@ss.hasPermission('bpm:oa-clock:query')")
    public CommonResult<PageResult<OaClockRespVO>> getOaClockPage(@Valid OaClockPageReqVO pageReqVO) {
        PageResult<OaClockDO> pageResult = oaClockService.getOaClockPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OaClockRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出OA 打卡 Excel")
    @PreAuthorize("@ss.hasPermission('bpm:oa-clock:export')")
    public void exportOaClockExcel(@Valid OaClockPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OaClockDO> list = oaClockService.getOaClockPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "OA 打卡.xls", "数据", OaClockRespVO.class,
                        BeanUtils.toBean(list, OaClockRespVO.class));
    }

    @GetMapping("/test")
    @PermitAll
    public void testPre(){
        System.out.println("123456");
    }

}
