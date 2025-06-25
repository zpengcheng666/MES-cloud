package com.miyu.cloud.macs.restServer.service;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public interface MacsInteractiveService {

    List<Instance> getAllInstances() throws NacosException;

    URI getUriByDeviceCode(String deviceCode) throws URISyntaxException;
}
