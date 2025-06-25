package com.miyu.module.qms.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.qms.api.dto.InspectionSheetReqDTO;
import com.miyu.module.qms.api.dto.InspectionSheetRespDTO;
import com.miyu.module.qms.api.dto.InspectionSheetSchemeMaterialRespDTO;
import com.miyu.module.qms.api.dto.InspectionSheetSelfReqDTO;
import com.miyu.module.qms.api.inspectionsheet.InspectionSheetApi;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetSaveReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetSelfCheckSaveReqVO;
import com.miyu.module.qms.dal.dataobject.inspectionsheet.InspectionSheetDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import com.miyu.module.qms.service.inspectionsheet.InspectionSheetService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class InspectionSheetApiImpl implements InspectionSheetApi {

    @Resource
    private InspectionSheetService inspectionSheetService;

    @Override
    public CommonResult<String> createInspectionSheet(InspectionSheetReqDTO reqDTO) {
        return CommonResult.success(inspectionSheetService.createInspectionSheet(BeanUtils.toBean(reqDTO, InspectionSheetSaveReqVO.class)));
    }

    @Override
    public CommonResult<String> createInspectionSheetSelfInspection(InspectionSheetSelfReqDTO reqDTO) {
        return CommonResult.success(inspectionSheetService.createInspectionSheetSelfInspection(BeanUtils.toBean(reqDTO, InspectionSheetSelfCheckSaveReqVO.class)));
    }

    @Override
    public CommonResult<List<InspectionSheetRespDTO>> getInspectionSheetListByRecordNumber(Collection<String> numbers) {
        List<InspectionSheetDO> sheetList = inspectionSheetService.getInspectionSheetListByRecordNumber(numbers);
        return CommonResult.success(BeanUtils.toBean(sheetList, InspectionSheetRespDTO.class, vo -> {
            vo.setSchemes(BeanUtils.toBean(inspectionSheetService.getInspectionSheetSchemeListBySheetId(vo.getId()), InspectionSheetRespDTO.Scheme.class));
        }));
    }

    /**
     * 检验单号查询检验单产品检验结果（生产）
     * @param number
     * @return
     */
    @Override
    public CommonResult<List<InspectionSheetSchemeMaterialRespDTO>> getInspectionMaterialListByRecordNumber(String number, String tenantId) {
        List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetService.getInspectionSheetMaterialListByRecordNumber(number);
        return CommonResult.success(BeanUtils.toBean(materialList, InspectionSheetSchemeMaterialRespDTO.class));
    }

    /**
     * 检验单号查询检验单产品检验结果 批量 （采购、销售）
     * @param numbers
     * @return
     */
    @Override
    public CommonResult<List<InspectionSheetSchemeMaterialRespDTO>> getInspectionMaterialListByRecordNumber(Collection<String> numbers) {
        List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetService.getInspectionSheetMaterialListByRecordNumberBatch(numbers);
        return CommonResult.success(BeanUtils.toBean(materialList, InspectionSheetSchemeMaterialRespDTO.class));
    }
}
