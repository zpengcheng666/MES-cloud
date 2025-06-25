package com.miyu.module.dc.service.producttype;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import cn.iocoder.yudao.module.system.api.notify.NotifyMessageSendApi;
import cn.iocoder.yudao.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.miyu.cloud.dc.api.devicedate.dto.CommonDevice;
import com.miyu.cloud.dms.api.ledger.LedgerApi;
import com.miyu.cloud.dms.api.ledger.dto.LedgerDataResDTO;
import com.miyu.module.Influxdb.service.DataCollectionService;
import com.miyu.module.convert.group.DeviceDateConvert;
import com.miyu.module.dc.dal.dataobject.collectattributes.CollectAttributesDO;
import com.miyu.module.dc.dal.dataobject.devicecollect.DeviceCollectDO;
import com.miyu.module.dc.dal.mysql.collectattributes.CollectAttributesMapper;
import com.miyu.module.dc.dal.mysql.device.DeviceMapper;
import com.miyu.module.dc.dal.mysql.devicecollect.DeviceCollectMapper;
import com.miyu.module.dc.service.device.DeviceService;
import com.miyu.module.dc.service.device.DeviceServiceImpl;
import com.miyu.module.dc.service.devicedate.DeviceDateServiceImpl;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.dc.controller.admin.producttype.vo.*;
import com.miyu.module.dc.dal.dataobject.producttype.ProductTypeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.dc.dal.mysql.producttype.ProductTypeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.Influxdb.enums.InfluxdbConstans.*;
import static com.miyu.cloud.dc.enums.ErrorCodeConstants.*;
import static com.miyu.cloud.dc.enums.LogRecordConstants.*;

