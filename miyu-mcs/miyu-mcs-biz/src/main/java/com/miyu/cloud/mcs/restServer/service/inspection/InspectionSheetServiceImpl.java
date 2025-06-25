package com.miyu.cloud.mcs.restServer.service.inspection;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.restServer.api.InspectionApiMapping;
import com.miyu.module.qms.api.dto.InspectionSheetSchemeMaterialRespDTO;
import com.miyu.module.qms.api.dto.InspectionSheetSelfReqDTO;
import com.miyu.module.qms.api.inspectionsheet.InspectionSheetApi;
import com.miyu.module.qms.api.inspectionsheetcreatetask.dto.TaskDTO;
import com.miyu.module.qms.enums.InspectionSheetSourceTypeEnum;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
@Validated
public class InspectionSheetServiceImpl implements InspectionSheetService {

    @Resource
    private InspectionSheetApi inspectionSheetApi;
    @Resource
    private InspectionApiMapping inspectionApiMapping;
    @Resource
    private MaterialStockApi materialStockApi;

    @Override
    public MaterialStockRespDTO getMaterialById(String materialId) {
        CommonResult<MaterialStockRespDTO> commonResult = materialStockApi.getById(materialId);
        if (!commonResult.isSuccess()) throw new ServiceException(commonResult.getCode(),commonResult.getMsg());
        return commonResult.getData();
    }

    @Override
    public void createInspectionSheet(BatchRecordDO batchRecord, MaterialStockRespDTO material, String schemeId, String userId, boolean first) {
        InspectionSheetSelfReqDTO inspectionSheetSelfReqDTO = new InspectionSheetSelfReqDTO();
        inspectionSheetSelfReqDTO.setRecordNumber(batchRecord.getNumber());
        inspectionSheetSelfReqDTO.setMaterialConfigId(material.getMaterialConfigId());
        inspectionSheetSelfReqDTO.setBatchNumber(material.getBatchNumber());
        inspectionSheetSelfReqDTO.setSchemeId(schemeId);
        inspectionSheetSelfReqDTO.setMaterialId(material.getId());
        inspectionSheetSelfReqDTO.setProcessId(batchRecord.getProcessId());
        inspectionSheetSelfReqDTO.setAssignmentId(userId);
        inspectionSheetSelfReqDTO.setBarCode(material.getBarCode());
        inspectionSheetSelfReqDTO.setSourceType(InspectionSheetSourceTypeEnum.PRODUCE.getStatus());
        inspectionSheetSelfReqDTO.setNeedFirstInspection(first?1:0);
        CommonResult<String> inspectionSheet = inspectionSheetApi.createInspectionSheetSelfInspection(inspectionSheetSelfReqDTO);
        if (!inspectionSheet.isSuccess()) throw new ServiceException(inspectionSheet.getCode(), inspectionSheet.getMsg());
    }

    @Override
    public void createInspectionSheetTask(BatchRecordDO batchRecord, MaterialStockRespDTO material, String technologyId) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setRecordId(batchRecord.getId());
        taskDTO.setRecordNumber(batchRecord.getNumber());
        taskDTO.setSheetName("生产检验"+batchRecord.getNumber());
        taskDTO.setMaterialConfigId(material.getMaterialConfigId());
        taskDTO.setSourceType(InspectionSheetSourceTypeEnum.PRODUCE.getStatus());
        taskDTO.setBatchNumber(material.getBatchNumber());
        taskDTO.setTechnologyId(technologyId);
        taskDTO.setProcessId(batchRecord.getProcessId());
        taskDTO.setSchemeType(2);
        TaskDTO.Detail detail = new TaskDTO.Detail();
        detail.setMaterialId(material.getId());
        detail.setBarCode(material.getBarCode());
        detail.setBatchNumber(material.getBatchNumber());
        taskDTO.setDetails(Collections.singletonList(detail));
        CommonResult<String> result =  inspectionApiMapping.createInspectionSheetTask(taskDTO,"1");
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
    }

    @Override
    public InspectionSheetSchemeMaterialRespDTO getFinishedInspection(String number) {
        CommonResult<List<InspectionSheetSchemeMaterialRespDTO>> commonResult = inspectionSheetApi.getInspectionMaterialListByRecordNumber(number, "1");
        if (!commonResult.isSuccess()) throw new ServiceException(commonResult.getCode(),commonResult.getMsg());
        if (commonResult.getData().size() > 0) {
            return commonResult.getData().get(0);
        }
        return null;
    }
}
