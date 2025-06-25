package com.miyu.module.dc.service.offlinecollect;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.miyu.module.Influxdb.dal.Offline;
import com.miyu.module.Influxdb.service.DataCollectionService;
import com.miyu.module.Influxdb.service.OfflineService;
import com.miyu.module.convert.group.DeviceDateConvert;
import com.miyu.module.dc.controller.admin.offlinecollect.vo.OfflineCollectPageReqVO;
import com.miyu.module.dc.controller.admin.offlinecollect.vo.OfflineCollectResVO;
import com.miyu.module.dc.dal.dataobject.devicecollect.DeviceCollectDO;
import com.miyu.module.dc.dal.dataobject.producttype.ProductTypeDO;
import com.miyu.module.dc.dal.mysql.device.DeviceMapper;
import com.miyu.module.dc.dal.mysql.devicecollect.DeviceCollectMapper;
import com.miyu.module.dc.dal.mysql.producttype.ProductTypeMapper;
import com.miyu.module.dc.service.devicedate.DeviceDateServiceImpl;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;

import static com.miyu.cloud.Influxdb.enums.InfluxdbConstans.CORM_TYPE;
import static com.miyu.cloud.Influxdb.enums.InfluxdbConstans.TOPIC_ID;

@Service
@Validated
public class OfflineCollectServiceImpl implements OfflineCollectService {

    @Resource
    private DeviceCollectMapper deviceCollectMapper;

    @Resource
    private ProductTypeMapper productTypeMapper;

    @Resource
    private OfflineService offlineService;

    @Resource
    private DeviceDateServiceImpl deviceDateServiceImpl;

    @Resource
    private DataCollectionService dataCollectionService;

    @Resource
    private DeviceMapper deviceMapper;

    /**
     * 采集日志查询
     * @param reqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OfflineCollectResVO> queryCollectList(OfflineCollectPageReqVO reqVO) {

        //获取查询表名
        DeviceCollectDO deviceCollectDO = deviceCollectMapper.queryDeviceByTopicId(reqVO.getTopicId());
        reqVO.setDeviceId(deviceMapper.selectById(deviceCollectDO.getDeviceId()).getDeviceId());
        String table = productTypeMapper.getTopicIdByProductTypeId(deviceCollectDO.getProductTypeId());

        //生成基础数据
        Offline offline = offlineService.queryOffline(table,reqVO);

        //拼接查询语句
        //无条件
        String command = offline.getSelectSQL() + offline.getLimit();

        //tag筛选
        if(reqVO.getTopicId()!=null) {
            command = offline.getSelectSQL() + " and \"" + TOPIC_ID + "\" = '" + reqVO.getTopicId() + "'" + offline.getLimit();
        }
        //时间筛选
        if(reqVO.getCreateTime()!=null && reqVO.getCreateTime().length >0 ){ //时间筛选
            //转换时间
            String bTime = deviceDateServiceImpl.getTZTime(reqVO.getCreateTime()[0]);
            String eTime = deviceDateServiceImpl.getTZTime(reqVO.getCreateTime()[1]);
            command = offline.getSelectSQL() + " and time >= " + "'" + bTime + "'" + " and time <=" + "'" + eTime + "'" + offline.getLimit();
        }

        //调取查询接口并获取返回值
        List<OfflineCollectResVO> list = new ArrayList<>();
        QueryResult queryResult =  dataCollectionService.query(command);
        //当不存在查询数据时返回空对象
        if(queryResult.getResults().get(0).getSeries() == null || queryResult.getResults().get(0).getSeries().isEmpty()) { return list ;}
        //当存在查询数据时返回数据
        List<List<Object>> result = queryResult.getResults().get(0).getSeries().get(0).getValues();
        result.forEach(i->{
            JSONArray object = JSONObject.parseArray(i.get(3).toString());
            String items = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
            //获取objs数据
            List<Object> obsList =new ArrayList<>();
            obsList.add(items);
            List<Map<String,String>> list1 = new ArrayList<>();
            obsList.forEach(l->{
                Map<String,String> map = new HashMap<>();
                map.put("item",l.toString());
                list1.add(map);
            });
            OfflineCollectResVO offlineCollectResVO = new OfflineCollectResVO();
            String time = i.get(0).toString().substring(0,19).replace("T"," ");
            offlineCollectResVO.setTopicId((String) i.get(2)).setTime(time).setItems(items).setObjs(list1).setDeviceId((String) i.get(1));
            list.add(offlineCollectResVO);
        });

        //获取全部产品类型数据
        Map<String, ProductTypeDO> productTypeMap = CollectionUtils.convertMap(productTypeMapper.selectList(),ProductTypeDO::getId);
        //获取全部设备采集类型数据
        Map<String,DeviceCollectDO> collectMap =  CollectionUtils.convertMap(deviceCollectMapper.selectList(), DeviceCollectDO::getTopicId);

        return DeviceDateConvert.INSTANCE.converOfflineCollect(list,productTypeMap,collectMap);
    }


    /**
     * 查询数据总数
     * @param reqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Number queryCollectCount(OfflineCollectPageReqVO reqVO) {

        //获取查询表名
        DeviceCollectDO deviceCollectDO = deviceCollectMapper.queryDeviceByTopicId(reqVO.getTopicId());
        reqVO.setDeviceId(deviceMapper.selectById(deviceCollectDO.getDeviceId()).getDeviceId());
        String table = productTypeMapper.getTopicIdByProductTypeId(deviceCollectDO.getProductTypeId());

        //生成基础数据
        Offline offline = offlineService.queryCountOffline(table, reqVO);

        //拼接查询语句
        //无条件
        String command = offline.getSelectSQL();

        //tag筛选
        if (reqVO.getTopicId() != null) {
            command = offline.getSelectSQL() + " and \"" + TOPIC_ID + "\" = '" + reqVO.getTopicId() + "'";
        }
        //时间筛选
        if (reqVO.getCreateTime() != null && reqVO.getCreateTime().length > 0) { //时间筛选
            //转换时间
            String bTime = deviceDateServiceImpl.getTZTime(reqVO.getCreateTime()[0]);
            String eTime = deviceDateServiceImpl.getTZTime(reqVO.getCreateTime()[1]);
            command = offline.getSelectSQL() + " and time >= " + "'" + bTime + "'" + " and time <=" + "'" + eTime + "'";
        }

        if (dataCollectionService.query(command).getResults().get(0).getSeries() == null || dataCollectionService.query(command).getResults().get(0).getSeries().isEmpty()) {
            return null;
        } else {
            return (Number) dataCollectionService.query(command).getResults().get(0).getSeries().get(0).getValues().get(0).get(1);
        }
    }


}
