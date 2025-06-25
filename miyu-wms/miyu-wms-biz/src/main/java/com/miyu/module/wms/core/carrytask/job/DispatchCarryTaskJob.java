package com.miyu.module.wms.core.carrytask.job;

import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import com.miyu.module.wms.api.order.OrderDistributeApiImpl;
import com.miyu.module.wms.core.carrytask.service.DispatchCarryTaskFactory;
import com.miyu.module.wms.core.carrytask.service.DispatchCarryTaskLogicService;
import com.miyu.module.wms.core.carrytask.service.impl.MaterialInServiceImpl;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Transactional
public class DispatchCarryTaskJob {
    @Resource
    private CarryTaskService carryTaskService;
    @Resource
    private DispatchCarryTaskFactory dispatchCarryTaskFactory;
    @Resource
    private OrderDistributeApiImpl orderDistributeApiImpl;

    // 定时处理 挂起的搬运任务
    @XxlJob("handleHoldCarryTaskJob")
//    @TenantIgnore
    @TenantJob
    public void handleHoldCarryTaskJob() throws Exception {
        System.out.println("开始处理挂起的搬运任务"+Thread.currentThread().getName());
        List<CarryTaskDO> hangingCarryTask = carryTaskService.getHangingCarryTask();
        for (CarryTaskDO carryTask : hangingCarryTask) {
            DispatchCarryTaskLogicService dispatchService = dispatchCarryTaskFactory.getDispatchService(carryTask.getTaskType());
            // 处理挂起的搬运任务
            dispatchService.handleHoldCarryTaskLogic(carryTask);
        }
    }

    // 定时处理 不在默认存放仓库的空闲托盘
    @XxlJob("handleIdleTrayJob")
    @TenantJob
    public void handleIdleTrayJob() throws Exception {
        System.out.println("开始处理空闲托盘"+Thread.currentThread().getName());
        MaterialInServiceImpl materialInServiceImpl = (MaterialInServiceImpl)dispatchCarryTaskFactory.getDispatchService(DictConstants.WMS_CARRY_TASK_TYPE_IN);
        materialInServiceImpl.handleIdleTray();
    }


    // 定时处理  自动化线体库会根据出入库单自动调度出入库
    @XxlJob("handleAutoLineJob")
    @TenantJob
    public void handleAutoLineJob() throws Exception {
        System.out.println("开始处理在自动化线体库的出入库单"+Thread.currentThread().getName());
        orderDistributeApiImpl.autoLine();
    }

}
