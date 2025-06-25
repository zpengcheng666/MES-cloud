package com.miyu.cloud.mcs.service.productionrecords;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.miyu.cloud.mcs.dal.mysql.batchorder.BatchOrderMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import com.miyu.cloud.mcs.controller.admin.productionrecords.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.productionrecords.ProductionRecordsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.mcs.dal.mysql.productionrecords.ProductionRecordsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.*;

/**
 * 现场作业记录 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
public class ProductionRecordsServiceImpl implements ProductionRecordsService {

    @Resource
    private ProductionRecordsMapper productionRecordsMapper;
    @Resource
    private BatchOrderMapper batchOrderMapper;

    @Override
    public String createProductionRecords(ProductionRecordsSaveReqVO createReqVO) {
        // 插入
        ProductionRecordsDO productionRecords = BeanUtils.toBean(createReqVO, ProductionRecordsDO.class);
        productionRecordsMapper.insert(productionRecords);
        // 返回
        return productionRecords.getId();
    }

    @Override
    public void updateProductionRecords(ProductionRecordsSaveReqVO updateReqVO) {
        // 校验存在
        validateProductionRecordsExists(updateReqVO.getId());
        // 更新
        ProductionRecordsDO updateObj = BeanUtils.toBean(updateReqVO, ProductionRecordsDO.class);
        productionRecordsMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductionRecords(String id) {
        // 校验存在
        validateProductionRecordsExists(id);
        // 删除
        productionRecordsMapper.deleteById(id);
    }

    private void validateProductionRecordsExists(String id) {
        if (productionRecordsMapper.selectById(id) == null) {
            throw exception(PRODUCTION_RECORDS_NOT_EXISTS);
        }
    }

    @Override
    public ProductionRecordsDO getProductionRecords(String id) {
        return productionRecordsMapper.selectById(id);
    }

    @Override
    public PageResult<ProductionRecordsDO> getProductionRecordsPage(ProductionRecordsPageReqVO pageReqVO) {
        LambdaQueryWrapper<ProductionRecordsDO> wrapper = new LambdaQueryWrapperX<ProductionRecordsDO>()
                .likeIfPresent(ProductionRecordsDO::getOrderNumber, pageReqVO.getOrderNumber())
                .likeIfPresent(ProductionRecordsDO::getBatchNumber, pageReqVO.getBatchNumber())
                .eqIfPresent(ProductionRecordsDO::getEquipmentId, pageReqVO.getEquipmentId())
                .likeIfPresent(ProductionRecordsDO::getBarCode, pageReqVO.getBarCode())
                .eqIfPresent(ProductionRecordsDO::getOperationType, pageReqVO.getOperationType())
                .orderByDesc(ProductionRecordsDO::getOrderId)
                .orderByAsc(ProductionRecordsDO::getId);
        IPage<ProductionRecordsDO> mpPage = MyBatisUtils.buildPage(pageReqVO, null);
        IPage<ProductionRecordsDO> productionRecordsPage = productionRecordsMapper.getProductionRecordsPage(mpPage, wrapper);
        return new PageResult<>(productionRecordsPage.getRecords(), productionRecordsPage.getTotal());
    }

}
