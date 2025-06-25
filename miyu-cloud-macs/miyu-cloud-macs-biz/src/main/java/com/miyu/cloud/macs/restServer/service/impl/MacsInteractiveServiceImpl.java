package com.miyu.cloud.macs.restServer.service.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.miyu.cloud.macs.restServer.service.MacsInteractiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Service
public class MacsInteractiveServiceImpl implements MacsInteractiveService {

    private final String serverName = "net-debug";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private NacosNamingService nacosNamingService;

    //获取所有服务
    @Override
    public List<Instance> getAllInstances() throws NacosException {
        return nacosNamingService.getAllInstances(serverName);
    }

    //根据设备编号查找连接的服务
    @Override
    public URI getUriByDeviceCode(String deviceCode) throws URISyntaxException {
        if (deviceCode == null || "".equals(deviceCode)) return null;
        Object mapList = redisTemplate.opsForValue().get("macDeviceInstances");
        if (mapList == null) return null;
        String uriString = ((Map<String, List<String>>) mapList).entrySet().stream().filter(entry -> entry.getValue().stream().anyMatch(deviceCode::equals)).findFirst().map(Map.Entry::getKey).orElse(null);
        return uriString == null ? null : new URI(uriString);
    }
}
