package com.miyu.cloud.mcs.service.batchorderapsresult;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerRespVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarSpecialDO;
import com.miyu.cloud.dms.dal.dataobject.calendardevice.CalendarDeviceDO;
import com.miyu.cloud.dms.dal.dataobject.calendardevice.CalendarProductionlineDO;
import com.miyu.cloud.dms.dal.dataobject.calendarshift.ShiftTimeDO;
import com.miyu.cloud.dms.dal.dataobject.devicetype.DeviceTypeDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.dal.mysql.calendar.CalendarSpecialMapper;
import com.miyu.cloud.dms.dal.mysql.calendardevice.CalendarDeviceMapper;
import com.miyu.cloud.dms.dal.mysql.calendardevice.CalendarProductionlineMapper;
import com.miyu.cloud.dms.dal.mysql.calendarshift.ShiftTimeMapper;
import com.miyu.cloud.dms.dal.mysql.devicetype.DeviceTypeMapper;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.linestationgroup.LineStationGroupMapper;
import com.miyu.cloud.dms.service.linestationgroup.LineStationGroupService;
import com.miyu.cloud.mcs.controller.admin.batchorderapsresult.result.BatchOrderResult;
import com.miyu.cloud.mcs.controller.admin.batchorderapsresult.result.BatchRecordResult;
import com.miyu.cloud.mcs.controller.admin.batchorderapsresult.result.OrderFormResult;
import com.miyu.cloud.mcs.controller.admin.batchorderapsresult.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import com.miyu.cloud.mcs.dal.dataobject.batchorderapsresult.BatchOrderApsResultDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecordstep.BatchRecordStepDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.dal.mysql.batchorder.BatchOrderMapper;
import com.miyu.cloud.mcs.dal.mysql.batchorderapsresult.BatchOrderApsResultMapper;
import com.miyu.cloud.mcs.dal.mysql.batchrecord.BatchRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.batchrecordstep.BatchRecordStepMapper;
import com.miyu.cloud.mcs.dal.mysql.orderform.OrderFormMapper;
import com.miyu.cloud.mcs.dto.schedule.*;
import com.miyu.cloud.mcs.dto.schedule.utils.IntervalVarList;
import com.miyu.cloud.mcs.restServer.service.order.OrderRestService;
import com.miyu.cloud.mcs.restServer.service.technology.TechnologyRestService;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureDetailRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcessPlanDetailRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.StepRespDTO;
import ilog.concert.*;
import ilog.cp.IloCP;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.APS_DATE_NOT_MATCH_SHIFT;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.BATCH_ORDER_APS_RESULT_NOT_EXISTS;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.LEDGER_HAS_NOT_SHIFT;

