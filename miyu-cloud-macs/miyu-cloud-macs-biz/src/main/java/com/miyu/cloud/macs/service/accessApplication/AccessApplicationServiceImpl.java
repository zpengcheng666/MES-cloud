package com.miyu.cloud.macs.service.accessApplication;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.miyu.cloud.macs.api.AccessApplicationControlApi;
import com.miyu.cloud.macs.dal.dataobject.device.DeviceDO;
import com.miyu.cloud.macs.dal.dataobject.visitor.VisitorDO;
import com.miyu.cloud.macs.dal.dataobject.visitorRegion.VisitorRegionDO;
import com.miyu.cloud.macs.dal.mysql.visitor.VisitorMapper;
import com.miyu.cloud.macs.dal.mysql.visitorRegion.VisitorRegionMapper;
import com.miyu.cloud.macs.restServer.api.MacsInteractiveApi;
import com.miyu.cloud.macs.restServer.entity.MacsRestDevice;
import com.miyu.cloud.macs.restServer.service.MacsInteractiveService;
import com.miyu.cloud.macs.service.device.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.miyu.cloud.macs.controller.admin.accessApplication.vo.*;
import com.miyu.cloud.macs.dal.dataobject.accessApplication.AccessApplicationDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.macs.dal.mysql.accessApplication.AccessApplicationMapper;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.macs.enums.ErrorCodeConstants.*;

/**
 * 通行申请 Service 实现类
 *
 * @author 芋道源码
 */
@Slf4j
@Service
@Validated
public class AccessApplicationServiceImpl implements AccessApplicationService {

    @Resource
    private AccessApplicationMapper accessApplicationMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private MacsInteractiveService macsInteractiveService;
    @Autowired
    private MacsInteractiveApi macsInteractiveApi;
    @Autowired
    private DeviceService macsDeviceService;
    @Resource
    private VisitorRegionMapper visitorRegionMapper;
    @Autowired
    private VisitorMapper visitorMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    public String createAccessApplication(AccessApplicationSaveReqVO createReqVO) {
        // 插入
        AccessApplicationDO accessApplication = BeanUtils.toBean(createReqVO, AccessApplicationDO.class);
        accessApplicationMapper.insert(accessApplication);

        // 插入子表
        createVisitorRegionList(accessApplication.getId(), createReqVO.getVisitorRegions());
        // 返回
        return accessApplication.getId();
    }

    private void createVisitorRegionList(String applicationId, List<VisitorRegionDO> list) {
        List<VisitorRegionDO> saveList = new ArrayList<>();
        for (VisitorRegionDO visitorRegion : list) {
            visitorRegion.setApplicationId(applicationId);
            List<String> regionIds = visitorRegion.getRegionIds();
            if (regionIds.size() > 1) {
                for (String regionId : regionIds) {
                    VisitorRegionDO visitorRegionDO = new VisitorRegionDO(visitorRegion);
                    visitorRegionDO.setRegionId(regionId);
                    saveList.add(visitorRegionDO);
                }
            } else if (regionIds.size() == 1){
                saveList.add(visitorRegion.setRegionId(regionIds.get(0)));
            }
        }
        visitorRegionMapper.insertBatch(saveList);
    }

    @Override
    public void updateAccessApplication(AccessApplicationSaveReqVO updateReqVO) {
        // 校验存在
        validateAccessApplicationExists(updateReqVO.getId());
        // 更新
        AccessApplicationDO updateObj = BeanUtils.toBean(updateReqVO, AccessApplicationDO.class);
        accessApplicationMapper.updateById(updateObj);
    }

    @Override
    public void deleteAccessApplication(String id) {
        // 校验存在
        validateAccessApplicationExists(id);
        // 删除
        accessApplicationMapper.deleteById(id);
    }

    private void validateAccessApplicationExists(String id) {
        if (accessApplicationMapper.selectById(id) == null) {
            throw exception(ACCESS_APPLICATION_NOT_EXISTS);
        }
    }

    @Override
    public AccessApplicationDO getAccessApplication(String id) {
        return accessApplicationMapper.selectById(id);
    }

    @Override
    public PageResult<AccessApplicationDO> getAccessApplicationPage(AccessApplicationPageReqVO pageReqVO) {
        return accessApplicationMapper.selectPage(pageReqVO);
    }

    @PostConstruct
    private void init() {
        redisTemplate.delete("macDeviceInstances");
        //获取所有服务,分配设备
        Map<String, List<String>> deviceInstances = getDeviceInstances();
        //按分配连接设备
        connectionInit(deviceInstances);
    }

    @Override
    public void reset() {
        init();
    }

    @Override
    public void resetDeviceConnection() {
        Map<String, List<String>> deviceInstances = this.currentDeviceInstancesGet();
        connectionInit(deviceInstances);
    }

