package com.miyu.cloud.mcs.service.taskAdditionalInformation;

import com.alibaba.fastjson.JSON;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.taskAdditionalInformation.TaskAdditionalInfoDO;
import com.miyu.cloud.mcs.dal.mysql.taskAdditionalInformation.TaskAdditionalInfoMapper;
import com.miyu.cloud.mcs.dto.externalManufacture.McsMeasureResultDataDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.StringJoiner;

import static com.miyu.cloud.mcs.enums.DictConstants.*;

/**
 * 任务附加信息 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
@Transactional
public class TaskAdditionalInfoServiceImpl implements TaskAdditionalInfoService {

    @Resource
    private TaskAdditionalInfoMapper taskAdditionalInfoMapper;

    @Override
    public void addTaskRecordInfoMeasureInfo(BatchRecordDO batchRecordDO, McsMeasureResultDataDTO measureData) {
        TaskAdditionalInfoDO taskAdditionalInfoDO = new TaskAdditionalInfoDO();
        taskAdditionalInfoDO.setRecordId(batchRecordDO.getId());
        taskAdditionalInfoDO.setTypeCode(MCS_ADDITIONAL_TYPE_MEASURE_INFO);
        taskAdditionalInfoDO.setStorageMode(MCS_ADDITIONAL_STORAGE_MODE_DATABASE);
        taskAdditionalInfoDO.setContent(JSON.toJSONString(measureData));
        taskAdditionalInfoMapper.insert(taskAdditionalInfoDO);
    }

    @Override
    public void addTaskRecordInfoMeasureFile(BatchRecordDO batchRecordDO, List<String> pathList, String prePath) {
        TaskAdditionalInfoDO taskAdditionalInfoDO = new TaskAdditionalInfoDO();
        taskAdditionalInfoDO.setRecordId(batchRecordDO.getId());
        taskAdditionalInfoDO.setTypeCode(MCS_ADDITIONAL_TYPE_MEASURE_FILE);
        taskAdditionalInfoDO.setStorageMode(MCS_ADDITIONAL_STORAGE_MODE_FILE);
        StringJoiner joiner = new StringJoiner(",");
        pathList.stream().map(cs -> prePath + cs).forEach(joiner::add);
        taskAdditionalInfoDO.setPath(joiner.toString());
        taskAdditionalInfoMapper.insert(taskAdditionalInfoDO);
    }
}
