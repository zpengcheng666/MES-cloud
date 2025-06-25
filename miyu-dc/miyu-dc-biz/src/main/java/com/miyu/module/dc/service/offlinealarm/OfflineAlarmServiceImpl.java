package com.miyu.module.dc.service.offlinealarm;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.miyu.module.Influxdb.dal.Offline;
import com.miyu.module.Influxdb.service.DataCollectionService;
import com.miyu.module.Influxdb.service.OfflineService;
import com.miyu.module.convert.group.DeviceDateConvert;
import com.miyu.module.dc.controller.admin.offlinealarm.vo.OfflineAlarmPageReqVO;
import com.miyu.module.dc.controller.admin.offlinealarm.vo.OfflineAlarmResVO;
import com.miyu.module.dc.controller.admin.offlineerror.vo.OfflineErrorResVO;
import com.miyu.module.dc.dal.dataobject.devicecollect.DeviceCollectDO;
import com.miyu.module.dc.dal.dataobject.producttype.ProductTypeDO;
import com.miyu.module.dc.dal.mysql.devicecollect.DeviceCollectMapper;
import com.miyu.module.dc.dal.mysql.producttype.ProductTypeMapper;
import com.miyu.module.dc.service.devicedate.DeviceDateServiceImpl;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.miyu.cloud.Influxdb.enums.InfluxdbConstans.CORM_TYPE;
import static com.miyu.cloud.Influxdb.enums.InfluxdbConstans.OLINE_TYPE;

@Service
@Validated
public class OfflineAlarmServiceImpl implements OfflineAlarmService {

    @Resource
    private OfflineService offlineService;

    @Resource
    private DeviceDateServiceImpl deviceDateServiceImpl;

    @Resource
    private DataCollectionService dataCollectionService;

    @Resource
    private ProductTypeMapper productTypeMapper;

    @Resource
    private DeviceCollectMapper deviceCollectMapper;


