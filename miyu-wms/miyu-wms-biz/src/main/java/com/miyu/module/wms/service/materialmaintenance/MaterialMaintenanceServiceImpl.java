package com.miyu.module.wms.service.materialmaintenance;

import com.miyu.module.wms.service.materialstock.MaterialStockService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.wms.controller.admin.materialmaintenance.vo.*;
import com.miyu.module.wms.dal.dataobject.materialmaintenance.MaterialMaintenanceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.materialmaintenance.MaterialMaintenanceMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 物料维护记录 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class MaterialMaintenanceServiceImpl implements MaterialMaintenanceService {

    @Resource
    private MaterialMaintenanceMapper materialMaintenanceMapper;

    @Override
    public String createMaterialMaintenance(MaterialMaintenanceSaveReqVO createReqVO) {
        // 插入
        MaterialMaintenanceDO materialMaintenance = BeanUtils.toBean(createReqVO, MaterialMaintenanceDO.class);
        materialMaintenanceMapper.insert(materialMaintenance);
        // 返回
        return materialMaintenance.getId();
    }

    @Override
    public void updateMaterialMaintenance(MaterialMaintenanceSaveReqVO updateReqVO) {
        // 校验存在
        validateMaterialMaintenanceExists(updateReqVO.getId());
        // 更新
        MaterialMaintenanceDO updateObj = BeanUtils.toBean(updateReqVO, MaterialMaintenanceDO.class);
        materialMaintenanceMapper.updateById(updateObj);
    }

    @Override
    public void deleteMaterialMaintenance(String id) {
        // 校验存在
        validateMaterialMaintenanceExists(id);
        // 删除
        materialMaintenanceMapper.deleteById(id);
    }

    private void validateMaterialMaintenanceExists(String id) {
        if (materialMaintenanceMapper.selectById(id) == null) {
            throw exception(MATERIAL_MAINTENANCE_NOT_EXISTS);
        }
    }

    @Override
    public MaterialMaintenanceDO getMaterialMaintenance(String id) {
        return materialMaintenanceMapper.selectById(id);
    }

    @Override
    public PageResult<MaterialMaintenanceDO> getMaterialMaintenancePage(MaterialMaintenancePageReqVO pageReqVO) {
        return materialMaintenanceMapper.selectPage(pageReqVO);
    }
    @Override
    public void generateMaterialMaintenance(String materialStockId, Integer type, String description) {
        this.generateMaterialMaintenance(materialStockId, 1, type, description);
    }

    @Override
    public void generateMaterialMaintenance(String materialStockId, Integer quantity, Integer type, String description) {
        MaterialMaintenanceDO materialMaintenanceDO = new MaterialMaintenanceDO();
        materialMaintenanceDO.setMaterialStockId(materialStockId);
        materialMaintenanceDO.setQuantity(quantity);
        materialMaintenanceDO.setType(type);
        materialMaintenanceDO.setDescription(description);
        materialMaintenanceMapper.insert(materialMaintenanceDO);
    }

}