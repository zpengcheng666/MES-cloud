package com.miyu.module.pdm.controller.admin.part;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.BomReqVO;
import com.miyu.module.pdm.controller.admin.part.vo.*;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.NcReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessVersionNcReqVO;
import com.miyu.module.pdm.dal.dataobject.attachment.PartAttachmentDO;
import com.miyu.module.pdm.dal.dataobject.document.PartDocumentVersionDO;
import com.miyu.module.pdm.dal.dataobject.part.PartInstanceDO;
import com.miyu.module.pdm.dal.dataobject.version.PartVersionDO;
import com.miyu.module.pdm.service.part.PartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 产品数据对象")
@RestController
@RequestMapping("/pdm/part")
@Validated
public class PartController {
    @Resource
    private PartService partService;

    @GetMapping("/list")
    @Operation(summary = "获得pdm_std_part_instance列表")
    @PreAuthorize("@ss.hasPermission('pdm:std-part-instance:query')")
    public CommonResult<List<PartInstanceRespVO>> getPartInstanceList(@Valid PartInstanceListReqVO listReqVO) {
        List<PartInstanceDO> list = partService.getPartInstanceList(listReqVO);
        return success(BeanUtils.toBean(list, PartInstanceRespVO.class));
    }
    @GetMapping("/getNewFile")
    @Operation(summary = "获得文件")
    @Parameter(name = "id", description = "数据包编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:part:query')")
    public CommonResult<PartAttachmentRespVO> getNewFile(@RequestParam("id") String id) {
        PartAttachmentDO part = partService.findAttachmentsByDatapackageBomId(id);
        return success(BeanUtils.toBean(part, PartAttachmentRespVO.class));

    }

    @PostMapping("saveNewFile")
    @Operation(summary = "保存文件")
    public CommonResult<String> saveNewFile(@RequestBody @Valid PartAttachmentRespVO saveReqVO) {
        partService.saveNewFile(saveReqVO);
        return success("保存成功");
    }

    @DeleteMapping("/deleteNewFile")
    @Operation(summary = "删除文件")
    public CommonResult<Boolean> deleteNc(@Valid PartAttachmentRespVO saveReqVO) {
        partService.deleteNewFile(saveReqVO);
        return success(true);
    }

    @GetMapping("/getall")
    @Operation(summary = "获得所有")
    @Parameter(name = "partVersionId", description = "零件编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:part:query')")
    public CommonResult<PartVersionRespVO> getall(@RequestParam("partVersionId") String partVersionId) {
        PartVersionDO part = partService.selectPartInfoById(partVersionId);
        return success(BeanUtils.toBean(part, PartVersionRespVO.class));
    }
    @GetMapping("/getdetails")
    @Operation(summary = "获得零件目录")
    @Parameter(name = "partVersionId", description = "零件编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:part:query')")
    public CommonResult<List<Map<String, Object>>> getDetails(@RequestParam("partVersionId") String partVersionId) {
        List<Map<String, Object>> list = partService.selectDetailsByPartVersionId(partVersionId);
        return success(list);
    }

    @GetMapping("/getdatapackageBomId")
    @Operation(summary = "获得文件")
    @Parameter(name = "partVersionId", description = "零件编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:part:query')")
    public CommonResult<List<Map<String, Object>>> getDataPackageBomId(@RequestParam("partVersionId") String partVersionId) {
        List<Map<String, Object>> list = partService.getCombinedDataByPartVersionId(partVersionId);
        return success(list);
    }

    @GetMapping("/getdocument")
    @Operation(summary = "文件版本")
    @Parameter(name = "partVersionId", description = "零件编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:part:query')")
    public CommonResult<PartDocumentVersionRespVO> getDocument(@RequestParam("partVersionId") String partVersionId) {
        PartDocumentVersionDO part = partService.getDocumentInfoByPartVersionId(partVersionId);
        return success(BeanUtils.toBean(part, PartDocumentVersionRespVO.class));
    }

    @GetMapping("/getPartAttrs")
    @Operation(summary = "获取零件设计属性")
    public CommonResult<List<Map<String, Object>>> getPartAttrs(@Valid PartAttrReqVO reqVO) {
        List<Map<String, Object>> list = partService.getPartAttrs(reqVO);
        return success(list);
    }

    @GetMapping("/getModelUrl")
    @Operation(summary = "获取轻量化模型地址")
    @Parameter(name = "fileName", description = "数模文件名称", required = true, example = "1024")
    public CommonResult<String> getModelUrl(@RequestParam("fileName") String fileName, @RequestParam("fileType") String fileType) {
        String modelUrl = partService.getModelUrl(fileName, fileType);
        return success(modelUrl);
    }
    @PostMapping("/addPart")
    @Operation(summary = "新增零件")
    public CommonResult<String> addPart(@RequestBody @Valid AddPartReqVO reqVO) {
        partService.addPart(reqVO);
        return success("新增成功");
    }
    @DeleteMapping("/deletePart")
    @Operation(summary = "删除零件")
    @Parameter(name = "id", description = "PVID", required = true)
    public CommonResult<Boolean> deletePart(@RequestBody @Valid AddPartReqVO reqVO) {
        partService.deletePart(reqVO);
        return success(true);
    }

}