    //添加新服务,重新分配设备
    @Override
    public boolean reconnectServer(String ip, String port) {
        String uri = "http://" + ip + ":" + port;
        Map<String, List<String>> deviceInstances = this.currentDeviceInstancesGet();
        List<String> deviceCodes = deviceInstances.get(uri);
        if (deviceCodes == null) {
            return deviceAssignment(deviceInstances, uri);
        } else {
            List<MacsRestDevice> deviceList = macsDeviceService.getListForConnection(deviceCodes);
            try {
                return macsInteractiveApi.connectionInit(URI.create(uri), deviceList);
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return false;
            }
        }
    }

    //服务销毁,回收已分配的设备
    @Override
    public boolean serverDestroy(String uri) {
        Map<String, List<String>> deviceInstances = this.currentDeviceInstancesGet();
        List<String> deviceCodes = deviceInstances.get(uri);
        deviceInstances.remove(uri);
        if (deviceCodes != null && deviceCodes.size() > 0) {
            return deviceAssignment(deviceInstances, deviceCodes);
        }
        return true;
    }

    //获取分配结果
    @Override
    public Map<String, List<String>> currentDeviceInstancesGet() {
        Object mapList =  redisTemplate.opsForValue().get("macDeviceInstances");
        if (mapList == null) {
            return new HashMap<>();
        }
        return (Map<String, List<String>>) mapList;
    }

    @Override
    public boolean removeDevice(String deviceCode) throws NacosException {
        if (deviceCode == null || "".equals(deviceCode)) return false;
        Map<String, List<String>> deviceInstances = this.currentDeviceInstancesGet();
        flag: for (Map.Entry<String, List<String>> entry : deviceInstances.entrySet()) {
            List<String> codes = entry.getValue();
            for (String code : codes) {
                if (deviceCode.equals(code)) {
                    codes.remove(code);
                    break flag;
                }
            }
        }
        connectionInit(deviceInstances);
        return true;
    }

