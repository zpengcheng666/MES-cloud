package com.miyu.module.dc.service.devicedate;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;


import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import cn.iocoder.yudao.module.system.api.notify.NotifyMessageSendApi;
import cn.iocoder.yudao.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import cn.iocoder.yudao.module.system.api.permission.UserRoleApi;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.com.google.gson.GsonBuilder;
import com.miyu.cloud.dc.api.devicedate.dto.CommonDevice;
import com.miyu.cloud.dms.api.ledger.LedgerApi;
import com.miyu.cloud.dms.api.ledger.dto.LedgerDataResDTO;
import com.miyu.module.Influxdb.service.DataCollectionService;
import com.miyu.module.dc.dal.dataobject.collectattributes.CollectAttributesDO;
import com.miyu.module.dc.dal.dataobject.devicecollect.DeviceCollectDO;

import com.miyu.module.dc.dal.dataobject.producttype.ProductTypeDO;
import com.miyu.module.dc.dal.mysql.collectattributes.CollectAttributesMapper;
import com.miyu.module.dc.dal.mysql.device.DeviceMapper;
import com.miyu.module.dc.dal.mysql.devicecollect.DeviceCollectMapper;
import com.miyu.module.dc.dal.mysql.producttype.ProductTypeMapper;
import com.miyu.module.dc.service.device.DeviceServiceImpl;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.miyu.cloud.Influxdb.enums.InfluxdbConstans.*;
import static com.miyu.cloud.dc.enums.ErrorCodeConstants.*;

@Service
@Validated
public class DeviceDateServiceImpl {

//    @Value("${spring.mqtt.host}")
//    private String host;

    private String reg = "^-?[0-9]+(.[0-9]+)?$";

    private static final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Resource
    private DataCollectionService dataCollectionService;

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private DeviceCollectMapper deviceCollectMapper;

    @Resource
    private ProductTypeMapper productTypeMapper;

    @Resource
    private CollectAttributesMapper collectAttributesMapper;

    @Resource
    private NotifyMessageSendApi notifyMessageSendApi;

    @Resource
    private LedgerApi ledgerApi;


    /**
     * mqtt数据采集业务
     */
    @Transactional(rollbackFor = Exception.class)
    @TenantIgnore
    public void insertDeviceDate(String topicId, String code) {
        Integer commType = 1;
        //获取消息体内容中的数据
        CommonDevice commonDevice = JSONObject.parseObject(code,CommonDevice.class);
        this.insertTopicData(commonDevice,topicId,commType);
    }



    /**
     * 获取系统时间为国际标准时间 将国际标准时间转为中国时间
     * @param time
     * @return
     */
    public Long getTimeLong(Long time){
        //国际标准时间对比中国时间时差为8小时 需增加8小时保持一致
        //8小时 =8×60=480分 =8×60×60=28800秒 =8×60×60×1000 =28800000毫秒
        time = time + (8*60*60*1000);
        return time;
    }

