package com.miyu.cloud.mcs.restServer.service.inspection;

import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.module.qms.api.dto.InspectionSheetRespDTO;
import com.miyu.module.qms.api.dto.InspectionSheetSchemeMaterialRespDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;

import java.util.Collection;
import java.util.List;

public interface InspectionSheetService {

    MaterialStockRespDTO getMaterialById(String materialId);

    //自检互检 生成检验单
    void createInspectionSheet(BatchRecordDO batchRecord, MaterialStockRespDTO material, String schemeId, String userId, boolean first);

    //工序检 成品检验 生成检验单
    void createInspectionSheetTask(BatchRecordDO batchRecord, MaterialStockRespDTO material, String technologyId);

    InspectionSheetSchemeMaterialRespDTO getFinishedInspection(String numberSet);
}
