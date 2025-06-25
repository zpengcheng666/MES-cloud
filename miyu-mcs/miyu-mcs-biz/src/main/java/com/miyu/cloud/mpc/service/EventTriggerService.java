package com.miyu.cloud.mpc.service;

import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;

public interface EventTriggerService {

    void checkDetectionDeviceCarry();

    void createInspectionSheetTask(BatchRecordDO batchRecordDO);
}