    /**
     * 时间格式转换为Influxdb可查询时间格式
     */
    public String getTZTime(LocalDateTime time){
        //Influxdb 可查询时间格式为TZ 需进行转换
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).replace(" ", "T")+"Z";
    }

    /**
     * 校验查询是否存在符合topic_id
     */
    public DeviceCollectDO exitsTopicId(String topicId , Integer commType ,String deviceId){
        String device = deviceMapper.getIdByDeviceId(deviceId);
        List<DeviceCollectDO> list = deviceCollectMapper.queryDeviceCollectByTopic(topicId,device);
        if(list.size() != 1 ){
            log.info(DEVICE_TOPIC_ERROR.getMsg());
            return null;
        }
        else {
            if (deviceMapper.selectDeviceById(list.get(0).getDeviceId()).getCommType() != commType) {
                log.info(DEVICE_COMMTYPE_ERROR.getMsg());
                return null;
            }
            else {
                return list.get(0);
            }
        }
    }

    /**
     * 数据同步存储业务逻辑
     */
    @Transactional(rollbackFor = Exception.class)
    @TenantIgnore
    public void insertTopicData(CommonDevice commonDevice,String topicId,Integer commType){

        //解析传入数据值
        Map<String,Object> map = commonDevice.getData();
        String deviceId = map.get("id").toString();
        Map<String,Object> items = JSONObject.parseObject(map.get("items").toString().replace("[","").replace("]",""));

        //校验查询是否存在符合topic_id
        DeviceCollectDO deviceCollectDO = this.exitsTopicId(topicId, commType , deviceId);

        if(deviceCollectDO != null) {

            Long time = this.getTimeLong(System.currentTimeMillis());

            //获取采集方式
            ProductTypeDO productTypeDO = productTypeMapper.selectProductTypeById(deviceCollectDO.getProductTypeId());

            //获取产品
            List<CollectAttributesDO> list = collectAttributesMapper.getListByProductTypeId(deviceCollectDO.getProductTypeId());

            //获取所有采集项
            List<String> attributesValue = list.stream().map(CollectAttributesDO::getCollectAttributesValue).collect(Collectors.toList());

            //获取所有传入项
            List<String> keyList = new ArrayList<>(items.keySet());

            //校验不同采集标准
            switch (productTypeDO.getCollectAttributesType()) {

                case 1:   //严格 传入的数据必须和规定的一致否则进入失败表 任何一项不符合标准值报警
                    if (new HashSet<>(attributesValue).containsAll(keyList) && new HashSet<>(keyList).containsAll(attributesValue)) {
                        this.exitNum(deviceCollectDO, list, deviceId, items, time, productTypeDO, commonDevice);
                    } else {
                        //插入失败表
                        insertCheckError(deviceId, productTypeDO.getTopicId(), deviceCollectDO.getTopicId(), time, commonDevice);
                    }
                    break;

                case 2:   //过滤 传入的数据可以多不可以少否则进入失败表 任何一项不符合标准值报警
                    if (new HashSet<>(keyList).containsAll(attributesValue)) {
                        this.exitNum(deviceCollectDO, list, deviceId, items, time, productTypeDO, commonDevice);
                    } else {
                        //插入失败表
                        insertCheckError(deviceId, productTypeDO.getTopicId(), deviceCollectDO.getTopicId(), time, commonDevice);
                    }
                    break;

                case 3:   //不过滤 传入的数据只要包含一项就算成功 一项都不包含定义为失败
                    final Integer[] number = {0};
                    attributesValue.forEach(i -> {
                        if (new HashSet<>(keyList).contains(i)) {
                            number[0] = number[0] + 1;
                        }
                    });
                    if (number[0] > 0) {
                        this.exitNum(deviceCollectDO, list, deviceId, items, time, productTypeDO, commonDevice);
                    } else {
                        //插入失败表
                        insertCheckError(deviceId, productTypeDO.getTopicId(), deviceCollectDO.getTopicId(), time, commonDevice);
                    }
                    break;
            }
        }
    }


    /**
     * 数据体格式校验
     */
    @TenantIgnore
    public void exitNum(DeviceCollectDO deviceCollectDO ,List<CollectAttributesDO> list,String deviceId,Map<String,Object> items , Long time , ProductTypeDO productTypeDO,CommonDevice commonDevice){

        //校验是否符合标准值
        Map<String,Object> map = this.alarmNorm(list,items,deviceId,time,deviceCollectDO);
        //插入成功表
        if(map!=null){
            insertCheck(map,deviceId,productTypeDO.getTopicId(),deviceCollectDO.getTopicId(),time,commonDevice);
        }
        else {
            insertCheckError(deviceId,productTypeDO.getTopicId(),deviceCollectDO.getTopicId(),time,commonDevice);
        }

    }


    /**
     * 标准值校验
     */
    @TenantIgnore
    public Map<String,Object> alarmNorm(List<CollectAttributesDO> list,Map<String,Object> items,String deviceId,Long time,DeviceCollectDO deviceCollectDO){
        //记录采集成功数据表使用
        Map<String,Object> map = new HashMap<>();
        //存储标准值异常使用
        Map<String,String> alarmData = new HashMap<>();
        //校验是否符合标准值
        list.forEach(i->{
                items.forEach((k,v)->{
                    //标准值匹配
                    if(i.getCollectAttributesValue().equals(k)){
                        if(i.getCollectAttributesIlk()==1) {
                            if(v.toString().matches(reg)) {
                                BigDecimal value = new BigDecimal(v.toString());
                                if (value.compareTo(i.getCollectAttributesUpper()) == 1 || value.compareTo(i.getCollectAttributesFloor()) == -1) {
                                    alarmData.put(k, v.toString());
                                }
                                map.put(k, v);
                            }else {
                                alarmData.put(k, v.toString());
                                map.put(k, v);
                            }
                        }
                        if(i.getCollectAttributesIlk()==2) {
                            if(!i.getCollectAttributesNorm().equals(v.toString())){
                                alarmData.put(k, v.toString());
                            }
                            map.put(k, v);
                        }
                        if(i.getCollectAttributesIlk()==3) {
                            if(!Arrays.asList(i.getCollectAttributesEnum().split("\\s+")).contains(v.toString())){
                                alarmData.put(k, v.toString());
                            }
                            map.put(k, v);
                        }
                    }
                });
        });
        DeviceCollectDO updateDeviceCollect = deviceCollectMapper.queryDeviceByTopicId(deviceCollectDO.getTopicId());
        if(!alarmData.isEmpty()){
            //触发站内信报警
            NotifySendSingleToUserReqDTO notifySendSingleToUserReqDTO = new NotifySendSingleToUserReqDTO();
            Map<String, Object> templateParams = new HashMap<>();
            //站内信
            LedgerDataResDTO ledger = ledgerApi.getNameByDeviceId(deviceId).getData();
            templateParams.put("devicename",ledger.getCode()+" "+ledger.getName());
            templateParams.put("producttypename",productTypeMapper.selectProductTypeById(deviceCollectDO.getProductTypeId()).getProductTypeName());

            List<String> userIds = ledger.getUsers();
            userIds.add(ledger.getSuperintendent());
            userIds.forEach(o->{
                notifySendSingleToUserReqDTO.setUserId(Long.valueOf(o)).setTemplateCode(NORM_ALARM).setTemplateParams(templateParams);
                notifyMessageSendApi.sendSingleMessageToAdmin(notifySendSingleToUserReqDTO);
            });

            //触发标准值报警
            this.insertAlarm(alarmData,deviceId,deviceCollectDO.getTopicId(),time,CORM_TYPE);
            //更新标准值状态 + 在线状态
            updateDeviceCollect.setOnlineStatus(1).setNormStatus(2);
            deviceCollectMapper.updateById(updateDeviceCollect);
        }else {
            //更新标准值状态 + 在线状态
            updateDeviceCollect.setOnlineStatus(1).setNormStatus(1);
            deviceCollectMapper.updateById(updateDeviceCollect);
        }
        return map;
    }

    /**
     * Influxdb插入采集成功数据
     */
    public void insertCheck(Map<String,Object> items,String deviceId,String tableId,String topicId,Long time,CommonDevice commonDevice){
        //时序库数据同步+时序库日志表记录日志
        Map<String,String> map = new HashMap<>();
        map.put(DEVICE_ID,deviceId);
        map.put(TOPIC_ID,topicId);
        map.put(STATUSTYPE,SUCCESS);
        items.put(DATA,commonDevice.getData().get("items").toString());
        dataCollectionService.insert(tableId,time,map,items);
    }

    /**
     * Influxdb插入采集失败数据
     */
    public void insertCheckError(String deviceId,String tableId,String topicId,Long time,CommonDevice commonDevice){
        //时序库数据同步+时序库日志表记录日志
        Map<String,String> map = new HashMap<>();
        map.put(DEVICE_ID,deviceId);
        map.put(TOPIC_ID,topicId);
        map.put(STATUSTYPE,FAIL);
        Map<String ,Object> items = new HashMap<>();
        items.put(DATA,commonDevice.getData().get("items").toString());
        dataCollectionService.insert(tableId,time,map,items);
    }

    /**
     * 标准值报警插入时序库
     */
    public void insertAlarm(Map<String,String> alarmData, String deviceId, String topicId, Long time, Integer type){
        //后续加入短信等第三方接口位置->
        Map<String,String> map = new HashMap<>();
        map.put(DEVICE_ID,deviceId);
        map.put(TOPIC_ID,topicId);
        map.put(AlARM_TYPE, String.valueOf(type));
        Map<String,Object> items = new HashMap<>();
        items.put(ALARM_DATA,new GsonBuilder().enableComplexMapKeySerialization().create().toJson(alarmData));
        dataCollectionService.insert(ALARM_DATABASE,time,map,items);
    }


}
