package com.miyu.cloud.mpc.service;

import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.StepRespDTO;

import java.util.List;

public interface InstructionExecuteService {
    void sendFurnacePlanStart(OrderFormDO orderForm, List<BatchRecordDO> batchRecordDOList, StepRespDTO step, LedgerDO ledgerDO);
}
