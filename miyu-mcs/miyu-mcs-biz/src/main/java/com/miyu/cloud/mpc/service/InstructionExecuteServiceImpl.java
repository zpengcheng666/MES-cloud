package com.miyu.cloud.mpc.service;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.dto.productionProcess.McsPlanStepNcDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.StepRespDTO;
import com.miyu.cloud.mpc.dto.FurnacePlanStartDTO;
import com.miyu.cloud.mpc.restApi.FurnacePlanApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Validated
@Transactional
public class InstructionExecuteServiceImpl implements InstructionExecuteService {

    @Resource
    private FurnacePlanApi furnacePlanApi;

    @Override
    public void sendFurnacePlanStart(OrderFormDO orderForm, List<BatchRecordDO> batchRecordDOList, StepRespDTO step, LedgerDO ledgerDO) {
        FurnacePlanStartDTO furnacePlanStartDTO = new FurnacePlanStartDTO();
        String orderNumber = orderForm.getOrderNumber();
        StringBuilder codeBuffer = new StringBuilder(orderNumber);
        codeBuffer.append(",").append(step.getStepNum());
        String hideString = orderNumber + "_";
        for (BatchRecordDO batchRecordDO : batchRecordDOList) {
            String replace = batchRecordDO.getNumber().replace(hideString, "");
            codeBuffer.append(",").append(replace);
        }
        String code = codeBuffer.toString();
        furnacePlanStartDTO.setOrderNumber(code);
        furnacePlanStartDTO.setProcessName(step.getStepName());
        furnacePlanStartDTO.setDeviceNumber(ledgerDO.getCode());
        furnacePlanStartDTO.setNcList(BeanUtils.toBean(step.getNcList(), McsPlanStepNcDTO.class) );
        Map<String, Object> map = furnacePlanApi.furnacePlanStart(furnacePlanStartDTO);
        Boolean success = (Boolean) map.get("success");
        if (!success) {
            throw new ServiceException(5101, (String) map.get("message"));
        }
    }
}
