package com.miyu.module.pdm.controller.admin.dataImport;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.controller.admin.dataImport.vo.DataImportReqVO;
import com.miyu.module.pdm.service.dataImport.DataImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - PDM 数据包导入")
@RestController
@RequestMapping("/pdm/data-import")
@Validated
public class DataImportController {

    @Resource
    private DataImportService dataImportService;

//    @PostMapping("/import")
//    @Operation(summary = "数据包导入")
//    @Parameters({
//            @Parameter(name = "file", description = "zip 文件", required = true),
//            @Parameter(name = "rootProductId", description = "产品id", example = "1"),
//            @Parameter(name = "projectCode", description = "项目编号", example = "1"),
//            @Parameter(name = "structureId", description = "结构编号", example = "1")
//    })
//    public CommonResult<Boolean> dataImport(@RequestParam("file") MultipartFile file,
//                                             @RequestParam("rootProductId") String rootProductId,
//                                             @RequestParam("projectCode") String projectCode,
//                                             @RequestParam("structureId") Long structureId) throws Exception {
//        DataImportReqVO dataImportReqVO = new DataImportReqVO();
//        dataImportReqVO.setRootProductId(rootProductId);
//        dataImportReqVO.setProjectCode(projectCode);
//        dataImportReqVO.setStructureId(structureId);
//        dataImportService.dataImport(file, dataImportReqVO);
//        return success(true);
//    }

    @PostMapping("/import")
    @Operation(summary = "数据包导入")
    @Parameters({
            @Parameter(name = "file", description = "zip 文件", required = true),
            @Parameter(name = "companyId", description = "厂家id", example = "1"),
            @Parameter(name = "companyName", description = "厂家名称", example = "1"),
            @Parameter(name = "structureId", description = "结构编号", example = "1")
    })
    public CommonResult<Boolean> dataImport(@RequestParam("file") MultipartFile file,
                                            @RequestParam("companyId") String companyId,
                                            @RequestParam("companyName") String companyName,
                                            @RequestParam("structureId") Long structureId) throws Exception {
        DataImportReqVO dataImportReqVO = new DataImportReqVO();
        //updateByLy-产品id暂写死
        dataImportReqVO.setRootProductId("97e734f8f5fe4e068f38c7be046b3537");
        dataImportReqVO.setCompanyId(companyId);
        dataImportReqVO.setCompanyName(companyName);
        dataImportReqVO.setStructureId(structureId);
        dataImportService.dataImport(file, dataImportReqVO);
        return success(true);
    }
}
