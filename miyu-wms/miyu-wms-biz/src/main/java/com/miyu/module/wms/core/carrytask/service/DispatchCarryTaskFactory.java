package com.miyu.module.wms.core.carrytask.service;

import com.miyu.module.wms.core.carrytask.service.impl.CallMaterialServiceImpl;
import com.miyu.module.wms.core.carrytask.service.impl.CallTrayServiceImpl;
import com.miyu.module.wms.core.carrytask.service.impl.MaterialInServiceImpl;
import com.miyu.module.wms.core.carrytask.service.impl.MoveMaterialServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DispatchCarryTaskFactory {

    @Autowired
    private MaterialInServiceImpl materialInService;
    @Autowired
    private CallMaterialServiceImpl callMaterialService;
    @Autowired
    private MoveMaterialServiceImpl moveMaterialService;
    @Autowired
    private CallTrayServiceImpl callTrayService;


    public DispatchCarryTaskLogicService getDispatchService(Integer taskType) {
        switch (taskType) {
            case 1://入库任务
                return materialInService;
            case 2://出库任务
                return callMaterialService;
            case 3://库存移交
                return moveMaterialService;
            case 4://呼叫托盘
                return callTrayService;
            default:
                return null;
        }
    }
}