    //添加设备后,更新设备分配,连接
    @Override
    public boolean addDevices(List<String> deviceCodes) {
        Map<String, List<String>> deviceInstances = this.currentDeviceInstancesGet();
        return deviceAssignment(deviceInstances, deviceCodes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createVisitorApplication(AccessApplicationSaveReqVO createReqVO) {
        // 插入
        AccessApplicationDO accessApplication = BeanUtils.toBean(createReqVO, AccessApplicationDO.class);
        accessApplicationMapper.insert(accessApplication);
        String applicationId = accessApplication.getId();
        List<VisitorRegionDO> visitorRegions = createReqVO.getVisitorRegions();
        List<VisitorRegionDO> saveList = new ArrayList<>();
        for (VisitorRegionDO visitorRegion : visitorRegions) {
            visitorRegion.setApplicationId(applicationId);
            VisitorDO visitor = visitorRegion.getVisitor();
            if (visitor.getIdCard() != null) {
                if (visitor.getId() == null) {
                    List<VisitorDO> list = visitorMapper.selectList(new QueryWrapper<VisitorDO>().eq("id_card", visitor.getIdCard()));
                    if (list.size() > 0) visitor.setId(list.get(0).getId());
                }
                if (visitor.getSex() == null) {
                    String idCard = visitor.getIdCard();
                    if (idCard.length() > 2) {
                        int num = Integer.parseInt(String.valueOf(idCard.charAt(idCard.length() - 2)));
                        visitor.setSex(num%2>0?1:2);
                    }
                }
            }
            if (visitor.getId() != null) {
                visitorMapper.updateById(visitor);
            } else {
                visitorMapper.insert(visitor);
            }
            visitorRegion.setVisitorId(visitor.getId());
            List<String> regionIds = visitorRegion.getRegionIds();
            if (regionIds.size() > 1) {
                for (String regionId : regionIds) {
                    VisitorRegionDO visitorRegionDO = new VisitorRegionDO(visitorRegion);
                    visitorRegionDO.setRegionId(regionId);
                    saveList.add(visitorRegionDO);
                }
            } else if (regionIds.size() == 1){
                saveList.add(visitorRegion.setRegionId(regionIds.get(0)));
            }
        }
        visitorRegionMapper.insertBatch(saveList);

        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        String processInstanceId = processInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(),
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(AccessApplicationControlApi.PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(accessApplication.getId())).getCheckedData();

        accessApplication.setProcessInstanceId(processInstanceId);
        accessApplicationMapper.updateById(accessApplication);
        return accessApplication.getId();
    }

    //分配设备 平均分配
    @NotNull
    private Map<String, List<String>> getDeviceInstances(){
        Map<String, List<String>> deviceInstances = new HashMap<>();
        try {
            List<Instance> instances = macsInteractiveService.getAllInstances();
            if (instances.size() == 0) return deviceInstances;
            List<DeviceDO> deviceList = macsDeviceService.getAllAvailableDevices();
            for (int i = 0; i < deviceList.size();) {
                for (Instance instance : instances) {
                    String uri = "http://" + instance.getIp() + ":" + instance.getPort();
                    if (!deviceInstances.containsKey(uri)) {
                        deviceInstances.put(uri, new ArrayList<>());
                    }
                    deviceInstances.get(uri).add(deviceList.get(i).getCode());
                    if (++i >= deviceList.size()) break;
                }
            }
        } catch (NacosException ignored) {}
        return deviceInstances;
    }

    //发送连接设备信息
    private void connectionInit(Map<String, List<String>> deviceInstances) {
        synchronized("MacsConnectionInit") {
            deviceInstancesSave(deviceInstances);
            for (Map.Entry<String, List<String>> entry : deviceInstances.entrySet()) {
                String uri = entry.getKey();
                List<String> deviceCodes = entry.getValue();
                List<MacsRestDevice> deviceList = macsDeviceService.getListForConnection(deviceCodes);
                try {
                    macsInteractiveApi.connectionInit(URI.create(uri), deviceList);
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                    redisTemplate.delete("macDeviceInstances");
                }
            }
        }
    }

    //保存分配结果
    private void deviceInstancesSave(Map<String, List<String>> deviceInstances) {
        redisTemplate.opsForValue().set("macDeviceInstances",deviceInstances);
    }

    //分配设备 回收设备重新分配
    private boolean deviceAssignment(Map<String, List<String>> deviceInstances, List<String> deviceCodes) {
        synchronized("MacsDeviceAssignment") {
            if (deviceInstances.size() == 0) {
                reset();
                return true;
            }
            try {
                List<Instance> instances = macsInteractiveService.getAllInstances();
                if (instances.size() == 0) {
                    reset();
                    return false;
                }
                for (String deviceCode : deviceCodes) {
                    Instance instance = getServerWithLeastConnections(instances, deviceInstances);
                    if (instance == null) return false;
                    deviceInstances.get("http://" + instance.getIp() + ":" + instance.getPort()).add(deviceCode);
                }
                connectionInit(deviceInstances);
            } catch (NacosException e) {
                return false;
            }
            return true;
        }
    }

    //获取连接数最少得服务
    private Instance getServerWithLeastConnections(List<Instance> instances, Map<String, List<String>> stringListMap) {
        Instance result = null;
        int count = 99999;
        for (Instance instance : instances) {
            String uri = "http://" + instance.getIp() + ":" + instance.getPort();
            if (!stringListMap.containsKey(uri)) {
                stringListMap.put(uri, new ArrayList<>());
                return instance;
            }
            if (stringListMap.get(uri).size() == 0) {
                return instance;
            }
            int size = stringListMap.get(uri).size();
            if (count > size) {
                count = size;
                result = instance;
            }
        }
        return result;
    }

    //分配设备 添加服务重新分配
    private boolean deviceAssignment(Map<String, List<String>> deviceInstances, String uri) {
        synchronized("MacsDeviceAssignment") {
            if (deviceInstances.size() == 0) {
                reset();
                return true;
            }
            try {
                List<Instance> instances = macsInteractiveService.getAllInstances();
                if (instances.size() == 0) {
                    reset();
                    return false;
                }
                List<DeviceDO> deviceList = macsDeviceService.getAllAvailableDevices();
                int size = deviceList.size() / instances.size();
                List<String> newDeviceInstance = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    List<String> deviceCodes = getDeviceFromServerWithMostConnections(deviceInstances);
                    if (deviceCodes == null || deviceCodes.size() == 0 || deviceCodes.size() < size) return false;
                    if (deviceCodes.size() <= size) break;
                    newDeviceInstance.add(deviceCodes.remove(deviceCodes.size() - 1));
                }
                deviceInstances.put(uri, newDeviceInstance);
                if (size == 0) return true;
                connectionInit(deviceInstances);
            } catch (NacosException e) {
                return false;
            }
            return true;
        }
    }

    //获取连接数最多得服务的连接设备编码
    private List<String> getDeviceFromServerWithMostConnections(Map<String, List<String>> deviceInstances) {
        List<String> result = null;
        int count = 0;
        for (List<String> deviceCodes : deviceInstances.values()) {
            if (deviceCodes.size() > count) {
                count = deviceCodes.size();
                result = deviceCodes;
            }
        }
        return result;
    }

    @Override
    public void updateApplicationStatus(String applicationId, Integer status) {
        accessApplicationMapper.update(new UpdateWrapper<AccessApplicationDO>().eq("id",applicationId).set("status", status));
    }

    @Override
    public List<AccessApplicationDO> listByVisitor(String id) {
        return accessApplicationMapper.selectListByVisitor(id);
    }

    @Override
    public List<AccessApplicationDO> effectiveListByVisitor(String visitorId) {
        return accessApplicationMapper.effectiveListByVisitor(visitorId);
    }
}
