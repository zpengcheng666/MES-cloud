package com.miyu.module.pdm.controller.admin.structureDefinition;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.structureDefinition.vo.StructureListReqVO;
import com.miyu.module.pdm.controller.admin.structureDefinition.vo.StructureRespVO;
import com.miyu.module.pdm.controller.admin.structureDefinition.vo.StructureSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.structure.StructureDO;
import com.miyu.module.pdm.dal.dataobject.structure.StructureExcelDO;
import com.miyu.module.pdm.service.structure.StructureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

@Tag(name = "管理后台 - PDM 数据包结构定义")
@RestController
@RequestMapping("/pdm/structure-definition")
@Validated
public class StructureController {

    @Resource
    private StructureService structureService;

    @PostMapping("/create")
    @Operation(summary = "创建数据包结构")
    @PreAuthorize("@ss.hasPermission('pdm:structure-definition:create')")
    public CommonResult<Long> createStructure(@Valid @RequestBody StructureSaveReqVO createReqVO) {
        return success(structureService.createStructure(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新数据包结构")
    @PreAuthorize("@ss.hasPermission('pdm:structure-definition:update')")
    public CommonResult<Boolean> updateStructure(@Valid @RequestBody StructureSaveReqVO updateReqVO) {
        structureService.updateStructure(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除数据包结构")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pdm:structure-definition:delete')")
    public CommonResult<Boolean> deleteStructure(@RequestParam("id") Long id) {
        structureService.deleteStructure(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得数据包结构")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:structure-definition:query')")
    public CommonResult<StructureRespVO> getStructure(@RequestParam("id") Long id) {
        StructureDO category = structureService.getStructure(id);
        return success(BeanUtils.toBean(category, StructureRespVO.class));
    }

    @GetMapping("list")
    @Operation(summary = "获取数据包结构列表")
    @PreAuthorize("@ss.hasPermission('pdm:structure-definition:query')")
    public CommonResult<List<StructureRespVO>> getStructureList(StructureListReqVO reqVO) {
        List<StructureDO> list = structureService.getStructureList(reqVO);
        return success(BeanUtils.toBean(list, StructureRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得数据包结构精简列表", description = "只包含被开启的分类，主要用于前端的下拉选项")
    public CommonResult<List<StructureRespVO>> getStructureSimpleList() {
        List<StructureDO> list = structureService.getStructureList(
                new StructureListReqVO().setStatus(CommonStatusEnum.ENABLE.getStatus()).setName(""));
        return success(convertList(list, category -> new StructureRespVO()
                .setId(category.getId()).setName(category.getName()).setParentId(category.getParentId())));
    }

    @GetMapping("/structure-excel/list-by-structure-id")
    @Operation(summary = "获得excel规则详情列表")
    @Parameter(name = "structureId", description = "结构id")
    @PreAuthorize("@ss.hasPermission('pdm:structure-definition:query')")
    public CommonResult<List<StructureExcelDO>> getStructureExcelListByStructureId(@RequestParam("structureId") Long structureId) {
        return success(structureService.getStructureExcelListByStructureId(structureId));
    }

    @GetMapping("/childList")
    @Operation(summary = "获取数据包结构子级列表-递归遍历")
    @PreAuthorize("@ss.hasPermission('pdm:structure-definition:query')")
    public CommonResult<List<StructureRespVO>> getStructureChildList(StructureListReqVO reqVO) {
        List<StructureDO> list = structureService.getStructureChildList(reqVO);
        return success(BeanUtils.toBean(list, StructureRespVO.class));
    }
}