/**
 * 产品类型 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductTypeServiceImpl implements ProductTypeService {

    @Resource
    private ProductTypeMapper productTypeMapper;

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private DataCollectionService dataCollectionService;

    @Resource
    CollectAttributesMapper collectAttributesMapper;

    @Resource
    private DeviceCollectMapper deviceCollectMapper;

    @Resource
    private DeviceDateServiceImpl deviceDateServiceImpl;

    @Resource
    private NotifyMessageSendApi notifyMessageSendApi;

    @Resource
    private LedgerApi ledgerApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProductType(ProductTypeSaveReqVO createReqVO) {
        // 校验数据
        validateProductType(createReqVO);
        // 插入
        ProductTypeDO productType = BeanUtils.toBean(createReqVO, ProductTypeDO.class);
        productTypeMapper.insert(productType);
        // 插入子表
        createReqVO.getCollectAttributesDetails().forEach(i -> {
            if(i.getCollectAttributesIlk()==2 && i.getCollectAttributesNorm()==null){
                i.setCollectAttributesNorm("");
            }
            i.setProductTypeId(productType.getId());
        });
        collectAttributesMapper.insertBatch(createReqVO.getCollectAttributesDetails());
        // 返回
        return productType.getId();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = PRODUCT_TYPE_CLUE_TYPE, subType = PRODUCT_TYPE_SUBMIT_SUB_TYPE, bizNo = "{{#id}}",
            success = PRODUCT_TYPE_SUBMIT_SUCCESS)
    public void updateProductType(ProductTypeSaveReqVO updateReqVO) {
        // 校验存在
        validateProductTypeExists(updateReqVO.getId());
        // 校验数据
        validateProductType(updateReqVO);

        // 更新
        ProductTypeDO updateObj = BeanUtils.toBean(updateReqVO, ProductTypeDO.class);
        productTypeMapper.updateById(updateObj);

        //删除相关数据
        collectAttributesMapper.deleteAll(updateReqVO.getId());

        // 插入子表
        updateReqVO.getCollectAttributesDetails().forEach(i -> {
            if(i.getCollectAttributesIlk()==2 && i.getCollectAttributesNorm()==null){
                i.setCollectAttributesNorm("");
            }
            i.setProductTypeId(updateReqVO.getId());
        });
        collectAttributesMapper.insertBatch(updateReqVO.getCollectAttributesDetails());

        // 4. 记录日志
        LogRecordContext.putVariable("name", updateReqVO.getProductTypeName());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductType(String id) {
        // 校验存在
        validateProductTypeExists(id);
        //校验绑定关系
        if(!deviceCollectMapper.getProductById(id).isEmpty()){
            throw exception(PRODUCT_TYPE_EXISTS);
        }
        // 物理删除绑定数据关系
        collectAttributesMapper.deleteAll(id);
        // 删除
        productTypeMapper.deleteById(id);
    }

    private void validateProductTypeExists(String id) {
        if (productTypeMapper.selectById(id) == null) {
            throw exception(PRODUCT_TYPE_NOT_EXISTS);
        }
    }

    @Override
    public ProductTypeDO getProductType(String id) {
        ProductTypeDO productTypeDO = productTypeMapper.selectById(id);
        productTypeDO.setCollectAttributesDetails(collectAttributesMapper.getAttributesList(id));
        return productTypeDO;
    }

    @Override
    public PageResult<ProductTypeDO> getProductTypePage(ProductTypePageReqVO pageReqVO) {
        return productTypeMapper.selectPage(pageReqVO);
    }

    /**
     * 获取产品类型列表
     * @return
     */
    @Override
    public List<ProductTypeDO> getProductTypeList() {
        return productTypeMapper.selectList();
    }

    /**
     * 获取产品详情信息
     * @param id
     * @return
     */
    @Override
    public List<ProductTypeDO> queryProductByDeviceId(String id) {
        //获取设备相关产品
        String deviceId = deviceMapper.selectById(id).getDeviceId();
        List<DeviceCollectDO> list = deviceCollectMapper.getProductTypeByDeviceId(id);
        List<String> productTypeIds = list.stream().map(DeviceCollectDO::getProductTypeId).collect(Collectors.toList());
        List<ProductTypeDO> productTypeDOList = productTypeMapper.getProductTypeList(productTypeIds);
        productTypeDOList.forEach(i->{
            //JSON格式展示
            List<CollectAttributesDO> collectAttributesDOS = collectAttributesMapper.getAttributesList(i.getId());
            collectAttributesDOS.forEach(
                    l->{if(l.getCollectAttributesIlk()==1){l.setCollectAttributesTypeName(String.valueOf(l.getCollectAttributesFloor()));}
                        if(l.getCollectAttributesIlk()==2){l.setCollectAttributesTypeName(l.getCollectAttributesNorm());}
                        if(l.getCollectAttributesIlk()==3){l.setCollectAttributesTypeName(l.getCollectAttributesEnum().split(" ")[0]);}
                    });
            Map<String, String> collect = collectAttributesDOS.stream().distinct().collect(Collectors.toMap(CollectAttributesDO::getCollectAttributesValue, CollectAttributesDO::getCollectAttributesTypeName));
            Map<String, Object> map = new HashMap<>();
            map.put("id", deviceId);
            List<Map<String, String>> collectMap = new ArrayList<>();
            collectMap.add(collect);
            map.put("items", collectMap);
            CommonDevice commonDevice = new CommonDevice().setData(map).setCode(200).setMsg("");
            String json = new Gson().toJson(commonDevice);
            JSONObject object = JSONObject.parseObject(json);
            String pretty = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
            i.setTypeCode(pretty).setTopicId(deviceCollectMapper.queryTopic(id,i.getId()));
        });
        return productTypeDOList;
    }

    /**
     * 根据设备获取产品信息
     * @param id
     * @return
     */
    @Override
    public List<ProductTypeDO> getProductListByDeviceId(String id) {
        //查询设备下产品信息
        List<DeviceCollectDO> list = deviceCollectMapper.getProductTypeByDeviceId(id);
        Map<String,DeviceCollectDO> map = CollectionUtils.convertMap(list,DeviceCollectDO::getProductTypeId);
        List<String> productTypeIds = list.stream().map(DeviceCollectDO::getProductTypeId).collect(Collectors.toList());
        List<ProductTypeDO> productTypeDOList = productTypeMapper.getProductTypeList(productTypeIds);
        return DeviceDateConvert.INSTANCE.converProductTypeList(productTypeDOList,map);
    }

    /**
     * 校验重复
     * @param createReqVO
     */
    private void validateProductType(ProductTypeSaveReqVO createReqVO){
        //校验名称是否重复
        if(!productTypeMapper.existsType(createReqVO).isEmpty()){
            throw exception(COLLECT_TYPE_EXISTS);
        }
        //校验最高最低值
        createReqVO.getCollectAttributesDetails().forEach(i -> {
            if(i.getCollectAttributesIlk()==1) {
                if (i.getCollectAttributesUpper().compareTo(i.getCollectAttributesFloor()) < 0) {
                    throw exception(COLLECT_ATTRIBUTES_COMPARE);
                }
            }
        });
        //校验是否使用中
        if(!deviceCollectMapper.getProductById(createReqVO.getId()).isEmpty()){
            throw exception(PRODUCT_TYPE_EXISTS_USE);
        }
        //校验topic
        if(!productTypeMapper.existsTopic(createReqVO).isEmpty()){
            throw exception(COLLECT_TOPIC_EXISTS);
        }
    }

    /**
     * 校验当前系统设备采集产品是否在线
     */
    @TenantIgnore
    public void getOnlineStatus(){

        Long tTime = deviceDateServiceImpl.getTimeLong(System.currentTimeMillis());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime nowTime = LocalDateTime.parse(LocalDateTime.now().format(fmt), fmt);
        //查询当前系统存在的设备-产品类型信息
        List<DeviceCollectDO> list = deviceCollectMapper.selectList();
        //遍历数据
        list.forEach(i->{
            ProductTypeDO productTypeDO = productTypeMapper.selectById(i.getProductTypeId());
            i.setProductTopicId(productTypeDO.getTopicId()).setCollectAttributesCycle(productTypeDO.getCollectAttributesCycle());
            //拼接sql语句
            String countSql = "select \"data\" , \"time\" FROM \"" + i.getProductTopicId() + "\" where \"" + TOPIC_ID + "\" = '" + i.getTopicId() + "' order by time DESC LIMIT 1";
            //INFLUXDB查询
            QueryResult queryResult =  dataCollectionService.query(countSql);

            //最后数据时间
            if(queryResult.getResults().get(0).getSeries()!=null){
                String dbTime = queryResult.getResults().get(0).getSeries().get(0).getValues().get(0).get(0).toString().substring(0,19).replace("T"," ");
                LocalDateTime time = LocalDateTime.parse(LocalDateTime.parse(dbTime,fmt).plusMinutes(i.getCollectAttributesCycle().longValue()).format(fmt),fmt);
                if(nowTime.isAfter(time)){
                    //后续加入短信/邮件逻辑
                    String deviceId = deviceMapper.selectById(i.getDeviceId()).getDeviceId();
                    //站内报警
                    //触发站内信报警
                    NotifySendSingleToUserReqDTO notifySendSingleToUserReqDTO = new NotifySendSingleToUserReqDTO();
                    Map<String, Object> templateParams = new HashMap<>();
                    //站内信
                    LedgerDataResDTO ledger = ledgerApi.getNameByDeviceId(deviceId).getData();
                    templateParams.put("devicename",ledger.getCode()+" "+ledger.getName());
                    templateParams.put("producttypename",productTypeMapper.selectById(productTypeDO.getId()).getProductTypeName());

                    List<String> userIds = ledger.getUsers();
                    userIds.add(ledger.getSuperintendent());
                    userIds.forEach(o->{
                        notifySendSingleToUserReqDTO.setUserId(Long.valueOf(o)).setTemplateCode(ONLINE_ALARM).setTemplateParams(templateParams);
                        notifyMessageSendApi.sendSingleMessageToAdmin(notifySendSingleToUserReqDTO);
                    });

                    //时序库插入离线数据
                    deviceDateServiceImpl.insertAlarm(new HashMap<>(),deviceId,i.getTopicId(),tTime,OLINE_TYPE);
                    i.setOnlineStatus(2);
                }else {
                    i.setOnlineStatus(1);
                }
                deviceCollectMapper.updateById(i);
            }
        });
    }

}