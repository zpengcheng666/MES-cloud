package com.miyu.module.wms.service.takedelivery;

import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.mysql.inwarehousedetail.InWarehouseDetailMapper;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.inwarehousedetail.InWarehouseDetailService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.materialconfig.MaterialConfigService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.wms.controller.admin.takedelivery.vo.*;
import com.miyu.module.wms.dal.dataobject.takedelivery.TakeDeliveryDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.takedelivery.TakeDeliveryMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 物料收货 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class TakeDeliveryServiceImpl implements TakeDeliveryService {

    @Resource
    private TakeDeliveryMapper takeDeliveryMapper;

    @Resource
    private MaterialStockService materialStockService;

    @Resource
    private MaterialConfigService materialConfigService;

    @Resource
    private InWarehouseDetailService inWarehouseDetailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createTakeDelivery(TakeDeliverySaveReqVO createReqVO) {
        // 插入
        TakeDeliveryDO takeDelivery = BeanUtils.toBean(createReqVO, TakeDeliveryDO.class);

        // 校验采购入库单号是否存在 todo: QianJY 待完善 OpenFeign 接口    // 调用采购服务获取 仓库id 和 入库类型
        String orderNumber = createReqVO.getOrderNumber();

        // 校验物料是否存在
        String materialConfigId = createReqVO.getMaterialConfigId();
        MaterialConfigDO materialConfig = materialConfigService.getMaterialConfig(materialConfigId);
        if (materialConfig == null) {
            throw exception(MATERIAL_TYPE_NOT_EXISTS);
        }

        // 生成物料库存
        String materialStockId = materialStockService.createMaterialStock(createReqVO);
        // 生成收获记录
        takeDeliveryMapper.insert(takeDelivery);

        inWarehouseDetailService.takeDeliveryUpdateInWarehouseDetail(takeDelivery,materialStockId,materialConfig);

        // 返回
        return takeDelivery.getId();
    }

    @Override
    public void updateTakeDelivery(TakeDeliverySaveReqVO updateReqVO) {
        // 校验存在
        validateTakeDeliveryExists(updateReqVO.getId());
        // 更新
        TakeDeliveryDO updateObj = BeanUtils.toBean(updateReqVO, TakeDeliveryDO.class);
        takeDeliveryMapper.updateById(updateObj);
    }

    @Override
    public void deleteTakeDelivery(String id) {
        // 校验存在
        validateTakeDeliveryExists(id);
        // 删除
        takeDeliveryMapper.deleteById(id);
    }

    private void validateTakeDeliveryExists(String id) {
        if (takeDeliveryMapper.selectById(id) == null) {
            throw exception(TAKE_DELIVERY_NOT_EXISTS);
        }
    }

    @Override
    public TakeDeliveryDO getTakeDelivery(String id) {
        return takeDeliveryMapper.selectById(id);
    }

    @Override
    public PageResult<TakeDeliveryDO> getTakeDeliveryPage(TakeDeliveryPageReqVO pageReqVO) {
        return takeDeliveryMapper.selectPage(pageReqVO);
    }

}