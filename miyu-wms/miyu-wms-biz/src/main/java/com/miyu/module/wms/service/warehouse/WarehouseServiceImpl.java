package com.miyu.module.wms.service.warehouse;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.miyu.module.wms.controller.admin.warehouse.vo.*;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.warehouse.WarehouseMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 仓库表 Service 实现类
 *
 * @author Qianjy
 */
@Service
@Validated
public class WarehouseServiceImpl implements WarehouseService {

    @Resource
    private WarehouseMapper warehouseMapper;
    @Resource
    private MaterialStockService materialStockService;

    @Override
    public String createWarehouse(WarehouseSaveReqVO createReqVO) {
        // 插入
        WarehouseDO warehouse = BeanUtils.toBean(createReqVO, WarehouseDO.class);
        warehouseMapper.insert(warehouse);
        // 返回
        return warehouse.getId();
    }

    @Override
    public void updateWarehouse(WarehouseSaveReqVO updateReqVO) {
        // 校验存在
        validateWarehouseExists(updateReqVO.getId());
        // 更新
        WarehouseDO updateObj = BeanUtils.toBean(updateReqVO, WarehouseDO.class);
        warehouseMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarehouse(String id) {
        // 校验存在
        validateWarehouseExists(id);
        // 删除
        warehouseMapper.deleteById(id);
    }

    private void validateWarehouseExists(String id) {
        if (warehouseMapper.selectById(id) == null) {
            throw exception(WAREHOUSE_NOT_EXISTS);
        }
    }

    @Override
    public WarehouseDO getWarehouse(String id) {
        return warehouseMapper.selectById(id);
    }

    @Override
    public List<WarehouseDO> getWarehouseByIds(Collection<String> ids) {
        if(CollectionUtils.isAnyEmpty(ids)){
            return Collections.emptyList();
        }
        return warehouseMapper.selectBatchIds(ids);
    }

    /**
     * 获得仓库列表
     *
     * @param ids 编号集合
     * @return
     */
    @Override
    public List<WarehouseDO> getWarehouseList(Collection<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return warehouseMapper.selectBatchIds(ids);
    }

    /**
     * 获得仓库 Map
     *
     * @param ids 编号集合
     * @return
     */
    public Map<String, WarehouseDO> getWarehouseMap(Collection<String> ids) {
        List<WarehouseDO> warehouseDO = getWarehouseList(ids);
        return CollectionUtils.convertMap(warehouseDO, WarehouseDO::getId);
    }

    @Override
    public PageResult<WarehouseDO> getWarehousePage(WarehousePageReqVO pageReqVO) {
        return warehouseMapper.selectPage(pageReqVO);
    }

    @Override
    public List<WarehouseDO> getWarehouseList() {
        return warehouseMapper.selectList();
    }

    @Override
    public WarehouseDO getWarehouseByMaterialStockId(String materialStockId) {
        // 查询物料所在库位
        String locationId = materialStockService.getLocationIdByMaterialStockId(materialStockId);
        return this.getWarehouseByLocationId(locationId);
    }

    @Override
    public WarehouseDO getWarehouseByLocationId(String locationId) {
        return warehouseMapper.selectByLocationId(locationId);
    }

    /**
     * 仓库编码查询仓库
     * @param code
     * @return
     */
    @Override
    public List<WarehouseDO> getWarehouseByCode(String code) {
        return warehouseMapper.selectList(WarehouseDO::getWarehouseCode, code);
    }

    @Override
    public List<WarehouseDO> getWarehouseByTypeS(Collection<Integer> warehouseTypes) {
        return warehouseMapper.selectList(
                new LambdaQueryWrapper<WarehouseDO>()
                        .in( WarehouseDO::getWarehouseType, warehouseTypes)
                        .eq(WarehouseDO::getWarehouseState, 1));
    }
}
