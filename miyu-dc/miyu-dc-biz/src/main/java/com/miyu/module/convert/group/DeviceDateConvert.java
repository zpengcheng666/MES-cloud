package com.miyu.module.convert.group;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import com.miyu.module.dc.controller.admin.offlinealarm.vo.OfflineAlarmResVO;
import com.miyu.module.dc.controller.admin.offlinecollect.vo.OfflineCollectResVO;
import com.miyu.module.dc.controller.admin.offlineerror.vo.OfflineErrorResVO;
import com.miyu.module.dc.dal.dataobject.device.DeviceDO;
import com.miyu.module.dc.dal.dataobject.devicecollect.DeviceCollectDO;
import com.miyu.module.dc.dal.dataobject.producttype.ProductTypeDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeviceDateConvert {

    DeviceDateConvert INSTANCE = Mappers.getMapper(DeviceDateConvert.class);

    default List<OfflineCollectResVO> converOfflineCollect(List<OfflineCollectResVO> list, Map<String, ProductTypeDO> productTypeMap , Map<String, DeviceCollectDO> collectMap) {
        return CollectionUtils.convertList(list, offlineCollectResVO -> {
                MapUtils.findAndThen(collectMap, offlineCollectResVO.getTopicId(), a -> {offlineCollectResVO.setProductTypeName(a.getProductTypeId());});
                MapUtils.findAndThen(productTypeMap, offlineCollectResVO.getProductTypeName(), a -> {offlineCollectResVO.setProductTypeName(a.getProductTypeName());});
            return offlineCollectResVO;
        });
    }

    default List<OfflineErrorResVO> converOfflineError(List<OfflineErrorResVO> list, Map<String, ProductTypeDO> productTypeMap , Map<String, DeviceCollectDO> collectMap) {
        return CollectionUtils.convertList(list, offlineErrorResVO -> {
            MapUtils.findAndThen(collectMap, offlineErrorResVO.getTopicId(), a -> {offlineErrorResVO.setProductTypeName(a.getProductTypeId());});
            MapUtils.findAndThen(productTypeMap, offlineErrorResVO.getProductTypeName(), a -> {offlineErrorResVO.setProductTypeName(a.getProductTypeName());});
            return offlineErrorResVO;
        });
    }

    default List<ProductTypeDO> converProductTypeList(List<ProductTypeDO> list , Map<String,DeviceCollectDO> map) {
        return CollectionUtils.convertList(list,productTypeDO -> {
                MapUtils.findAndThen(map, productTypeDO.getId(), a -> {productTypeDO.setTopicId(a.getTopicId()).setOnlineStatus(a.getOnlineStatus()).setNormStatus(a.getNormStatus());});
                return productTypeDO;
        });
    }

    default List<OfflineAlarmResVO> converOfflineAlarm(List<OfflineAlarmResVO> list , Map<String, ProductTypeDO> productTypeMap ,Map<String, DeviceCollectDO> collectMap) {
        return CollectionUtils.convertList(list,offlineAlarmResVO ->{
            MapUtils.findAndThen(collectMap, offlineAlarmResVO.getTopicId(), a -> {offlineAlarmResVO.setProductTypeName(a.getProductTypeId());});
            MapUtils.findAndThen(productTypeMap, offlineAlarmResVO.getProductTypeName(), a -> {offlineAlarmResVO.setProductTypeName(a.getProductTypeName());});
            return offlineAlarmResVO;
        });
    }


}