/**
 * 排产结果 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class BatchOrderApsResultServiceImpl implements BatchOrderApsResultService {

    @Resource
    private BatchOrderApsResultMapper batchOrderApsResultMapper;

    @Resource
    private LedgerMapper ledgerMapper;

    @Resource
    private LineStationGroupMapper lineStationGroupMapper;

    @Resource
    private OrderFormMapper orderFormMapper;
    @Resource
    private BatchOrderMapper batchOrderMapper;
    @Resource
    private BatchRecordMapper batchRecordMapper;
    @Resource
    private BatchRecordStepMapper batchRecordStepMapper;
    @Resource
    private TechnologyRestService technologyRestService;

    @Resource
    private CalendarDeviceMapper calendarDeviceMapper;

    @Resource
    private CalendarSpecialMapper calendarSpecialMapper;

    @Resource
    private CalendarProductionlineMapper calendarProductionlineMapper;

    @Resource
    private ShiftTimeMapper shiftTimeMapper;

    @Override
    public String createBatchOrderApsResult(BatchOrderApsResultSaveReqVO createReqVO) {
        // 插入
        BatchOrderApsResultDO batchOrderApsResult = BeanUtils.toBean(createReqVO, BatchOrderApsResultDO.class);
        batchOrderApsResultMapper.insert(batchOrderApsResult);
        // 返回
        return batchOrderApsResult.getId();
    }

    @Override
    public void updateBatchOrderApsResult(BatchOrderApsResultSaveReqVO updateReqVO) {
        // 校验存在
        validateBatchOrderApsResultExists(updateReqVO.getId());
        // 更新
        BatchOrderApsResultDO updateObj = BeanUtils.toBean(updateReqVO, BatchOrderApsResultDO.class);
        batchOrderApsResultMapper.updateById(updateObj);
    }

    @Override
    public void deleteBatchOrderApsResult(String id) {
        // 校验存在
        validateBatchOrderApsResultExists(id);
        // 删除
        batchOrderApsResultMapper.deleteById(id);
    }

    private void validateBatchOrderApsResultExists(String id) {
        if (batchOrderApsResultMapper.selectById(id) == null) {
            throw exception(BATCH_ORDER_APS_RESULT_NOT_EXISTS);
        }
    }

    @Override
    public BatchOrderApsResultDO getBatchOrderApsResult(String id) {
        return batchOrderApsResultMapper.selectById(id);
    }

    @Override
    public PageResult<BatchOrderApsResultDO> getBatchOrderApsResultPage(BatchOrderApsResultPageReqVO pageReqVO) {
        return batchOrderApsResultMapper.selectPage(pageReqVO);
    }

    @Override
    public String productionScheduling(OrderScheduleSaveVO createReqVO) throws Exception {
        List<ScheduleLedger> deviceList = createReqVO.getDeviceList();
        OrderScheduleStrategy strategy = createReqVO.getStrategy();
        Map<String, Map<String, List<ScheduleLedger>>> planDevice = createReqVO.getPlanDevice();

        for (ScheduleLedger device : deviceList) {
            if (StringUtils.isNotBlank(device.getLintStationGroup())) {
                LineStationGroupDO lineStationGroupDO = lineStationGroupMapper.selectById(device.getLintStationGroup());
                device.setLintStationGroupType(lineStationGroupDO.getAffiliationDeviceType());
            }
        }
        List<SchedulePlan> schedulePlans = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<ScheduleLedger>>> entry : planDevice.entrySet()) {
            OrderFormDO orderFormDO = orderFormMapper.selectById(entry.getKey());
            ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(orderFormDO.getId(), orderFormDO.getTechnologyId());
            List<ProcedureRespDTO> procedureList = technology.getProcedureList();
            Map<String, List<ScheduleLedger>> processPlan = entry.getValue();
            SchedulePlan schedulePlan = new SchedulePlan();
            schedulePlan.setId(orderFormDO.getId());
            schedulePlan.setNumber(orderFormDO.getOrderNumber());
            schedulePlan.setProjectNumber(orderFormDO.getProjectNumber());
            schedulePlan.setPartNumber(orderFormDO.getPartNumber());
            schedulePlan.setTechnologyId(orderFormDO.getTechnologyId());
            schedulePlan.setPriority(orderFormDO.getPriority());
            schedulePlan.setCount(orderFormDO.getCount());
            schedulePlan.setReceptionTime(orderFormDO.getReceptionTime());
            schedulePlan.setDeliveryTime(orderFormDO.getDeliveryTime());
            schedulePlan.setIsFirst(orderFormDO.getFirst());
            for (Map.Entry<String, List<ScheduleLedger>> processEntry : processPlan.entrySet()) {
                Optional<ProcedureRespDTO> first = procedureList.stream().filter(item -> item.getId().equals(processEntry.getKey())).findFirst();
                if (first.isPresent()) {
                    ProcedureRespDTO process = first.get();
                    ScheduleProcess scheduleProcess = new ScheduleProcess();
                    scheduleProcess.setId(process.getId());
                    scheduleProcess.setProcessingTime(process.getProcessingTime());
                    scheduleProcess.setProcedureName(process.getProcedureName());
                    scheduleProcess.setIsInspect(process.getIsInspect());
                    scheduleProcess.setIsOut(process.getIsOut());
                    scheduleProcess.setPreparationTime(process.getPreparationTime());
                    scheduleProcess.setProcessingTime(process.getProcessingTime());
                    scheduleProcess.setResourceList(BeanUtils.toBean(process.getResourceList(), ScheduleResourceType.class));
                    List<ScheduleLedger> ledgerList = processEntry.getValue();
                    if (ledgerList !=null && ledgerList.size() > 0) {
                        scheduleProcess.setLedgerIdList(ledgerList.stream().map(ScheduleLedger::getId).collect(Collectors.toList()));
                    }
                    scheduleProcess.setStepList(new ArrayList<>());
                    for (StepRespDTO stepRespDTO : process.getStepList()) {
                        ScheduleStep scheduleStep = BeanUtils.toBean(stepRespDTO, ScheduleStep.class);
                        scheduleStep.setResourceList(BeanUtils.toBean(stepRespDTO.getResourceList(), ScheduleResourceType.class));
                        scheduleProcess.getStepList().add(scheduleStep);
                    }
                    if (schedulePlan.getProcessList() == null) {
                        schedulePlan.setProcessList(new ArrayList<>());
                    }
                    schedulePlan.getProcessList().add(scheduleProcess);
                }
            }
            schedulePlans.add(schedulePlan);
        }

        ScheduleConfig scheduleConfig = new ScheduleConfig();
        scheduleConfig.setLedgerList(deviceList);
        scheduleConfig.setStartTime(strategy.getTime());
        scheduleConfig.setMaxResourcesCount(strategy.getResource());
        scheduleConfig.setPlanList(schedulePlans);
        return createApsResult2(scheduleConfig);
    }

    @Override
    public String createApsResult2(ScheduleConfig scheduleConfig) throws Exception {
        //设备列表
        List<ScheduleLedger> ledgerList = scheduleConfig.getLedgerList();
        Map<String, ScheduleLedger> scheduleLedgerMap = CollectionUtils.convertMap(ledgerList, ScheduleLedger::getId);

        //排产开始时间
        //Date startTime = scheduleConfig.getStartTime();
        LocalDateTime startTime = scheduleConfig.getStartTime();

        //最大资源数量,目前不考虑
        //List<Map<String, Integer>> maxResourcesCount = scheduleConfig.getMaxResourcesCount();
        List<ScheduleResourceType> maxResourcesCount1 = scheduleConfig.getMaxResourcesCount();
        //批次计划
        List<SchedulePlan> planList = scheduleConfig.getPlanList();

        /** ********************************/
        //开始
        IloCP cp = new IloCP();
        //开始构建算法，所有任务形成一个列表，
        List<IloNumExpr> ends = new ArrayList<>();
        List<IloIntervalVar> allTaskIntervals = new ArrayList<>();
        //批次任务
        for (SchedulePlan schedulePlan : planList) {
            Integer count = schedulePlan.getCount();
            //具体任务
            for (int i = 0; i < count; i++) {
                ScheduleJob scheduleJob = new ScheduleJob(schedulePlan.getId(), i);
                schedulePlan.getTaskJobs().add(scheduleJob);
                //工序
                List<ScheduleProcess> processList = schedulePlan.getProcessList();
                //任务工序
                List<ScheduleJobProcess> jobProcessList = BeanUtils.toBean(processList, ScheduleJobProcess.class);
                //前置工序
                IloIntervalVar prec = null;
                ScheduleJobProcess scheduleJobProcess = null;
                for (ScheduleJobProcess proc : jobProcessList) {
                    List<String> ledgerIdList = proc.getLedgerIdList();
                    //工序上的设备,用来判断所有设备是不是同一种，不是同一种就得特殊处理了。
                    List<ScheduleLedger> procLedgerList = new ArrayList<>();
                    if(ledgerIdList!=null&&ledgerIdList.size()>0){
                        //ledgerIdList有数据,指定特别设备的情况,无需处理
                    }else {
                        ledgerIdList = new ArrayList<>();
                        //没有指定设备
                        List<ScheduleResourceType> resourceList = proc.getResourceList();
                        //只取设备资源类型
                        List<String> resourcesTypeIds = resourceList.stream().filter(item -> item.getResourcesType() == 1).map(ScheduleResourceType::getResourcesTypeId).collect(Collectors.toList());
                        //匹配设备
                        for (ScheduleLedger scheduleLedger : ledgerList) {
                            //用产线匹配货设备类型匹配
                            if(resourcesTypeIds.contains(scheduleLedger.getLintStationGroup())||resourcesTypeIds.contains(scheduleLedger.getEquipmentStationType())){
                                ledgerIdList.add(scheduleLedger.getId());
                                procLedgerList.add(scheduleLedger);
                            }
                        }
                        proc.setLedgerIdList(ledgerIdList);
                        proc.setLedgerList(procLedgerList);
                    }

                    //工序层面决策变量,工序时长作为区间变量
                    Integer processingTime = proc.getProcessingTime();
                    //外协
                    if(proc.getIsOut()==1){
                        if(scheduleJobProcess!=null&&(scheduleJobProcess.getIsOut()==1)){
                            //上一步是外协
                            processingTime = 60;
                        }else{
                            //上一步不是外协
                            processingTime = 3*24*60;
                        }
                    }
                    IloIntervalVar procInterval =  cp.intervalVar(processingTime);

                    //不仅判断设备数量还直接判断了不工序外协
                    if(ledgerIdList.size()>0&&proc.getIsOut()==0){
                        //设备类型
                        List<String> equipmentStationTypes = procLedgerList.stream().map(ScheduleLedger::getEquipmentStationType).distinct().collect(Collectors.toList());
                        //设备工序区间集合(只是为了从中选一个)
                        IntervalVarList deviceTasks = new IntervalVarList();
                        if(equipmentStationTypes.size()>1){
                            //TODO 如果设备类型大于1,得选多个设备,得特殊处理,先不处理了
                            for (String ledgerId : ledgerIdList) {
                                ScheduleLedger scheduleLedger = scheduleLedgerMap.get(ledgerId);
                                IloIntervalVar ti =  cp.intervalVar(proc.getProcessingTime());
                                //这个唯一
                                ti.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId()+"-"+ledgerId);
                                ti.setOptional();
                                deviceTasks.add(ti);
                                //该设备任务队列 ?设备的任务队列,为什么记录这个(这个也是重复的)
                                scheduleLedger.getMachineIntervals().add(ti);
                                //实际上是所有的工序任务队列(一个工序多个设备的也加进来了)
                                allTaskIntervals.add(ti);
                            }
                        }else {
                            //设备类型相同,从多个里选一个就行
                            for (String ledgerId : ledgerIdList) {
                                ScheduleLedger scheduleLedger = scheduleLedgerMap.get(ledgerId);
                                IloIntervalVar ti =  cp.intervalVar(proc.getProcessingTime());
                                //这个唯一
                                ti.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId()+"-"+ledgerId);
                                ti.setOptional();
                                deviceTasks.add(ti);
                                //该设备任务队列 ?设备的任务队列,为什么记录这个(这个也是重复的)
                                scheduleLedger.getMachineIntervals().add(ti);
                                //实际上是所有的工序任务队列(一个工序多个设备的也加进来了)
                                allTaskIntervals.add(ti);
                            }
                        }
                        //可选设备有且仅有一个可执行该任务
                        cp.add(cp.alternative(procInterval, deviceTasks.toArray()));//当多个设备满足时候，通过alternative从多个中最终产生一个替代第一个
                    }else {
                        //TODO 没有设备那应该是工序外协，特殊处理
                        if(scheduleJobProcess!=null&&(scheduleJobProcess.getIsOut()==1)){
                            //上一道工序就是外协,给他加一个小时
//                            IloIntervalVar ti = cp.intervalVar(60);
//                            ti.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId());
//                            allTaskIntervals.add(ti);
//                            IloIntervalVar ti = cp.intervalVar(60);
//                            ti.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId());
                            procInterval.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId());
                            allTaskIntervals.add(procInterval);

                        }else {
                            //上一道工序不是外协,给他加三天
//                            IloIntervalVar ti =  cp.intervalVar(3*24*60);
//                            procInterval =  cp.intervalVar(3*24*60);
//                            ti.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId());
//                            ti.setOptional();
//                            allTaskIntervals.add(ti);
                            procInterval.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId());
                            allTaskIntervals.add(procInterval);
                        }
                    }
                    proc.setInnerInterval(procInterval);
                    //重要约束,前面工序的结束时间在后面工序的开始时间之前
                    if(prec!=null){
                        cp.add(cp.endBeforeStart(prec,procInterval));
                    }
                    prec = procInterval;
                    scheduleJobProcess = proc;
                }
                //获取所有产品最后一个工序的加工时间
                ends.add(cp.endOf(prec));
                //工序存储在任务上
                scheduleJob.setTaskSteps(jobProcessList);
            }
        }

        //所有的设备的任务时间不能重叠
        for (ScheduleLedger device : ledgerList) {
            IloIntervalSequenceVar deviceSequence = cp.intervalSequenceVar(device.getMachineIntervals().toArray(),device.getId());
            cp.add(cp.noOverlap(deviceSequence));
        }
        cp.add(cp.minimize(cp.max(ends.toArray(new IloNumExpr[0]))));
        cp.setParameter(IloCP.IntParam.FailLimit,10000);

        /***********************************************/
        BatchOrderApsResultDO batchOrderApsResult = new BatchOrderApsResultDO();
        //求解
        if (cp.solve()) {
            double objValue = cp.getObjValue();
            System.out.println(objValue);
            //存储所有的工序解
            Map<String,IloIntervalVar> cpMap = new HashMap<>();
            for (int i = 0; i < allTaskIntervals.size(); i++) {
                IloIntervalVar var = (IloIntervalVar) allTaskIntervals.get(i);
                if (cp.isPresent(var)){
                    IloIntervalVar ti = allTaskIntervals.get(i);
                    String name = ti.getName();
                    cpMap.put(name,ti);
                }
            }
            //排产开始时间
            Instant instant = startTime.atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instant);
            long beginApsTime = date.getTime()/60/1000;

            for (SchedulePlan schedulePlan : planList) {
                List<ScheduleJob> taskJobs = schedulePlan.getTaskJobs();

                for (int i = 0; i < taskJobs.size(); i++) {
                    ScheduleJob scheduleJob = taskJobs.get(i);
                    List<ScheduleJobProcess> taskSteps = scheduleJob.getTaskSteps();
                    int count = 0;
                    for (ScheduleJobProcess proc : taskSteps) {
                        String name = "";
                        if(proc.getLedgerIdList()!=null&&proc.getLedgerIdList().size()>0&&proc.getIsOut()==0){
                            //正常加工
                            for (String deviceId : proc.getLedgerIdList()) {
                                name = schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId()+"-"+deviceId;
                                if(cpMap.containsKey(name)){
                                    //TODO 暂时不考虑多设备
                                    proc.setSelectLedgerIdList(Arrays.asList(deviceId));
                                    break;
                                }
                            }
                        }else {
                           //外协
                            name = schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId();
                        }

                        //String name = schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId();
                        if(cpMap.containsKey(name)){
                            IloIntervalVar ti = cpMap.get(name);
                            Date beginDate = new Date((beginApsTime + cp.getStart(ti)) * 60 * 1000);
                            Date endDate = new Date((beginApsTime + cp.getEnd(ti)) * 60 * 1000);
                            proc.setPlanStartTime(beginDate);
                            proc.setPlanEndTime(endDate);
                            //还要赋值任务整体的开始时间和结束时间
                            if(count==0){
                                scheduleJob.setPlanStartTime(beginDate);
                            }else {
                                boolean before = scheduleJob.getPlanStartTime().before(beginDate);
                                //boolean before = scheduleJob.getPlanStartTime().isBefore(beginDate);
                                if(!before){
                                    scheduleJob.setPlanStartTime(beginDate);
                                }
                            }
                            if(ObjectUtil.isNull(scheduleJob.getPlanEndTime())){
                                scheduleJob.setPlanEndTime(endDate);
                            }else {
                                //boolean after = scheduleJob.getPlanEndTime().isAfter(endDate);
                                boolean after = scheduleJob.getPlanEndTime().after(endDate);
                                if(!after){
                                    scheduleJob.setPlanEndTime(endDate);
                                }
                            }
                            count++;
                        }
                    }
                }

            }
            //转成字符串存起来
            String apsContent = JSON.toJSONString(scheduleConfig,
                    SerializerFeature.WriteNullListAsEmpty,
                    SerializerFeature.WriteNullStringAsEmpty,
                    SerializerFeature.WriteNullNumberAsZero,
                    SerializerFeature.WriteNullBooleanAsFalse,
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat,
                    SerializerFeature.DisableCircularReferenceDetect,
                    SerializerFeature.PrettyFormat
                    );
            //Date startDate = new Date(beginApsTime);
            //LocalDateTime startLocalDateTime = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            batchOrderApsResult.setStartTime(startTime);
            batchOrderApsResult.setApsContent(apsContent);
            batchOrderApsResult.setStatus(1).setSubmitedTime(LocalDateTime.now());
            batchOrderApsResultMapper.insert(batchOrderApsResult);

        }
        return batchOrderApsResult.getId();
    }

    /**
     * 之前的排到工序，但是无法应对多设备的情况,现在排到工步
     * @param scheduleConfig
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createApsResult(ScheduleConfig scheduleConfig) throws Exception {
        //设备列表
        List<ScheduleLedger> ledgerList = scheduleConfig.getLedgerList();
        Map<String, ScheduleLedger> scheduleLedgerMap = CollectionUtils.convertMap(ledgerList, ScheduleLedger::getId);

        //排产开始时间
        //Date startTime = scheduleConfig.getStartTime();
        LocalDateTime startTime = scheduleConfig.getStartTime();
        //最大资源数量,目前不考虑
        //List<Map<String, Integer>> maxResourcesCount = scheduleConfig.getMaxResourcesCount();
        List<ScheduleResourceType> maxResourcesCount = scheduleConfig.getMaxResourcesCount();

        //TODO 设备日历
//        boolean match = matchDeviceCalendar(ledgerList, startTime);
//        if(!match){
//            throw exception(APS_DATE_NOT_MATCH_SHIFT);
//        }

//        //查询所有设备的设备日历
//        List<String> ledgerIds = ledgerList.stream().map(ScheduleLedger::getId).collect(Collectors.toList());
//        //判断排产开始时间是否在设备班次内
//        LambdaQueryWrapper<CalendarDeviceDO> deviceWrapper = new LambdaQueryWrapper<>();
//        deviceWrapper.in(CalendarDeviceDO::getDeviceId, ledgerIds);
//        LocalDate currentDate = startTime.toLocalDate();
//        deviceWrapper.eq(CalendarDeviceDO::getDate, currentDate);
//        List<CalendarDeviceDO> calendarDeviceDOS = calendarDeviceMapper.selectList(deviceWrapper);
//        List<String> calendarDeviceIds = calendarDeviceDOS.stream().map(CalendarDeviceDO::getDeviceId).distinct().collect(Collectors.toList());
////        //查到的设备少,说明有的设备没有安排班次
////        if(ledgerIds.size()>calendarDeviceIds.size()){
////            throw exception(LEDGER_HAS_NOT_SHIFT);
////        }
//
//        LocalTime currentTime = startTime.toLocalTime();
//        //上面的现在是特殊设备日历，还要查普通设备日历(特殊日历被查到,普通的就不用查了)
//        List<String> remianDeviceIds = ledgerIds.stream().filter((item) -> {
//            return !calendarDeviceIds.contains(item);
//        }).collect(Collectors.toList());
//
//
//
//        for (ScheduleLedger ledger : ledgerList) {
//            List<CalendarDeviceDO> list = new ArrayList<>();
//            for (CalendarDeviceDO calendarDeviceDO : calendarDeviceDOS) {
//                if(ledger.getId().equals(calendarDeviceDO.getDeviceId())){
//                    list.add(calendarDeviceDO);
//                }
//            }
//            //判断时间段是不是匹配上了
//            boolean match = false;
//            for (CalendarDeviceDO calendarDeviceDO : list) {
//                LocalTime startTime1 = calendarDeviceDO.getStartTime();
//                LocalTime endTime1 = calendarDeviceDO.getEndTime();
//                if(currentTime.isAfter(startTime1)&&currentTime.isBefore(endTime1)){
//                    match = true;
//                }
//            }
//            //没匹配上就得抛异常了
//            if(!match){
//                throw exception(APS_DATE_NOT_MATCH_SHIFT);
//            }
//        }


        //批次计划
        List<SchedulePlan> planList = scheduleConfig.getPlanList();

        /** ********************************/
        //开始
        IloCP cp = new IloCP();
        //工装资源限制(工装类型,限制最大资源数量)
        Map<String,IloCumulFunctionExpr> resourcelimitMap = new HashMap<>();
        Map<String, Integer> resourceCountMap = CollectionUtils.convertMap(maxResourcesCount, ScheduleResourceType::getResourcesTypeId, ScheduleResourceType::getCount);

        //开始构建算法，所有任务形成一个列表，
        List<IloNumExpr> ends = new ArrayList<>();
        List<IloIntervalVar> allTaskIntervals = new ArrayList<>();
        //批次任务
        for (SchedulePlan schedulePlan : planList) {
            Integer count = schedulePlan.getCount();
            //具体任务
            for (int i = 0; i < count; i++) {
                //任务是新创建的对象，不会重复使用，没问题。
                ScheduleJob scheduleJob = new ScheduleJob(schedulePlan.getId(), i);
                schedulePlan.getTaskJobs().add(scheduleJob);
                //工序
                List<ScheduleProcess> processList = schedulePlan.getProcessList();
                //任务工序(复制来的工序,应该也没有问题,是新的)
                List<ScheduleJobProcess> jobProcessList = BeanUtils.toBean(processList, ScheduleJobProcess.class);
                //前置工序
                IloIntervalVar prec = null;
                ScheduleJobProcess scheduleJobProcess = null;
                //最后一个工步
                IloIntervalVar lastStepPrec = null;

                //***************工步改造顶******************
                //这轮只是为了给每个工步放上设备,从下面那个抽离
                for (ScheduleJobProcess proc : jobProcessList) {
                    //大概是浅拷贝的缘故,工序复制的时候，工步使用了引用
                    List<ScheduleStep> stepListTemp = proc.getStepList();
                    //创建新工步列表,消除浅拷贝影响
                    List<ScheduleStep> stepList = BeanUtils.toBean(stepListTemp, ScheduleStep.class);
                    //指定设备
                    List<String> ledgerIdList = proc.getLedgerIdList();
                    if(stepList!=null&&stepList.size()>0){
                        //有工步
                        //指定特殊设备
                        if(ledgerIdList!=null&&ledgerIdList.size()>0){
                            //遍历工步
                            for (ScheduleStep step : stepList) {
                                //把设备按在工步上
                                List<String> stepLedgerIdList = new ArrayList<>();
                                List<String> resourcesTypeIds = step.getResourceList().stream().filter((item) -> {
                                    return item.getResourcesType() == 1;
                                }).map(ScheduleResourceType::getResourcesTypeId).collect(Collectors.toList());
                                for (String deviceId : ledgerIdList) {
                                    ScheduleLedger scheduleLedger = scheduleLedgerMap.get(deviceId);
                                    //只能用设备类型匹配,因为这种是工步的
                                    if(resourcesTypeIds.contains(scheduleLedger.getEquipmentStationType())){
                                        //匹配上说明是用在这个工步上的设备
                                        stepLedgerIdList.add(deviceId);
                                    }
                                }
                                //TODO 如果等于0说明出错了,该选的没选,抛出异常
                                if(stepLedgerIdList.size()==0){
                                    return "false";
                                }
                                step.setLedgerIdList(stepLedgerIdList);
                            }
                        }else {
                           //未指定特殊设备
                            //遍历工步
                            for (ScheduleStep step : stepList) {
                                //把设备按在工步上
                                List<String> stepLedgerIdList = new ArrayList<>();
                                //要匹配工序的产线类型，还要匹配工步的设备类型
                                List<ScheduleResourceType> resourceList = proc.getResourceList();
                                List<ScheduleResourceType> resourceList1 = step.getResourceList();
                                //产线资源为空,说明没有设备(是外协)
                                if(CollectionUtils.isAnyEmpty(resourceList)){
                                    //设置空的就行，根本没选设备
                                    step.setLedgerIdList(stepLedgerIdList);
                                    continue;
                                }
                                //工序资源类型(匹配产线)
                                List<String> resourcesTypeIds = resourceList.stream().filter((item) -> {
                                    return item.getResourcesType() == 1;
                                }).map(ScheduleResourceType::getResourcesTypeId).collect(Collectors.toList());
                                //工步资源类型(匹配设备)
                                List<String> resourcesType1Ids = resourceList1.stream().filter((item) -> {
                                    return item.getResourcesType() == 1;
                                }).map(ScheduleResourceType::getResourcesTypeId).collect(Collectors.toList());

                                for (ScheduleLedger scheduleLedger : ledgerList) {
                                    if(resourcesTypeIds.contains(scheduleLedger.getLintStationGroup())&&resourcesType1Ids.contains(scheduleLedger.getEquipmentStationType())){
                                        stepLedgerIdList.add(scheduleLedger.getId());
                                    }
                                }
                                step.setLedgerIdList(stepLedgerIdList);
                            }
                        }

                    }else {
                       //无工步(这种是单机设备,没有产线)
                        //创建假工步
                        ScheduleStep virtualStep = new ScheduleStep();
                        //直接继承工序的时间
                        if(proc.getProcessingTime()!=null){
                            virtualStep.setProcessingTime(proc.getProcessingTime());
                        }else {
                            virtualStep.setProcessingTime(0);
                        }

                        if(ledgerIdList!=null&&ledgerIdList.size()>0){
                            //有指定特殊设备,直接就能用上
                            virtualStep.setLedgerIdList(ledgerIdList);
                        }else {
                            ledgerIdList = new ArrayList<>();
                            List<ScheduleResourceType> resourceList = proc.getResourceList();
                            if(CollectionUtils.isAnyEmpty(resourceList)){
                                //如果是空的，设置空设备即可
                                virtualStep.setLedgerIdList(ledgerIdList);
                            }else {
                                List<String> resourcesTypeIds = resourceList.stream().filter((item) -> {
                                    return item.getResourcesType() == 1;
                                }).map(ScheduleResourceType::getResourcesTypeId).collect(Collectors.toList());
                                for (ScheduleLedger scheduleLedger : ledgerList) {
                                    //用设备类型匹配
                                    if(resourcesTypeIds.contains(scheduleLedger.getEquipmentStationType())){
                                        ledgerIdList.add(scheduleLedger.getId());
                                    }
                                }
                                virtualStep.setLedgerIdList(ledgerIdList);
                            }
                        }
                        //工序添加假工步
                        //proc.setStepList(Arrays.asList(virtualStep));
                        stepList = Arrays.asList(virtualStep);
                    }
                    proc.setStepList(stepList);
                }

                //前置工步,放到工序外面,这样所有的工步才能连成一条线
                IloIntervalVar stepPrec = null;

                //这轮是排产
                for (ScheduleJobProcess proc : jobProcessList) {
                    //获取工步
                    List<ScheduleStep> stepList = proc.getStepList();
                    //工装类型
                    String gzMaterialId = null;
                    List<String> gzList = proc.getResourceList().stream().filter((item) -> {
                        return item.getResourcesType() == 3;
                    }).map(ScheduleResourceType::getResourcesTypeId).collect(Collectors.toList());
                    if(gzList.size()==1){
                        gzMaterialId = gzList.get(0);
                    }

                    //遍历工步
                    for (ScheduleStep step : stepList) {
                        //刀具类型
                        String cutterMaterialId = null;
                        if(step.getResourceList()!=null){
                            List<String> cutterList = step.getResourceList().stream().filter((item) -> {
                                return item.getResourcesType() == 2;
                            }).map(ScheduleResourceType::getResourcesTypeId).collect(Collectors.toList());
                            if(cutterList.size()==1){
                                cutterMaterialId = cutterList.get(0);
                            }
                        }


                        int mark = 0;
                        Integer processingTime = step.getProcessingTime();
                        //外协,外协没有
                        if(proc.getIsOut()==1){
                            if(scheduleJobProcess!=null&&(scheduleJobProcess.getIsOut()==1)){
                                //上一步是外协
                                processingTime = 60;
                            }else{
                                if(mark==0){
                                    //上一步不是外协(第一工步才加3天)
                                    processingTime = 3*24*60;
                                }else {
                                    processingTime = 60;
                                }
                            }
                        }

                        //工步层面决策变量,但用的工序时长作为区间变量
                        IloIntervalVar stepInterval =  cp.intervalVar(processingTime);

                        List<String> ledgerIdList = step.getLedgerIdList();

                        if(ledgerIdList.size()>0){
                            //设备工步区间集合(只是为了从中选一个)
                            IntervalVarList deviceTasks = new IntervalVarList();
                            for (String ledgerId : ledgerIdList) {
                                ScheduleLedger scheduleLedger = scheduleLedgerMap.get(ledgerId);
                                //工步时间
                                int pTime = step.getProcessingTime();
                                IloIntervalVar ti =  cp.intervalVar(pTime);
                                //这个唯一
                                if(step.getId()!=null){
                                    ti.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId()+"-"+step.getId()+"-"+ledgerId);
                                }else {
                                    ti.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId()+"-"+ledgerId);
                                }
                                //ti.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId()+"-"+step.getId()+"-"+ledgerId);
                                ti.setOptional();
                                deviceTasks.add(ti);
                                //该设备任务队列 ?设备的任务队列,为什么记录这个(这个也是重复的)
                                scheduleLedger.getMachineIntervals().add(ti);

                                //实际上是所有的工序任务队列(一个工序多个设备的也加进来了)
                                allTaskIntervals.add(ti);
                                if(gzMaterialId!=null){
                                    //TODO 工装资源限制
                                    if(resourcelimitMap.containsKey(gzMaterialId)){
                                        IloCumulFunctionExpr iloCumulFunctionExpr = cp.pulse(ti, 1);
                                        resourcelimitMap.get(gzMaterialId).add(iloCumulFunctionExpr);
                                    }else {
                                        IloCumulFunctionExpr iloCumulFunctionExpr = cp.pulse(ti, 1);
                                        resourcelimitMap.put(gzMaterialId,iloCumulFunctionExpr);
                                    }
                                }
                                if(cutterMaterialId!=null){
                                    //TODO 刀具资源限制
                                    if(resourcelimitMap.containsKey(cutterMaterialId)){
                                        IloCumulFunctionExpr iloCumulFunctionExpr = cp.pulse(ti, 1);
                                        resourcelimitMap.get(cutterMaterialId).add(iloCumulFunctionExpr);
                                    }else {
                                        IloCumulFunctionExpr iloCumulFunctionExpr = cp.pulse(ti, 1);
                                        resourcelimitMap.put(cutterMaterialId,iloCumulFunctionExpr);
                                    }
                                }
//                                IloNumToNumStepFunction iloNumToNumStepFunction = cp.numToNumStepFunction();
//                                iloNumToNumStepFunction.addValue(0,1000,1);
//                                cp.add(cp.forbidStart(ti,iloNumToNumStepFunction));

                            }
                            cp.add(cp.alternative(stepInterval, deviceTasks.toArray()));
                        }else {
                            //没有设备,要么是外协,要么是入库或者来料检验那种
                            if(step.getId()!=null){
                                stepInterval.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId()+"-"+step.getId());
                            }else {
                                stepInterval.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId());
                            }
                            allTaskIntervals.add(stepInterval);
                            step.setInnerInterval(stepInterval);
                        }

                        //重要约束,前面工序的结束时间在后面工序的开始时间之前
                        if(stepPrec!=null){
                            cp.add(cp.endBeforeStart(stepPrec,stepInterval));
                        }
                        step.setInnerInterval(stepInterval);
                        //前工步等于现工步
                        stepPrec = stepInterval;
                        //最后的工步
                        lastStepPrec = stepInterval;
                    }
                }
                //***************工步改造底******************

                //获取所有产品最后一个工序的加工时间
                ends.add(cp.endOf(lastStepPrec));
                //工序存储在任务上
                scheduleJob.setTaskSteps(jobProcessList);
            }
        }
        //求效率最大化用的
        List<IloNumExpr> sumIloIntExprList = new ArrayList<>();

        //所有的设备的任务时间不能重叠
        for (ScheduleLedger device : ledgerList) {
            IloIntervalSequenceVar deviceSequence = cp.intervalSequenceVar(device.getMachineIntervals().toArray(),device.getId());
            cp.add(cp.noOverlap(deviceSequence));

            List<IloNumExpr> endList = new ArrayList<>();
            for (IloIntervalVar machineInterval : device.getMachineIntervals()) {
                if(machineInterval.getSizeMax()>0){
                    IloNumExpr endof = cp.endOf(machineInterval);
                    endList.add(endof);
                }
            }
            if(endList.size()>0){
                IloNumExpr max = cp.max(endList.toArray(new IloNumExpr[0]));
                sumIloIntExprList.add(max);
            }
            //IloNumExpr max = cp.max(endList.toArray(new IloNumExpr[0]));
            //IloNumExpr diff = cp.diff(max, min);
            //sumIloIntExprList.add(max);
        }
        //工装约束
        for (Map.Entry<String, Integer> entry : resourceCountMap.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if(value>0){
                IloCumulFunctionExpr iloCumulFunctionExpr = resourcelimitMap.get(key);
                cp.add(cp.le(iloCumulFunctionExpr,value));
            }

        }


        //总时长最短
        cp.add(cp.minimize(cp.max(ends.toArray(new IloNumExpr[0]))));
        //效率最大,求和最小
        //cp.add(cp.minimize(cp.sum(sumIloIntExprList.toArray(new IloNumExpr[0]))));
        //cp.addLe(cp.max(ends.toArray(new IloNumExpr[0])),1440*7*4);

        cp.setParameter(IloCP.IntParam.FailLimit,10000);

        /***********************************************/

        //TODO 所以现在ti name分为四种情况
        //1.有工步的正常加工            ti.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId()+"-"+step.getId()+"-"+ledgerId);
        //2.有工步的外协              stepInterval.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId()+"-"+step.getId());
        //2.无工步(假工步)的正常加工   ti.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId()+"-"+ledgerId);
        //2.无工步(假工步)的外协         stepInterval.setName(schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId());
        BatchOrderApsResultDO batchOrderApsResult = new BatchOrderApsResultDO();
        //求解
        if (cp.solve()) {
            double objValue = cp.getObjValue();
            System.out.println(objValue);
            //存储所有的工序解
            Map<String,IloIntervalVar> cpMap = new HashMap<>();
            for (int i = 0; i < allTaskIntervals.size(); i++) {
                IloIntervalVar var = (IloIntervalVar) allTaskIntervals.get(i);
                if (cp.isPresent(var)){
                    IloIntervalVar ti = allTaskIntervals.get(i);
                    String name = ti.getName();
                    cpMap.put(name,ti);
                }
            }
            //排产开始时间
            //long beginApsTime = startTime.getTime()/60/1000;
            Instant instant = startTime.atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instant);
            long beginApsTime = date.getTime()/60/1000;
            for (SchedulePlan schedulePlan : planList) {
                List<ScheduleJob> taskJobs = schedulePlan.getTaskJobs();

                for (int i = 0; i < taskJobs.size(); i++) {
                    ScheduleJob scheduleJob = taskJobs.get(i);
                    List<ScheduleJobProcess> taskSteps = scheduleJob.getTaskSteps();
                    //System.out.println(taskSteps);
                    int count = 0;
                    for (ScheduleJobProcess proc : taskSteps) {
                        String name = "";
                        /******************* 处理分类**********************/
                        if(proc.getIsOut()==0){
                            //正常加工
                            String id = proc.getStepList().get(0).getId();
                            if(id!=null){
                                //真工步
                                List<ScheduleStep> stepList = proc.getStepList();
                                //工步计数
                                int stepCount = 0;
                                for (ScheduleStep step : stepList) {
                                    List<String> ledgerIdList = step.getLedgerIdList();
                                    for (String ledgerId : ledgerIdList) {
                                        name = schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId()+"-"+step.getId()+"-"+ledgerId;
                                        if(cpMap.containsKey(name)){
                                            step.setSelectLedgerIdList(Arrays.asList(ledgerId));
                                            proc.getSelectLedgerIdList().add(ledgerId);
                                            break;
                                        }
                                    }
                                    if(cpMap.containsKey(name)){
                                        IloIntervalVar ti = cpMap.get(name);
                                        int start = cp.getStart(ti);
                                        int end = cp.getEnd(ti);
                                        Date beginDate = new Date((beginApsTime + cp.getStart(ti)) * 60 * 1000);
                                        Date endDate = new Date((beginApsTime + cp.getEnd(ti)) * 60 * 1000);
                                        step.setPlanStartTime(beginDate);
                                        step.setPlanEndTime(endDate);
                                        //工步时间
                                        if(stepCount==0){
                                            proc.setPlanStartTime(beginDate);
                                            proc.setPlanEndTime(endDate);
                                        }else {
                                            boolean before = proc.getPlanStartTime().before(beginDate);
                                            if(!before){
                                                proc.setPlanStartTime(beginDate);
                                            }
                                            boolean after = proc.getPlanEndTime().after(endDate);
                                            if(!after){
                                                proc.setPlanEndTime(endDate);
                                            }
                                        }
                                    }
                                    stepCount++;
                                }

                            }else {
                                //假工步
                                List<ScheduleStep> stepList = proc.getStepList();
                                //假工步里只有一步
                                ScheduleStep scheduleStep = stepList.get(0);
                                List<String> ledgerIdList = scheduleStep.getLedgerIdList();
                                for (String ledgerId : ledgerIdList) {
                                    name = schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId()+"-"+ledgerId;
                                    if(cpMap.containsKey(name)){
                                        scheduleStep.setSelectLedgerIdList(Arrays.asList(ledgerId));
                                        proc.getSelectLedgerIdList().add(ledgerId);
                                        break;
                                    }
                                }
                                if(cpMap.containsKey(name)){
                                    IloIntervalVar ti = cpMap.get(name);
                                    Date beginDate = new Date((beginApsTime + cp.getStart(ti)) * 60 * 1000);
                                    Date endDate = new Date((beginApsTime + cp.getEnd(ti)) * 60 * 1000);
                                    scheduleStep.setPlanStartTime(beginDate);
                                    scheduleStep.setPlanEndTime(endDate);
                                    //工步时间
                                    proc.setPlanStartTime(beginDate);
                                    proc.setPlanEndTime(endDate);
                                }
                            }
                        }else {
                            //外协
                            String id = proc.getStepList().get(0).getId();
                            if(id!=null){
                                //真工步
                                List<ScheduleStep> stepList = proc.getStepList();
                                //工步计数
                                int stepCount = 0;
                                for (ScheduleStep step : stepList) {
                                    name = schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId()+"-"+step.getId();
                                    if(cpMap.containsKey(name)){
                                        IloIntervalVar ti = cpMap.get(name);
                                        Date beginDate = new Date((beginApsTime + cp.getStart(ti)) * 60 * 1000);
                                        Date endDate = new Date((beginApsTime + cp.getEnd(ti)) * 60 * 1000);
                                        step.setPlanStartTime(beginDate);
                                        step.setPlanEndTime(endDate);
                                        //工步时间
                                        if(stepCount==0){
                                            proc.setPlanStartTime(beginDate);
                                            proc.setPlanEndTime(endDate);
                                        }else {
                                            //工序开始时间在工步之前
                                            boolean before = proc.getPlanStartTime().before(beginDate);
                                            if(!before){
                                                proc.setPlanStartTime(beginDate);
                                            }
                                            boolean after = proc.getPlanEndTime().after(endDate);
                                            if(!after){
                                                proc.setPlanEndTime(endDate);
                                            }
                                        }
                                    }
                                    stepCount++;
                                }

                            }else {
                                //假工步
                                List<ScheduleStep> stepList = proc.getStepList();
                                ScheduleStep scheduleStep = stepList.get(0);
                                name = schedulePlan.getId()+"-"+scheduleJob.getIndex()+"-"+proc.getId();
                                if(cpMap.containsKey(name)){
                                    IloIntervalVar ti = cpMap.get(name);
                                    Date beginDate = new Date((beginApsTime + cp.getStart(ti)) * 60 * 1000);
                                    Date endDate = new Date((beginApsTime + cp.getEnd(ti)) * 60 * 1000);
                                    scheduleStep.setPlanStartTime(beginDate);
                                    scheduleStep.setPlanEndTime(endDate);
                                    //工步时间
                                    proc.setPlanStartTime(beginDate);
                                    proc.setPlanEndTime(endDate);
                                }
                            }
                        }

                        //整个任务的时间
                        if(count==0){
                            scheduleJob.setPlanStartTime(proc.getPlanStartTime());
                            scheduleJob.setPlanEndTime(proc.getPlanEndTime());
                        }else {
                            boolean before = scheduleJob.getPlanStartTime().before(proc.getPlanStartTime());
                            if(!before){
                                scheduleJob.setPlanStartTime(proc.getPlanStartTime());
                            }
                            boolean after = scheduleJob.getPlanEndTime().after(proc.getPlanEndTime());
                            if(!after){
                                scheduleJob.setPlanEndTime(proc.getPlanEndTime());
                            }
                        }
                        count++;
                    }
                    //System.out.println(scheduleJob);
                }

            }
            //System.out.println(scheduleConfig);
            //转成字符串存起来
            String apsContent = JSON.toJSONString(scheduleConfig,
                    SerializerFeature.WriteNullListAsEmpty,
                    SerializerFeature.WriteNullStringAsEmpty,
                    SerializerFeature.WriteNullNumberAsZero,
                    SerializerFeature.WriteNullBooleanAsFalse,
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat,
                    SerializerFeature.DisableCircularReferenceDetect,
                    SerializerFeature.PrettyFormat
            );
            //BatchOrderApsResultDO batchOrderApsResult = new BatchOrderApsResultDO();
            //Date startDate = new Date(beginApsTime);
            //LocalDateTime startLocalDateTime = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            batchOrderApsResult.setStartTime(startTime);
            batchOrderApsResult.setApsContent(apsContent);
            batchOrderApsResult.setStatus(1).setSubmitedTime(LocalDateTime.now());
            batchOrderApsResultMapper.insert(batchOrderApsResult);

