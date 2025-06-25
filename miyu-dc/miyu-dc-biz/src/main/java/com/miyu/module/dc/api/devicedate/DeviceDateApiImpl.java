package com.miyu.module.dc.api.devicedate;

import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.miyu.cloud.dc.api.devicedate.dto.CommonDevice;
import com.miyu.cloud.dc.api.devicedate.DeviceDateApi;
import com.miyu.module.dc.service.devicedate.DeviceDateServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class DeviceDateApiImpl implements DeviceDateApi {

    @Resource
    DeviceDateServiceImpl deviceDateServiceImpl;

    /**
     * restful数据采集业务
     * @param commonDevice
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertDeviceDate(CommonDevice commonDevice , String topicId) {
        Integer commType = 2;
        //解析传入数据值
        String json = JSON.toJSONString(commonDevice);
        CommonDevice commonDevices = JSONObject.parseObject(json,CommonDevice.class);
        deviceDateServiceImpl.insertTopicData(commonDevices,topicId,commType);
    }

}