    /**
     * 设备离线报警查询
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OfflineAlarmResVO> queryOlineStatusDetailPage(OfflineAlarmPageReqVO offlineAlarmPageReqVO) {

        Offline offline = offlineService.queryAlarm(offlineAlarmPageReqVO,OLINE_TYPE);

        String command = offline.getSelectSQL() + offline.getLimit();

        if(offlineAlarmPageReqVO.getCreateTime()!=null && offlineAlarmPageReqVO.getCreateTime().length >0 ){
            //转换时间
            String bTime = deviceDateServiceImpl.getTZTime(offlineAlarmPageReqVO.getCreateTime()[0]);
            String eTime = deviceDateServiceImpl.getTZTime(offlineAlarmPageReqVO.getCreateTime()[1]);
            command = offline.getSelectSQL() + " and time >= " + "'" + bTime + "'" + " and time <=" + "'" + eTime + "'" + offline.getLimit();
        }

        List<OfflineAlarmResVO> list = new ArrayList<>();
        QueryResult queryResult =  dataCollectionService.query(command);
        if(queryResult.getResults().get(0).getSeries() == null || queryResult.getResults().get(0).getSeries().isEmpty()) { return list ;}
        List<List<Object>> result = queryResult.getResults().get(0).getSeries().get(0).getValues();
        result.forEach(i->{
            OfflineAlarmResVO offlineAlarmResVO = new OfflineAlarmResVO();
            String time = i.get(0).toString().substring(0,19).replace("T"," ");
            offlineAlarmResVO.setTopicId((String) i.get(2)).setDeviceId((String) i.get(3)).setTime(time);
            list.add(offlineAlarmResVO);
        });

        //获取全部产品类型数据
        Map<String, ProductTypeDO> productTypeMap = CollectionUtils.convertMap(productTypeMapper.selectList(),ProductTypeDO::getId);
        //获取全部设备采集类型数据
        Map<String, DeviceCollectDO> collectMap =  CollectionUtils.convertMap(deviceCollectMapper.selectList(), DeviceCollectDO::getTopicId);

        return DeviceDateConvert.INSTANCE.converOfflineAlarm(list , productTypeMap , collectMap);
    }

    /**
     * 设备离线报警总数查询
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Number queryOlineStatusDetailCount(OfflineAlarmPageReqVO offlineAlarmPageReqVO) {

        Offline offline = offlineService.queryCountAlarm(offlineAlarmPageReqVO,OLINE_TYPE);

        String command = offline.getSelectSQL();

        //时间筛选
        if (offlineAlarmPageReqVO.getCreateTime() != null && offlineAlarmPageReqVO.getCreateTime().length > 0) { //时间筛选
            //转换时间
            String bTime = deviceDateServiceImpl.getTZTime(offlineAlarmPageReqVO.getCreateTime()[0]);
            String eTime = deviceDateServiceImpl.getTZTime(offlineAlarmPageReqVO.getCreateTime()[1]);
            command = offline.getSelectSQL() + " and time >= " + "'" + bTime + "'" + " and time <=" + "'" + eTime + "'";
        }

        if (dataCollectionService.query(command).getResults().get(0).getSeries() == null || dataCollectionService.query(command).getResults().get(0).getSeries().isEmpty()) {
            return null;
        } else {
            return (Number) dataCollectionService.query(command).getResults().get(0).getSeries().get(0).getValues().get(0).get(1);
        }
    }

    /**
     * 设备标准值报警查询
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OfflineAlarmResVO> queryNormStatusDetailPage(OfflineAlarmPageReqVO offlineAlarmPageReqVO) {

        Offline offline = offlineService.queryAlarm(offlineAlarmPageReqVO,CORM_TYPE);

        String command = offline.getSelectSQL() + offline.getLimit();

        if(offlineAlarmPageReqVO.getCreateTime()!=null && offlineAlarmPageReqVO.getCreateTime().length >0 ){
            //转换时间
            String bTime = deviceDateServiceImpl.getTZTime(offlineAlarmPageReqVO.getCreateTime()[0]);
            String eTime = deviceDateServiceImpl.getTZTime(offlineAlarmPageReqVO.getCreateTime()[1]);
            command = offline.getSelectSQL() + " and time >= " + "'" + bTime + "'" + " and time <=" + "'" + eTime + "'" + offline.getLimit();
        }

        List<OfflineAlarmResVO> list = new ArrayList<>();
        QueryResult queryResult =  dataCollectionService.query(command);
        if(queryResult.getResults().get(0).getSeries() == null || queryResult.getResults().get(0).getSeries().isEmpty()) { return list ;}
        List<List<Object>> result = queryResult.getResults().get(0).getSeries().get(0).getValues();
        result.forEach(i->{
            JSONArray obj = JSONObject.parseArray("["+i.get(1).toString()+"]");
            String items = JSON.toJSONString(obj, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
            OfflineAlarmResVO offlineAlarmResVO = new OfflineAlarmResVO();
            String time = i.get(0).toString().substring(0,19).replace("T"," ");
            offlineAlarmResVO.setTopicId((String) i.get(2)).setDeviceId((String) i.get(3)).setTime(time).setAlarmData(items);
            list.add(offlineAlarmResVO);
        });

        //获取全部产品类型数据
        Map<String, ProductTypeDO> productTypeMap = CollectionUtils.convertMap(productTypeMapper.selectList(),ProductTypeDO::getId);
        //获取全部设备采集类型数据
        Map<String, DeviceCollectDO> collectMap =  CollectionUtils.convertMap(deviceCollectMapper.selectList(), DeviceCollectDO::getTopicId);

        return DeviceDateConvert.INSTANCE.converOfflineAlarm(list , productTypeMap , collectMap);

    }

    /**
     * 设备标准值报警总数查询
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Number queryNormStatusDetailCount(OfflineAlarmPageReqVO offlineAlarmPageReqVO) {
        Offline offline = offlineService.queryCountAlarm(offlineAlarmPageReqVO,CORM_TYPE);

        String command = offline.getSelectSQL();

        //时间筛选
        if (offlineAlarmPageReqVO.getCreateTime() != null && offlineAlarmPageReqVO.getCreateTime().length > 0) { //时间筛选
            //转换时间
            String bTime = deviceDateServiceImpl.getTZTime(offlineAlarmPageReqVO.getCreateTime()[0]);
            String eTime = deviceDateServiceImpl.getTZTime(offlineAlarmPageReqVO.getCreateTime()[1]);
            command = offline.getSelectSQL() + " and time >= " + "'" + bTime + "'" + " and time <=" + "'" + eTime + "'";
        }

        if (dataCollectionService.query(command).getResults().get(0).getSeries() == null || dataCollectionService.query(command).getResults().get(0).getSeries().isEmpty()) {
            return null;
        } else {
            return (Number) dataCollectionService.query(command).getResults().get(0).getSeries().get(0).getValues().get(0).get(1);
        }
    }
}