//            for (ScheduleLedger device : ledgerList) {
//                System.out.println(device.getId());
//                for (IloIntervalVar machineInterval : device.getMachineIntervals()) {
//                    IloIntExpr startOf = cp.startOf(machineInterval);
//                    double startvalue = cp.getValue(startOf);
//                    System.out.println("startOf"+startvalue);
//                    IloIntExpr endof = cp.endOf(machineInterval);
//                    double endvalue = cp.getValue(endof);
//                    System.out.println("endOf"+endvalue);
//                }
////                if(device.getMachineIntervals().size()>0){
////                    //前后时长最小，效率最大
////                    IloNumExpr startOf = cp.startOf(device.getMachineIntervals().get(0));
////                    double startvalue = cp.getValue(startOf);
////                    System.out.println("startOf"+startvalue);
////                    IloNumExpr endof = cp.endOf(device.getMachineIntervals().get(device.getMachineIntervals().size()-1));
////                    double endvalue = cp.getValue(endof);
////                    System.out.println("endOf"+endvalue);
////                }
//            }
//            for (IloNumExpr iloNumExpr : sumIloIntExprList) {
//                System.out.println(cp.getValue(iloNumExpr));
//            }
        }
        return batchOrderApsResult.getId();
    }

    public boolean matchDeviceCalendar(List<ScheduleLedger> ledgerList,LocalDateTime startTime){
        //先判断日期，是否是工作日
        int year = startTime.getYear();
        int monthValue = startTime.getMonthValue();
        int dayOfMonth = startTime.getDayOfMonth();
        LocalTime currentTime = startTime.toLocalTime();
        String startDateStr = year+"-"+monthValue+"-"+dayOfMonth;
        List<CalendarSpecialDO> calendarSpecialDOS = calendarSpecialMapper.selectList(CalendarSpecialDO::getCsdate, startDateStr);
        if(calendarSpecialDOS.size()>0){
            CalendarSpecialDO calendarSpecialDO = calendarSpecialDOS.get(0);
            if(calendarSpecialDO.getCsname()==1){
                //等于1说明今天休。
                return false;
            }
        }else {
            //没有特殊日历，那就判断今天是周几
            int weekValue = startTime.getDayOfWeek().getValue();
            //和calendar不一样，7代表周日
            if(weekValue ==6||weekValue ==7){
                return false;
            }
        }

        //先查特殊设备日历
        //设备id
        List<String> ledgerIds = ledgerList.stream().map(ScheduleLedger::getId).collect(Collectors.toList());
        LambdaQueryWrapper<CalendarDeviceDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CalendarDeviceDO::getDate,startDateStr);
        wrapper.in(CalendarDeviceDO::getDeviceId,ledgerIds);
        List<CalendarDeviceDO> calendarDeviceDOS = calendarDeviceMapper.selectList(wrapper);
        //设备日历map
        Map<String,List<CalendarDeviceDO>> map = new HashMap<>();
        for (CalendarDeviceDO calendarDeviceDO : calendarDeviceDOS) {
            String deviceId = calendarDeviceDO.getDeviceId();
            if(map.containsKey(deviceId)){
                map.get(deviceId).add(calendarDeviceDO);
            }else {
                List<CalendarDeviceDO> list = new ArrayList<>();
                list.add(calendarDeviceDO);
                map.put(deviceId,list);
                //查到特殊日历的就不用再查普通日历了
                ledgerIds.remove(deviceId);
            }
        }

        //查询普通设备日历
        LambdaQueryWrapper<CalendarProductionlineDO> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(CalendarProductionlineDO::getDeviceId,ledgerIds);
        //关联班次
        List<CalendarProductionlineDO> calendarProductionlineDOS = new ArrayList<>();
        if (ledgerIds.size()>0){
            calendarProductionlineDOS = calendarProductionlineMapper.selectList(wrapper1);
        }
        //关联时间
        List<String> shiftIds = calendarProductionlineDOS.stream().map(CalendarProductionlineDO::getShiftId).distinct().collect(Collectors.toList());
        List<ShiftTimeDO> shiftTimeDOS = new ArrayList<>();
        if(shiftIds.size()>0){
            shiftTimeDOS = shiftTimeMapper.selectList(ShiftTimeDO::getTypeId, shiftIds);
        }

        //封装设备日历
        for (CalendarProductionlineDO calendarProductionlineDO : calendarProductionlineDOS) {
            String shiftId = calendarProductionlineDO.getShiftId();
            String deviceId = calendarProductionlineDO.getDeviceId();
            for (ShiftTimeDO shiftTimeDO : shiftTimeDOS) {
                //班次能匹配上
                if(shiftTimeDO.getTypeId().equals(shiftId)){
                    CalendarDeviceDO calendarDeviceDO = new CalendarDeviceDO();
                    String[] startSplit = shiftTimeDO.getStartTime().split(":");
                    String[] endSplit = shiftTimeDO.getEndTime().split(":");
                    LocalTime startLocalTime = LocalTime.of(Integer.valueOf(startSplit[0]),Integer.valueOf(startSplit[1]));
                    LocalTime endLocalTime = LocalTime.of(Integer.valueOf(endSplit[0]),Integer.valueOf(endSplit[1]));
                    calendarDeviceDO.setDeviceId(deviceId).setStartTime(startLocalTime).setEndTime(endLocalTime);

                    if(map.containsKey(deviceId)){
                        map.get(deviceId).add(calendarDeviceDO);
                    }else {
                        List<CalendarDeviceDO> list = new ArrayList<>();
                        list.add(calendarDeviceDO);
                        map.put(deviceId,list);
                    }
                }
            }
        }

        //判断map中key的数量
        if(map.size()<ledgerList.size()){
            //数量不够，有设备没班次
            throw exception(APS_DATE_NOT_MATCH_SHIFT);
        }
        //最后匹配一下
        for (ScheduleLedger ledger : ledgerList) {
            List<CalendarDeviceDO> list = new ArrayList<>();
            for (CalendarDeviceDO calendarDeviceDO : calendarDeviceDOS) {
                if(ledger.getId().equals(calendarDeviceDO.getDeviceId())){
                    list.add(calendarDeviceDO);
                }
            }
            //判断时间段是不是匹配上了
            boolean match = false;
            for (CalendarDeviceDO calendarDeviceDO : list) {
                LocalTime startTime1 = calendarDeviceDO.getStartTime();
                LocalTime endTime1 = calendarDeviceDO.getEndTime();
                if(currentTime.isAfter(startTime1)&&currentTime.isBefore(endTime1)){
                    match = true;
                }
            }
            //没匹配上就得抛异常了
            if(!match){
                throw exception(APS_DATE_NOT_MATCH_SHIFT);
            }
        }
        return true;
    }

    @Override
    public List<LedgerDO> getLedgerNameListByApsId(String id) {
        BatchOrderApsResultDO batchOrderApsResultDO = batchOrderApsResultMapper.selectById(id);
        String apsContent = batchOrderApsResultDO.getApsContent();
        ScheduleConfig scheduleConfig = JSON.parseObject(apsContent, ScheduleConfig.class);
        Set<String> deviceIdSet = new HashSet<>();
        for (SchedulePlan schedulePlan : scheduleConfig.getPlanList()) {
            for (ScheduleJob taskJob : schedulePlan.getTaskJobs()) {
                for (ScheduleJobProcess taskProcess : taskJob.getTaskSteps()) {
                    //排产结果中不存在无工步的情况
                    for (ScheduleStep scheduleStep : taskProcess.getStepList()) {
                        List<String> ledgerIdList = scheduleStep.getSelectLedgerIdList();
                        if (ledgerIdList != null && ledgerIdList.size() > 0) {
                            deviceIdSet.addAll(ledgerIdList);
                        }
                    }
                }
            }
        }
        if (deviceIdSet.size() == 0) return new ArrayList<>();
        LambdaQueryWrapper<LedgerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(LedgerDO::getId,deviceIdSet);
        queryWrapper.orderByAsc(LedgerDO::getLintStationGroup);
        queryWrapper.orderByAsc(LedgerDO::getEquipmentStationType);
        queryWrapper.orderByAsc(LedgerDO::getCode);
        return ledgerMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, Object> getLedgerLoadByApsId(String id) {
        BatchOrderApsResultDO batchOrderApsResultDO = batchOrderApsResultMapper.selectById(id);
        String apsContent = batchOrderApsResultDO.getApsContent();
        ScheduleConfig scheduleConfig = JSON.parseObject(apsContent, ScheduleConfig.class);
        Map<String, Object> result = new HashMap<>();
        //key: deviceId; value: plan
        Map<String,List<DeviceLoadData>> devicePlanListMap = new HashMap<>();
        for (SchedulePlan schedulePlan : scheduleConfig.getPlanList()) {
            String orderFormId = schedulePlan.getId();
            String orderNumber = schedulePlan.getNumber();
            String partNumber = schedulePlan.getPartNumber();
            int partIndex = 0;
            for (ScheduleJob taskJob : schedulePlan.getTaskJobs()) {
                partIndex++;
                for (ScheduleJobProcess taskProcess : taskJob.getTaskSteps()) {
                    String procedureNum = taskProcess.getProcedureNum();
                    //排产结果中不存在无工步的情况
                    for (ScheduleStep taskStep : taskProcess.getStepList()) {
                        List<String> selectLedgerIdList = taskStep.getSelectLedgerIdList();
                        if (selectLedgerIdList == null || selectLedgerIdList.size() == 0) continue;
                        String deviceId = selectLedgerIdList.get(0);
                        DeviceLoadData loadData = new DeviceLoadData();
                        loadData.setOrderFormId(orderFormId);
                        loadData.setOrderNumber(orderNumber);
                        loadData.setValue(taskStep.getProcessingTime()); //精确到分钟
                        loadData.setPartNumber(partNumber);
                        loadData.setProcessNumber(procedureNum);
                        loadData.setStepNumber(StringUtils.isNotBlank(taskStep.getStepNum()) ? taskStep.getStepNum() : "1");
                        loadData.setPartIndex(partIndex+"");
                        loadData.setStartTime(taskStep.getPlanStartTime());
                        loadData.setEndTime(taskStep.getPlanEndTime());
                        loadData.setShow(true);
                        if (!devicePlanListMap.containsKey(deviceId)) {
                            devicePlanListMap.put(deviceId, new ArrayList<>());
                        }
                        devicePlanListMap.get(deviceId).add(loadData);
                    }
                }
            }
        }
        ZonedDateTime zdt = scheduleConfig.getStartTime().atZone(ZoneId.systemDefault());
        long startTime = zdt.toInstant().toEpochMilli();
        //排产数据按设备分类 结构:最大任务[设备[任务]]
        List<List<DeviceLoadData>> deviceLoadDataList = new ArrayList<>();
        //当前设备计算到的时间, 计算间隔用
        Map<String,Long> deviceTimeMap = devicePlanListMap.keySet().stream().collect(Collectors.toMap(item -> item, item -> startTime, (a,b) ->b));
        if (devicePlanListMap.size() == 0) return result;
        //所有设备 注意排序
        LambdaQueryWrapper<LedgerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(LedgerDO::getId, devicePlanListMap.keySet());
        queryWrapper.orderByAsc(LedgerDO::getLintStationGroup);
        queryWrapper.orderByAsc(LedgerDO::getEquipmentStationType);
        queryWrapper.orderByAsc(LedgerDO::getCode);
        List<LedgerDO> ledgerDOList = ledgerMapper.selectList(queryWrapper);
        //已经无任务的设备集合
        Set<String> noPlanDeviceSet = new HashSet<>();
        while (noPlanDeviceSet.size() < ledgerDOList.size()) {
            List<DeviceLoadData> oneLayerList = new ArrayList<>();
            for (LedgerDO ledgerDO : ledgerDOList) {
                String deviceId = ledgerDO.getId();
                List<DeviceLoadData> deviceLoadData = devicePlanListMap.get(deviceId);
                //如果已经遍历完成
                if (deviceLoadData == null || deviceLoadData.size() == 0) {
                    noPlanDeviceSet.add(deviceId);
                    DeviceLoadData loadData = new DeviceLoadData();
                    loadData.setValue("-");
                    loadData.setShow(false);
                    oneLayerList.add(loadData);
                    continue;
                }
                long stepStartTime = deviceLoadData.get(0).getStartTime().getTime();
                //如果有时间间隔
                if (stepStartTime > deviceTimeMap.get(deviceId)) {
                    DeviceLoadData loadDataHide = new DeviceLoadData();
                    long difference = stepStartTime - deviceTimeMap.get(deviceId);
                    loadDataHide.setValue(difference/60000);
                    loadDataHide.setShow(false);
                    oneLayerList.add(loadDataHide);
                    deviceTimeMap.put(deviceId, stepStartTime);
                    continue;
                }
                DeviceLoadData loadData = deviceLoadData.remove(0);
                oneLayerList.add(loadData);
                deviceTimeMap.put(deviceId, loadData.getEndTime().getTime());
            }
            deviceLoadDataList.add(oneLayerList);
        }
        result.put("data", deviceLoadDataList);
        result.put("startTime", scheduleConfig.getStartTime());
        return result;
    }

    @Override
    public void schedulingAdopt(ScheduleConfig scheduleConfig) {
        for (SchedulePlan schedulePlan : scheduleConfig.getPlanList()) {
            String orderId = schedulePlan.getId();
            List<BatchOrderDO> batchOrderDOList = batchOrderMapper.selectList(BatchOrderDO::getOrderId, orderId);
            for (ScheduleJob taskJob : schedulePlan.getTaskJobs()) {
                BatchOrderDO batchOrderDO = batchOrderDOList.get(taskJob.getIndex());
                List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(BatchRecordDO::getBatchId, batchOrderDO.getId());
                Date batchStart = null;
                Date batchEnd = null;
                for (ScheduleJobProcess taskStep : taskJob.getTaskSteps()) {
                    Optional<BatchRecordDO> first = batchRecordDOList.stream().filter(item -> item.getProcessId().equals(taskStep.getId())).findFirst();
                    if (!first.isPresent()) continue;
                    BatchRecordDO batchRecordDO = first.get();
                    Date processStart = taskStep.getPlanStartTime();
                    Date processEnd = taskStep.getPlanEndTime();
                    if (batchStart == null || batchStart.after(processStart)) {
                        batchStart = processStart;
                    }
                    if (batchEnd == null || batchEnd.before(processEnd)) {
                        batchEnd = processEnd;
                    }
                    List<String> selectLedgerIdList = taskStep.getSelectLedgerIdList();
                    List<LedgerDO> ledgerDOList = ledgerMapper.selectBatchIds(selectLedgerIdList);
                    Set<String> unitSet = ledgerDOList.stream().map(item -> StringUtils.isNotBlank(item.getLintStationGroup()) ? item.getLintStationGroup() : item.getEquipmentStationType()).collect(Collectors.toSet());
                    batchRecordDO.setPlanStartTime(LocalDateTime.ofInstant(processStart.toInstant(), ZoneId.systemDefault()));
                    batchRecordDO.setPlanEndTime(LocalDateTime.ofInstant(processEnd.toInstant(), ZoneId.systemDefault()));
                    batchRecordDO.setProcessingUnitId(String.join(",", unitSet));
                    batchRecordDO.setDeviceId(String.join(",", selectLedgerIdList));
                    batchRecordMapper.updateById(batchRecordDO);
                    List<BatchRecordStepDO> batchRecordStepDOS = batchRecordStepMapper.selectList(BatchRecordStepDO::getBatchRecordId, batchRecordDO.getId());
                    for (ScheduleStep scheduleStep : taskStep.getStepList()) {
                        Date stepStart = scheduleStep.getPlanStartTime();
                        Date stepEnd = scheduleStep.getPlanEndTime();
                        List<String> ledgerIdList = scheduleStep.getSelectLedgerIdList();
                        Optional<BatchRecordStepDO> first1 = batchRecordStepDOS.stream().filter(item -> item.getStepId().equals(scheduleStep.getId())).findFirst();
                        if (!first1.isPresent()) continue;
                        BatchRecordStepDO batchRecordStepDO = first1.get();
                        batchRecordStepDO.setPlanStartTime(LocalDateTime.ofInstant(stepStart.toInstant(), ZoneId.systemDefault()));
                        batchRecordStepDO.setPlanStartTime(LocalDateTime.ofInstant(stepEnd.toInstant(), ZoneId.systemDefault()));
                        List<LedgerDO> stepLedgerDOList = ledgerMapper.selectBatchIds(ledgerIdList);
                        batchRecordStepDO.setDefineDeviceId(String.join(",", ledgerIdList));
                        batchRecordStepDO.setDefineDeviceNumber(stepLedgerDOList.stream().map(LedgerDO::getCode).collect(Collectors.joining(",")));
                        batchRecordStepMapper.updateById(batchRecordStepDO);
                    }
                }
                if (batchStart != null) batchOrderDO.setPlanStartTime(LocalDateTime.ofInstant(batchStart.toInstant(), ZoneId.systemDefault()));
                if (batchEnd != null) batchOrderDO.setPlanStartTime(LocalDateTime.ofInstant(batchEnd.toInstant(), ZoneId.systemDefault()));
                batchOrderMapper.updateById(batchOrderDO);
            }
        }
    }
}
