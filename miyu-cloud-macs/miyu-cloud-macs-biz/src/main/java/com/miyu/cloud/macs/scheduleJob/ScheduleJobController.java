package com.miyu.cloud.macs.scheduleJob;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.miyu.cloud.macs.dal.dataobject.device.DeviceDO;
import com.miyu.cloud.macs.restServer.service.MacsInteractiveService;
import com.miyu.cloud.macs.restServer.service.MacsRestService;
import com.miyu.cloud.macs.service.accessApplication.AccessApplicationService;
import com.miyu.cloud.macs.service.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ScheduleJobController {

    @Autowired
    private AccessApplicationService accessControlSystemService;
    @Autowired
    private MacsInteractiveService macsInteractiveService;
    @Autowired
    private DeviceService macsDeviceService;
    @Autowired
    private MacsRestService macsRestService;

    @Scheduled(fixedDelay = 30000)
    public void checkInstances() throws NacosException {
        List<Instance> instances = macsInteractiveService.getAllInstances();
        Map<String, List<String>> deviceInstances = accessControlSystemService.currentDeviceInstancesGet();
        Map<String, Instance> map = instances.stream().collect(Collectors.toMap(instance -> "http://" + instance.getIp() + ":" + instance.getPort(), instance -> instance, (a, b) -> b));
        for (String uri : deviceInstances.keySet()) {
            if (map.remove(uri) == null) {
                accessControlSystemService.serverDestroy(uri);
            }
        }
        for (Instance instance : map.values()) {
            accessControlSystemService.reconnectServer(instance.getIp(), ""+instance.getPort());
        }
    }

    @Scheduled(fixedDelay = 30000)
    public void checkDevices() throws NacosException {
        List<DeviceDO> devices = macsDeviceService.getAllAvailableDevices();
        List<String> deviceCodes = devices.stream().map(DeviceDO::getCode).collect(Collectors.toList());
        Map<String, List<String>> deviceInstances = accessControlSystemService.currentDeviceInstancesGet();
        for (List<String> list : deviceInstances.values()) {
            for (String code : list) {
                if (!deviceCodes.remove(code)) {
                    accessControlSystemService.removeDevice(code);
                }
            }
        }
        if (deviceCodes.size() > 0) {
            accessControlSystemService.addDevices(deviceCodes);
        }
    }

    @Scheduled(fixedDelay = 600000)
    public void checkUserAndVisitor() throws NacosException, URISyntaxException {
        macsRestService.checkUserAndVisitor();
    }
}
