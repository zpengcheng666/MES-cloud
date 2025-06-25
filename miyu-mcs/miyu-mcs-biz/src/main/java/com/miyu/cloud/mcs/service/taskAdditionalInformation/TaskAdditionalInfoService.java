package com.miyu.cloud.mcs.service.taskAdditionalInformation;

import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dto.externalManufacture.McsMeasureResultDataDTO;

import java.util.List;

/**
 * 任务附加信息 Service 接口
 *
 * @author miyu
 */
public interface TaskAdditionalInfoService {

    void addTaskRecordInfoMeasureInfo(BatchRecordDO batchRecordDO, McsMeasureResultDataDTO measureData);

    void addTaskRecordInfoMeasureFile(BatchRecordDO batchRecordDO, List<String> pathList, String prePath);
}
