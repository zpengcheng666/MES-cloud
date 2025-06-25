package com.miyu.module.dc.service.device;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.dms.api.ledger.LedgerApi;
import com.miyu.cloud.dms.api.ledger.dto.LedgerDataResDTO;
import com.miyu.module.convert.group.DeviceDateConvert;
import com.miyu.module.dc.dal.dataobject.devicecollect.DeviceCollectDO;
import com.miyu.module.dc.dal.dataobject.mqttuser.MqttUserDO;
import com.miyu.module.dc.dal.dataobject.producttype.ProductTypeDO;
import com.miyu.module.dc.dal.mysql.collectattributes.CollectAttributesMapper;
import com.miyu.module.dc.dal.mysql.devicecollect.DeviceCollectMapper;
import com.miyu.module.dc.dal.mysql.mqttuser.MqttUserMapper;
import com.miyu.module.dc.dal.mysql.producttype.ProductTypeMapper;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.dc.controller.admin.device.vo.*;
import com.miyu.module.dc.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.dc.dal.mysql.device.DeviceMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.dc.api.devicedate.DeviceDateApi.PREFIX;
import static com.miyu.cloud.dc.enums.ErrorCodeConstants.*;
import static com.miyu.cloud.dc.enums.LogRecordConstants.*;

/**
 * 设备 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DeviceServiceImpl implements DeviceService {

    @Value("${spring.mqtt.host}")
    private String host;

//    private static final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private static final String topicPrefix = "dc/"; ;

    private static final String urlPrefix =  PREFIX + "creat/{{Topic}}"; ;

    private static final String url =  "http://localhost:48099";

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private DeviceCollectMapper deviceCollectMapper;

    @Resource
    MqttUserMapper mqttUserMapper;

    @Resource
    private ProductTypeMapper productTypeMapper;

    @Resource
    private LedgerApi ledgerApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDevice(DeviceSaveReqVO createReqVO) {
        //获取设备分类下全部设备
        List<String> list = Arrays.asList(createReqVO.getDeviceId());
        switch (createReqVO.getCommType()){
            case 1:  //mqtt
                list.forEach(i->{
                    //生成客户端id
                    String clientId = i;
                    //生成账号
                    String userName = String.valueOf(new Random().nextInt(900000) + 100000);
                    //生成密码(随机八位数)
                    String password = generateRandomString(8);
                    //mqtt账号权限表新增数据
                    mqttUserMapper.insertUser(new MqttUserDO().setUsername(userName).setPassword(password));
                    //创建设备数据
                    DeviceDO deviceDO = new DeviceDO().setDeviceClientId(clientId).setDeviceUrl(host).setUsername(userName).setPassword(password).setDeviceId(i).setDeviceTypeId(createReqVO.getDeviceTypeId()).setCommType(1).setProductTypeId(createReqVO.getProductTypeId());
                    // 插入设备信息表数据
                    this.insertDeviceCollect(deviceDO,topicPrefix+i);
                });
                break;
            case 2:  //restful
                list.forEach(l->{
                    DeviceDO deviceDO = new DeviceDO().setDeviceUrl(url + urlPrefix).setDeviceId(l).setDeviceTypeId(createReqVO.getDeviceTypeId()).setCommType(2).setProductTypeId(createReqVO.getProductTypeId());
                    // 插入设备信息表数据
                    this.insertDeviceCollect(deviceDO,l);
                });
                break;
        };
    }

    /**
     * 新增设备采集类型关联表 设备采集表
     * @param reqVO
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertDeviceCollect(DeviceDO reqVO , String topicId) {
        // 插入设备信息表数据
        deviceMapper.insert(reqVO);
        //生成采集类型数据
        //生成设备采集表
        List<DeviceCollectDO> list = new ArrayList<>();
        Arrays.asList(reqVO.getProductTypeId()).forEach(i->{
            ProductTypeDO productTopic = productTypeMapper.selectById(i);
            list.add(new DeviceCollectDO().setDeviceId(reqVO.getId()).setProductTypeId(i).setTopicId(topicId+"_"+productTopic.getTopicId()));
        });
        deviceCollectMapper.insertBatch(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = DEVICE_CLUE_TYPE, subType = DEVICE_SUBMIT_SUB_TYPE, bizNo = "{{#id}}",
            success = DEVICE_SUBMIT_SUCCESS)
    public void updateDevice(DeviceUpdateReqVO updateReqVO) {
        String topicId = null;
        if(updateReqVO.getCommType() == 1){
            topicId = topicPrefix+updateReqVO.getDeviceId();
        }
        if(updateReqVO.getCommType() == 2){
            topicId = updateReqVO.getDeviceId();
        }
        // 校验存在
        validateDeviceExists(updateReqVO.getId());
        // 更新设备信息表数据
        DeviceDO updateObj = BeanUtils.toBean(updateReqVO, DeviceDO.class);
        deviceMapper.updateById(updateObj);
        //物理删除设备采集类型关联表
        deviceCollectMapper.deleteAll(updateReqVO.getId());
        //新增设备设备采集类型关联表
        List<DeviceCollectDO> list = new ArrayList<>();
        String finalTopicId = topicId;
        Arrays.asList(updateReqVO.getProductTypeId()).forEach(i->{
            ProductTypeDO productTopic = productTypeMapper.selectById(i);
            list.add(new DeviceCollectDO().setDeviceId(updateReqVO.getId()).setProductTypeId(i).setTopicId(finalTopicId +"_"+productTopic.getTopicId()));
        });
        deviceCollectMapper.insertBatch(list);

        // 4. 记录日志
        LogRecordContext.putVariable("id", updateReqVO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDevice(String id) {
        // 校验存在
        validateDeviceExists(id);
        DeviceDO deviceDO = deviceMapper.getUserName(id);
        // 物理删除账号密码表
        mqttUserMapper.deleteUser(deviceDO.getUsername());
        // 物理删除设备表
        deviceMapper.deleteDeviceById(id);
        //物理删除设备采集类型关联表
        deviceCollectMapper.deleteAll(id);
    }

    private void validateDeviceExists(String id) {
        if (deviceMapper.selectById(id) == null) {
            throw exception(DEVICE_NOT_EXISTS);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceDO getDevice(String id) {
        DeviceDO deviceDO = deviceMapper.selectById(id);
        List<String> ProductTypeList = deviceCollectMapper.queryById(id);
        deviceDO.setProductTypeId(ProductTypeList.toArray(new String[ProductTypeList.size()]));
        return deviceDO;

    }

    @Override
    public PageResult<DeviceDO> getDevicePage(DevicePageReqVO pageReqVO) {
        return deviceMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DeviceDO> getDeviceList() {
        return deviceMapper.selectList();
    }

    /**
     * 获取设备监控分页
     * @param pageReqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResult<DeviceOfflineRespVO> getDeviceOfflinePage(DevicePageReqVO pageReqVO) {
        //查询设备信息
        PageResult<DeviceDO> deviceDoPage = deviceMapper.selectPage(pageReqVO);
        List<DeviceOfflineRespVO> deviceOfflineRespVOS =  BeanUtils.toBean(deviceDoPage.getList() , DeviceOfflineRespVO.class);
        deviceOfflineRespVOS.forEach(i->{
            //查询设备产品信息
            List<DeviceCollectDO> deviceCollectDOS = deviceCollectMapper.getProductTypeByDeviceId(i.getId());
            Map<String,DeviceCollectDO> map = CollectionUtils.convertMap(deviceCollectDOS,DeviceCollectDO::getProductTypeId);
            List<String> productTypeIds = deviceCollectDOS.stream().map(DeviceCollectDO::getProductTypeId).collect(Collectors.toList());
            List<ProductTypeDO> productTypeDOList = productTypeMapper.getProductTypeList(productTypeIds);
            List<ProductTypeDO> list = DeviceDateConvert.INSTANCE.converProductTypeList(productTypeDOList,map);
            i.setProductTypeList(list);
        });
        deviceOfflineRespVOS.forEach(i->{
            List<Integer> list = new ArrayList<>();
            i.getProductTypeList().forEach(l->{
                list.add(l.getNormStatus());
                list.add(l.getOnlineStatus());
            });
            if(list.stream().allMatch(status->status==3)){
                i.setDeviceStatus(3);
            }
            else if(list.stream().allMatch(status->status==1)){
                i.setDeviceStatus(1);
            }
            else {
                i.setDeviceStatus(2);
            }
        });
        List<DeviceOfflineRespVO> deviceOfflineList = new ArrayList<>();
        if(pageReqVO.getDeviceStatus()!=null){
            deviceOfflineList = deviceOfflineRespVOS.stream().filter(i->i.getDeviceStatus() == pageReqVO.getDeviceStatus()).collect(Collectors.toList());
            return new PageResult<DeviceOfflineRespVO>().setList(deviceOfflineList);
        }

        return new PageResult<DeviceOfflineRespVO>().setList(deviceOfflineRespVOS).setTotal(deviceDoPage.getTotal());

    }


    /**
     * 获取当前数据采集未存在设备
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<LedgerDataResDTO> getDeviceListByCollet(String id) {
        List<LedgerDataResDTO> list = ledgerApi.getLedgerListByDeviceType(id).getData();
        List<String> deviceDOList = deviceMapper.selectList(new QueryWrapper<DeviceDO>()).stream().map(DeviceDO::getDeviceId).collect(Collectors.toList());
        List<LedgerDataResDTO> resDTOList = list.stream().filter(o->!deviceDOList.contains(o.getId())).collect(Collectors.toList());
        return resDTOList;
    }


    /**
     * 生成随机编码
     * @param length
     * @return
     */
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

}