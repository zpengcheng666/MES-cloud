package com.miyu.module.ppm.service.warehousedetail;

import com.miyu.module.ppm.api.purchaseConsignment.dto.WarehouseRespDTO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.ppm.controller.admin.warehousedetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.warehousedetail.WarehouseDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.warehousedetail.WarehouseDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 入库详情表 对应仓库库存 来源WMS Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class WarehouseDetailServiceImpl implements WarehouseDetailService {

    @Resource
    private WarehouseDetailMapper warehouseDetailMapper;

    @Override
    public String createWarehouseDetail(WarehouseDetailSaveReqVO createReqVO) {
        // 插入
        WarehouseDetailDO warehouseDetail = BeanUtils.toBean(createReqVO, WarehouseDetailDO.class);
        warehouseDetailMapper.insert(warehouseDetail);
        // 返回
        return warehouseDetail.getId();
    }

    @Override
    public void updateWarehouseDetail(WarehouseDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateWarehouseDetailExists(updateReqVO.getId());
        // 更新
        WarehouseDetailDO updateObj = BeanUtils.toBean(updateReqVO, WarehouseDetailDO.class);
        warehouseDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarehouseDetail(String id) {
        // 校验存在
        validateWarehouseDetailExists(id);
        // 删除
        warehouseDetailMapper.deleteById(id);
    }

    private void validateWarehouseDetailExists(String id) {
        if (warehouseDetailMapper.selectById(id) == null) {
//            throw exception(WAREHOUSE_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public WarehouseDetailDO getWarehouseDetail(String id) {
        return warehouseDetailMapper.selectById(id);
    }

    @Override
    public PageResult<WarehouseDetailDO> getWarehouseDetailPage(WarehouseDetailPageReqVO pageReqVO) {
        return warehouseDetailMapper.selectPage(pageReqVO);
    }

    /**
     * 新增入库明细表数据
     * @param warehouseDetailDO
     */
    @Override
    public void addWarehouseDetail(List<WarehouseDetailDO> warehouseDetailDO) {
         warehouseDetailMapper.insertBatch(warehouseDetailDO);
    }

    /**
     * 查询退货单明细对应的入库详情
     */
    @Override
    public List<WarehouseDetailDO> queryWareHouseList(String consignmentReturnId) {
        return warehouseDetailMapper.queryWareHouseList(consignmentReturnId);
    }

    /**
     * 查询收货单对应的产品信息
     * @param consignmentReturnNo
     * @return
     */
    @Override
    public List<WarehouseDetailDO> queryWareHouseListByConsignmentReturnNo(String consignmentReturnNo) {
        return warehouseDetailMapper.queryWareHouseListByConsignmentReturnNo(consignmentReturnNo);
    }

}